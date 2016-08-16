package com.esapos.lib.model.Component.HttpLibrary;


import java.io.Serializable;
import java.util.Map;

/**
 * Created by VickyLeu on 2016/7/14.
 * @Author Vickyleu
 * @Company Esapos
 *
 */
public final class HttpResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String requestUrl;
    private byte[] response;
    private int which;
    private Map<String, Object> attachParams;

    public HttpResponseModel(String requestUrl, byte[] response) {
        this.requestUrl = requestUrl;
        this.response = response;
    }

    public HttpResponseModel(String requestUrl, byte[] response, int which) {
        this.requestUrl = requestUrl;
        this.response = response;
        this.which = which;
    }

    public HttpResponseModel(String requestUrl, byte[] response, int which, Map<String, Object> attachParams) {
        this.requestUrl = requestUrl;
        this.response = response;
        this.which = which;
        this.attachParams = attachParams;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public byte[] getResponse() {
        return this.response;
    }

    public int getWhich() {
        return this.which;
    }

    public Map<String, Object> getAttachParams() {
        return this.attachParams;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}

