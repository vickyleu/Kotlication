package com.esapos.lib.Utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by VickyLeu on 2016/8/8.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class IMeiUtil {
    public static String IMei(Context ctx) {
        return ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
    }

}
