package com.gastos.gastosprovider;

public class SliderModal {

    private int imgId;
    private String title;
    private String description;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SliderModal(int imgId, String title, String description) {
        this.imgId = imgId;
        this.title = title;
        this.description = description;
    }
}
