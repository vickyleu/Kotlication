package com.esapos.lib.model.Component.HttpLibrary;



/**
 * Created by VickyLeu on 2016/7/14.
 * @Author Vickyleu
 * @Company Esapos
 *
 */
public interface HttpHandler {
    void httpErr(HttpResponseModel var1) throws Exception;

    void httpSuccess(HttpResponseModel var1) throws Exception;
}

