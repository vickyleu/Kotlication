package com.esapos.lib.Controller;

import android.app.Activity;
import android.view.Gravity;

import com.esapos.lib.R;
import com.esapos.lib.View.Dialog.DialogView;
import com.esapos.lib.View.Dialog.MDialog;
import com.esapos.lib.model.Component.Dialog.DialogParamsBuilder;
import com.esapos.lib.model.Component.HttpLibrary.HttpCacheApp;

/**
 */
public class DialogBuilder {

    private static final String TAG = DialogBuilder.class.getSimpleName();
    private DialogParamsBuilder _params;
    private Activity _activity;
    private MDialog _dialog;
    static DialogBuilder builder;

    static DialogBuilder getInstant() {
        return builder;
    }


    /**
     * @param activity
     */
    public DialogBuilder(Activity activity) {
        builder = this;
        _activity = activity;
        _params = new DialogParamsBuilder();
    }

    public DialogBuilder(Activity activity, String title) {
        this(activity);
        setTitle(title);
    }

    public DialogBuilder(Activity activity, String title, String content) {
        this(activity);
        setTitle(title).setContent(content);
    }

    public DialogBuilder(Activity activity, String title, String content,
                         String center) {
        this(activity);
        setCenterBtnText(center).setTitle(title).setContent(content);
    }

    public DialogBuilder(Activity activity, String title, String content,
                         String left, String right, MDialog.DialogBtnCallback callback) {
        this(activity);
        setRightBtnText(right).setLeftBtnText(left).setTitle(title)
                .setContent(content).setCancel(false);
        setLeftRightDialog(callback);
    }

    private DialogBuilder setCancel(boolean cancel) {
        _params.setCancel(cancel);
        return this;
    }

    private DialogBuilder setLeftRightDialog(MDialog.DialogBtnCallback callback) {
        int height = (int) (HttpCacheApp.getInstance().getWorkSpaceHeight() / 2);
        return this.setRightBgResId(R.drawable.dialog_green_btn)
                .setGravity(Gravity.CENTER)
                .setLeftBgResId(R.drawable.dialog_gray_btn)
                .setBtnCallback(callback).setHeight(height)
                .setLineColor(getColorById(R.color.dialog_line_color))
                .setTitleColor(getColorById(R.color.custom_blue));
    }

    public final int getColorById(int resId) {
        return _activity.getResources().getColor(resId);
    }

    @SuppressWarnings("ValidFragment")
    public MDialog create() {
        return new DialogView(_activity, _params) {
            @Override
            public void onResume() {
                super.onResume();
                getContentTextView().setLineSpacing(5f, 1f);
            }
        };
    }

    public MDialog show() {
        _dialog = create();
        _dialog.show();
        return _dialog;
    }

    public void dismiss() {
        _dialog.dismiss();
    }

    public DialogBuilder setTitle(String title) {
        _params.setTitle(title);
        return this;
    }

    public DialogBuilder setContent(String content) {
        _params.setContent(content);
        return this;
    }

    public DialogBuilder setTopViewBgResId(int topViewBgResId) {
        _params.setTopViewBgResId(topViewBgResId);
        return this;
    }

    public DialogBuilder setDialogBgResId(int dialogBgResId) {
        _params.setDialogBgResId(dialogBgResId);
        return this;
    }

    public DialogBuilder setBottomViewBgResId(int bottomViewBgResId) {
        _params.setBottomViewBgResId(bottomViewBgResId);
        return this;
    }

    public DialogBuilder setTopViewBgColor(int topViewBgColor) {
        _params.setTopViewBgColor(topViewBgColor);
        return this;
    }

    public DialogBuilder setTitleSize(float titleSize) {
        _params.setTitleSize(titleSize);
        return this;
    }

    public DialogBuilder setTitleColor(int titleColor) {
        _params.setTitleColor(titleColor);
        return this;
    }

    public DialogBuilder setContentSize(float contentSize) {
        _params.setContentSize(contentSize);
        return this;
    }

    public DialogBuilder setContentColor(int contentColor) {
        _params.setContentColor(contentColor);
        return this;
    }

    public DialogBuilder setLineColor(int lineColor) {
        _params.setLineColor(lineColor);
        return this;
    }

    public DialogBuilder setWidth(int width) {
        _params.setWidth(width);
        return this;
    }

    public DialogBuilder setHeight(int height) {
        _params.setHeight(height);
        return this;
    }

    public DialogBuilder setLeftBtnText(String left) {
        _params.setLeftBtnText(left);
        return this;
    }

    public DialogBuilder setCenterBtnText(String center) {
        _params.setCenterBtnText(center);
        return this;
    }

    public DialogBuilder setRightBtnText(String right) {
        _params.setRightBtnText(right);
        return this;
    }

    public DialogBuilder setEventCode(int eventCode) {
        _params.setEventCode(eventCode);
        return this;
    }

    public DialogBuilder setBtnCallback(
            MDialog.DialogBtnCallback btnCallback) {
        _params.setBtnCallback(btnCallback);
        return this;
    }

    public DialogBuilder setBtnTextSize(int btnTextSize) {
        _params.setBtnTextSize(btnTextSize);
        return this;
    }

    public DialogBuilder setLeftBgResId(int leftBgResId) {
        _params.setLeftBgResId(leftBgResId);
        return this;
    }

    public DialogBuilder setCenterBgResId(int centerBgResId) {
        _params.setCenterBgResId(centerBgResId);
        return this;
    }

    public DialogBuilder setRightBgResId(int rightBgResId) {
        _params.setRightBgResId(rightBgResId);
        return this;
    }

    public DialogBuilder setLeftTextColor(int leftTextColor) {
        _params.setLeftTextColor(leftTextColor);
        return this;
    }

    public DialogBuilder setCenterTextColor(int centerTextColor) {
        _params.setCenterTextColor(centerTextColor);
        return this;
    }

    public DialogBuilder setRightTextColor(int rightTextColor) {
        _params.setRightTextColor(rightTextColor);
        return this;
    }

    public DialogBuilder setLeftBgColor(int leftBgColor) {
        _params.setLeftBgColor(leftBgColor);
        return this;
    }

    public DialogBuilder setCenterBgColor(int centerBgColor) {
        _params.setCenterBgColor(centerBgColor);
        return this;
    }

    public DialogBuilder setRightBgColor(int rightBgColor) {
        _params.setRightBgColor(rightBgColor);
        return this;
    }

    public DialogParamsBuilder getParams() {
        return _params;
    }

    public Activity getActivity() {
        return _activity;
    }


    public DialogBuilder setGravity(int gravity) {
        _params.setGravity(gravity);
        return this;
    }
}
