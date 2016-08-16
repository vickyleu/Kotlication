package com.esapos.lib.model.Component.HttpLibrary;




import com.esapos.lib.Controller.DialogHelper;
import com.esapos.lib.Utils.SystemTime;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
@SuppressWarnings("unchecked")
public final class BaseHttpConnectPool {
    public static final boolean DEBUG = true;
    private static Map<String, BaseHttpRequest> httpRequests = new ConcurrentHashMap();

    public static BaseHttpConnectPool getInstance() {
        return Hcp.httpConnectionPool;
    }

    private BaseHttpConnectPool(BaseHttpConnectPool baseHttpConnectPool) {
    }

    public final void removeAllRequest() {
        try {
            Set e = httpRequests.entrySet();

            for (Object obj : e) {
                Map.Entry entry = (Map.Entry) obj;
                ((BaseHttpRequest) entry.getValue()).setRequesting(false);
            }

            httpRequests.clear();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private void removeDieRequest() {
        try {
            Set e = httpRequests.entrySet();

            for (Object obj : e) {
                Map.Entry entry = (Map.Entry) obj;
                if (!((BaseHttpRequest) entry.getValue()).isAlive()) {
                    //noinspection SuspiciousMethodCalls
                    httpRequests.remove(entry.getKey());
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static void removeRequest(String requestTag) {
        if (requestTag != null) {
            try {
                if (httpRequests.containsKey(requestTag)) {
                    httpRequests.remove(requestTag);
                }
            } catch (Exception var2) {
                var2.printStackTrace();
            }

        }
    }

    public synchronized void addRequest(String errUrl, String requestUrl, Object params, String method, Header header, BaseHttpHandler callBack, int timeout, int which, Map<String, Object> attachParams) {
        synchronized (this) {
            if (DEBUG)
                Logger.log("requestStartTime:" + SystemTime.time());
            this.removeDieRequest();
            String requestTag = requestUrl;
            if (which != -1) {
                requestTag = requestUrl + which;
            }

            if (httpRequests.containsKey(requestTag)) {
                if (DEBUG)
                    Logger.log("requestNotAllow");
//                DialogHelper.getInstance().closePrompt();
            } else {
//                DialogHelper.getInstance().closePrompt();
                BaseHttpRequest baseHttpRequest = new BaseHttpRequest(errUrl,requestUrl, params, method, header, callBack, timeout, which, attachParams);
                httpRequests.put(requestTag, baseHttpRequest);
                baseHttpRequest.start();
            }
        }
    }

    public synchronized void addRequest(String requestUrl, Object params, String method, Header header, BaseHttpHandler callBack, int timeout, int which) {
        this.addRequest(null, requestUrl, params, method, header, callBack, timeout, which, null);
    }

    public synchronized void addRequest(String errUrl, String requestUrl, Object params, String method, Header header, BaseHttpHandler callBack, int timeout, int which) {
        this.addRequest(errUrl, requestUrl, params, method, header, callBack, timeout, which, null);
    }

    public synchronized void addRequest(String errUrl, String requestUrl, Object params, String method, Header header, BaseHttpHandler callBack, int timeout) {
        this.addRequest(errUrl, requestUrl, params, method, header, callBack, timeout, -1, null);
    }

    public synchronized void addRequest(String requestUrl, Object params, String method, Header header, BaseHttpHandler callBack, int timeout) {
        this.addRequest(null, requestUrl, params, method, header, callBack, timeout, -1, null);
    }

    public synchronized void addRequest(String errUrl, String requestUrl, Object params, String method, Header header, BaseHttpHandler callBack, Map<String, Object> attachParams) {
        this.addRequest(errUrl, requestUrl, params, method, header, callBack, 10000, -1, attachParams);
    }

    public synchronized void addRequest(String requestUrl, Object params, String method, Header header, BaseHttpHandler callBack, Map<String, Object> attachParams) {
        this.addRequest(null, requestUrl, params, method, header, callBack, 10000, -1, attachParams);
    }

    private static class Hcp {
        static BaseHttpConnectPool httpConnectionPool = new BaseHttpConnectPool((BaseHttpConnectPool) null);

        private Hcp() {
        }
    }

    public static class Header {
        private final Map<String, String> kv = new HashMap<String, String>() {
            {
                put("Content-Type", "application/hal+json;charset=UTF-8");
                put("Connection", "Keep-Alive");
                put("Content-Length", "");
                put("Host", "");
            }
        };

        public Header setType(String s) {
            kv.put("Content-Type", s);
            return this;
        }

        public Header setConnection(String s) {
            kv.put("Connection", s);
            return this;
        }


        public Header setLength(String s) {
            kv.put("Content-Length", s);
            return this;
        }

        public Header setHost(String s) {
            kv.put("Host", s);
            return this;
        }

        public Set<Map.Entry<String, String>> keyVaule() {
            return kv.entrySet();
        }
    }
}

