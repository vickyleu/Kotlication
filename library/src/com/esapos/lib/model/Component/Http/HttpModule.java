package com.esapos.lib.model.Component.Http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class HttpModule<D extends RespBody> implements Parser<D> {
    private static final String TAG = HttpModule.class.getSimpleName();

    public int retCode = -1;
    public int commandId = -1;
    public String errMsg = "";
    public String tokenId = "";
    public String reqNode = "";
    public D body = null;



    @Override
    public Parser parser(String result, D body) throws Exception {
        JSONObject resp = null;
        try {
            resp = new JSONObject(result);
            retCode = resp.optInt("retCode", -1);
            commandId = resp.optInt("commandId", -1);
            errMsg = resp.optString("errMsg", null);
            tokenId = resp.optString("tokenId", null);
            reqNode = resp.optString("reqNode", null);
            body.setBody(resp.optJSONObject("body"));
            this.body = body;
        } catch (JSONException ignored) {
        }
        return this;
    }

    @Override
    public String toString() {
        return "HttpModule{" +
                "retCode=" + retCode +
                ", commandId=" + commandId +
                ", errMsg='" + errMsg + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", reqNode='" + reqNode + '\'' +
                ", body=" + body +
                '}';
    }
}

