package com.esapos.example.controller.Component;

import android.app.Activity;
import android.util.Log;

import com.esapos.lib.Controller.DialogHelper;
import com.esapos.lib.model.Component.Http.HttpModule;
import com.esapos.lib.model.Component.HttpLibrary.BaseHttpConnectPool;
import com.esapos.lib.model.Component.HttpLibrary.BaseHttpHandler;
import com.esapos.lib.model.Component.HttpLibrary.HttpHandler;
import com.esapos.lib.model.Component.HttpLibrary.HttpResponseModel;
import com.esapos.example.R;
import com.esapos.example.controller.AppCenter;
import com.esapos.example.model.IConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by VickyLeu on 2016/7/20.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public abstract class HttpClient {
    BaseHttpConnectPool http;

    protected HttpClient() {
        http = AppCenter.http();
    }


    public void shutdown() {
        http.removeAllRequest();
    }

    public void HttpObservable(final Activity activity, final HttpHandler handler,
                               final int req, final String httpMethod, final Object[] object,final Map<String, Object> attach) {
        this.HttpObservable(activity, handler, req, httpMethod, true, object, attach);
    }
    public void HttpObservable(final Activity activity, final HttpHandler handler,
                               final int req, final String httpMethod, final Object[] object) {
        this.HttpObservable(activity, handler, req, httpMethod, true, object, null);
    }

    public void HttpObservable(final Activity activity, final HttpHandler handler,
                               final int req, final String httpMethod, final boolean errorUrlFlag, final Object[] object, final Map<String, Object> attach) {
        Observable.create(new Observable.OnSubscribe<HttpResponseModel>() {
            @Override
            public void call(final Subscriber<? super HttpResponseModel> subscriber) {
                DialogHelper.getInstance().createPrompt(activity, "请稍候",
                        DialogHelper.DialogTheme.RECT, false);
                DialogHelper.getInstance().openPrompt();
                String s = "";
                switch (req) {
                    case IConstant.commandId.BalanceQueryReq:
                        s = "BalanceQueryReq";
                        break;
                    case IConstant.commandId.SaleReq:
                        s = "SaleReq";
                        break;
                    case IConstant.commandId.UnSaleReq:
                        s = "UnSaleReq";
                        break;
                    case IConstant.commandId.UpdateWorkKeyReq:
                        s = "UpdateWorkKeyReq";
                        break;
                    case IConstant.commandId.UpdateTermkeyReq:
                        s = "UpdateWorkKeyReq";
                        break;
                    case IConstant.commandId.CheckVersionReq:
                        s = "CheckVersionReq";
                        break;
                    case IConstant.commandId.SignReq:
                        s = "SignReq";
                        break;
                    case IConstant.commandId.QueryTransReq:
                        s = "QueryTransReq";
                        break;
                }

                String url ="";
                if (!s.equals("CheckVersionReq")){
                    url=BaseUrl();
                }else url=IConstant.UPDATE_URL;


                if (BaseHttpConnectPool.DEBUG)
                    Log.e("com.vicky.http:", "==========================================================================================");
                if (errorUrlFlag) {
                    http.addRequest(s,url , getParams(req, object), httpMethod
                            , new BaseHttpConnectPool.Header(),
                            new BaseHttpHandler(new HttpHandler() {
                                @Override
                                public void httpErr(HttpResponseModel var1) throws Exception {
                                    DialogHelper.getInstance().closePrompt();
                                    handler.httpErr(var1);
                                }

                                @Override
                                public void httpSuccess(HttpResponseModel var1) throws Exception {
                                    DialogHelper.getInstance().closePrompt();
                                    handler.httpSuccess(var1);
                                }
                            }), 1000*60+5);
                } else {
                    http.addRequest(url, getParams(req, object), httpMethod
                            , new BaseHttpConnectPool.Header(),
                            new BaseHttpHandler(new HttpHandler() {
                                @Override
                                public void httpErr(HttpResponseModel var1) throws Exception {
                                    DialogHelper.getInstance().closePrompt();
                                    handler.httpErr(var1);
                                }

                                @Override
                                public void httpSuccess(HttpResponseModel var1) throws Exception {
                                    DialogHelper.getInstance().closePrompt();
                                    handler.httpSuccess(var1);
                                }
                            }), 1000*60+5);
                }

            }
        }).subscribe(new Subscriber<HttpResponseModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(HttpResponseModel httpResponseModel) {

            }
        });

    }

    protected abstract String BaseUrl();

    public HttpModule ResponseDataPick(HttpResponseModel var1, JSONObject json) {

        return null;
    }


    public class HttpMethod {
        public static final String POST = "POST";
        public static final String GET = "GET";
        public static final String DELETE = "DELETE";
    }

    private JSONObject getParams(int option, Object... object) {
        JSONObject params = new JSONObject();
        try {
            params = SetDataByYourSelf(option, params, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    protected abstract JSONObject SetDataByYourSelf(int option, JSONObject params, Object[] object) throws Exception;


    protected void putIntToJson(JSONObject body, String key, int value) throws JSONException {

        try {
            body.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void putStringToJson(JSONObject body, String key, String value) throws JSONException {
        if (value == null) value = "";
        body.put(key, value);
    }

    protected void putObjToJson(JSONObject body, Object value) throws JSONException {
        if (value == null) value = new JSONObject();
        body.put(getString(R.string.Public), (JSONObject) value);
    }

    protected String getString(int res) {
        return AppCenter.getInstance().getString(res);
    }
}
