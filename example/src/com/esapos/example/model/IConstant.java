package com.esapos.example.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public class IConstant {
    public static final String PLATFORM = "生产";
//    public static final String PLATFORM = "测试";


    public static Handler handler = new Handler(Looper.getMainLooper());
    public static ExecutorService exec = Executors.newFixedThreadPool(3);

    public static void Toast(Context mContext, String str) {
        Toast(mContext, str, 0);
    }

    public static void Toast(final Context mContext, final String str, final int i) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, str, i);
            }
        });
    }
    public static final String BASE_URL = "http://www.baidu.com";
    public static final String UPDATE_URL = "http://www.google.com";


    public static final class commandId {
        public static final int BalanceQueryReq = 0x0601;
        public static final int SaleReq = 0x0603;
        public static final int UnSaleReq = 0x0605;
        public static final int UpdateWorkKeyReq = 0x0607;
        public static final int UpdateTermkeyReq = 0x0613;
        public static final int CheckVersionReq = 0x0501;

        public static final int SignReq = 0x0609;
        public static final int QueryTransReq = 0x0611;


        public static final int UpdateWorkKeyResp = 0x0608;
        public static final int SignResp = 0x0610;
        public static final int QueryTransResp = 0x0612;
        public static final int BalanceQueryResp = 0x0602;
        public static final int UnSaleResp = 0x0606;
        public static final int SaleResp = 0x0604;
        public static final int UpdateTermkeyResp = 0x0614;
        public static final int CheckVersionResp = 0x0502;
    }

    public static class ID {
        public static final int rv = 1210001;
        public static final int et = 1210002;
        public static final int imageRvItem = 1210003;
        public static final int titleRvItem = 1210004;
        public static final int addrRvItem=1210005;
        public static final int pairRvItem=1210006;
    }
}
