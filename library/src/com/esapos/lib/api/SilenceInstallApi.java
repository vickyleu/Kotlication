package com.esapos.lib.api;

import android.os.DeadObjectException;
import android.util.Log;

import java.io.File;

public class SilenceInstallApi {

    private static final int INSTALL_FILE_NOT_EXIT = -8899;
    private static final int SERVICE_NOT_CONNECT = -7788;
    private static final int SERVICE_ERROR = -6677;

    private static final String TAG = SilenceInstallApi.class.getSimpleName();

    private static SilenceInstallApi instance = null;

    private SilenceInstallApi() {

    }

    public static synchronized SilenceInstallApi getInstance() {
        if (instance == null) {
            instance = new SilenceInstallApi();
        }
        return instance;
    }


    /**
     * 初始化交易.
     *
     * @param service //     * @param appKey
     * @param apkName
     * @return
     */
    public int installAPK(SilenceInstallService service, File file, String pkgName, String apkName) {

        if (!file.exists()) {
            return INSTALL_FILE_NOT_EXIT;
        }
        try {
            int retCode = service.installApp(file.getAbsolutePath(), pkgName, apkName);
            return retCode;
        } catch (Exception e) {
            Log.e(TAG, "installAPK:getMessage " + e.getMessage());
            if (e instanceof DeadObjectException) {
                return installAPK(service, file, pkgName, apkName);
            }
            return SERVICE_ERROR;
        }

    }

}
