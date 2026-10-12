package com.openground.dto;

import java.util.UUID;

public class GroundImageDto {
    private UUID id;
    private String imageUrl;
    private boolean primary;
    private String caption;

    public GroundImageDto() {
    }

    public GroundImageDto(UUID id, String imageUrl, boolean primary, String caption) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.primary = primary;
        this.caption = caption;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
}
