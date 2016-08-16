package com.esapos.example.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.esapos.lib.Controller.BtHelper;
import com.esapos.lib.Controller.DialogBuilder;
import com.esapos.lib.Controller.DialogHelper;
import com.esapos.lib.Controller.DownLoadMgr;
import com.esapos.lib.Controller.ObservableController;
import com.esapos.lib.Utils.SharedTool;
import com.esapos.lib.View.Dialog.MDialog;
import com.esapos.lib.model.Component.HttpLibrary.HttpHandler;
import com.esapos.lib.model.Component.RxJava.ResultCall;
import com.esapos.lib.model.Component.RxJava.RxCall;
import com.esapos.lib.model.Component.RxJava.RxConstant;
import com.esapos.lib.model.Component.RxJava.RxHandler;
import com.esapos.lib.model.Component.RxJava.RxTask;
import com.esapos.example.R;
import com.esapos.example.controller.Component.HttpClient;
import com.esapos.example.model.Bluetooth.BtResult;
import com.esapos.example.model.Component.TradeCurrency;
import com.esapos.example.model.Http.Req.BankPublic;
import com.esapos.example.model.Http.Req.CheckVersionReq;
import com.esapos.example.model.Http.Req.QueryTransReq;
import com.esapos.example.model.Http.Req.SignReq;
import com.esapos.example.model.Http.Resp.UpdateWorkKeyResp;
import com.esapos.example.model.IConstant;
import com.esapos.example.model.PrintEsc;
import com.google.gson.Gson;
import com.nexgo.libbluetooth.SppConnectMain;
import com.nexgo.oaf.apiv2.BlueToothOperateInterface;
import com.nexgo.oaf.apiv2.RequestDeviceInterface;
import com.nexgo.oaf.device.MultiLineAttributes;

import org.json.JSONException;
import org.json.JSONObject;
import org.scf4a.Event;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import rx.Subscriber;

/**
 * Created by VickyLeu on 2016/7/21.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public final class EventKit {
    private static final String TAG = EventKit.class.getSimpleName();
    private static Gson gson = new Gson();
    private static DialogBuilder db = null;


    /***********************************************************************************
     * ********************************网络请求**************************************
     **********************************************************************************/
    public static void UpdateWorkKeyReq(Activity activity, HttpHandler httpHandler) {
        GdHttpClient.getInstance().HttpObservable(activity, httpHandler,
                IConstant.commandId.UpdateWorkKeyReq, HttpClient.HttpMethod.POST,
                null);
    }

    public static void BalanceQueryReq(Activity activity, HttpHandler httpHandler, BankPublic bankPublic) {
        JSONObject obj = null;
        try {
            String str = gson.toJson(bankPublic, BankPublic.class);
            Log.e(TAG, "BalanceQueryReq: " + str.toString());
            obj = new JSONObject(str);
            Log.e(TAG, "BalanceQueryReq222: " + obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Object[] obj2 = new Object[]{obj};
        GdHttpClient.getInstance().HttpObservable(activity, httpHandler,
                IConstant.commandId.BalanceQueryReq, HttpClient.HttpMethod.POST,
                obj2);
    }

    public static void SaleReq(Activity activity, HttpHandler httpHandler, BankPublic bankPublic, String amount) {

        JSONObject obj = null;
        try {
            String str = gson.toJson(bankPublic, BankPublic.class);
            Log.e(TAG, "SaleReq: " + str.toString());
            obj = new JSONObject(str);
            Log.e(TAG, "SaleReq222: " + obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Object[] obj2 = new Object[]{obj, amount};
        GdHttpClient.getInstance().HttpObservable(activity, httpHandler,
                IConstant.commandId.SaleReq, HttpClient.HttpMethod.POST, obj2);
    }

    public static void UnSaleReq(Activity activity, HttpHandler httpHandler, BankPublic bankPublic, String referenceNo) {
        JSONObject obj = null;
        try {
            String str = gson.toJson(bankPublic, BankPublic.class);
            Log.e(TAG, "SaleReq: " + str.toString());
            obj = new JSONObject(str);
            Log.e(TAG, "SaleReq222: " + obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Object[] obj2 = new Object[]{obj, referenceNo};

        GdHttpClient.getInstance().HttpObservable(activity, httpHandler,
                IConstant.commandId.UnSaleReq, HttpClient.HttpMethod.POST,
                obj2
        );
    }

    public static void SignReq(Activity activity, HttpHandler httpHandler, SignReq signReq) {
        GdHttpClient.getInstance().HttpObservable(activity, httpHandler,
                IConstant.commandId.SignReq, HttpClient.HttpMethod.POST,
                new Object[]{signReq});
    }

    public static void QueryTransReq(Activity activity, HttpHandler httpHandler, QueryTransReq transReq) {
        GdHttpClient.getInstance().HttpObservable(activity, httpHandler,
                IConstant.commandId.QueryTransReq, HttpClient.HttpMethod.POST,
                new Object[]{transReq});
    }

    public static void CheckVersionReq(Activity activity, HttpHandler httpHandler) {
        GdHttpClient.getInstance().HttpObservable(activity, httpHandler,
                IConstant.commandId.CheckVersionReq, HttpClient.HttpMethod.POST,
                new Object[]{new CheckVersionReq()});
    }

    /***********************************************************************************
     * ********************************网络请求**************************************
     **********************************************************************************/


    /***********************************************************************************
     * ********************************硬件操作**************************************
     **********************************************************************************/
    public static void showMsg(MultiLineAttributes att) {
        BtTradeProxy.getInstance().showMsg(att);
    }

    public static void CheckCard(RxHandler mHandler, final Activity act, final EventCheckCardResult event, View view,
                                 final String money) {
        RxCall c = new RxCall(mHandler, BtResult.CHECKCARD) {
            @Override
            public Boolean call() throws Exception {
                BtTradeProxy.getInstance().CheckCard(money);
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                if (event != null) event.onCheckCard(result, money);

            }
        };
        postTask("刷卡", act, c, CloseCheck(view), "请在30秒内刷卡", 30, "", "刷卡失败");
    }

    public static void RefreshDevice(RxHandler mHandler, final Activity act, final EventRefreshResult event,
                                     final RequestDeviceInterface dev, BlueToothOperateInterface bl) {
        final RxCall call = new RxCall(mHandler, BtResult.DEVICE) {
            @Override
            public Boolean call() throws Exception {
                dev.requestDeviceInfo();
                while (flag) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                if (event != null) event.onRefresh(result);
            }
        };

        final int timeout = 5;

        final String success = "设备信息读取成功";
        final String error = "设备信息读取超时";

        postTask("获取设备信息", act, call, null, "获取设备信息", timeout, success, error);
    }

    public static void scanner(final RxHandler mHandler, final BlueToothOperateInterface bl) {
        final RxCall call = new RxCall(mHandler, BtResult.SEARCH) {
            @Override
            public Boolean call() throws Exception {
                while (flag) {
                    if (bl != null && !bl.requestBlueToothConnectedState()) {
                        Log.e(TAG, "scanner: ");
                        try {
                            bl.requestStartScannerBlueTooth();
                        } catch (Exception e) {
                            bl.requestStopScannerBlueTooth();
                        }

                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return false;
                    }

                }

                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
//                if (bl != null)
//                    try {
//                        bl.requestStopScannerBlueTooth();
//                    } catch (Exception e) {
//                    }
            }
        };
        final int timeout = 60;
        postTask("搜索蓝牙设备", null, call, null, null, timeout, null, null);
    }

    public static void killScanner(RxHandler mHandler) {
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = null;
            msg.arg1 = BtResult.SEARCH;
            mHandler.sendMessage(msg);
        }
    }

    public static void Connect(final RxHandler mHandler, final Activity act, final EventConnectResult event,
                               final BlueToothOperateInterface bl, final String device, final boolean cleanHistory) {
        final RxCall call = new RxCall(mHandler, BtResult.CONN) {
            @Override
            public Boolean call() throws Exception {
                bl.requestConnectBlueTooth(Event.ConnectType.SPP, device, Event.ConnectMachine.K200);
                while (flag) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                if (event != null) event.onConnected(result);
            }
        };
        final int timeout = 30;
        final String success = "连接成功";
        final String error = "连接超时";
        if (cleanHistory) {
            ResultCall<Object, Boolean> cleanHistoryTask = ObservableController.handleFinal(null, new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onNext(Boolean param) {
                    if (!param) {
                        BtTradeProxy.getInstance().reset();
                        BtHelper.cleanLast(act);
                        EventKit.disconnect(mHandler, act, null, bl);
                    }
                }
            });
            postTask("连接蓝牙设备", act, call, cleanHistoryTask, "连接蓝牙设备", timeout, success, error);
        } else postTask("连接蓝牙设备", act, call, null, "连接蓝牙设备", timeout, success, error);
    }

    public static void disconnect(RxHandler mHandler, Activity act, final EventConnectResult event,
                                  final BlueToothOperateInterface bl) {
        if (bl != null) bl.requestDisConnectBlueTooth(Event.ConnectType.SPP);
    }

    public static void WriteAidKey(RxHandler mHandler, final Activity act, final EventWriteAidResult event,
                                   BlueToothOperateInterface bl) {
        RxCall c = new RxCall(mHandler, BtResult.AID) {
            @Override
            public Boolean call() throws Exception {
                BtTradeProxy.getInstance().Aid("");
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                event.onWriteAid(result);
            }
        };
        postTask("设置AID", act, c, CloseBtForResult(bl), "设置AID", 5, "", "秘钥组更新失败");
    }


    public static void WriteTransKey(RxHandler mHandler, final Activity act, final EventWriteTransResult event, final String key) {
        final String keyValue = "097F3A9E35D68B339508A5C1FFF71E7A";
        RxCall c = new RxCall(mHandler, BtResult.AID) {
            @Override
            public Boolean call() throws Exception {

                BtTradeProxy.getInstance().TransKey(keyValue);
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                if (event != null) event.onWriteTransKey(result, key, keyValue);
            }
        };
        postTask("设置传输秘钥", act,c, null, "设置传输秘钥", 5, "", "秘钥组更新失败");
    }


    public static void WriteWorkKey(RxHandler mHandler, final Activity act, final EventWriteWorkKeyResult event,
                                    BlueToothOperateInterface bl, final UpdateWorkKeyResp workKey) {
        RxCall c = new RxCall(mHandler, BtResult.WORKKEY) {
            @Override
            public Boolean call() throws Exception {
                BtTradeProxy.getInstance().workKey(workKey);
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                event.onWriteWorkKey(result);
            }
        };
        postTask("更新工作秘钥", act, c, CloseBtForResult(bl), "更新工作秘钥", 10, "", "秘钥组更新失败");
    }

    public static void WriteMainKey(RxHandler mHandler, final Activity act, final EventWriteMainKeyResult event,
                                    final String termKey, final String keyValue, BlueToothOperateInterface bl) {
        RxCall c = new RxCall(mHandler, BtResult.MAINKEY) {
            @Override
            public Boolean call() throws Exception {
                BtTradeProxy.getInstance().mainKey(termKey, keyValue);
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                if (event != null) event.onWriteMainKey(result);
            }
        };
        postTask("更新主秘钥", act, c, CloseBtForResult(bl), "更新主秘钥", 5, "", "秘钥组更新失败");
    }

    public static void WritePublicKey(RxHandler mHandler, final Activity act, final EventWritePublicKeyResult event,
                                      BlueToothOperateInterface bl) {

        RxCall c = new RxCall(mHandler, BtResult.PUBKEY) {
            @Override
            public Boolean call() throws Exception {
                BtTradeProxy.getInstance().publicKey("");
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                if (event != null) event.onWritePublicKey(result);
            }
        };
        postTask("更新公钥", act, c, CloseBtForResult(bl), "更新公钥", 5, "", "秘钥组更新失败");
    }

    public static void InputPin(RxHandler mHandler, final Activity act, final EventInputPinResult event,
                                final BankPublic bank, final String cardNum, final String money, final String track2,
                                final String track3, final String cardType) {
        Log.e(TAG, "InputPin: ");
        RxCall c = new RxCall(mHandler, BtResult.INPUTPIN) {
            @Override
            public Boolean call() throws Exception {
                BtTradeProxy.getInstance().InputPIN(cardNum, money);
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                if (bank.isEmpty()) {
                    bank.writeData(String.valueOf(new Date().getTime()),
                            AppCenter.getInstance().getSn(),
                            cardType,
                            cardNum, null, track2, track3,
                            TradeCurrency.CHINA, null, null, null);
                }

                if (event != null) event.onInputPin(bank, money, result);
            }
        };
        postTask("输入密码", act, c, null, "输入密码", 60, "", "输入有误");
    }

    public static void InputAmount(RxHandler mHandler, final Activity act, final EventInputPinResult event,
                                   final String cardNum, final String money) {
        Log.e(TAG, "InputPin: ");
        RxCall c = new RxCall(mHandler, BtResult.INPUTAMOUMT) {
            @Override
            public Boolean call() throws Exception {
                BtTradeProxy.getInstance().InputPIN(cardNum, money);
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
//                event.onInputPin(cardNum, money, track2, cardNum, money, result);
            }
        };
        postTask("输入密码", act, c, null, "输入密码", 60, "", "输入有误");
    }


    private static ResultCall<View, Boolean> CloseCheck(final View view) {
        return ObservableController.handleFinal(view, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onNext(Boolean param) {
                ((Button) view).setText("确定");
            }
        });
    }

    private static ResultCall<BlueToothOperateInterface, Boolean> CloseBtForResult(final BlueToothOperateInterface bl) {
        return ObservableController.handleFinal(bl, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onNext(Boolean param) {
                if (!param) {
                    SppConnectMain.getInstance().unRegister();
                    bl.requestDisConnectBlueTooth(Event.ConnectType.SPP);
                }
            }
        });
    }

    /***********************************************************************************
     * ********************************硬件操作**************************************
     **********************************************************************************/
    public static void print(RxHandler mHandler, final Activity act, final EventPrintResult event,
                             final PrintEsc esc) {
        RxCall c = new RxCall(mHandler) {
            @Override
            public Boolean call() throws Exception {
                PrintProxy.getInstance().print(mHandler, esc);
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                if (event != null) {
                    PrintProxy.getInstance().closeUSB();
                    event.onPrintResult(result);
                }
            }
        };
        postTask("打印签购单", act, c, null, "打印签购单", 15, null, "打印失败");
    }


    public static void pbocStart(RxHandler mHandler, final Activity act, final EventPbocResult event,
                                 final String cardNum, final String money, final String cardType) {
        RxCall c = new RxCall(mHandler, BtResult.PBOCSTART) {
            @Override
            public Boolean call() throws Exception {
                BtTradeProxy.getInstance().pbocStart(money);
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                BankPublic bank = new BankPublic(String.valueOf(new Date().getTime()),
                        AppCenter.getInstance().getSn(),
                        cardType,
                        cardNum, null, null, null,
                        TradeCurrency.CHINA, null, null, null);

                if (event != null) event.onPbocStart(result, bank, money, cardType);
            }
        };
        postTask("鉴权开始", act, c, null, "鉴权开始", 30, "", "鉴权失败");
    }


    public static void qpbocStart(RxHandler mHandler, final Activity act, final EventPbocResult event,
                                  final String cardNum, final String money, final String cardType) {
        RxCall c = new RxCall(mHandler, BtResult.PBOCSTART) {
            @Override
            public Boolean call() throws Exception {
                BtTradeProxy.getInstance().qpbocStart(money);
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                BankPublic bank = new BankPublic(String.valueOf(new Date().getTime()),
                        AppCenter.getInstance().getSn(),
                        cardType,
                        cardNum, null, null, null,
                        TradeCurrency.CHINA, null, null, null);

                if (event != null) event.onQPbocStart(result, bank, money, cardType);
            }
        };
        postTask("鉴权开始", act, c, null, "鉴权开始", 30, "", "鉴权失败");
    }

    public static void pbocStop(RxHandler mHandler, final Activity act, final EventPbocResult event,
                                final String cardNum, final String money) {
        RxCall c = new RxCall(mHandler, BtResult.PBOCSTOP) {
            @Override
            public Boolean call() throws Exception {
                BtTradeProxy.getInstance().pbocStop(money);
                while (flag) {
                    Thread.sleep(400);
                }
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
                if (event != null) event.onPbocStop(result);
            }
        };
        postTask("鉴权关闭", act, c, null, "关闭鉴权", 30, "", "关闭失败");
    }


    /**
     * 延时响应任务
     *
     * @param name
     * @param activity
     * @param call
     * @param finalCall
     * @param tips
     * @param timeout
     * @param successText
     * @param errorText
     * @param <T>
     * @param <B>
     */
    public static <T, B> void postTask(String name, Activity activity, RxCall call, ResultCall<T, B> finalCall,
                                       String tips, int timeout, String successText, String errorText) {
        ObservableController.getInstance()
                .addTask(new RxTask(name, activity, tips, timeout, call, finalCall, successText, errorText));
    }

    public static boolean MainKeyIsWrited(Context context) {
        return SharedTool.getBooleanString(context,
                context.getString(R.string.mainKey), context.getString(R.string.write), IConstant.PLATFORM);
    }

    public static void UpdateTermkeyReq(Activity activity, HttpHandler httpHandler) {
        GdHttpClient.getInstance().HttpObservable(activity, httpHandler,
                IConstant.commandId.UpdateTermkeyReq, HttpClient.HttpMethod.POST,
                null);
    }

    public static void Update(final RxHandler handler, final Activity activity, final DownLoadMgr.CompleteInstall install, final int needUpdate, final String releaseNotes, final String downloadAddr) {
        if (needUpdate == 1) {
            DialogHelper.getInstance().createPrompt(activity, "下载更新", DialogHelper.DialogTheme.RECT, false);
            DialogHelper.getInstance().openPrompt();

            DownLoadMgr.start(activity, downloadAddr, new DownLoadMgr.ProgressUpd() {
                @Override
                public void update(int progress) {
                    DialogHelper.getInstance().replaceContent(progress + "%");
                }

                @Override
                public void Complete(Object object) {

                    if (!(object instanceof Boolean)) {
                        if (install != null) install.DownloadComplete((File) object);
                    } else {
                        if (install != null) install.DownloadError(needUpdate, object, null);
                    }

//                    completeEvent(object, null, handler, activity);
                }
            });
        } else {
            if (db != null) {
                try {
                    db.dismiss();
                } catch (Exception e) {
                }
            }

            Log.e(TAG, "Update: ");
            db = new DialogBuilder(activity, "应用更新", releaseNotes, "以后再说", "立即更新", new MDialog.DialogBtnCallback() {
                @Override
                public void onClick(final MDialog dialog, final View view, MDialog.Position position, int eventCode) {
                    if (position == MDialog.Position.LEFT) {
                        dialog.dismiss();
                        return;
                    }
                    if (position == MDialog.Position.RIGHT) {
                        Log.e(TAG, "onClick: " + releaseNotes);
                        dialog.turnOff();
                        dialog.replaceContent(releaseNotes);
                        Log.e(TAG, "onClick: ");
                        DownLoadMgr.start(activity, downloadAddr, new DownLoadMgr.ProgressUpd() {
                            @Override
                            public void update(int progress) {
                                MDialog.Progress progressBar = dialog.showProgress();
                                if (progressBar != null) {
                                    progressBar.setProgress(progress);
                                }
                            }

                            @Override
                            public void Complete(Object object) {
                                if (dialog != null) dialog.hideProgress();
                                if (!(object instanceof Boolean)) {
                                    if (install != null) install.DownloadComplete((File) object);
                                } else {
                                    if (install != null)
                                        install.DownloadError(needUpdate, object, dialog);
                                }
//                                completeEvent(object, dialog, null, activity);
                            }
                        });

                    }
                }
            });
            db.show();
        }
    }

    private static void completeEvent(Object object, final MDialog dialog, final RxHandler handler, final Activity activity) {
        if (dialog != null) dialog.hideProgress();
        if (!(object instanceof Boolean)) {
            final File file = (File) object;
            if (file != null && file.exists()) {
                final String pkg = file.getName();
                if (handler != null) {
                    if (activity != null) DialogHelper.getInstance().replaceContent("准备安装更新");
                } else if (dialog != null) dialog.replaceContent("准备安装更新");
                new Thread() {
                    public void run() {
                        final int reCode = AppCenter.getInstance().startInstallService(file, pkg, activity.getString(R.string.app_name));
                        Log.e(TAG, "应用商店结果" + reCode);

                        if (activity != null) activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (reCode == -1) {
                                    if (handler != null) {
                                        DialogHelper.getInstance().replaceContent("安装失败\n即将推出");
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                DialogHelper.getInstance().closePrompt();
                                                activity.finish();
                                            }
                                        }, 1000 * 3);
                                        return;
                                    }
                                    if (dialog != null) {
                                        dialog.replaceContent("安装失败");
                                        dialog.turnOn();
                                    }
                                } else if (reCode == -2) {
                                    Log.e(TAG, "无权限");
                                    if (handler != null) {
                                        Log.e(TAG, "run: ");
                                        DialogHelper.getInstance().replaceContent("无权限\n即将推出");
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                DialogHelper.getInstance().closePrompt();
                                                activity.finish();
                                            }
                                        }, 1000 * 3);
                                        return;
                                    }
                                    Log.e(TAG, "无权限无权限无权限: ");
                                    if (dialog != null) {
                                        dialog.replaceContent("无权限");
                                        dialog.turnOn();
                                    }
                                } else if (reCode == -7788) {
                                    if (handler != null) {
                                        DialogHelper.getInstance().replaceContent("未安装商店\n即将推出");
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                DialogHelper.getInstance().closePrompt();
                                                activity.finish();
                                            }
                                        }, 1000 * 3);
                                        return;
                                    }
                                    if (dialog != null) {
                                        dialog.replaceContent("应用商店未安装");
                                        dialog.turnOn();
                                    }
                                } else if (reCode == -8899) {
                                    if (handler != null) {
                                        DialogHelper.getInstance().replaceContent("安装文件不存在\n即将推出");
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                DialogHelper.getInstance().closePrompt();
                                                activity.finish();
                                            }
                                        }, 1000 * 3);
                                        return;
                                    }
                                    if (dialog != null) {
                                        dialog.replaceContent("安装文件不存在");
                                        dialog.turnOn();
                                    }
                                } else {
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }

                                }
                            }
                        });
                    }
                }.start();

            } else {
                if (handler != null) {
                    if (activity != null) Toast.makeText(activity, "下载文件已损坏", 0).show();
                    return;
                }
                if (dialog != null) dialog.turnOn();
                if (dialog != null) dialog.replaceContent("下载文件已损坏");
            }
        } else {
            Log.e(TAG, "Complete: " + object);
            if (handler != null) {
                if (activity != null) Toast.makeText(activity, "下载错误,请重试", 0).show();
                return;
            }
            if (dialog != null) dialog.turnOn();
            if (dialog != null) dialog.replaceContent("下载错误,请重试");
        }
    }

    protected static File getSaveFile(Context context, String fileName) {
        File file = null;
        if (isSDCardMounted()) {
            file = new File(Environment.getExternalStorageDirectory(),
                    fileName);
        } else {
            file = new File(context.getFilesDir(), fileName);
            setFileLimits(file);
        }
        return file;
    }

    public static void setFileLimits(File file) {
        String[] command = {"chmod", "777", file.getPath()};
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isSDCardMounted() {
        boolean ret = false;
        String status = Environment.getExternalStorageState();
        ret = status.equals(Environment.MEDIA_MOUNTED) ? true : false;
        return ret;
    }


    public static interface EventCheckCardResult {
        void onCheckCard(Object result, String money);
    }

    public static interface EventWriteAidResult {
        void onWriteAid(Object result);
    }

    public static interface EventWriteWorkKeyResult {
        void onWriteWorkKey(Object result);
    }

    public static interface EventWriteMainKeyResult {
        void onWriteMainKey(Object result);
    }

    public static interface EventWritePublicKeyResult {
        void onWritePublicKey(Object result);
    }

    public static interface EventRefreshResult {
        void onRefresh(Object result);


    }

    public static interface EventConnectResult {
        void onConnected(Object result);
    }

    public static interface EventWriteTransResult {
        void onWriteTransKey(Object result, String key, String keyValue);
    }

    public static interface EventInputPinResult {
        void onInputPin(BankPublic bank, String money, Object result);

        void onInputAmount(Object result);
    }

    public static interface EventPbocResult {
        void onPbocStart(Object result, BankPublic bank, String money, String cardType);

        void onPbocStop(Object result);

        void onQPbocStart(Object result, BankPublic bank, String money, String cardType);
    }

    public static interface EventPrintResult {
        void onPrintResult(Object result);
    }
}
