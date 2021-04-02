package com.gastos.gastosprovider;

public class QRCodeRVModal {

    private String scannedCode;
    private String qrCodeImg;
    private String upiId;

    public QRCodeRVModal(){

    }
    public String getScannedCode() {
        return scannedCode;
    }

    public void setScannedCode(String scannedCode) {
        this.scannedCode = scannedCode;
    }

    public String getQrCodeImg() {
        return qrCodeImg;
    }

    public void setQrCodeImg(String qrCodeImg) {
        this.qrCodeImg = qrCodeImg;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public QRCodeRVModal(String scannedCode, String qrCodeImg, String upiId) {
        this.scannedCode = scannedCode;
        this.qrCodeImg = qrCodeImg;
        this.upiId = upiId;
    }
}
