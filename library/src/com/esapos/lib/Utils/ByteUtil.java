package com.esapos.lib.Utils;

import android.util.Log;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class ByteUtil {
    public static final String BYTECODE = "0123456789ABCDEF";

    public static byte[] hexStringToByte(String hex) {
        Log.e("hexStringToByte", "hexStringToByte: " + hex);
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] toCharArray = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int position = i * 2;
            result[i] = (byte) (toByte(toCharArray[position]) << 4 | toByte(toCharArray[position + 1]));
        }
        return result;
    }

    public static String asciiToString(String value) {
        StringBuilder sbu = new StringBuilder();
        String[] chars = value.split(",");
        for (String aChar : chars) {
            sbu.append((char) Integer.parseInt(aChar));
        }
        return sbu.toString();
    }

    private static byte toByte(char c) {
        return (byte) BYTECODE.indexOf(c);
    }
}
