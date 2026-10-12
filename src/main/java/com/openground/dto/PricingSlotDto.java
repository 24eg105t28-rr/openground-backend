package com.openground.dto;

import com.openground.entity.SlotType;
import java.math.BigDecimal;
import java.util.UUID;

public class PricingSlotDto {
    private UUID id;
    private SlotType slotType;
    private String startTime;
    private String endTime;
    private BigDecimal weekdayPrice;
    private BigDecimal weekendPrice;

    public PricingSlotDto() {
    }

    public PricingSlotDto(UUID id, SlotType slotType, String startTime, String endTime,
                          BigDecimal weekdayPrice, BigDecimal weekendPrice) {
        this.id = id;
        this.slotType = slotType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekdayPrice = weekdayPrice;
        this.weekendPrice = weekendPrice;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public SlotType getSlotType() { return slotType; }
    public void setSlotType(SlotType slotType) { this.slotType = slotType; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public BigDecimal getWeekdayPrice() { return weekdayPrice; }
    public void setWeekdayPrice(BigDecimal weekdayPrice) { this.weekdayPrice = weekdayPrice; }

    public BigDecimal getWeekendPrice() { return weekendPrice; }
    public void setWeekendPrice(BigDecimal weekendPrice) { this.weekendPrice = weekendPrice; }
}
