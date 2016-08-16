package com.esapos.example.model.Http.Req;


import com.esapos.example.controller.AppCenter;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class CheckVersionReq {
    public String pkgName;//应用包名
    public String verName;//版本名称
    public int verCode;//版本编号
    public int merId;//内部商户编码
    private String reqNode;

    private CheckVersionReq(String pkgName, String verName, int verCode, int merId, String reqNode) {
        this.pkgName = pkgName;
        this.verName = verName;
        this.verCode = verCode;
        this.merId = merId;
        this.reqNode = reqNode;
    }

    public CheckVersionReq() {
        this(AppCenter.getInstance().getAppPkg(), AppCenter.getInstance().getVerName(), AppCenter.getInstance().getVerCode(), 8090, "APP");
//        this("appstore.esapos.com", "1.0.0",1, 8090, "APP");
    }

    @Override
    public String toString() {
        return "CheckVersionReq{" +
                "pkgName='" + pkgName + '\'' +
                ", verName='" + verName + '\'' +
                ", verCode=" + verCode +
                ", merId=" + merId +
                '}';
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public int getMerId() {
        return merId;
    }

    public void setMerId(int merId) {
        this.merId = merId;
    }

    public String getReqNode() {
        return reqNode;
    }
}
