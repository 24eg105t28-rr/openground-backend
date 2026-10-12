package com.openground.repository;

import com.openground.entity.Booking;
import com.openground.entity.BookingStatus;
import com.openground.entity.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    Optional<Booking> findByBookingReference(String bookingReference);
    List<Booking> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<Booking> findByGroundIdOrderByBookingDateDesc(UUID groundId);

    @Query("SELECT b FROM Booking b WHERE b.ground.owner.id = :ownerId ORDER BY b.createdAt DESC")
    List<Booking> findByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("SELECT b FROM Booking b WHERE b.ground.id = :groundId " +
           "AND b.bookingDate = :date AND b.status = 'CONFIRMED'")
    List<Booking> findActiveBookingsOnDate(@Param("groundId") UUID groundId, @Param("date") LocalDate date);

    boolean existsByGroundIdAndBookingDateAndSlotTypeAndStatus(
            UUID groundId, LocalDate bookingDate, SlotType slotType, BookingStatus status
    );

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.ground.id = :groundId " +
           "AND b.bookingDate = :bookingDate " +
           "AND b.status = 'CONFIRMED' " +
           "AND (:slotType = 'FULL_DAY' OR b.slotType = 'FULL_DAY' OR b.slotType = :slotType)")
    boolean isSlotOverlapping(@Param("groundId") UUID groundId,
                             @Param("bookingDate") LocalDate bookingDate,
                             @Param("slotType") SlotType slotType);
}
