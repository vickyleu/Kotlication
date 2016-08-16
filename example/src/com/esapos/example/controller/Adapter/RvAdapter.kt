package com.esapos.example.controller.Adapter

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.esapos.lib.Utils.FindView
import com.esapos.example.R
import com.esapos.example.model.IConstant
import org.jetbrains.anko.*
import java.util.*

/**
 * Created by Administrator on 2016/8/15.
 */

class RvAdapter(var mContext: Context,  var mList: MutableList<BluetoothDevice>?) :
        RecyclerView.Adapter<RvAdapter.VH>(), View.OnClickListener {
    //    internal var mList: MutableList<BluetoothDevice> = ArrayList()
    private var itemClick: Item? = null
    internal var anko=AnkoContext.createReusable(mContext, this)
    internal var dm = DisplayMetrics()
    private var currDev: BluetoothDevice? = null

    init {
        val win = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dsp = win.defaultDisplay
        dsp.getMetrics(dm)
        if (mList == null) {
            mList = ArrayList()
        }
    }

    internal class BtItem() : AnkoComponent<RvAdapter> {
        fun createItemView(ui: AnkoContext<RvAdapter>) = buildView(ui)

        private fun buildView(ui: AnkoContext<RvAdapter>): View {
            val view = ui.apply {
                val lay =
                verticalLayout {
                    space { }.lparams {
                        weight = 0.4f
                        height = matchParent
                        width = 0

                    }
                    imageView {
                        id = IConstant.ID.imageRvItem
                        scaleType = ImageView.ScaleType.FIT_XY
                        imageResource = R.drawable.disconn

                    }.lparams {
                        weight = 2f
                        width = 0
                        height = matchParent
                    }

                    space {}.lparams {
                        weight = 0.3f
                        height = matchParent
                        width = 0
                    }

                    verticalLayout {
                        textView("MPOS1") {
                            id = IConstant.ID.titleRvItem
                            lines = 1
                            text="MPOS1"
                            textSize = 16f
                        }.lparams {
                            weight = 1f
                            width = matchParent
                            gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
                            height = 0
                        }
                        textView("1.1.1.1") {
                            id = IConstant.ID.addrRvItem
                            lines = 1
                            text="1.1.1.1"
                            textSize = 14f
                        }.lparams {
                            weight = 1f
                            width = matchParent
                            gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
                            height = 0
                        }
                    }.lparams {
                        orientation = LinearLayout.VERTICAL
                        width = 0
                        weight = 5f
                        weightSum = 2f
                        gravity = Gravity.CENTER_VERTICAL
                        height = matchParent
                    }



                    verticalLayout {
                        button("配对") {
                            onClick {
                                owner.onClick(this)
                            }
                            id = IConstant.ID.pairRvItem
                            lines = 1
                            backgroundResource=R.drawable.btn_selector
                            textSize = 10f
                        }.lparams {
                            weight = 2f
                            width = matchParent
                            height = 0
                        }
                    }.lparams {
                        orientation = LinearLayout.VERTICAL
                        width = 0
                        weight = 2f
                        weightSum = 5f
                        gravity = Gravity.CENTER
                        height = matchParent
                    }


                }
                lay.weightSum=10f
                lay.layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                lay.orientation = LinearLayout.HORIZONTAL
            }.view
            view.layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)
            Log.e(TAG, view.toString()+"")
            return view
        }

        override fun createView(ui: AnkoContext<RvAdapter>) = createItemView(ui)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val view = BtItem().createItemView(anko)
        val pm = view.getLayoutParams()
        Log.e(TAG, "onCreateViewHolder")
        pm.height = dm.heightPixels / 7
        view.setLayoutParams(pm)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Log.e(TAG, "onBindViewHolder")
        val dv = mList!![position]
        holder.title.text = if (!TextUtils.isEmpty(dv.name)) dv.name else "unknown"
        holder.addr.text = dv.address
        when (dv.bondState) {
            BluetoothDevice.BOND_BONDED -> {
                holder.icon.setImageResource(R.drawable.current_dev)
                holder.pair.text = "已配对"
                Log.e(TAG, "onBindViewHolder: " + currDev!!)
            }
            BluetoothDevice.BOND_BONDING -> {
                holder.icon.setImageResource(R.drawable.disconn)
                holder.pair.text = "配对中.."
            }
            BluetoothDevice.BOND_NONE -> {
                holder.icon.setImageResource(R.drawable.disconn)
                holder.pair.text = "配对"
            }
        }
        if (currDev != null && currDev!!.address == dv.address) {
            Log.e(TAG, "onBindViewHolder:currDev " + currDev!!)
            holder.pair.text = "已连接"
        }
        holder.pair.tag = position
        holder.pair.setOnClickListener(this)
    }


    fun add(device: BluetoothDevice) {
        Log.e("====", "" + device.name)
        if (mList == null) {
            mList = ArrayList()
        }
        if (!mList!!.contains(device)) {
            Log.e("===", "" + device.name)
            mList!!.add(device)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        
        Log.e(TAG,"getItemCount")
        if (mList!!.isEmpty()) return 0
        return mList!!.size
    }

    override fun onClick(v: View) {
        if (v is Button) {
            val pos = v.tag as Int
            if (itemClick != null) itemClick!!.onItem(v, pos, mList!![pos])
        }
    }


    fun setOnItemClick(itemClick: Item) {
        this.itemClick = itemClick
    }

    fun putCurr(currDev: BluetoothDevice) {
        this.currDev = currDev
    }

    fun clear() {
        mList!!.clear()
        notifyDataSetChanged()
    }

    interface Item {
        fun onItem(view: View, position: Int, device: BluetoothDevice)
    }

    inner class VH(iv: View) : RecyclerView.ViewHolder(iv) {
        var icon: ImageView
        var title: TextView
        var addr: TextView
        var pair: Button

        init {
            icon = find(iv, ImageView::class.java, IConstant.ID.imageRvItem)
            title = find(iv, TextView::class.java, IConstant.ID.titleRvItem)
            addr = find(iv, TextView::class.java, IConstant.ID.addrRvItem)
            pair = find(iv, Button::class.java, IConstant.ID.pairRvItem)
        }
    }

    private fun <T : View> find(iv: View, cls: Class<T>, resId: Int): T {
        return FindView.findView(iv, cls, resId)
    }

    companion object {
        private val TAG = RvAdapter::class.java.simpleName
    }

}
