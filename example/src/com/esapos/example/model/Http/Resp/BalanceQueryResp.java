package com.esapos.example.model.Http.Resp;



import com.esapos.lib.model.Component.Http.RespBody;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/22.
 *  余额查询接口
 */

public class BalanceQueryResp extends RespBody {
    public String balanceAmount;//余额

    @Override
    public RespBody parser(JSONObject json) {
        if (json == null) return null;
        balanceAmount = json.optString("balanceAmount");
        return super.parser(json);
    }
}
