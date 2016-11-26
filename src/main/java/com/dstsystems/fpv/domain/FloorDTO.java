package com.dstsystems.fpv.domain;

import java.io.Serializable;

/**
 * A Floor.
 */
public class FloorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] image;
    private String imageContentType;

    public FloorDTO(Floor floor) {
        this.image = floor.getImage();
        this.imageContentType = floor.getImageContentType();
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }
}
