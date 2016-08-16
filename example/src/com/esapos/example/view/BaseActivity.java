package com.esapos.example.view;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.esapos.lib.Controller.BtHelper;
import com.esapos.lib.Controller.BtMgr;
import com.esapos.lib.Controller.DownLoadMgr;
import com.esapos.lib.Utils.FindView;
import com.esapos.lib.Utils.SharedTool;
import com.esapos.lib.View.BaseAct;
import com.esapos.lib.View.Dialog.MDialog;
import com.esapos.lib.model.Component.Http.HttpModule;
import com.esapos.lib.model.Component.Http.RespBody;
import com.esapos.lib.model.Component.HttpLibrary.HttpResponseModel;
import com.esapos.example.R;
import com.esapos.example.controller.AppCenter;
import com.esapos.example.controller.BtTradeProxy;
import com.esapos.example.controller.EventKit;
import com.esapos.example.controller.GdHttpClient;
import com.esapos.example.model.Bluetooth.DeviceDiscovery;
import com.esapos.example.model.Http.Resp.BalanceQueryResp;
import com.esapos.example.model.Http.Resp.CheckVersionResp;
import com.esapos.example.model.Http.Resp.QueryTransResp;
import com.esapos.example.model.Http.Resp.SaleResp;
import com.esapos.example.model.Http.Resp.SignResp;
import com.esapos.example.model.Http.Resp.UnSaleResp;
import com.esapos.example.model.Http.Resp.UpdateTermkeyResp;
import com.esapos.example.model.Http.Resp.UpdateWorkKeyResp;
import com.esapos.example.model.IConstant;
import com.nexgo.libbluetooth.SppConnectMain;
import com.nexgo.oaf.apiv2.BlueToothOperateInterface;
import com.nexgo.oaf.apiv2.RequestDeviceInterface;
import com.nexgo.oaf.device.MultiLineAttributes;
import com.nexgo.oaf.key.Result1LLVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public abstract class BaseActivity extends BaseAct implements EventKit.EventConnectResult, DeviceDiscovery, EventKit.EventWriteWorkKeyResult, EventKit.EventWriteMainKeyResult, EventKit.EventWriteAidResult, EventKit.EventWritePublicKeyResult, DownLoadMgr.CompleteInstall, EventKit.EventWriteTransResult {
    protected BlueToothOperateInterface bl = AppCenter.getInstance().BlueToothController(rxHandler, this);
    protected RequestDeviceInterface dev = AppCenter.getInstance().DeviceController();
    protected BluetoothDevice currDev = null;
    private static final String TAG = BaseActivity.class.getSimpleName();

    private boolean needCheck = NeedCheck();
    static BaseActivity mAct;

    /**
     * 是否需要搜索蓝牙设备,默认不搜索,子类中重写
     */
    protected boolean NeedCheck() {
        return false;
    }



    /**
     *
     * @param cls
     * @param resId
     * @param <T>通过泛型查找view的id,并通过传入的类型返回对应的view类型,以减少findViewId和cast强转的代码量
     * @return
     */
    public final <T extends View> T find(Class<T> cls, int resId) {
        return FindView.findView(this, cls, resId);
    }

    /**
     *
     * @param resId
     * @return 同上
     */
    public final View find(int resId) {
        return FindView.findView(this, View.class, resId);
    }


    @SuppressLint("ShowToast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SppConnectMain.getInstance().init(this);
        mAct = this;
        int stat = BtMgr.open(this);
        switch (stat) {
            case 0:
                break;
            case -1:
                showToast("设备不支持蓝牙");
                break;
            case 1:
                showToast("请手动开启蓝牙");
                break;
            case 2:
                showToast("请手动开启蓝牙");
                break;
        }
        if (AppCenter.getInstance().checkIsConn() && EventKit.MainKeyIsWrited(this) && !AppCenter.getInstance().isWorkKeySucc()) {
            EventKit.UpdateWorkKeyReq(this, this);
        }
        initView();
    }

    protected void errorMSG(String errMsg) {
        if (TextUtils.isEmpty(errMsg)) errMsg = "未知错误";
        showToast(errMsg);
    }


    @Override
    public synchronized void onResume() {
        super.onResume();
        if (needCheck && bl != null) {
            AppCenter.getInstance().mountDiscoveryCallBack(this);
            BtMgr.open(this);
            EventKit.scanner(rxHandler, bl);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (needCheck && bl != null) {
            EventKit.killScanner(rxHandler);
        }
    }

    @Override
    protected void beforeIntentOption() {
        super.beforeIntentOption();
        GdHttpClient.getInstance().shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GdHttpClient.getInstance().shutdown();
        currDev = null;

    }

    protected void Bond(final BluetoothDevice device) {
        int mj = device.getBluetoothClass().getMajorDeviceClass();
        switch (mj) {
            case BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED:
                ConnectBt(device.getAddress());
                break;
            default:
                showToast("设备不支持");
                break;
        }

    }

    protected void bondStatus(boolean rlt) {

    }


    protected void ConnectLast() {
        String addr = BtHelper.lastDevice(this);
        if (!TextUtils.isEmpty(addr)) {
            if (BtHelper.getRemoteDevice(addr) != null)
                currDev = BtHelper.getRemoteDevice(addr);
            ConnectBt(addr);
        }
    }


    /**
     * @param device 连接蓝牙设备
     */
    public final void ConnectBt(String device) {
        try {
            EventKit.Connect(rxHandler, this, this, bl, device, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param device       连接蓝牙设备
     * @param cleanHistory 清除蓝牙连接末次地址历史记录
     */
    public final void ConnectBt(String device, boolean cleanHistory) {
        try {
            EventKit.Connect(rxHandler, this, this, bl, device, cleanHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 断开蓝牙
     */
    public final void DisconnectBt() {
        EventKit.disconnect(rxHandler, this, this, bl);
    }


    @Override
    public void httpErr(HttpResponseModel var1) throws Exception {
        super.httpErr(var1);
        if (var1 == null) {
            return;
        }
        showToast(new String(var1.getResponse()));
        BtTradeProxy.getInstance().reset();
        if (var1.getRequestUrl().equals("SaleReq")) {
            MultiLineAttributes attr = new MultiLineAttributes(5, new MultiLineAttributes.MultiLineAttribute[]{
                    new MultiLineAttributes.MultiLineAttribute(3, 1, 2, "消费失败"),
                    new MultiLineAttributes.MultiLineAttribute(4, 1, 2, "网络故障")
            });
            EventKit.showMsg(attr);
            rxHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BtTradeProxy.getInstance().reset();
                }
            }, 5000);
        }
    }

    @Override
    public void httpSuccess(HttpResponseModel var1) throws Exception {
        super.httpSuccess(var1);
        JSONObject json = null;
        try {
            json = new JSONObject(new String(var1.getResponse()));
        } catch (JSONException e) {
            e.printStackTrace();
            json = null;
        }
        HttpModule module = GdHttpClient.getInstance().ResponseDataPick(var1, json);
        HttpModule(module);
    }


    protected <D extends RespBody> HttpModule<D> ConvertHttpModule(Class<D> clazz, HttpModule module) throws Exception {
        return (HttpModule<D>) module;
    }

    public static BaseActivity getCurrent() {
        return mAct;
    }

    @Override
    public void onConnected(Object result) {
        showToast("连接成功");
        if (!EventKit.MainKeyIsWrited(this)) {
            EventKit.UpdateTermkeyReq(this, this);
        } else {
            EventKit.UpdateWorkKeyReq(this, this);
        }
    }

    @Override
    public void onDisConnected() {
        showToast("连接断开");
        AppCenter.getInstance().mountDiscoveryCallBack(this);
        currDev = null;
        AppCenter.getInstance().setWorkKeySucc(false);
    }

    protected void HttpModule(HttpModule module) {
        if (module != null) {
            switch (module.commandId) {
                case IConstant.commandId.CheckVersionResp:
                    HttpModule<CheckVersionResp> respCheckVer = null;
                    try {
                        respCheckVer = ConvertHttpModule(CheckVersionResp.class,
                                module);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (respCheckVer.retCode == 0) {
                        if (respCheckVer.body.flags == 1) {
                            EventKit.Update(rxHandler, this, this, respCheckVer.body.needUpdate, respCheckVer.body.releaseNotes, respCheckVer.body.downloadAddr);
                        }
                    } else errorMSG(respCheckVer.errMsg);
                    break;
                case IConstant.commandId.UpdateTermkeyResp:
                    HttpModule<UpdateTermkeyResp> respUpdateTermkey = null;
                    try {
                        respUpdateTermkey = ConvertHttpModule(UpdateTermkeyResp.class,
                                module);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(respUpdateTermkey.errMsg)) {
                        EventKit.WriteTransKey(rxHandler,this,this,respUpdateTermkey.body.termKey);

                    } else errorMSG(respUpdateTermkey.errMsg);
                    break;
                case IConstant.commandId.UpdateWorkKeyResp:
                    HttpModule<UpdateWorkKeyResp> respUpdateWork = null;
                    try {
                        respUpdateWork = ConvertHttpModule(UpdateWorkKeyResp.class,
                                module);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (respUpdateWork.retCode == 0) {
                        UpdateWorkKeyResp workKey = respUpdateWork.body;
                        EventKit.WriteWorkKey(rxHandler, this, this, bl, workKey);
                    } else {
                        errorMSG(respUpdateWork.errMsg);
                    }
                    break;
                case IConstant.commandId.BalanceQueryResp:
                    HttpModule<BalanceQueryResp> respBalance = null;
                    try {
                        respBalance = ConvertHttpModule(BalanceQueryResp.class,
                                module);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (respBalance.retCode == 0) {
                        showToast("余额查询--金额:" + respBalance.body.balanceAmount);
                    } else errorMSG(respBalance.errMsg);
                    break;
                case IConstant.commandId.QueryTransResp:
                    HttpModule<QueryTransResp> transResp = null;
                    try {
                        transResp = ConvertHttpModule(QueryTransResp.class,
                                module);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (transResp.retCode == 0) {
                        showToast("交易明细--总数:" + transResp.body.totalNum);
                    } else errorMSG(transResp.errMsg);
                    break;

                case IConstant.commandId.UnSaleResp:
                    HttpModule<UnSaleResp> unSaleResp = null;
                    try {
                        unSaleResp = ConvertHttpModule(UnSaleResp.class,
                                module);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (unSaleResp.retCode == 0) {
                        showToast("撤销消费--金额:" + unSaleResp.body.amount);
                    } else errorMSG(unSaleResp.errMsg);
                    break;
                case IConstant.commandId.SignResp:
                    HttpModule<SignResp> signResp = null;
                    try {
                        signResp = ConvertHttpModule(SignResp.class,
                                module);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (signResp.retCode == 0) {

                        showToast("签名图片--地址:" + signResp.body.signOrderUrl);
                    } else errorMSG(signResp.errMsg);

                    BtTradeProxy.getInstance().reset();
                    break;
                case IConstant.commandId.SaleResp:
                    HttpModule<SaleResp> saleResp = null;
                    try {
                        saleResp = ConvertHttpModule(SaleResp.class,
                                module);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (saleResp.retCode == 0) {
                        showToast("消费--金额:" + saleResp.body.amount);
                    } else errorMSG(saleResp.errMsg);
                    break;
            }
        }
    }

    @Override
    public void onWriteMainKey(Object result) {
        String s = "";

        if (0 == (Integer) result) {
            s = "主密钥更新成功!";
            PublicKey();
        } else if (1 == (Integer) result) {
            s = "算法不支持!";
        } else if (2 == (Integer) result) {
            s = "密钥不存在!";
        } else if (3 == (Integer) result) {
            s = "其他错误!";
        }
        showToast(s);
    }

    protected void AidKey() {
        EventKit.WriteAidKey(rxHandler, this, this, bl);
    }

    protected void PublicKey() {
        EventKit.WritePublicKey(rxHandler, this, this, bl);
    }

    @Override
    public void onWriteAid(Object result) {
        String s = "";
        Result1LLVar result1LLVar = (Result1LLVar) result;
        if (result1LLVar != null && 0 == result1LLVar.getState()) {
            s = "设置AID成功! ";
            SharedTool.saveBooleanString(this, getString(R.string.mainKey), getString(R.string.write), IConstant.PLATFORM);
            EventKit.UpdateWorkKeyReq(this, this);
        } else {
            s = "设置AID失败!";
        }
        showToast(s);
    }

    @Override
    public void onWritePublicKey(Object result) {
        String s = "";
        Result1LLVar result1LLVar = (Result1LLVar) result;
        if (null != result1LLVar && 0 == result1LLVar.getState()) {
            s = "设置公钥成功!";
            AidKey();
        } else {
            s = "设置公钥失败!";
        }
        showToast(s);
    }


    @Override
    public void onWriteWorkKey(Object result) {
        String s = "";
        if (0 == (Integer) result) {
            s = "工作密钥更新成功!";
            AppCenter.getInstance().setWorkKeySucc(true);
        } else if (1 == (Integer) result) {
            s = "算法不支持!";
        } else if (2 == (Integer) result) {
            s = "密钥不存在!";
        } else if (3 == (Integer) result) {
            s = "秘钥错误!";
        }
        showToast(s);
    }

    @Override
    public void onDiscovery(BluetoothDevice bluetoothDevice) {
    }

    @Override
    public void DownloadComplete(File file) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + file.getPath()), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    @Override
    public void DownloadError(int needUpdate, Object object, MDialog dialog) {
        if (needUpdate == 1)
            finish();
        else {
            if (dialog != null) {
                dialog.replaceContent("下载失败");
                dialog.turnOn();
            }
            ConnectLast();
        }
    }

    @Override
    public void onWriteTransKey(Object result, String key, String keyValue) {
        int obj = (int) result;
        if (obj==1) {
            showToast("传输秘钥设置失败");
            return;
        }
        EventKit.WriteMainKey(rxHandler, this, this, key,keyValue, bl);
    }
}
