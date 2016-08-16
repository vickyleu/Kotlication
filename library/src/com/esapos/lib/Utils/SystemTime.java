package com.esapos.lib.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public class SystemTime {
    public static String time() {
//        return new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 SSSS ", Locale.CHINA).format(new Date(System.currentTimeMillis()));

        return new SimpleDateFormat("mm分ss秒SSS毫秒 ", Locale.CHINA).format(new Date(System.currentTimeMillis()));
    }



}
