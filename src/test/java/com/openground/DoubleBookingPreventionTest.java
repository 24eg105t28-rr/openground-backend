package com.openground;

import com.openground.dto.BookingRequest;
import com.openground.dto.BookingResponseDto;
import com.openground.entity.*;
import com.openground.exception.DoubleBookingException;
import com.openground.repository.GroundRepository;
import com.openground.repository.UserRepository;
import com.openground.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
class DoubleBookingPreventionTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private GroundRepository groundRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testDoubleBookingPrevention() {
        // Create test user and ground
        User owner = userRepository.save(new User(UUID.randomUUID(), "test.owner@cricket.org", "pass", "Owner", "123", Role.OWNER));
        User customer1 = userRepository.save(new User(UUID.randomUUID(), "team1@cricket.org", "pass", "Team 1 Captain", "123", Role.CUSTOMER));
        User customer2 = userRepository.save(new User(UUID.randomUUID(), "team2@cricket.org", "pass", "Team 2 Captain", "123", Role.CUSTOMER));

        Ground ground = groundRepository.save(new Ground(
                UUID.randomUUID(), owner, "Centurion Outdoor Stadium", "centurion-outdoor-stadium",
                "Full-size 70m boundary outdoor natural grass cricket stadium",
                "Sector 5", "Hyderabad", "Telangana", 70, PitchType.NATURAL_TURF,
                true, "Deluxe Pavilion", 2000, BigDecimal.valueOf(3000), GroundStatus.APPROVED
        ));

        LocalDate matchDate = LocalDate.now().plusDays(14);

        // 1. First team books MORNING slot
        BookingRequest req1 = new BookingRequest();
        req1.setGroundId(ground.getId());
        req1.setBookingDate(matchDate);
        req1.setSlotType(SlotType.MORNING);
        req1.setPaymentMethod(PaymentMethod.PAY_AT_GROUND);

        BookingResponseDto booking1 = bookingService.createBooking(req1, customer1);
        assertNotNull(booking1);
        assertNotNull(booking1.getBookingReference());
        assertEquals(BookingStatus.CONFIRMED, booking1.getStatus());

        // 2. Second team attempts to book SAME slot on SAME date -> DoubleBookingException
        BookingRequest req2 = new BookingRequest();
        req2.setGroundId(ground.getId());
        req2.setBookingDate(matchDate);
        req2.setSlotType(SlotType.MORNING);
        req2.setPaymentMethod(PaymentMethod.PAY_AT_GROUND);

        assertThrows(DoubleBookingException.class, () -> {
            bookingService.createBooking(req2, customer2);
        }, "Double booking must be rejected with DoubleBookingException");

        // 3. Second team attempts to book FULL_DAY slot overlapping the MORNING slot -> DoubleBookingException
        BookingRequest req3 = new BookingRequest();
        req3.setGroundId(ground.getId());
        req3.setBookingDate(matchDate);
        req3.setSlotType(SlotType.FULL_DAY);
        req3.setPaymentMethod(PaymentMethod.PAY_AT_GROUND);

        assertThrows(DoubleBookingException.class, () -> {
            bookingService.createBooking(req3, customer2);
        }, "Full day reservation overlapping existing session must be rejected");
    }
}
