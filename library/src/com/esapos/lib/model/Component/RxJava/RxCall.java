package com.esapos.lib.model.Component.RxJava;


import com.esapos.lib.Controller.DialogHelper;

import java.util.concurrent.Callable;

/**
 * Created by VickyLeu on 2016/7/20.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public class RxCall implements Callable<Boolean> {
    protected boolean flag = true;

    public RxHandler mHandler;
    public int taskFlag = -1;

    public RxCall(RxHandler mHandler, int taskFlag) {
        this.mHandler = mHandler;
        this.taskFlag = taskFlag;
    }
    public RxCall(RxHandler mHandler) {
        this.mHandler = mHandler;
        this.taskFlag = -1;
    }

    @Override
    public Boolean call() throws Exception {

        return null;
    }


    public void Result(Object result) throws Exception {
        this.cancel();
    }

    public void cancel() {
        flag = false;
        DialogHelper.getInstance().closePrompt();
    }

    public RxHandler getHandler() {
        return mHandler;
    }
}
