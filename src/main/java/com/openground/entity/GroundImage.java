package com.openground.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ground_images")
public class GroundImage {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ground_id")
    private Ground ground;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column
    private String caption;

    public GroundImage() {
    }

    public GroundImage(UUID id, Ground ground, String imageUrl, Boolean isPrimary, String caption) {
        this.id = id != null ? id : UUID.randomUUID();
        this.ground = ground;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary != null ? isPrimary : false;
        this.caption = caption;
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

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
}
