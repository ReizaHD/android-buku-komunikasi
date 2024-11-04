package com.bintangjuara.bk.models;

public class CarouselItem {
    private int imageResource;
    private String text, imageUrl;

    public CarouselItem(String imageUrl, String text) {
        this.imageUrl = imageUrl;
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }
}
