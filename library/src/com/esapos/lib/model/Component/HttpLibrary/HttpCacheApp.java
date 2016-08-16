package com.esapos.lib.model.Component.HttpLibrary;


import android.app.Application;
import android.content.pm.PackageManager;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.esapos.lib.Utils.NetworkMgr;

import java.io.File;
import java.io.IOException;

/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public class HttpCacheApp extends Application {
    private static HttpCacheApp instance;
    private int workSpaceWidth;
    private int workSpaceHeight;

    public HttpCacheApp() {
    }

    public final boolean checkNetwork() {
        return NetworkMgr.getInstance(this).isNetwork();
    }

    public static HttpCacheApp getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        WindowManager manager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;
        setWorkSpaceHeight(heightPixels);
        setWorkSpaceWidth(widthPixels);
        instance = this;


        File file = new File(this.getCacheDir(), "http");
        long size = 52428800L;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                HttpResponseCache.install(file, size);
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public int getWorkSpaceWidth() {
        return workSpaceWidth;
    }

    public int getWorkSpaceHeight() {
        return workSpaceHeight;
    }

    private void setWorkSpaceWidth(int workSpaceWidth) {
        this.workSpaceWidth = workSpaceWidth;
    }

    private void setWorkSpaceHeight(int workSpaceHeight) {
        this.workSpaceHeight = workSpaceHeight;
    }

    public String getAppPkg() {
        return this.getPackageName();
    }

    public String getVerName() {
        try {
            return this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getVerCode() {

        try {
            return this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }


}

