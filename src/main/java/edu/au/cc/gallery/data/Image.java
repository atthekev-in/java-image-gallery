package edu.au.cc.gallery.data;

import java.util.UUID;

public class Image {
    
    private String imageId;
    private String username;
    private String imageString;

    public Image(String username) {
        this.username = username;
        this.imageId = UUID.randomUUID().toString();
    }
    public Image(String imageId, String username) {
        this.imageId = imageId;
        this.username = username;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    
}
