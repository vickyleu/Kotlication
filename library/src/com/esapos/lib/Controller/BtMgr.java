package com.esapos.lib.Controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import java.util.Set;

/**
 * Created by Administrator on 2016/7/14.
 */
public class BtMgr {
    private static final String TAG = BtMgr.class.getSimpleName();
    static BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    public static int open(Context context) {
        if (adapter == null) {

            return -1;
        }
        if (!adapter.isEnabled()) {
            adapter.enable();

            int count = 3;
            while (!adapter.isEnabled() && count != 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count--;
            }
            if (!adapter.isEnabled()) {
//                Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                if (context instanceof Activity){
//                    ((Activity) context).startActivityForResult(mIntent, 10086);
                return 1;
//                } else return 2;
            }
        }
        return 0;
    }

    public static Set<BluetoothDevice> begin() {
        if (adapter != null) {
            if (adapter.isDiscovering()) adapter.cancelDiscovery();
            boolean rlt = adapter.startDiscovery();
            Log.e(TAG, "begin: " + rlt);
            return adapter.getBondedDevices();


        }
        return null;
    }

    public static void cancelDiscovery() {
        if (adapter != null) adapter.cancelDiscovery();
    }

    public static void close() {
        if (adapter != null) {
            adapter.disable();

//            BluetoothServerSocket serverSocket = adapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM, UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        }
    }

    public static BluetoothDevice getRemoteDev(String addr) {
        if (adapter != null) {
            try {
                return adapter.getRemoteDevice(addr);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
