package com.esapos.lib.api;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.io.File;

public class SilenceInstallService extends Service {

    private static final int INSTALL_FILE_NOT_EXIT = -8899;

    public static final String KEY_RE_CODE = "re_code";
    public static final String KEY_PKG_NAME = "pkg_name";
    public static final String KEY_APK_PATH = "apk_path";
    public static final String KEY_APP_NAME = "app_name";

    private Handler handler = new Handler();

    private static Object LOCK = new Object();
    public static int retCode = 0;
    static SilenceInstallService service;
    public static SilenceInstallService getInstant() {
        return service;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        service=this;
        return super.onStartCommand(intent, flags, startId);
    }

    public int installApp(final String apkPath, final String pkgName, final String appName) throws RemoteException {
        File file = new File(apkPath);
        if (!file.exists()) {
            return INSTALL_FILE_NOT_EXIT;
        }
        synchronized (LOCK) {
            try {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Intent it = new Intent(SilenceInstallService.this, InstallAppAct.class); //跳转到代理activity类
                        it.putExtra(KEY_APK_PATH, apkPath);
                        it.putExtra(KEY_PKG_NAME, pkgName);
                        it.putExtra(KEY_APP_NAME, appName);
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                    }
                });
                LOCK.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return retCode;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static class InstallAppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) { //广播接收数据.
            retCode = intent.getIntExtra(KEY_RE_CODE, -1);
            synchronized (LOCK) {
                LOCK.notifyAll();
            }
        }

    }

}
