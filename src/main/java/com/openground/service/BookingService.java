package com.openground.service;

import com.openground.dto.*;
import com.openground.entity.*;
import com.openground.exception.DoubleBookingException;
import com.openground.exception.InvalidBookingException;
import com.openground.exception.ResourceNotFoundException;
import com.openground.exception.UnauthorizedException;
import com.openground.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final BookingRepository bookingRepository;
    private final GroundRepository groundRepository;
    private final PricingSlotRepository pricingSlotRepository;
    private final AvailabilityClosureRepository closureRepository;
    private final PaymentRepository paymentRepository;

    public BookingService(BookingRepository bookingRepository,
                          GroundRepository groundRepository,
                          PricingSlotRepository pricingSlotRepository,
                          AvailabilityClosureRepository closureRepository,
                          PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.groundRepository = groundRepository;
        this.pricingSlotRepository = pricingSlotRepository;
        this.closureRepository = closureRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public PriceCalculationResponse calculatePrice(CalculatePriceRequest request) {
        Ground ground = groundRepository.findById(request.getGroundId())
                .orElseThrow(() -> new ResourceNotFoundException("Ground not found with ID: " + request.getGroundId()));

        LocalDate date = request.getBookingDate();
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Cannot book a date in the past");
        }

        PriceCalculationResponse response = new PriceCalculationResponse();
        response.setBookingDate(date);
        response.setSlotType(request.getSlotType());

        // Check maintenance or rainout closures
        List<AvailabilityClosure> closures = closureRepository.findClosuresOnDate(ground.getId(), date);
        if (!closures.isEmpty()) {
            response.setAvailable(false);
            response.setUnavailableReason("Ground is closed on this date: " + closures.get(0).getReason());
            return response;
        }

        // Check for existing active bookings overlapping this slot
        boolean hasOverlap = bookingRepository.isSlotOverlapping(ground.getId(), date, request.getSlotType());
        if (hasOverlap) {
            response.setAvailable(false);
            response.setUnavailableReason("This slot is already booked for an outdoor fixture on the selected date.");
            return response;
        }

        // Retrieve pricing rule
        PricingSlot slot = pricingSlotRepository.findByGroundIdAndSlotType(ground.getId(), request.getSlotType())
                .orElse(null);

        boolean isWeekend = (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
        response.setWeekend(isWeekend);

        if (slot != null) {
            response.setStartTime(slot.getStartTime());
            response.setEndTime(slot.getEndTime());
            BigDecimal price = isWeekend ? slot.getWeekendPrice() : slot.getWeekdayPrice();
            response.setBaseRate(ground.getHourlyBaseRate());
            response.setTotalAmount(price);
            response.setAvailable(true);
        } else {
            // Fallback estimation if custom slot
            BigDecimal hours = (request.getSlotType() == SlotType.FULL_DAY) ? BigDecimal.valueOf(9) : BigDecimal.valueOf(4.5);
            BigDecimal multiplier = isWeekend ? BigDecimal.valueOf(1.25) : BigDecimal.valueOf(1.0);
            BigDecimal calculated = ground.getHourlyBaseRate().multiply(hours).multiply(multiplier);
            response.setStartTime("07:00 AM");
            response.setEndTime(request.getSlotType() == SlotType.FULL_DAY ? "05:00 PM" : "11:30 AM");
            response.setBaseRate(ground.getHourlyBaseRate());
            response.setTotalAmount(calculated);
            response.setAvailable(true);
        }

        return response;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized BookingResponseDto createBooking(BookingRequest request, User user) {
        Ground ground = groundRepository.findById(request.getGroundId())
                .orElseThrow(() -> new ResourceNotFoundException("Ground not found"));

        LocalDate date = request.getBookingDate();
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Cannot book a cricket ground for a past date");
        }

        // 1. Verify Ground Closures
        List<AvailabilityClosure> closures = closureRepository.findClosuresOnDate(ground.getId(), date);
        if (!closures.isEmpty()) {
            throw new InvalidBookingException("Ground is unavailable on " + date + " due to: " + closures.get(0).getReason());
        }

        // 2. Concurrency Check / Double-booking prevention
        boolean isOverlapping = bookingRepository.isSlotOverlapping(ground.getId(), date, request.getSlotType());
        if (isOverlapping) {
            log.warn("Double booking collision prevented for ground {} on date {} slot {}", ground.getId(), date, request.getSlotType());
            throw new DoubleBookingException("Slot conflict: This ground has already been reserved for the requested session. Please select another slot or date.");
        }

        // 3. Server-side price calculation (NEVER trust client price)
        PriceCalculationResponse priceCalculation = calculatePrice(new CalculatePriceRequest(ground.getId(), date, request.getSlotType()));
        if (!priceCalculation.isAvailable()) {
            throw new InvalidBookingException(priceCalculation.getUnavailableReason());
        }

        // 4. Generate unique readable booking reference
        String dateStr = date.toString().replace("-", "");
        String ref = "OG-" + dateStr + "-" + String.format("%04X", RANDOM.nextInt(0x10000));

        PaymentStatus pStatus = (request.getPaymentMethod() == PaymentMethod.ONLINE_SANDBOX) ? PaymentStatus.PAID : PaymentStatus.PENDING;

        Booking booking = new Booking(
                UUID.randomUUID(),
                ref,
                ground,
                user,
                date,
                request.getSlotType(),
                priceCalculation.getStartTime(),
                priceCalculation.getEndTime(),
                priceCalculation.getTotalAmount(),
                pStatus,
                request.getPaymentMethod(),
                BookingStatus.CONFIRMED,
                request.getNotes()
        );

        Booking saved = bookingRepository.save(booking);

        // Record initial payment ledger
        Payment payment = new Payment(
                UUID.randomUUID(),
                saved,
                "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                saved.getTotalAmount(),
                saved.getPaymentMethod(),
                pStatus,
                request.getPaymentMethod() == PaymentMethod.ONLINE_SANDBOX ? "Sandbox payment approved" : "Pay at ground arranged"
        );
        paymentRepository.save(payment);

        log.info("Booking created successfully: reference={}, ground={}, user={}, amount={}",
                saved.getBookingReference(), ground.getName(), user.getEmail(), saved.getTotalAmount());

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(UUID id, User user) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        // Authorization check: User can view if they are owner of ground, customer who booked, or ADMIN
        if (user.getRole() != Role.ADMIN &&
            !booking.getUser().getId().equals(user.getId()) &&
            !booking.getGround().getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not have permission to view this booking");
        }

        return toDto(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getUserBookings(User user) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getOwnerBookings(User owner) {
        return bookingRepository.findByOwnerId(owner.getId()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookingsForAdmin() {
        return bookingRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponseDto cancelBooking(UUID bookingId, CancelBookingRequest request, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (user.getRole() != Role.ADMIN && !booking.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to cancel this reservation");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingException("This booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(request.getReason());

        if (booking.getPaymentStatus() == PaymentStatus.PAID) {
            booking.setPaymentStatus(PaymentStatus.REFUNDED);
        }

        Booking updated = bookingRepository.save(booking);
        log.info("Booking {} cancelled by user {}. Reason: {}", updated.getBookingReference(), user.getEmail(), request.getReason());

        return toDto(updated);
    }

    public BookingResponseDto toDto(Booking b) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(b.getId());
        dto.setBookingReference(b.getBookingReference());
        dto.setGroundId(b.getGround().getId());
        dto.setGroundName(b.getGround().getName());
        dto.setGroundAddress(b.getGround().getAddress());
        dto.setGroundCity(b.getGround().getCity());
        dto.setUserId(b.getUser().getId());
        dto.setCustomerName(b.getUser().getFullName());
        dto.setCustomerPhone(b.getUser().getPhone());
        dto.setBookingDate(b.getBookingDate());
        dto.setSlotType(b.getSlotType());
        dto.setStartTime(b.getStartTime());
        dto.setEndTime(b.getEndTime());
        dto.setTotalAmount(b.getTotalAmount());
        dto.setPaymentStatus(b.getPaymentStatus());
        dto.setPaymentMethod(b.getPaymentMethod());
        dto.setStatus(b.getStatus());
        dto.setNotes(b.getNotes());
        dto.setCancellationReason(b.getCancellationReason());
        dto.setCreatedAt(b.getCreatedAt());

        String imgUrl = "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?auto=format&fit=crop&w=1200&q=80";
        if (b.getGround().getImages() != null && !b.getGround().getImages().isEmpty()) {
            imgUrl = b.getGround().getImages().get(0).getImageUrl();
        }
        dto.setPrimaryImageUrl(imgUrl);

        return dto;
    }
}
