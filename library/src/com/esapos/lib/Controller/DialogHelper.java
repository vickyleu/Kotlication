package com.esapos.lib.Controller;

/**
 * Created by VickyLeu on 2016/7/20.
 *
 * @Author Vickyleu
 * @Company Esapos
 */

import android.app.Activity;
import android.text.TextUtils;
import android.widget.TextView;

import com.esapos.lib.R;
import com.esapos.lib.View.Dialog.MDialog;
import com.esapos.lib.View.Dialog.MRectDialog;


/**
 * @description: 对话框辅助类
 */
public class DialogHelper {
    private static final String TAG = DialogHelper.class.getSimpleName();
    public MDialog _dialog;

    static DialogHelper helper;

    public DialogHelper() {
    }

    public static synchronized DialogHelper getInstance() {
        if (helper == null) helper = new DialogHelper();
        return helper;
    }

    /**
     * 创建一个加载对话框
     *
     * @param activity
     * @param content
     */


    public MDialog createPrompt(Activity activity, String content,
                                DialogTheme theme) {
        if (theme == DialogTheme.RECT) {
            if (TextUtils.isEmpty(content)) content = "请稍候";
            _dialog = MRectDialog.getInstance(activity, content);

        }

        return _dialog;
    }

    public MDialog createPrompt(Activity activity, String content,
                                DialogTheme theme, boolean cancel) {
        if (theme == DialogTheme.RECT) {
            if (TextUtils.isEmpty(content)) content = "请稍候";
            _dialog = MRectDialog.getInstance(activity, content, cancel);

        }

        return _dialog;
    }


    public void replaceContent(String content) {
        try {
            TextView tv = (TextView) _dialog.getDialog().findViewById(R.id.tips_loading_msg_tv);
            tv.setText(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 打开加载对话框
     *
     * @param activity
     */
    public void openPrompt(Activity activity, DialogTheme theme) {
        openPrompt(activity, null, theme);
    }

    public void openPrompt(Activity activity, String content,
                           DialogTheme theme) {
        createPrompt(activity, content, theme);
        openPrompt();
    }

    public void openPrompt(Activity activity, String content,
                           DialogTheme theme, boolean cancel) {

        createPrompt(activity, content, theme, cancel);
        openPrompt();
    }

    /**
     * 打开加载对话框
     */
    public void openPrompt() {
        if (_dialog != null && !_dialog.isShowing()) {
            _dialog.show();
        }
    }

    /**
     * 关闭加载对话框
     */
    public void closePrompt() {
        if (_dialog != null && _dialog.isShowing()) {
            try {
                _dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void closePrompt2() {
        if (_dialog != null && _dialog.isShowing()) {
            try {
                _dialog.dismissAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public enum DialogTheme {
        RECT
        //, STRIP
    }

}

