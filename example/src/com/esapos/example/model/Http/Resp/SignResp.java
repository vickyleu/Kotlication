package com.esapos.example.model.Http.Resp;



import com.esapos.lib.model.Component.Http.RespBody;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/22.
 * 上传电子签名接口
 */
public class SignResp extends RespBody {
    public String signOrderUrl;//电子签购单访问地址，当成功应答时才有

    @Override
    public RespBody parser(JSONObject json) {
        if (json == null) return null;
        signOrderUrl = json.optString("signOrderUrl");
        return super.parser(json);
    }
}
