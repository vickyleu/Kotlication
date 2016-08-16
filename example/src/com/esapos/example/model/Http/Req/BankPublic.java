package com.esapos.example.model.Http.Req;

/**
 * Created by Administrator on 2016/7/22.
 * BankPublic数据
 */
public class BankPublic {
    public String voucherNo = ""; //凭证号
    public String sn = ""; //设备序列号
    public String cardType = ""; //刷卡类型,磁条为 MagCard,IC 卡 为 IcCard,非接卡为 QIcCard
    public String pan = ""; //卡号
    public String pin = ""; //卡密

    public String track2 = ""; //二磁道数据
    public String track3 = ""; //三磁道数据
    public String currency = ""; //货币代码


    public String iccExpiryDay = ""; //IC 卡有效期
    public String iccSn = ""; //IC 卡序列号
    public String icData = ""; //IC 卡数据


    public transient boolean empty = true;//初始化方式,标记变量,非BankPublic元素

    /**
     * 完整参数
     *
     * @param voucherNo
     * @param sn
     * @param pan
     * @param track2
     * @param currency
     * @param cardType
     * @param pin
     * @param iccExpiryDay
     * @param track3
     * @param iccSn
     * @param icData
     */
    public BankPublic(String voucherNo, String sn, String cardType, String pan, String pin,
                      String track2, String track3, String currency,
                      String iccExpiryDay, String iccSn, String icData) {
        setEmpty(false);
        this.voucherNo = voucherNo != null ? voucherNo : "";
        this.sn = sn != null ? sn : "";
        this.cardType = cardType != null ? cardType : "";
        this.pan = pan != null ? pan : "";
        this.pin = pin != null ? pin : "";
        this.track2 = track2 != null ? track2 : "";
        this.track3 = track3 != null ? track3 : "";
        this.currency = currency != null ? currency : "";
        this.iccExpiryDay = iccExpiryDay != null ? iccExpiryDay : "";
        this.iccSn = iccSn != null ? iccSn : "";
        this.icData = icData != null ? icData : "";
    }

    public void setIccExpiryDay(String iccExpiryDay) {
        this.iccExpiryDay = iccExpiryDay;
    }

    public void setIccSn(String iccSn) {
        this.iccSn = iccSn;
    }

    public void setIcData(String icData) {
        this.icData = icData;
    }

    public BankPublic() {
        setEmpty(true);
    }

    public boolean isEmpty() {
        return empty;
    }

    private void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    @Override
    public String toString() {
        return "BankPublic{" +
                "voucherNo='" + voucherNo + '\'' +
                ", sn='" + sn + '\'' +
                ", cardType='" + cardType + '\'' +
                ", pan='" + pan + '\'' +
                ", pin='" + pin + '\'' +
                ", track2='" + track2 + '\'' +
                ", track3='" + track3 + '\'' +
                ", currency='" + currency + '\'' +
                ", iccExpiryDay='" + iccExpiryDay + '\'' +
                ", iccSn='" + iccSn + '\'' +
                ", icData='" + icData + '\'' +
                '}';
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public void writeData(String voucherNo, String sn, String cardType, String pan, String pin, String track2,
                          String track3, String currency, String iccExpiryDay, String iccSn, String icData) {
        setEmpty(false);
        this.voucherNo = voucherNo != null ? voucherNo : "";
        this.sn = sn != null ? sn : "";
        this.cardType = cardType != null ? cardType : "";
        this.pan = pan != null ? pan : "";
        this.pin = pin != null ? pin : "";
        this.track2 = track2 != null ? track2 : "";
        this.track3 = track3 != null ? track3 : "";
        this.currency = currency != null ? currency : "";
        this.iccExpiryDay = iccExpiryDay != null ? iccExpiryDay : "";
        this.iccSn = iccSn != null ? iccSn : "";
        this.icData = icData != null ? icData : "";
    }
}
