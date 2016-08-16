package com.esapos.lib.Utils;

import android.app.Activity;
import android.view.View;

/**
 * Created by Administrator on 2016/7/24.
 */
public class FindView {
    public static <T extends View> T findView(Activity act, Class<T> cls, int id) {
        T t = null;
        try {
            t = (T) act.findViewById(id);
        } catch (Exception e) {
        }
        return t;
    }

    public static <T extends View> T findView(View view, Class<T> cls, int id) {
        T t = null;
        try {
            t = (T) view.findViewById(id);
        } catch (Exception e) {
        }
        return t;
    }
}
