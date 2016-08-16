package com.esapos.lib.model.Component.Http;

import org.json.JSONObject;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class RespBody {
    RespBody body;
    public RespBody() {
    }

    public RespBody setBody(JSONObject json) {
        body = parser(json);
        return this;
    }

    public RespBody parser(JSONObject json) {
        return this;
    }
}