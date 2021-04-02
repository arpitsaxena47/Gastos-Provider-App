package com.gastos.gastosprovider;

public class UserModal {
    private String userName;
    private String userAmount;
    private String date;
    private String imgUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(String userAmount) {
        this.userAmount = userAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public UserModal(String userName, String userAmount, String date, String imgUrl) {
        this.userName = userName;
        this.userAmount = userAmount;
        this.date = date;
        this.imgUrl = imgUrl;
    }
}
