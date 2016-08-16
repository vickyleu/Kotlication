package com.esapos.lib.model.Component.HttpLibrary;


import android.os.Handler;
import android.os.Message;

import com.esapos.lib.Utils.SystemTime;


/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public final class BaseHttpHandler extends Handler {
    HttpHandler httpHandler;

    public BaseHttpHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public final void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg != null) {
            if (this.httpHandler != null) {
                if (BaseHttpConnectPool.DEBUG) {
                    Logger.log("requestStopTime:" + SystemTime.time());
                    Logger.log("==========================================================================================");
                }

                int what = msg.what;
                switch (what) {
                    case HttpResponseMsgType.RESPONSE_ERR:
                        try {
                            this.httpHandler.httpErr((HttpResponseModel) msg.obj);
                        } catch (Exception var5) {
                            var5.printStackTrace();
                        }
                        break;
                    case HttpResponseMsgType.RESPONSE_SUCCESS:
                        try {
                            this.httpHandler.httpSuccess((HttpResponseModel) msg.obj);
                        } catch (Exception var4) {
                            var4.printStackTrace();
                        }
                }

            }
        }
    }
}

