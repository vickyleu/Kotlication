package com.esapos.lib.View.Dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esapos.lib.R;


/**
 * Created by VickyLeu on 2016/7/20.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
@SuppressLint("ValidFragment")
public class MRectDialog extends MDialog {

    private String _params = "";
    private TextView _content_tv;

    private static MDialog _dialog;

    public MRectDialog() {
    }

    private MRectDialog(Activity activity, String content) {
        super(activity);
        _params = content;
    }

    private MRectDialog(Activity activity, String content, boolean cancel) {
        super(activity, cancel);
        _params = content;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public synchronized static MDialog getInstance(Activity activity,
                                                   String params) {
        if (_dialog == null) {
            return new MRectDialog(activity, params);
        }
        return _dialog;
    }

    public synchronized static MDialog getInstance(Activity activity,
                                                   String params, boolean cancel) {
        if (_dialog == null) {
            return new MRectDialog(activity, params, cancel);
        }
        return _dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.layout_dialog, container);
        _content_tv = (TextView) view.findViewById(R.id.tips_loading_msg_tv);

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("onConfigurationChanged", "onConfigurationChanged: " + _params);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null)
            getDialog().setCanceledOnTouchOutside(false);
        else {
            dismissAllowingStateLoss();
            return;
        }
        if (TextUtils.isEmpty(_params)) dismissAllowingStateLoss();
        else _content_tv.setText(_params);
    }

}

