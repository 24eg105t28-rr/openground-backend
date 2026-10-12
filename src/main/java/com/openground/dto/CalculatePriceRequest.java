package com.openground.dto;

import com.openground.entity.SlotType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CalculatePriceRequest {

    @NotNull(message = "Ground ID is required")
    private UUID groundId;

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;

    @NotNull(message = "Slot type is required")
    private SlotType slotType;

    public CalculatePriceRequest() {
    }

    public CalculatePriceRequest(UUID groundId, LocalDate bookingDate, SlotType slotType) {
        this.groundId = groundId;
        this.bookingDate = bookingDate;
        this.slotType = slotType;
    }

    public UUID getGroundId() { return groundId; }
    public void setGroundId(UUID groundId) { this.groundId = groundId; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public SlotType getSlotType() { return slotType; }
    public void setSlotType(SlotType slotType) { this.slotType = slotType; }
}
