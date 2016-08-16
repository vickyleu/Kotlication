package com.esapos.lib.Utils;

/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static final String MD5 = "MD5";
    private static final String ZERO = "0";

    private static String getEncryptString(String src, boolean is16Byte) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            byte[] b = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte aB : b) {
                int i = aB;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(i));
            }
            String target = sb.toString();
            return is16Byte ? target.substring(8, 24) : target;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getEncryptString(byte[] src, boolean is16Byte) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src);
            byte[] b = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte aB : b) {
                int i = aB;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(i));
            }
            String target = sb.toString();
            return is16Byte ? target.substring(8, 24) : target;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getEncryptStringBy16(String src) {
        return getEncryptString(src, true);
    }

    public static String getEncryptStringBy32(String src) {
        return getEncryptString(src, false);
    }

    public static String getEncryptStringBy32(byte[] src) {
        return getEncryptString(src, false);
    }
}
