package com.openground.dto;

import com.openground.entity.PaymentMethod;
import com.openground.entity.SlotType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public class BookingRequest {

    @NotNull(message = "Ground ID is required")
    private UUID groundId;

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;

    @NotNull(message = "Slot type is required")
    private SlotType slotType;

    private PaymentMethod paymentMethod = PaymentMethod.PAY_AT_GROUND;

    private String notes;

    public BookingRequest() {
    }

    public UUID getGroundId() { return groundId; }
    public void setGroundId(UUID groundId) { this.groundId = groundId; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public SlotType getSlotType() { return slotType; }
    public void setSlotType(SlotType slotType) { this.slotType = slotType; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
