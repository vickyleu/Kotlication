package com.esapos.lib.Utils;

import android.os.Environment;

/**
 * Created by VickyLeu on 2016/8/11.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class SDCardUtils {

    public static boolean isSDCardMounted() {
        boolean ret = false;
        String status = Environment.getExternalStorageState();
        ret = status.equals(Environment.MEDIA_MOUNTED) ? true : false;
        return ret;
    }
}
