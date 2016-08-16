package com.esapos.lib.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class NetworkMgr {

    // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
    ConnectivityManager connectivityManager = null;

    static NetworkMgr instance;

    private NetworkMgr(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static synchronized NetworkMgr getInstance(Context context) {
        if (instance == null) instance = new NetworkMgr(context);
        return instance;
    }

    public boolean isNetwork() {
        // 获取NetworkInfo对象
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo info : networkInfo) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }
}
