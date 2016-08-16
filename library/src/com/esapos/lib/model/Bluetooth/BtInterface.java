package com.esapos.lib.model.Bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.Set;

/**
 * Created by Administrator on 2016/7/14.
 */
public  interface BtInterface {
    void Offline(Set<BluetoothDevice> devices);
    void Found(Set<BluetoothDevice> devices);
}
