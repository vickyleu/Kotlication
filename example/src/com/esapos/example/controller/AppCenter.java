package com.esapos.example.controller;

import android.bluetooth.BluetoothDevice;
import android.os.Message;
import android.util.Log;

import com.esapos.lib.Controller.BtHelper;
import com.esapos.lib.Controller.BtMgr;
import com.esapos.lib.model.Component.HttpLibrary.BaseHttpConnectPool;
import com.esapos.lib.model.Component.RxJava.RxConstant;
import com.esapos.lib.model.Component.RxJava.RxHandler;
import com.esapos.example.controller.Component.Base.BaseApp;
import com.esapos.example.model.Bluetooth.BtResult;
import com.esapos.example.model.Bluetooth.DeviceDiscovery;
import com.nexgo.oaf.apiv2.BlueToothOperateInterface;
import com.nexgo.oaf.apiv2.RequestCardInterface;
import com.nexgo.oaf.apiv2.RequestDeviceInterface;
import com.nexgo.oaf.device.DeviceInfo;
import com.nexgo.oaf.key.Result1LLVar;
import com.nexgo.oaf.peripheral.ResultInputAmount;
import com.nexgo.oaf.peripheral.ResultInputPIN;
import com.nexgo.oaf.ys_apiv2.YS_RequestKeyInterface;
import com.nexgo.oaf.ys_card.ICCardInfo;
import com.nexgo.oaf.ys_card.YS_MagCardInfo;

import org.scf4a.ConnSession;

import oaf.datahub.DatahubInit;

/**
 * Created by VickyLeu on 2016/7/20.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public class AppCenter extends BaseApp {
    private static final String TAG = AppCenter.class.getSimpleName();
    private static BaseHttpConnectPool pool = BaseHttpConnectPool.getInstance();


    @Override
    public void onCreate() {
        super.onCreate();
        setSn("E5HJ161Y10001078");
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        ConnSession.getInstance().uninit();
        DatahubInit.getInstance().uninit();
        BtMgr.close();
    }


    public static BaseHttpConnectPool http() {
        return pool;
    }

    @Override
    public BlueToothOperateInterface BlueToothController(RxHandler mHandler, DeviceDiscovery discovery) {
        return super.BlueToothController(mHandler, discovery);
    }


    @Override
    public RequestDeviceInterface DeviceController() {
        return super.DeviceController();
    }

    @Override
    public RequestCardInterface CardController() {
        return super.CardController();
    }

    @Override
    public YS_RequestKeyInterface KeyController() {
        return super.KeyController();
    }


    @Override
    public void onReceiveDiscoveredDevice(BluetoothDevice bluetoothDevice) {
        Log.e(TAG, "onReceiveDiscoveredDevice: " + discovery);
        if (discovery != null) {
            discovery.onDiscovery(bluetoothDevice);
        }
    }

    @Override
    public void onReceiveDeviceConnected() {
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = null;
            msg.arg1 = BtResult.CONN;
            mHandler.sendMessage(msg);
//            DialogHelper.getInstance().closePrompt();
            BtHelper.save(this, ConnSession.getInstance().getLastConnectedMAC());
        }
    }

    @Override
    public void onReceivePbocStartQPBOCOption(ICCardInfo icCardInfo) {
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = icCardInfo;
            msg.arg1 = BtResult.QPBOC;
            mHandler.sendMessage(msg);
        }
    }


    @Override
    public void onReceiveDeviceDisConnected() {
        if (disc != null) {
            disc.onDisConnected();
//            DialogHelper.getInstance().closePrompt2();
            if (bl != null) EventKit.scanner(mHandler, bl);
        }
    }


    @Override
    public void onReceiveDeviceInfo(DeviceInfo deviceInfo) {
        Log.e(TAG, "onReceiveDeviceInfo: " + deviceInfo + mHandler);
        if (mHandler != null) {
            Log.e(TAG, "onReceiveDeviceInfo: ");
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = deviceInfo;
            msg.arg1 = BtResult.DEVICE;
            mHandler.sendMessage(msg);
        }
    }


    @Override
    public void onReceiveCheckCard(YS_MagCardInfo ys_magCardInfo) {
        Log.e(TAG, "onReceiveCheckCard: " + mHandler);
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = ys_magCardInfo;
            msg.arg1 = BtResult.CHECKCARD;
            mHandler.sendMessage(msg);
        }
    }


    @Override
    public void onReceiveCloseCheckCard(int i) {
        Log.e(TAG, "onReceiveCloseCheckCard: " + mHandler);
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = i;
            msg.arg1 = BtResult.CLOSECHECK;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onReceivePbocStartOption(ICCardInfo bytes) {
        Log.e(TAG, "onReceivePbocStartOption: " + mHandler);
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = bytes;
            msg.arg1 = BtResult.PBOCSTART;
            mHandler.sendMessage(msg);
        }
    }


    @Override
    public void onReceiveSetPosTerminal(int result) {
        Log.e(TAG, "onReceiveSetPosTerminal:" + result);
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = result;
            msg.arg1 = BtResult.UNKNOWN;
            mHandler.sendMessage(msg);
        }
    }


    @Override
    public void onReceiveAppointInputPIN(ResultInputPIN result) {
        Log.e(TAG, "onReceiveAppointInputPIN: ");
        if (mHandler != null) {
            Log.e(TAG, "InputPIN: ");
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = result;
            msg.arg1 = BtResult.INPUTPIN;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onReceiveAppointInputAmount(ResultInputAmount result) {
        Log.e(TAG, "onReceiveAppointInputPIN: ");
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = result;
            msg.arg1 = BtResult.INPUTAMOUMT;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onReceivePbocSetPublicKey(Result1LLVar result1LLVar) {
        Log.e(TAG, "onReceiveSetPKCertificate: ");
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = result1LLVar;
            msg.arg1 = BtResult.PUBKEY;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onReceivePbocSetAID(Result1LLVar result1LLVar) {
        Log.e(TAG, "onReceivePbocSetAID: ");
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = result1LLVar;
            msg.arg1 = BtResult.AID;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onReceiveUpdateMasterKey(int i) {
        Log.e(TAG, "onReceiveUpdateMasterKey: ");
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = i;
            msg.arg1 = BtResult.MAINKEY;
            mHandler.sendMessage(msg);

        }
    }

    @Override
    public void onReceiveUpdateEncryMasterKey(int result) {
        Log.e(TAG, "onReceiveCloseCheckCard: " + mHandler);
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = result;
            msg.arg1 = BtResult.ENCRYMASTER;
            mHandler.sendMessage(msg);
        }
    }


    @Override
    public void onReceiveUpdateWorkingKey(int i) {
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = RxConstant.RxHandlerFlag;
            msg.obj = i;
            msg.arg1 = BtResult.WORKKEY;
            mHandler.sendMessage(msg);
        }
    }


}
