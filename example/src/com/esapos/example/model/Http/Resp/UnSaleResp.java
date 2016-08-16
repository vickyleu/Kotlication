package com.esapos.example.model.Http.Resp;


import com.esapos.lib.model.Component.Http.RespBody;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/22.
 * 消费撤销接口
 */
public class UnSaleResp extends RespBody {
    public String amount;//交易金额
    public String voucherNo;//凭证号
    public String referenceNo;//系统参考号
    public String tradeDate;//交易日期
    public String tradeTime;//交易时间

    public String mchtNo;//商户号
    public String termNo;//终端号
    public String issBank;//发卡行
    public String acquirer;//收单机构
    public String pan;//卡号

    @Override
    public RespBody parser(JSONObject json) {
        if (json == null) return null;
        amount = json.optString("amount");
        voucherNo = json.optString("voucherNo");
        referenceNo = json.optString("referenceNo");
        tradeDate = json.optString("tradeDate");
        tradeTime = json.optString("tradeTime");

        mchtNo = json.optString("mchtNo");
        termNo = json.optString("termNo");
        issBank = json.optString("issBank");
        acquirer = json.optString("acquirer");
        pan = json.optString("pan");
        pan = json.optString("pan");
        return super.parser(json);
    }
}
