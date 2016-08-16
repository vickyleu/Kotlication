package com.esapos.example.view

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.esapos.lib.Controller.BtHelper
import com.esapos.example.controller.Adapter.RvAdapter
import com.esapos.example.controller.AppCenter
import com.esapos.example.kotlinTools.loge
import com.esapos.example.model.IConstant
import org.jetbrains.anko.setContentView

class MainActivity : BaseActivity(), RvAdapter.Item {


    override fun layout(): Int {
        return 0
    }

    override fun NeedCheck(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutMain().setContentView(this)
        initView()
    }

    override fun initView() {

    }

    override fun onItem(view: View, position: Int, device: BluetoothDevice) {
        ConnectBt(device.address)
    }

    @Synchronized override fun onResume() {
        super.onResume()
        var device: BluetoothDevice? = null
        try {
            device = BtHelper.getRemoteDevice(BtHelper.lastDevice(this))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (bl != null && AppCenter.getInstance().checkIsConn() && device != null) {
            currDev = device
            PutDataToAdapter(device)
        }
    }

    private fun PutDataToAdapter(device: BluetoothDevice) {
        if (device == null) return
        val adapter = find(RecyclerView::class.java, IConstant.ID.rv).adapter as RvAdapter
        adapter.add(device)
    }

    private val MTAG: String = MainActivity::class.java.simpleName

    override fun onDiscovery(bluetoothDevice: BluetoothDevice?) {

        loge(MTAG, "" + bluetoothDevice)

        super.onDiscovery(bluetoothDevice)
        if (bluetoothDevice != null)
            PutDataToAdapter(bluetoothDevice)
    }

    override fun onConnected(result: Any?) {
        super.onConnected(result)
        val adapter = find(RecyclerView::class.java, IConstant.ID.rv).adapter as RvAdapter
        adapter.putCurr(currDev)
    }


}
