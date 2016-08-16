package com.esapos.example.model.Http.Resp;


import com.esapos.lib.model.Component.Http.RespBody;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/22.
 * 消费接口
 */
public class CheckVersionResp extends RespBody {
    public int flags;//是否有更新
    public int verCode;//版本编号
    public int needUpdate;//是否强制升级，0表示不强制，1表示强制
    public String releaseNotes;//更新内容说明
    public String downloadAddr;//版本下载地址
    public int pkgSize;//应用包大小
    public String verName;//版本名称

    @Override
    public RespBody parser(JSONObject json) {
        if (json == null) return null;
        flags = json.optInt("flags");
        verCode = json.optInt("verCode");
        needUpdate = json.optInt("needUpdate");
        releaseNotes = json.optString("releaseNotes");
        downloadAddr = json.optString("downloadAddr");
        pkgSize = json.optInt("pkgSize");
        verName = json.optString("verName");

        return super.parser(json);
    }
}
