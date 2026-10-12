package com.openground.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pricing_slots")
public class PricingSlot {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ground_id")
    private Ground ground;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot_type", nullable = false)
    private SlotType slotType;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @Column(name = "weekday_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal weekdayPrice;

    @Column(name = "weekend_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal weekendPrice;

    public PricingSlot() {
    }

    public PricingSlot(UUID id, Ground ground, SlotType slotType, String startTime, String endTime,
                       BigDecimal weekdayPrice, BigDecimal weekendPrice) {
        this.id = id != null ? id : UUID.randomUUID();
        this.ground = ground;
        this.slotType = slotType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekdayPrice = weekdayPrice;
        this.weekendPrice = weekendPrice;
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Ground getGround() { return ground; }
    public void setGround(Ground ground) { this.ground = ground; }

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
