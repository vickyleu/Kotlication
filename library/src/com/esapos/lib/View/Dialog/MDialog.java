package com.esapos.lib.View.Dialog;

/**
 * Created by VickyLeu on 2016/7/20.
 *
 * @Author Vickyleu
 * @Company Esapos
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.esapos.lib.R;


public abstract class MDialog extends DialogFragment {

    protected Activity _activity;
    protected boolean _cancel = true;
    private boolean _dismiss = true;


    public MDialog() {
    }

    public boolean isShowing() {
        if (_dismiss) return false;
        return true;
    }

    public MDialog(Activity activity) {
        _activity = activity;
    }

    public MDialog(Activity activity, boolean cancel) {
        _activity = activity;
        _cancel = cancel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.custom_dialog_base_style);
        setCancelable(_cancel);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getDialog() == null) {  // Returns mDialog
            // Tells DialogFragment to not use the fragment as a dialog, and so won't try to use mDialog
            setShowsDialog(false);
        }

        super.onActivityCreated(savedInstanceState);  // Will now complete and not crash
    }

    public void show(String tag) {
        super.show(((FragmentActivity) _activity).getSupportFragmentManager(), tag);
        _dismiss = false;
    }

    public void show() {
        super.show(((FragmentActivity) _activity).getSupportFragmentManager(), null);
        _dismiss = false;
    }


    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }

    public void turnOff() {
        try {
            getDialog().findViewById(R.id.prompt_left_bt).setEnabled(false);
            getDialog().findViewById(R.id.prompt_right_bt).setEnabled(false);
            getDialog().findViewById(R.id.prompt_center_bt).setEnabled(false);
        }catch (Exception e){e.printStackTrace();}
    }

    public void turnOn() {

        try {
            getDialog().findViewById(R.id.prompt_left_bt).setEnabled(true);
            getDialog().findViewById(R.id.prompt_right_bt).setEnabled(true);
            getDialog().findViewById(R.id.prompt_center_bt).setEnabled(true);
        }catch (Exception e){e.printStackTrace();}
    }


    public class Progress {
        ProgressBar bar;
        TextView textView;

        public Progress(View bar, View textView) {
            this.bar = (ProgressBar) bar;
            this.textView = (TextView) textView;
        }


        public void setProgress(int progress) {
            bar.setProgress(progress);
            textView.setText(progress + "/" + 100);
        }
    }

    public Progress showProgress() {
        try {
            View v = getDialog().findViewById(R.id.pgrs_container);
            if (v.getVisibility() == View.VISIBLE) {
                return new Progress(v.findViewById(R.id.progress), v.findViewById(R.id.tv));
            }
            v.setVisibility(View.VISIBLE);
            getDialog().findViewById(R.id.prompt_content).setVisibility(View.GONE);
            return new Progress(v.findViewById(R.id.progress), v.findViewById(R.id.tv));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        _dismiss = true;
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
        _dismiss = true;
    }


    public void hideProgress() {
        try {
            LinearLayout v = (LinearLayout) getDialog().findViewById(R.id.pgrs_container);
            if (v.getVisibility() == View.GONE) return;
            v.setVisibility(View.GONE);
            getDialog().findViewById(R.id.prompt_content).setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceContent(String content) {
        try {
            TextView tv = (TextView) getDialog().findViewById(R.id.prompt_content);
            tv.setText(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public enum Position {
        LEFT, CENTER, RIGHT
    }

    public interface DialogBtnCallback {
        public void onClick(MDialog dialog, View view, Position position, int eventCode);
    }
}

