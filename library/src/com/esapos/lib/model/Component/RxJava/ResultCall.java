package com.esapos.lib.model.Component.RxJava;



/**
 * Created by VickyLeu on 2016/7/14.
 * @Author Vickyleu
 * @Company Esapos
 *
 */
public class ResultCall<T, B> implements Runnable {

    T callEvent;

    public ResultCall(T callEvent) {
        this.callEvent = callEvent;
    }

    B callParam;

    public void result(T callParam, B param) {

    }

    public void begin(B b) {
        callParam = b;
        this.run();
    }

    @Override
    public void run() {
        result(callEvent, callParam);
    }
}
