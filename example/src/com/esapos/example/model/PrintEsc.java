package com.esapos.example.model;

import android.content.Context;

import com.esapos.lib.Controller.Esc.EscCommand;
import com.esapos.lib.Utils.StringUtil;
import com.esapos.lib.model.Component.Print.PrintData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by VickyLeu on 2016/8/6.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class PrintEsc extends PrintData {
    protected String title;
    protected String underline;
    protected String merName;
    protected String merId;
    protected String terminalNo;
    protected String terminalSN;
    protected String bankName;
    protected String cardNo;
    protected String saleType;
    protected String refNo;
    protected String timeStamp;
    protected String date;
    protected String amount;

    public PrintEsc(String merId, String bankName, String terminalNo, String terminalSN, String cardNo, String saleType, String refNo, String timeStamp,
                    String date, String amount) {
        this.title = "钱宝清算支付";
        this.underline = "商户存根";
        this.merName = "云刷科技";
        this.merId = merId;
        this.terminalNo = terminalNo;
        this.terminalSN = terminalSN;
        this.bankName = bankName;
        this.cardNo = StringUtil.cardEncrypt(cardNo);
        this.saleType = saleType;
        this.refNo = refNo;
        this.timeStamp = timeStamp;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        try {
            Date dt = sdf.parse(date);
            this.date = sdf2.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            this.date = date;
        }

        this.amount = amount;
    }

    public void setUnderline(String underline) {
        this.underline = underline;
    }

    @Override
    public void print(Context context, EscCommand esc) {

        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);//设置为倍高倍宽
        esc.addText(title + "\n");   //  打印文字
        /*打印文字*/
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
        esc.addText("-----------" + underline + "-----------\n");   //  打印文字
        esc.addText("商户名:  " + merName + "\n");   //  打印文字
        esc.addText("商户号:  " + merId + "\n");   //  打印文字
        esc.addText("终端号:  " + terminalNo + "\n");   //  打印文字
        esc.addText("设备号:  " + terminalSN + "\n");   //  打印文字
        esc.addText("发卡行:  " + bankName + "\n");   //  打印文字
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//设置为倍高倍宽
        esc.addText("卡 号:  \n");   //  打印文字

        esc.addText(" " + cardNo + "\n");   //  打印文字
        esc.addText("交易类型:" + saleType + "\n");   //  打印文字
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//取消倍高倍宽
        esc.addText("交易流水号:" + timeStamp + "\n");   //  打印文字
        esc.addText("参考号:" + refNo + "\n");   //  打印文字
        esc.addText("日期时间:" + date + "\n");   //  打印文字
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//设置为倍高倍宽
        esc.addText("金额:RMB " + amount + "\n");   //  打印文字
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//取消倍高倍宽
        esc.addText("持卡人签名: \n");   //  打印文字
        esc.addText("--------------------------------\n");   //  打印文字
        esc.addPrintAndLineFeed();
        esc.addPrintAndLineFeed();
        esc.addPrintAndLineFeed();
        esc.addPrintAndLineFeed();
        esc.addPrintAndLineFeed();
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
        esc.addText("--------------------------------\n");   //  打印文字
        esc.addText("本人确认以上交易同意计入本卡账户\n");   //  打印文字
        esc.addText("--------*--------------*--------\n");   //  打印文字
        esc.addPrintAndLineFeed();
        esc.addPrintAndLineFeed();

    }

    @Override
    public String toString() {
        return "PrintEsc{" +
                "title='" + title + '\'' +
                ", underline='" + underline + '\'' +
                ", merName='" + merName + '\'' +
                ", merId='" + merId + '\'' +
                ", terminalNo='" + terminalNo + '\'' +
                ", terminalSN='" + terminalSN + '\'' +
                ", bankName='" + bankName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", saleType='" + saleType + '\'' +
                ", refNo='" + refNo + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

}
