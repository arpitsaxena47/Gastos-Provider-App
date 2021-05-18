package com.gastos.gastosprovider.Setting.PaymentInformation;

public class QRCodeRVModal {

    private String upiName;
    private String upiId;

    public QRCodeRVModal(String upiName, String upiId) {
        this.upiName = upiName;
        this.upiId = upiId;
    }

    public String getUpiName() {
        return upiName;
    }

    public void setUpiName(String upiName) {
        this.upiName = upiName;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
}
