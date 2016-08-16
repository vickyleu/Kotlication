package com.esapos.example.model.Http.Resp;


import com.esapos.lib.model.Component.Http.RespBody;
import com.esapos.example.controller.AppCenter;

import org.json.JSONObject;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * 更新工作密钥接口
 */
public class UpdateTermkeyResp extends RespBody {
    public String mchtNo;//	  商户号
    public String mchtName;//	  商户名称
    public String termNo;//	  终端号
    public String termKey;//	  终端秘钥

    @Override
    public RespBody parser(JSONObject json) {
        if (json == null) return null;
        mchtNo = json.optString("mchtNo");
        AppCenter.getInstance().setMctNo(mchtNo);
        mchtName = json.optString("mchtName");
        AppCenter.getInstance().setMctName(mchtName);
        termNo = json.optString("termNo");
        termKey = json.optString("termKey");

        return super.parser(json);
    }
}
