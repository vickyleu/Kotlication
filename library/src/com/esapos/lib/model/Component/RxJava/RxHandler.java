package com.esapos.lib.model.Component.RxJava;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.esapos.lib.Controller.ObservableController;
import com.esapos.lib.model.Component.RxJava.RxTask;
import com.esapos.lib.model.Component.RxJava.RxConstant;


/**
 * Created by VickyLeu on 2016/7/27.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class RxHandler extends Handler {
    public RxHandler() {
        Looper.getMainLooper();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == RxConstant.RxHandlerFlag) {
            System.gc();
            RxTask task = ObservableController.getInstance().getCurrentTask();
            if (task == null) return;
            task.setCall( msg.obj);
        }
    }
}
