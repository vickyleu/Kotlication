package com.esapos.lib.Controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.util.Log;


import com.esapos.lib.Utils.SharedTool;
import com.esapos.lib.model.Bluetooth.BtInterface;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by VickyLeu on 2016/7/14.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public class BtHelper {
    private static final String TAG = BtHelper.class.getSimpleName();
    static Set<BluetoothDevice> found = new HashSet<>();
    static Set<BluetoothDevice> offline = new HashSet<>();
    static BtInterface btInterface = null;
    static String pairingCode = "0000";
    //    static final Object mLock = new Object();
    private static boolean mFlag = false;

    /**
     * 搜索
     *
     * @param context    上下文
     * @param interfaces 返回接口
     * @return 蓝牙状态
     */
    public static int Discovery(Context context, BtInterface interfaces) {
        btInterface = interfaces;
        offline.clear();
        found.clear();
        int stat = BtMgr.open(context);
        if (stat == -1 || stat == 1) {
            return stat;
        }
        Receiver(context);
        search();

        return stat;
    }

    /**
     * 开始搜索
     */
    private static void search() {
        Log.e(TAG, "search: ");
        Set<BluetoothDevice> set = BtMgr.begin();
        if (set != null && !set.isEmpty()) {
            offline.addAll(set);
        }
    }

    /**
     * 配对
     *
     * @param device 蓝牙设备
     * @return 配对状态
     */
    public static boolean bond(BluetoothDevice device) {
        Boolean returnValue = false;

        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            try {
//                Method removeBondMethod = BluetoothDevice.class.getDeclaredMethod("setPin", new Class[]{byte[].class});
//                Boolean b = (Boolean) removeBondMethod.invoke(device, new Object[]{pairingCode.getBytes()});
//                Log.e(TAG, "bond:==== "+b );

                Class<? extends BluetoothDevice> btClass = device.getClass();
                Method setPinRef = btClass
                        .getDeclaredMethod("setPin", byte[].class);
                Boolean ret = (Boolean) setPinRef.invoke(device, pairingCode.getBytes());

                Method createBondMethod = BluetoothDevice.class
                        .getMethod("createBond");
                returnValue = (Boolean) createBondMethod.invoke(device);
                Log.e(TAG, "bond: " + returnValue);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "bond: " + e.getMessage());
            }
        }

        Log.e(TAG, "bond: " + returnValue);

//        try {
//            if (device.getBondState() == BluetoothDevice.BOND_NONE) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                    device.setPin("0".getBytes());
//                    returnValue = device.createBond();
//                } else {
//                    //利用反射方法调用BluetoothDevice.createBond(BluetoothDevice remoteDevice);
//                    Method createBondMethod = BluetoothDevice.class
//                            .getMethod("createBond");
//
//                    returnValue = (Boolean) createBondMethod.invoke(device);
//                }
//            }
////            else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return returnValue;
    }

    /**
     * 取消搜索
     */
    public static void cancelDiscovery() {
        BtMgr.cancelDiscovery();
    }

    /**
     * @param context
     * @param intent  广播意图
     */
    private static void Change(Context context, Intent intent) {
        String action = intent.getAction();
        // 获得已经搜索到的蓝牙设备
        if (action.equals(BluetoothDevice.ACTION_UUID)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
            StringBuffer out = new StringBuffer();
            for (Parcelable anUuidExtra : uuidExtra) {
                out.append("\n  Device: " + device.getName() + ", " + device + ", Service: " + anUuidExtra.toString());
            }
        }

        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
            BluetoothDevice device = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // 搜索到的不是已经绑定的蓝牙设备
            found.add(device);
            if (btInterface != null) {
                btInterface.Found(found);
                btInterface.Offline(offline);
            }
            // 搜索完成
        } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
            Release(context);
        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDING:
                    Log.d("BlueToothTestActivity", "正在配对......");
                    break;
                case BluetoothDevice.BOND_BONDED:
                    Log.d("BlueToothTestActivity", "完成配对");

                    break;
                case BluetoothDevice.BOND_NONE:
                    Log.d("BlueToothTestActivity", "取消配对");
                default:
                    break;
            }
        }
    }

    /**
     * 注册广播
     *
     * @param context
     */
    private static void Receiver(Context context) {
        if (!mFlag) {
            IntentFilter mFilter = new IntentFilter();
            mFilter.setPriority(Integer.MAX_VALUE);
            mFilter.addAction(BluetoothDevice.ACTION_FOUND);
            mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            mFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            context.registerReceiver(mReceiver, mFilter);
            mFlag = true;
        }
    }

    /**
     * 释放蓝牙
     *
     * @param context
     */
    private static void Release(Context context) {


    }

    private static BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Change(context, intent);
        }
    };

    /**
     * 关闭蓝牙
     *
     * @param context
     */
    public static void shutdown(Context context) {
        Release(context);
//        BtMgr.close();
    }

    public static String lastDevice(Context context) {
        return SharedTool.getString(context, "Bluetooth", "last");
    }

    public static BluetoothDevice getRemoteDevice(String addr) {
        return BtMgr.getRemoteDev(addr);
    }

    public static void save(Context context, BluetoothDevice currDev) {
        SharedTool.saveString(context, "Bluetooth", "last", currDev.getAddress());
    }

    public static void save(Context context, String addr) {
        SharedTool.saveString(context, "Bluetooth", "last", addr);
    }

    public static void cleanLast(Context context) {
        SharedTool.saveString(context, "Bluetooth", "last", "");
    }
}
