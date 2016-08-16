package com.esapos.example.controller;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.esapos.lib.Utils.PrintEsc.POSMgr;
import com.esapos.lib.Utils.PrintEsc.USBPort;
import com.esapos.lib.model.Component.RxJava.RxConstant;
import com.esapos.lib.model.Component.RxJava.RxHandler;
import com.esapos.example.model.PrintEsc;

/**
 * Created by VickyLeu on 2016/8/6.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class PrintProxy {
    private static final String TAG = PrintProxy.class.getSimpleName();
    static PrintProxy proxy;
    private USBPort mUsbPort;
    private UsbManager mUsbManager;
    private POSMgr mPosMgr;
    public static final int VENDOR_ID = 1317;
    public static final int PRODUCT_ID = 42754;

    Handler handler = new Handler(Looper.getMainLooper());

    private PendingIntent mPermissionIntent;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    public PrintProxy() {
        mPermissionIntent = PendingIntent.getBroadcast(AppCenter.getInstance(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(mUsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(mUsbManager.ACTION_USB_DEVICE_ATTACHED);

//        AppCenter.getInstance().registerReceiver(mUsbReceiver, filter);
//        filter = new IntentFilter(mUsbManager.ACTION_USB_DEVICE_DETACHED);
//        AppCenter.getInstance().registerReceiver(mUsbReceiver, filter);
//        filter = new IntentFilter(mUsbManager.ACTION_USB_DEVICE_ATTACHED);
//
//
        AppCenter.getInstance().registerReceiver(mUsbReceiver, filter);

        mUsbManager = (UsbManager) AppCenter.getInstance().getSystemService(Context.USB_SERVICE);
        mUsbPort = new USBPort(mUsbManager);
        mPosMgr = new POSMgr(mUsbPort);

    }

    public static synchronized PrintProxy getInstance() {
        if (proxy == null) proxy = new PrintProxy();
        return proxy;
    }

    public void print(final RxHandler mHandler, final PrintEsc esc) {

        try {
//            UsbDevice foundDevice = mUsbPort.searchDevice(VENDOR_ID, PRODUCT_ID);
            UsbDevice foundDevice = mUsbPort.searchDevice(VENDOR_ID);
            if (foundDevice == null) {
                Log.e(TAG, "print: 未开启USB打印机");
                sendResult(mHandler, "未开启USB打印机");
                return;
            }
            if (!this.mUsbManager.hasPermission(foundDevice)) {
                this.mUsbManager.requestPermission(foundDevice, mPermissionIntent);
                sendResult(mHandler, "设备冷却中");
                return;
            }


            if (!mUsbPort.connectDevice(AppCenter.getInstance(), foundDevice)) {
                sendResult(mHandler, "设备连接失败");
                return;
            }


            mPosMgr.print(AppCenter.getInstance(), esc);
            final PrintEsc esc2 = (PrintEsc) esc.clone();
            esc2.setUnderline("持卡人存根");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mPosMgr != null) {

                        mPosMgr.print(AppCenter.getInstance(), esc2);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendResult(mHandler, "打印成功");
                            }
                        },500);

                    }

                }
            }, 1000 * 10);


        } catch (Exception e) {
            e.printStackTrace();
            sendResult(mHandler, "打印异常");
        }

    }

    public void closeUSB() {
        try {
            mUsbPort.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "run: InterruptedException" + e.getMessage());
        }
    }

    private void sendResult(RxHandler mHandler, String str) {
//        AppCenter.getInstance().unregisterReceiver(mUsbReceiver);
        Log.e(TAG, "sendResult: " + str);
        Message msg = new Message();
        msg.what = RxConstant.RxHandlerFlag;
        msg.obj = str;
        msg.arg1 = -1;
        mHandler.sendMessage(msg);
    }


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            // call method to set up device communication

                            Log.e(TAG, "onReceive: EXTRA_PERMISSION_GRANTED");
                        }
                    } else {
                        Log.d(TAG, "permission denied for device " + device);
                    }
                }
            }
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    // call your method that cleans up and closes communication with the device
                    mUsbPort.SetUSBConnectionFlag(false);
                    Log.e(TAG, "onReceive:  mUsbPort.SetUSBConnectionFlag(false);");
                }
            }

            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    // call your method that cleans up and closes communication with the device
                    mUsbPort.SetUSBConnectionFlag(false);
                    Log.e(TAG, "onReceive:  ACTION_USB_DEVICE_DETACHED;");
                }
            }
        }
    };

    String sn;
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
