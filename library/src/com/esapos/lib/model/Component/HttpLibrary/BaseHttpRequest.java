package com.esapos.lib.model.Component.HttpLibrary;


import android.os.Message;
import android.text.TextUtils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
final class BaseHttpRequest extends Thread {
    BaseHttpConnectPool.Header header;
    String method;
    int timeout;
    String requestUrl;
    Object params;
    BaseHttpHandler callBack;
    int which;
    String errUrl;
    Map<String, Object> attachParams;
    private String requestTag;
    private boolean requesting;

    public void setRequesting(boolean requesting) {
        this.requesting = requesting;
    }

    BaseHttpRequest(String errUrl, String requestUrl, Object params, String method, BaseHttpConnectPool.Header header, BaseHttpHandler callBack, int timeout, int which, Map<String, Object> attachParams) {
        this.requestUrl = requestUrl;
        this.params = params;
        this.callBack = callBack;
        this.method = method;
        this.header = header;
        this.which = which;
        this.attachParams = attachParams;
        this.requestTag = requestUrl + which;
        this.timeout = timeout;
        this.errUrl=errUrl;
    }

    public void run() {
        super.run();
        this.requesting = true;
        if (BaseHttpConnectPool.DEBUG) {
            Logger.log("requestUrl:" + this.requestUrl);
            Logger.log("requestParams:" + this.params);
        }


        try {
            connect(method);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        this.requesting = false;
        BaseHttpConnectPool.removeRequest(this.requestTag);
    }

    private void connect(String method) {

        String methodCopy = method.replace("attach", "");
        if (BaseHttpConnectPool.DEBUG)
            Logger.log("requestMethod:" + methodCopy);

        Message message = new Message();
        HttpResponseModel model = new HttpResponseModel(this.requestUrl, (byte[]) null, this.which, this.attachParams);
        message.obj = model;
        try {
            URL e = new URL(this.requestUrl);
            HttpURLConnection conn = (HttpURLConnection) e.openConnection();
            if (header != null) {
                Set<Map.Entry<String, String>> kv = header.keyVaule();
                for (Map.Entry<String, String> m : kv) {
                    if (!TextUtils.isEmpty(m.getValue()))
                        conn.addRequestProperty(m.getKey(), m.getValue());
                }
            }

            conn.setRequestMethod(method);

            if (BaseHttpConnectPool.DEBUG) {
                Set<Map.Entry<String, List<String>>> es = conn.getRequestProperties().entrySet();
                for (Map.Entry<String, List<String>> m : es) {
                    Logger.log("header " + m.getKey() + ": " + m.getValue());
                }
            }


            try {
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout);
                if (!method.equals("GET") && !method.equals("DELETE"))
                    conn.setDoOutput(true);
                conn.connect();
                if (!method.equals("GET") && !method.equals("DELETE")) {
                    OutputStream outputStream = conn.getOutputStream();
                    byte[] reqParams = objToByteArray(this.params);
                    if (reqParams != null) {
                        outputStream.write(reqParams);
                        outputStream.flush();
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            if (conn != null) {
                if (BaseHttpConnectPool.DEBUG)
                    Logger.log("responseCode:" + conn.getResponseCode());
                if (conn.getResponseCode() != 200) {
                    if (this.callBack != null) {
                        if (!TextUtils.isEmpty(errUrl)){
                            model.setRequestUrl(errUrl);
                        }
                        message.what = HttpResponseMsgType.RESPONSE_ERR;
                        switch (conn.getResponseCode()){
                            case 404:
                                model.setResponse("Server Not Found".getBytes());
                                break;
                            case 500:
                                model.setResponse("Server Inner Err".getBytes());
                                break;
                            default:
                                model.setResponse("Unknown Err".getBytes());
                                break;
                        }

                        this.callBack.sendMessage(message);
                    }
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer data = new StringBuffer();

                    String line;
                    while ((line = br.readLine()) != null && this.requesting) {
                        data.append(line);
                    }
                    if (BaseHttpConnectPool.DEBUG)
                        Logger.log(new String(data));
                    if (this.requesting) {
                        if (this.callBack != null) {
                            message.what = HttpResponseMsgType.RESPONSE_SUCCESS;
                            model.setResponse(data.toString().getBytes());
                            this.callBack.sendMessage(message);
                        }
                    } else {
                        if (BaseHttpConnectPool.DEBUG)
                            Logger.log("request cancle");
                    }
                }
                conn.disconnect();
            }

        } catch (Exception e) {
            if (this.callBack != null) {
                if (e instanceof SocketTimeoutException) {
                    model.setResponse("Connect Time Out".getBytes());
                } else {
                    if (!HttpCacheApp.getInstance().checkNetwork()){
                        model.setResponse("Network is unreachable or permission is missing!!!!".getBytes());
                    }else model.setResponse(("Connect error:"+e.getMessage()).getBytes());


                }
                if (!TextUtils.isEmpty(errUrl)){
                    model.setRequestUrl(errUrl);
                }
                message.what = HttpResponseMsgType.RESPONSE_ERR;
                this.callBack.sendMessage(message);
            }
        }

    }


    private static byte[] objToByteArray(Object params) {
        try {
            if (params == null) {
                return null;
            }

            if (params instanceof JSONObject) {
                return params.toString().getBytes();
            }

            if (params instanceof JSONArray) {
                return params.toString().getBytes();
            }

            if (params instanceof String) {
                return params.toString().getBytes();
            }

            if (params instanceof Map) {
                JSONObject e = new JSONObject();
                Set set = ((Map) params).entrySet();

                for (Object obj : set) {
                    Map.Entry entry = (Map.Entry) obj;
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (key != null) {
                        if (value == null) {
                            e.put(key.toString(), "");
                        } else {
                            e.put(key.toString(), value);
                        }
                    }
                }

                return e.toString().getBytes();
            }

            if (params instanceof byte[]) {
                return (byte[]) params;
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return null;
    }
}

