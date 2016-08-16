package com.esapos.example.model.Http.Req;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class SignReq {
    private String tradeDate;
    private String tradeTime;
    private String referenceNo;
    private String signImage;

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getSignImage() {
        return signImage;
    }

    public void setSignImage(String signImage) {
        this.signImage = signImage;
    }

    public SignReq(String tradeDate, String tradeTime, String referenceNo, String signImage) {
        this.tradeDate = tradeDate;
        this.tradeTime = tradeTime;
        this.referenceNo = referenceNo;
        this.signImage = signImage;
    }

    @Override
    public String toString() {
        return "SignReq{" +
                "tradeDate='" + tradeDate + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", signImage='" + signImage + '\'' +
                '}';
    }
}
