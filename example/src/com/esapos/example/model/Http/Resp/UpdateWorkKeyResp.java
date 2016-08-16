package com.esapos.example.model.Http.Resp;



import com.esapos.lib.model.Component.Http.RespBody;

import org.json.JSONObject;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * 更新工作密钥接口
 */
public class UpdateWorkKeyResp extends RespBody {
    public String pinKey;//PIN 密钥
    public String pinKeyCheckValue;//PIN 密钥校验值
    public String macKey;//MAC 密钥
    public String macKeyCheckValue;//MAC 密钥校验值
    public String trackKey;//磁道密钥
    public String trackKeyCheckValue;//磁道密钥校验值

    @Override
    public RespBody parser(JSONObject json) {
        if (json == null) return null;
        pinKey = json.optString("pinKey");
        pinKeyCheckValue = json.optString("pinKeyCheckValue");
        macKey = json.optString("macKey");
        macKeyCheckValue = json.optString("macKeyCheckValue");
        trackKey = json.optString("trackKey");
        trackKeyCheckValue = json.optString("trackKeyCheckValue");
        return super.parser(json);
    }
}
