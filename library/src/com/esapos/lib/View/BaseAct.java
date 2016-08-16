package com.esapos.lib.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.esapos.lib.Controller.DialogHelper;
import com.esapos.lib.model.Component.HttpLibrary.HttpHandler;
import com.esapos.lib.model.Component.HttpLibrary.HttpResponseModel;
import com.esapos.lib.model.Component.RxJava.RxConstant;
import com.esapos.lib.model.Component.RxJava.RxHandler;


/**
 * Created by VickyLeu on 2016/7/27.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public abstract class BaseAct extends AppCompatActivity implements HttpHandler {


    /**
     * Rx事务传输主循环Handler
     */

    public static RxHandler rxHandler = new RxHandler();
    private int mRes = 0;
    private int mTitleRes = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (layout()==0)return;
        setContentView(layout());
    }

    protected void setTitleRes(int res) {
        mRes = res;
    }

    protected void setTitleName(int res) {
        mTitleRes = res;
    }

    protected int getTitleRes() {
        return mRes;
    }

    protected int getTitleName() {
        return mTitleRes;
    }

    protected abstract int layout();

    protected abstract void initView();


    @Override
    public void httpErr(HttpResponseModel var1) throws Exception {

    }

    @Override
    public void httpSuccess(HttpResponseModel var1) throws Exception {

    }

    public void showToast(String str) {
        MToast.showToastShort(this, str);
    }

    protected void openIntent(Class<?> clazz) {
        openIntent_(clazz, null);
    }


    protected void openIntent(Class<?> clazz, Bundle bundle) {
        openIntent_(clazz, bundle);
    }

    private void openIntent_(Class<?> clazz, Bundle bundle) {
        DialogHelper.getInstance().closePrompt2();
        beforeIntentOption();
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void beforeIntentOption() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (DialogHelper.getInstance()._dialog != null && DialogHelper.getInstance()._dialog.isShowing())
                DialogHelper.getInstance().closePrompt();
        } catch (Exception e) {
        }

    }

    protected void sendResult(RxHandler mHandler) {
        Message msg = new Message();
        msg.what = RxConstant.RxHandlerFlag;
        msg.obj = null;
        msg.arg1 = -1;
        mHandler.sendMessage(msg);

    }

    protected void sendResult(RxHandler mHandler, Object obj) {
        Message msg = new Message();
        msg.what = RxConstant.RxHandlerFlag;
        msg.obj = obj;
        msg.arg1 = -1;
        mHandler.sendMessage(msg);
    }
}
