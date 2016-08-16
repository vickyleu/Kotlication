package com.esapos.lib.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by VickyLeu on 2016/7/19.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public class SharedTool {
    public static String getString(Context context, String table, String key) {
        try {
            return context.getSharedPreferences(table, Context.MODE_PRIVATE).getString(key, "");
        } catch (Exception ignored) {
        }
        return "";
    }

    public static boolean getBooleanString(Context context, String table, String key, String key2) {
        try {
            SharedPreferences sp = context.getSharedPreferences(table, Context.MODE_PRIVATE);
            return sp.getBoolean(key, false) && sp.getBoolean(key2, false);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    public static boolean getBoolean(Context context, String table, String key) {
        try {
            SharedPreferences sp = context.getSharedPreferences(table, Context.MODE_PRIVATE);
            return sp.getBoolean(key, false);
        } catch (Exception ignored) {
        }
        return false;
    }

    public static void saveString(Context context, String table, String key, String value) {
        context.getSharedPreferences(table, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static void saveBooleanString(Context context, String table, String key, String key2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(table, Context.MODE_PRIVATE).edit();
        edit.clear().apply();
        edit.putBoolean(key, true).apply();
        edit.putBoolean(key2, true).apply();
    }

    public static void saveBoolean(Context context, String table, String key, boolean value) {
        context.getSharedPreferences(table, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }
}
