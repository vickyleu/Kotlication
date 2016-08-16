package com.esapos.example.model.Http.Resp;

import com.esapos.lib.model.Component.Http.RespBody;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 *  查询交易明细接口
 */
public class QueryTransResp extends RespBody {
    public int totalNum;//符合条件的记录总数,单条查询 时,有则为 1 无则为 0;
    // 批量查询 时,返回符合条件的记录总数。
    public int listSize;//返回的记录数,单条查询时,有则 为 1 无则为 0;
    // 批量查询时,返回 应答中的记录数。
    public List<TransData> transList;//交易记录列表,
    // 单个交易记录详见TransData

    @Override
    public RespBody parser(JSONObject json) {
        if (json == null) return null;
        totalNum = json.optInt("totalNum");
        listSize = json.optInt("listSize");
        try {
            transList = new Gson().fromJson(json.optJSONArray("transList").toString(),
                    new TypeToken<ArrayList<TransData>>() {
                    }.getType());
        } catch (Exception e) {
            transList = new ArrayList<>();
        }


        return super.parser(json);
    }

    public class TransData {
        public String amount;//交易金额
        public String pan;//卡号
        public String voucherNo;//凭证号
        public String referenceNo;//系统参考号
        public String tradeDate;//交易日期
        public String tradeTime;//交易时间
        public String signOrderUrl;//电子签购单访问地址

        public TransData(String amount, String pan, String voucherNo, String referenceNo, String tradeDate, String tradeTime, String signOrderUrl) {
            this.amount = amount;
            this.pan = pan;
            this.voucherNo = voucherNo;
            this.referenceNo = referenceNo;
            this.tradeDate = tradeDate;
            this.tradeTime = tradeTime;
            this.signOrderUrl = signOrderUrl;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPan() {
            return pan;
        }

        public void setPan(String pan) {
            this.pan = pan;
        }

        public String getVoucherNo() {
            return voucherNo;
        }

        public void setVoucherNo(String voucherNo) {
            this.voucherNo = voucherNo;
        }

        public String getReferenceNo() {
            return referenceNo;
        }

        public void setReferenceNo(String referenceNo) {
            this.referenceNo = referenceNo;
        }

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

        public String getSignOrderUrl() {
            return signOrderUrl;
        }

        public void setSignOrderUrl(String signOrderUrl) {
            this.signOrderUrl = signOrderUrl;
        }

        @Override
        public String toString() {
            return "TransData{" +
                    "amount='" + amount + '\'' +
                    ", pan='" + pan + '\'' +
                    ", voucherNo='" + voucherNo + '\'' +
                    ", referenceNo='" + referenceNo + '\'' +
                    ", tradeDate='" + tradeDate + '\'' +
                    ", tradeTime='" + tradeTime + '\'' +
                    ", signOrderUrl='" + signOrderUrl + '\'' +
                    '}';
        }
    }
}
