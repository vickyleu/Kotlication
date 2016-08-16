package com.esapos.example.model.Bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by VickyLeu on 2016/7/21.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public interface DeviceDiscovery {
    void onDiscovery(BluetoothDevice bluetoothDevice);

    void onDisConnected();
}
