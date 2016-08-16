package com.esapos.lib.model.Component.HttpLibrary;


import android.util.Log;

/**
 * Created by VickyLeu on 2016/7/14.
 * @Author Vickyleu
 * @Company Esapos
 *
 */
public final class Logger {
    private static final String TAG = "com.vicky.http";
    public static boolean LOG = true;

    public Logger() {
    }

    public static final void log(Object o) {
        if(LOG) {
            if(o != null) {
                Log.e(TAG, o.toString());
            }

        }
    }
}

