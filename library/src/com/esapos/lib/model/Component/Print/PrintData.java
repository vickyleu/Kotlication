package com.esapos.lib.model.Component.Print;

import android.content.Context;

import com.esapos.lib.Controller.Esc.EscCommand;

/**
 * Created by VickyLeu on 2016/8/6.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public abstract class PrintData implements Cloneable {

    public abstract void print(Context context, EscCommand esc);

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
