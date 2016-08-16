package com.esapos.lib.model.Component.RxJava;

import android.app.Activity;
import android.text.TextUtils;

import com.esapos.lib.Controller.DialogHelper;
import com.esapos.lib.Utils.StringUtil;
import com.esapos.lib.View.MToast;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;


/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public class RxTask implements Runnable {
    private static final String TAG = RxTask.class.getSimpleName();
    private ResultCall finalCallable;
    private String name;
    private Activity mActivity;
    private String tips;
    private int timeout;
    private RxCall call;
    private String successText;
    private String errorText;
    private Set<String> idSets = null;

    public RxTask(String name, Activity mActivity, String tips, int timeout, RxCall call,
                  ResultCall finalCallable, String successText, String errorText) {
        if (TextUtils.isEmpty(name)) name = StringUtil.getRandomString(50);
        this.name = name;
        this.mActivity = mActivity;
        this.tips = tips;
        this.timeout = timeout;
        this.call = call;
        this.finalCallable = finalCallable;
        this.successText = successText;
        this.errorText = errorText;
    }

    public RxTask(Activity mActivity, String tips, int timeout, RxCall call,
                  ResultCall finalCallable, String successText, String errorText) {
        this(StringUtil.getRandomString(50), mActivity, tips, timeout, call, finalCallable, successText, errorText);
    }

    public RxTask(Activity mActivity, String tips, int timeout, RxCall call,
                  ResultCall finalCallable) {
        this(StringUtil.getRandomString(50), mActivity, tips, timeout, call, finalCallable, "", "");
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        if (mActivity != null)
            mActivity.runOnUiThread(() -> {
                try {
                    DialogHelper.getInstance().createPrompt(mActivity, tips, DialogHelper.DialogTheme.RECT, false);
                    DialogHelper.getInstance().openPrompt();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });


        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> sub) {

                new Thread(() -> {
                    Future<Boolean> fut = Executors.newSingleThreadExecutor().submit(call);
                    try {
                        fut.get(timeout, TimeUnit.SECONDS);
                        sub.onNext(true);
                    } catch (Exception e) {
                        fut.cancel(true);
                        e.printStackTrace();
                        sub.onError(e);
                    }
                    sub.onCompleted();
                }).start();
            }
        }).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                remove();
            }

            @Override
            public void onError(Throwable throwable) {
                call.cancel();
                remove();
                if (mActivity != null)
                    mActivity.runOnUiThread(() -> {
                        if (finalCallable != null) finalCallable.begin(false);
                        if (!TextUtils.isEmpty(errorText)) showToast(errorText);

                    });

            }

            @Override
            public void onNext(Boolean result) {
                if (!result) {
                    remove();
                    return;
                }
                remove();
                if (mActivity != null)
                    mActivity.runOnUiThread(() -> {
                        if (finalCallable != null) finalCallable.begin(true);
                        if (!TextUtils.isEmpty(successText)) showToast(successText);
                    });


            }
        });
    }

    public synchronized void remove() {
        if (idSets != null) idSets.remove(name);
    }

    public void setCall(Object result) {
        try {
            if (call == null) return;
//            if (task == null) return;
            call.Result(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showToast(String str) {
        if (mActivity != null)
            MToast.showToastShort(mActivity, str);
    }

    public void mountThread(Set<String> idSets) {
        this.idSets = idSets;
    }

}
