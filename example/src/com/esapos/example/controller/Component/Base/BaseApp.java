package com.esapos.example.controller.Component.Base;

import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.usb.UsbManager;

import com.esapos.lib.Utils.LogcatHelper;
import com.esapos.lib.Utils.SharedTool;
import com.esapos.lib.api.SilenceInstallApi;
import com.esapos.lib.api.SilenceInstallService;
import com.esapos.lib.model.Component.HttpLibrary.HttpCacheApp;
import com.esapos.lib.model.Component.RxJava.RxHandler;
import com.esapos.example.R;
import com.esapos.example.model.Bluetooth.DeviceDiscovery;
import com.nexgo.oaf.apiv2.BlueToothOperateInterface;
import com.nexgo.oaf.apiv2.BlueToothOperateListener;
import com.nexgo.oaf.apiv2.CallBackDeviceInterface;
import com.nexgo.oaf.apiv2.CallBackPeripheralInterface;
import com.nexgo.oaf.apiv2.RequestCardInterface;
import com.nexgo.oaf.apiv2.RequestDeviceInterface;
import com.nexgo.oaf.apiv2.RequestPeripheralInterface;
import com.nexgo.oaf.card.PowerOnICCardBean;
import com.nexgo.oaf.card.ResultCalculationMAC;
import com.nexgo.oaf.device.DateTime;
import com.nexgo.oaf.device.UpdateProgress;
import com.nexgo.oaf.key.CheckKeyResult;
import com.nexgo.oaf.key.Result1LLVar;
import com.nexgo.oaf.ys_apiv2.YS_APIProxy;
import com.nexgo.oaf.ys_apiv2.YS_CallBackCardInterface;
import com.nexgo.oaf.ys_apiv2.YS_CallBackKeyInterface;
import com.nexgo.oaf.ys_apiv2.YS_RequestCardInterface;
import com.nexgo.oaf.ys_apiv2.YS_RequestKeyInterface;
import com.nexgo.oaf.ys_card.SecondAuthResult;

import org.scf4a.ConnSession;

import java.io.File;
import java.util.List;

import oaf.datahub.DatahubInit;

/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public abstract class BaseApp extends HttpCacheApp implements BlueToothOperateListener, CallBackDeviceInterface, CallBackPeripheralInterface, YS_CallBackCardInterface, YS_CallBackKeyInterface {
    private static final String TAG = BaseApp.class.getSimpleName();

    protected BlueToothOperateInterface bl = YS_APIProxy.getBlueTooth(this);
    private RequestDeviceInterface dev = YS_APIProxy.getDevice(this);
    private RequestPeripheralInterface ph = YS_APIProxy.getPeripheral(this);
    private YS_RequestCardInterface card = YS_APIProxy.getYSCard(this);

    private YS_RequestKeyInterface keyInterface = YS_APIProxy.getYSKey(this);
    private String sn = null;
    private static BaseApp instance;
    protected RxHandler mHandler = null;
    protected DeviceDiscovery discovery = null;
    protected DeviceDiscovery disc = null;
    private boolean workKeySucc = false;
    private UsbManager mUsbMgr = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LogcatHelper.getInstance(this).start();

        ConnSession.getInstance();
        DatahubInit.getInstance();
    }

    public boolean isWorkKeySucc() {
        return workKeySucc;
    }

    public void setWorkKeySucc(boolean workKeySucc) {
        this.workKeySucc = workKeySucc;
    }

    public static BaseApp getInstance() {
        return instance;
    }

    public String getSn() {
        return sn;
    }


    public void setSn(String sn) {
        this.sn = sn;
    }


    public RequestDeviceInterface DeviceController() {
        return dev;
    }

    public BlueToothOperateInterface BlueToothController(RxHandler mHandler, DeviceDiscovery disc) {
        this.mHandler = mHandler;
        this.disc = disc;
        return bl;
    }

    public void mountDiscoveryCallBack(DeviceDiscovery discovery) {
        this.discovery = discovery;
    }

    public boolean checkIsConn() {
        return bl != null && bl.requestBlueToothConnectedState();
    }

    public RequestPeripheralInterface PeripheralController() {
        return ph;
    }

    public RequestCardInterface CardController() {
        return card;
    }

    public YS_RequestKeyInterface KeyController() {
        return keyInterface;
    }


    @Override
    public void onReceivePbocSecondAuthorize(SecondAuthResult secondAuthResult) {

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onReceiveCalculationMAC(ResultCalculationMAC resultCalculationMAC) {

    }

    @Override
    public void onReceivePosDataAndTime(DateTime dateTime) {

    }

    @Override
    public void onReceiveShowMultiLine(boolean b) {

    }

    @Override
    public void onReceiveSetPKCertificate(Result1LLVar result1LLVar) {

    }

    @Override
    public void onReceiveUpdatePosAppAndFirmware(int i) {

    }

    @Override
    public void onReceiveUpdateCore(int i) {

    }

    @Override
    public void onReceiveSetDefaultDisplay(int i) {

    }

    @Override
    public void onReceiveProcess(UpdateProgress updateProgress) {

    }

    @Override
    public void onPinPress(byte b) {

    }

    @Override
    public void onReceiveCardHolderInputPin(boolean b, int i) {

    }

    @Override
    public void onReceviePrintInit() {

    }

    @Override
    public void onReceivePrintState(int i) {

    }

    @Override
    public void onReceivePrintMovingPaper() {

    }

    @Override
    public void onReceivePrintWriteContent(int i) {

    }

    @Override
    public void onReceivePrintSetSpacingOfLine(int i) {

    }

    @Override
    public void onReceivePrintSetConcentration(int i) {

    }

    @Override
    public void onReceiveStartPrint(int i) {

    }

    @Override
    public void onReceiveDownLoadFile(int i) {

    }

    @Override
    public void onReceiveSetWriteBitmapContent(int i) {

    }

    @Override
    public void onReceiveSetWriteTextContent(int i) {

    }

    @Override
    public void onReceiveBatteryState(int i) {

    }

    @Override
    public void onReceiveSetSn(int i) {

    }

    @Override
    public void onReceiveGetSn(String s, String s1) {

    }


    @Override
    public void onReceivePower(PowerOnICCardBean powerOnICCardBean) {

    }


    @Override
    public void onReceiveReset() {

    }

    @Override
    public void onConfirmCardNo(String s) {

    }

    @Override
    public void onCardHolderInputPin(boolean b, int i) {

    }

    @Override
    public void onCertVerify(String s, String s1) {

    }

    @Override
    public void onSelApp(List<String> list, boolean b) {

    }


    @Override
    public void onInputKey(byte b) {

    }


    @Override
    public void onReceiveDataEncryptionAndDecrypt(Result1LLVar result1LLVar) {

    }


    @Override
    public void onReceiveCheckMAC(int i) {

    }


    @Override
    public void onReceiveUpdatePrivateKey(int i) {

    }

    @Override
    public void onReceiveLoadEncryptMKey(int i) {

    }

    @Override
    public void onReceiveDesByTmsKey(byte[] bytes) {

    }

    @Override
    public void onReceiveCheckKeyResult(CheckKeyResult checkKeyResult) {

    }

    @Override
    public void onReceiveSetMac(byte[] bytes) {

    }

    public UsbManager UsbManager() {
        if (mUsbMgr == null) mUsbMgr = (UsbManager) getSystemService(USB_SERVICE);
        return mUsbMgr;
    }

    public int startInstallService(final File file, final String pkg, String string) {
        Intent intent = new Intent("com.esapos.lib.api.iUSBService");
        startService(intent);
        final Box box = new Box();


        while (box.flag) {
            final SilenceInstallService srv = SilenceInstallService.getInstant();
            if (srv == null) continue;
            break;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                box.retCode = SilenceInstallApi.getInstance().installAPK(SilenceInstallService.getInstant(), file, pkg, getString(R.string.app_name));
                box.flag = false;

            }
        }).start();

        //noinspection StatementWithEmptyBody
        while (box.flag) {
        }


        return box.retCode;
    }

    public void setMctNo(String mctNo) {
        SharedTool.saveString(this, "mct", "mctNo", mctNo);
    }

    public void setMctName(String mctName) {
        SharedTool.saveString(this, "mct", "mctName", mctName);
    }

    public String getMctName() {
        return SharedTool.getString(this, "mct", "mctName");
    }

    public String getMctNo() {
        return SharedTool.getString(this, "mct", "mctNo");
    }

    class Box {
        boolean flag = true;
        int retCode = -1;
    }
}
