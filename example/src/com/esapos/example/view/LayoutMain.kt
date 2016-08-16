package com.esapos.example.view

import android.support.v7.widget.LinearLayoutManager
import com.esapos.lib.View.BaseAct
import com.esapos.example.R
import com.esapos.example.controller.Adapter.RvAdapter
import com.esapos.example.controller.EventKit
import com.esapos.example.model.IConstant
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Created by Administrator on 2016/8/15.
 */
class LayoutMain : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            weightSum = 14F
            val rv = recyclerView() { id = IConstant.ID.rv }.lparams {
                weight = 10F
                width= matchParent
                height=0
            }
            rv.layoutManager = LinearLayoutManager(ctx)
            var ada = RvAdapter(ctx, null);
            rv.adapter = ada
            ada.setOnItemClick(owner)
            val e = editText {
                id = IConstant.ID.et
            }.lparams {
                weight = 1f
                width= matchParent
                height=0
            }
            e.hint = "你们好"
            button(ctx.getString(R.string.abc_search_hint)) {
                onClick {
                    EventKit.scanner(BaseAct.rxHandler, owner.bl)
                    enabled = false
                    postDelayed({
                        enabled = true
                    }, 1000)
                }
                backgroundResource = R.drawable.btn_selector
                textColor=R.color.colorAccent
                textSize=18f
            }.lparams {
                weight = 2F
                width= matchParent
                height=0
            }
        }
    }
}


