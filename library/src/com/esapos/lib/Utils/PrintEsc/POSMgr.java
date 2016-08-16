/**
 * Copyright (c) <2015>, <Christian Ebner cebner@gmx.at>
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.esapos.lib.Utils.PrintEsc;

import android.content.Context;

import com.esapos.lib.Controller.Esc.EscCommand;
import com.esapos.lib.model.Component.Print.PrintData;

import java.nio.charset.Charset;

/**
 * EPSON ESC/POS Commands
 * Created by ebc on 05.01.2015.
 * http://content.epson.de/fileadmin/content/files/RSD/downloads/escpos.pdf
 */
public class POSMgr {
    private USBPort mPort;


    public static final byte ESC = 27;
    public static final byte FS = 28;
    public static final byte GS = 29;
    public static final byte DLE = 16;
    public static final byte EOT = 4;
    public static final byte ENQ = 5;
    public static final byte SP = 32;
    public static final byte HT = 9;
    public static final byte LF = 10;
    public static final byte CR = 13;
    public static final byte FF = 12;
    public static final byte CAN = 24;

    /**
     * CodePage table
     */
    public static class CodePage {
        public static final byte PC437 = 0;
        public static final byte KATAKANA = 1;
        public static final byte PC850 = 2;
        public static final byte PC860 = 3;
        public static final byte PC863 = 4;
        public static final byte PC865 = 5;
        public static final byte WPC1252 = 16;
        public static final byte PC866 = 17;
        public static final byte PC852 = 18;
        public static final byte PC858 = 19;
    }

    /**
     * BarCode table
     */
    public static class BarCode {
        public static final byte UPC_A = 0;
        public static final byte UPC_E = 1;
        public static final byte EAN13 = 2;
        public static final byte EAN8 = 3;
        public static final byte CODE39 = 4;
        public static final byte ITF = 5;
        public static final byte NW7 = 6;
        //public static final byte CODE93      = 72;
        // public static final byte CODE128     = 73;
    }

    public POSMgr(USBPort _port) {
        mPort = _port;
    }


    /**
     * Print and line feed
     * LF
     *
     * @return bytes for this command
     */
    public byte[] printLinefeed() {
        byte[] result = new byte[1];
        result[0] = LF;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Turn underline mode on, set at 1-dot width
     * ESC - n
     *
     * @return bytes for this command
     */
    public byte[] underline1DotOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Turn underline mode on, set at 2-dot width
     * ESC - n
     *
     * @return bytes for this command
     */
    public byte[] underline2DotOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Turn underline mode off
     * ESC - n
     *
     * @return bytes for this command
     */
    public byte[] underlineOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }


    /**
     * Initialize printer
     * Clears the data in the print buffer and resets the printer modes to the modes that were
     * in effect when the power was turned on.
     * ESC @
     *
     * @return bytes for this command
     */
    public byte[] initPrinter() {
        byte[] result = new byte[2];
        result[0] = ESC;
        result[1] = 64;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Turn emphasized mode on
     * ESC E n
     *
     * @return bytes for this command
     */
    public byte[] emphasizedOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Turn emphasized mode off
     * ESC E n
     *
     * @return bytes for this command
     */
    public byte[] emphasizedOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * doubleStrikeOn
     * ESC G n
     *
     * @return bytes for this command
     */
    public byte[] doubleStrikeOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 71;
        result[2] = 0xF;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * doubleStrikeOff
     * ESC G n
     *
     * @return bytes for this command
     */
    public byte[] doubleStrikeOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 71;
        result[2] = 0xF;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select Font A
     * ESC M n
     *
     * @return bytes for this command
     */
    public byte[] selectFontA() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select Font B
     * ESC M n
     *
     * @return bytes for this command
     */
    public byte[] selectFontB() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 1;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select Font C ( some printers don't have font C )
     * ESC M n
     *
     * @return bytes for this command
     */
    public byte[] selectFontC() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 2;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * double height width mode on Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public byte[] doubleHeightWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 56;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * double height width mode off Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public byte[] doubleHeightWidthOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select double height mode Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public byte[] doubleHeightOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 16;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * disable double height mode, select Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public byte[] doubleHeightOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * justificationLeft
     * ESC a n
     *
     * @return bytes for this command
     */
    public byte[] justificationLeft() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * justificationCenter
     * ESC a n
     *
     * @return bytes for this command
     */
    public byte[] justificationCenter() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * justificationRight
     * ESC a n
     *
     * @return bytes for this command
     */
    public byte[] justificationRight() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Print and feed n lines
     * Prints the data in the print buffer and feeds n lines
     * ESC d n
     *
     * @param n lines
     * @return bytes for this command
     */
    public byte[] printAndFeedLines(byte n) {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 100;
        result[2] = n;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Print and reverse feed n lines
     * Prints the data in the print buffer and feeds n lines in the reserve direction
     * ESC e n
     *
     * @param n lines
     * @return bytes for this command
     */
    public byte[] printAndReverseFeedLines(byte n) {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 101;
        result[2] = n;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Drawer Kick
     * Drawer kick-out connector pin 2
     * ESC p m t1 t2
     *
     * @return bytes for this command
     */
    public byte[] drawerKick() {
        byte[] result = new byte[5];
        result[0] = ESC;
        result[1] = 112;
        result[2] = 0;
        result[3] = 60;
        result[4] = 120;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select printing color1
     * ESC r n
     *
     * @return bytes for this command
     */
    public byte[] selectColor1() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 114;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select printing color2
     * ESC r n
     *
     * @return bytes for this command
     */
    public byte[] selectColor2() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 114;
        result[2] = 1;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select character code table
     * ESC t n
     *
     * @param cp example:CodePage.WPC1252
     * @return bytes for this command
     */
    public byte[] selectCodeTab(byte cp) {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 116;
        result[2] = cp;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * white printing mode on
     * Turn white/black reverse printing mode on
     * GS B n
     *
     * @return bytes for this command
     */
    public byte[] whitePrintingOn() {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 66;
        result[2] = (byte) 128;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * white printing mode off
     * Turn white/black reverse printing mode off
     * GS B n
     *
     * @return bytes for this command
     */
    public byte[] whitePrintingOff() {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 66;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * feed paper and cut
     * Feeds paper to ( cutting position + n x vertical motion unit )
     * and executes a full cut ( cuts the paper completely )
     *
     * @return bytes for this command
     */
    public byte[] feedPaperCut() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 65;
        result[3] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * feed paper and cut partial
     * Feeds paper to ( cutting position + n x vertical motion unit )
     * and executes a partial cut ( one point left uncut )
     *
     * @return bytes for this command
     */
    public byte[] feedPaperCutPartial() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 66;
        result[3] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * select bar code height
     * Select the height of the bar code as n dots
     * default dots = 162
     *
     * @param dots ( heigth of the bar code )
     * @return bytes for this command
     */
    public byte[] barcodeHeight(byte dots) {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 104;
        result[2] = dots;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * select font hri
     * Selects a font for the Human Readable Interpretation (HRI) characters when printing a barcode, using n as follows:
     *
     * @param n Font
     *          0, 48 Font A
     *          1, 49 Font B
     * @return bytes for this command
     */
    public byte[] selectFontHri(byte n) {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 102;
        result[2] = n;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * select position_hri
     * Selects the print position of Human Readable Interpretation (HRI) characters when printing a barcode, using n as follows:
     *
     * @param n Print position
     *          0, 48 Not printed
     *          1, 49 Above the barcode
     *          2, 50 Below the barcode
     *          3, 51 Both above and below the barcode
     * @return bytes for this command
     */
    public byte[] selectPositionHri(byte n) {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 72;
        result[2] = n;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * print bar code
     *
     * @param barcode_typ(Barcode.CODE39,Barcode.EAN8,...)
     * @param barcode2print
     * @return bytes for this command
     */
    public byte[] printBarCode(byte barcode_typ, String barcode2print) {
        byte[] barcodebytes = barcode2print.getBytes();
        byte[] result = new byte[3 + barcodebytes.length + 1];
        result[0] = GS;
        result[1] = 107;
        result[2] = barcode_typ;
        int idx = 3;

        for (int i = 0; i < barcodebytes.length; i++) {
            result[idx] = barcodebytes[i];
            idx++;
        }
        result[idx] = 0;
        mPort.AddData2Printer(result);

        return result;
    }


    /**
     * Set horizontal tab positions
     *
     * @param col ( coulumn )
     * @return bytes for this command
     */
    public byte[] setHTPosition(byte col) {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * printLine
     * adds a LF command to the text
     *
     * @param line (text to print)
     */
    public void printLine(String line) {
        if (line.isEmpty()) return;
        mPort.AddData2Printer(line.getBytes(Charset.forName("ISO-8859-1")));
        printLinefeed();
    }

    /**
     * printText
     * without LF , means text is not printed immediately
     *
     * @param line (text to print)
     */
    public void printText(String line) {
        if (line.isEmpty()) return;
        //mPort.AddData2Printer(line.getBytes());
        mPort.AddData2Printer(line.getBytes(Charset.forName("ISO-8859-1")));
    }


    public void printSample1() {
        String test = null;
        initPrinter();
        selectFontA();
        selectCodeTab(CodePage.WPC1252);
        underline1DotOn();
        justificationCenter();
        test = "Sample Receipt 1";
        printLine(test);
        test = "Umlaute";
        printLine(test);
        doubleHeightWidthOn();
        test = "ÄÖÜß";
        printLine(test);
        doubleHeightWidthOff();

        printLinefeed();
        feedPaperCut();
    }


    public void print(Context context,PrintData data) {
        EscCommand esc = new EscCommand(mPort);
        data.print(context,esc);
        esc.addPrintAndLineFeed();
        esc.addPrintAndLineFeed();
        esc.addPrintAndLineFeed();
        esc.addPrintAndLineFeed();
        esc.addCutPaper();

    }


}
