package com.esapos.lib.model.Component.Http;


import com.esapos.lib.model.Component.HttpLibrary.HttpResponseModel;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class HttpResponseFactory {
    static HttpResponseFactory factory;

    public static HttpResponseFactory obj() {
        if (factory == null) factory = new HttpResponseFactory();
        return factory;
    }
    public <D extends RespBody>HttpModule<D> buildModule(D clazz, HttpResponseModel var1) {
        HttpModule<D> module;
        HttpModule<D> hm = new HttpModule<>();
        module = buildModule(hm, clazz, var1);
        return module;
    }

    private  <F extends RespBody, D extends HttpModule<F>> D buildModule(D d, F f, HttpResponseModel var1) {
        try {
            d.parser(new String(var1.getResponse()), f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
}
