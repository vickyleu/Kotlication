package com.esapos.example.controller;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.esapos.lib.Utils.ByteUtil;
import com.esapos.lib.Utils.CryptUtil1;
import com.esapos.example.model.Http.Resp.UpdateWorkKeyResp;
import com.nexgo.oaf.card.PbocQPBOCProcessAttribute;
import com.nexgo.oaf.card.StartPbocOptionAttribute;
import com.nexgo.oaf.device.MultiLineAttributes;
import com.nexgo.oaf.key.CalculationMAC;
import com.nexgo.oaf.key.WorkingKeys;

/**
 * Created by VickyLeu on 2016/7/20.
 *
 * @Author Vickyleu
 * @Company Esapos
 */
public class BtTradeProxy {
    private static final String TAG = BtTradeProxy.class.getSimpleName();
    private static BtTradeProxy instance;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private BtTradeProxy() {
    }

    /**
     * @return 蓝牙交易中间件代理
     */
    public static synchronized BtTradeProxy getInstance() {
        if (instance == null) instance = new BtTradeProxy();
        return instance;
    }


    /**
     * 检查银行卡
     *
     * @param money 输入的金额
     */
    public synchronized void CheckCard(String money) {
        Log.e(TAG, "CheckCard: ");
        MultiLineAttributes attr = new MultiLineAttributes(30, new MultiLineAttributes.MultiLineAttribute[]{
                new MultiLineAttributes.MultiLineAttribute(2, 1, 2, "金额:" + money),
                new MultiLineAttributes.MultiLineAttribute(3, 1, 2, "请刷（插）卡:")
        });
        showMsg(attr);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCenter.getInstance().CardController().requestCheckCard(0x07, 30, 0, 0xff);
            }
        }, 400);

    }

    public synchronized void showMsg(MultiLineAttributes attr) {
        AppCenter.getInstance().DeviceController().onShowMultiLine(attr);
    }


    /**
     * 关闭银行卡检查
     */
    public synchronized void closeCheck() {
        Log.e(TAG, "closeCheck: ");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCenter.getInstance().CardController().requestCloseCheckCard(0x07);
            }
        }, 400);

    }

    /**
     * 重设Mpos状态
     */
    public synchronized void reset() {
        Log.e(TAG, "reset: ");
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCenter.getInstance().CardController().requestReset();
            }
        }, 20);

    }

    /**
     * @param key 主密钥更新
     * @param keyValue
     */
    public synchronized void mainKey(final String key, final String keyValue) {
        Log.e(TAG, "convert: ");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                byte[] keyData = ByteUtil.hexStringToByte(CryptUtil1.encryptString(CryptUtil1.decryptString(key),keyValue));
                AppCenter.getInstance().KeyController().requestUpdateMasterKey(0x00, 0x00, keyData);
            }
        }, 400);
    }

    /**
     * @param key 工作秘钥更新
     */
    public synchronized void workKey(final UpdateWorkKeyResp key) {
        Log.e(TAG, "workKey: " + key.toString());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                byte[] pikText = ByteUtil.hexStringToByte(key.pinKey);
                byte[] pikCheckValue = ByteUtil.hexStringToByte(key.pinKeyCheckValue);
                String str = key.macKey.substring(0, key.macKey.length() - 16);
                str = str + str;
                byte[] makText = ByteUtil.hexStringToByte(str);
                byte[] makCheckValue = ByteUtil.hexStringToByte(key.macKeyCheckValue);
                WorkingKeys.WorkingKey[] workingKeys = new WorkingKeys.WorkingKey[]{
                        new WorkingKeys.WorkingKey(WorkingKeys.FLAG_PIK, WorkingKeys.TYPE_3DES, pikText, pikCheckValue),
                        new WorkingKeys.WorkingKey(WorkingKeys.FLAG_MAK, WorkingKeys.TYPE_3DES, makText, makCheckValue),
//                        new WorkingKeys.WorkingKey(WorkingKeys.FLAG_TDK, WorkingKeys.TYPE_3DES, tdkText, tdkCheckValue)
                };
                AppCenter.getInstance().KeyController().requestUpdateWorkingKey(0, 3, workingKeys);
                Log.e(TAG, "workKey:workKeyworkKeyworkKey " );
            }
        }, 100);
    }


    /**
     * @param key 开始pboc流程
     */
    public synchronized void pbocStart(final String key) {
        Log.e(TAG, "pbocStart: " + key.toString());

//        final String str = String.valueOf(10000000000000L * Float.parseFloat(key));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartPbocOptionAttribute attribute = new StartPbocOptionAttribute();
//                attribute.setAuthAccount(str); //金额必须格式化为长度为12的字符串

                attribute.setAuthAccount("0000000000" + "01"); //金额必须格式化为长度为12的字符串
                attribute.setAuthAccountOther("000000000000");//金额必须格式化为长度为12的字符串
                attribute.setPbocProcess(0x06);
                AppCenter.getInstance().CardController().requestPbocStartOption(attribute);
            }
        }, 100);
    }

    /**
     * pboc二次授权
     */
    public synchronized void pbocSecond() {
        Log.e(TAG, "pbocSecond: ");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                byte[] tlv = new byte[1024];
                AppCenter.getInstance().CardController().requestPbocSecondAuthorize(tlv);
            }
        }, 100);
    }

    /**
     * @param key 结束pboc流程
     */
    public synchronized void pbocStop(final String key) {
        Log.e(TAG, "pbocSecond: " + key.toString());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCenter.getInstance().CardController().requestPbocEndOption(0);
            }
        }, 100);
    }

    /**
     * @param key 计算MAC
     */
    public synchronized void calculationMAC(final String key) {
        Log.e(TAG, "pbocSecond: " + key.toString());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
                AppCenter.getInstance().CardController().requestCalculationMAC(0,
                        CalculationMAC.MAC_ECB,
                        CalculationMAC.CERTIFICATE_X509, data);
            }
        }, 100);
    }


    /**
     * @param key Aid更新
     */
    public synchronized void Aid(String key) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                byte[] pbocAIDData1 = ByteUtil.hexStringToByte(key);

                byte[] pbocAIDData1 = {(byte) 0x9F, (byte) 0x06, (byte) 0x08, (byte) 0xA0, (byte) 0x00,
                        (byte) 0x00, (byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x01, (byte) 0x06, (byte) 0xDF, (byte) 0x01,
                        (byte) 0x01, (byte) 0x00, (byte) 0x9F, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x20, (byte) 0xDF,
                        (byte) 0x11, (byte) 0x05, (byte) 0xD8, (byte) 0x40, (byte) 0x00, (byte) 0xA8, (byte) 0x00, (byte) 0xDF,
                        (byte) 0x12, (byte) 0x05, (byte) 0xD8, (byte) 0x40, (byte) 0x04, (byte) 0xF8, (byte) 0x00, (byte) 0xDF,
                        (byte) 0x13, (byte) 0x05, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x9F,
                        (byte) 0x1B, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xDF, (byte) 0x15,
                        (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xDF, (byte) 0x16, (byte) 0x01,
                        (byte) 0x99, (byte) 0xDF, (byte) 0x17, (byte) 0x01, (byte) 0x99, (byte) 0xDF, (byte) 0x14, (byte) 0x03,
                        (byte) 0x9F, (byte) 0x37, (byte) 0x04, (byte) 0xDF, (byte) 0x18, (byte) 0x01, (byte) 0x01, (byte) 0x9F,
                        (byte) 0x7B, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00,
                        (byte) 0xDF, (byte) 0x19, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00,
                        (byte) 0x00, (byte) 0xDF, (byte) 0x20, (byte) 0x06, (byte) 0x00, (byte) 0x09, (byte) 0x99, (byte) 0x99,
                        (byte) 0x99, (byte) 0x99, (byte) 0xDF, (byte) 0x21, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x10, (byte) 0x00, (byte) 0x00};
                AppCenter.getInstance().KeyController().requestPbocSetAID(0x02, pbocAIDData1);
            }
        }, 400);
    }

    public void qpbocStart(final String key) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PbocQPBOCProcessAttribute attribute = new PbocQPBOCProcessAttribute();
                attribute.setAuthAccount("0000000000" + "01"); //金额必须格式化为长度为12的字符串
                attribute.setAuthAccountOther("000000000000");//金额必须格式化为长度为12的字符串
                attribute.setPbocProcess(0x06);
                attribute.setCompulsoryOnlineId(0x01);
                attribute.setTradeType(0x31);
                AppCenter.getInstance().CardController().requestPbocStartQPBOCOption(attribute);
            }
        }, 100);

    }

    public synchronized void TransKey(final String key) {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCenter.getInstance().KeyController().requestUpdateTransMasterKey(key);
            }
        }, 400);
    }

    class KeyEvent {
        public static final int CLEAN = 0x01;
        public static final int ADD = 0x02;
        public static final int DELETE = 0x03;
        public static final int LOAD = 0x04;
        public static final int LOAD_SINGLE = 0x05;
    }

    /**
     * @param key 公钥更新
     */
    public synchronized void publicKey(String key) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int operateCode = 0x02;
                byte[] pbocPKData1 = {(byte) 0x9F, (byte) 0x06, (byte) 0x05, (byte) 0xA0, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x9F, (byte) 0x22, (byte) 0x01, (byte) 0x06, (byte) 0xDF,
                        (byte) 0x05, (byte) 0x08, (byte) 0x32, (byte) 0x30, (byte) 0x31, (byte) 0x36, (byte) 0x31, (byte) 0x32,
                        (byte) 0x33, (byte) 0x31, (byte) 0xDF, (byte) 0x06, (byte) 0x01, (byte) 0x01, (byte) 0xDF, (byte) 0x07,
                        (byte) 0x01, (byte) 0x01, (byte) 0xDF, (byte) 0x02, (byte) 0x81, (byte) 0xF8, (byte) 0xCB, (byte) 0x26,
                        (byte) 0xFC, (byte) 0x83, (byte) 0x0B, (byte) 0x43, (byte) 0x78, (byte) 0x5B, (byte) 0x2B, (byte) 0xCE,
                        (byte) 0x37, (byte) 0xC8, (byte) 0x1E, (byte) 0xD3, (byte) 0x34, (byte) 0x62, (byte) 0x2F, (byte) 0x96,
                        (byte) 0x22, (byte) 0xF4, (byte) 0xC8, (byte) 0x9A, (byte) 0xAE, (byte) 0x64, (byte) 0x10, (byte) 0x46,
                        (byte) 0xB2, (byte) 0x35, (byte) 0x34, (byte) 0x33, (byte) 0x88, (byte) 0x3F, (byte) 0x30, (byte) 0x7F,
                        (byte) 0xB7, (byte) 0xC9, (byte) 0x74, (byte) 0x16, (byte) 0x2D, (byte) 0xA7, (byte) 0x2F, (byte) 0x7A,
                        (byte) 0x4E, (byte) 0xC7, (byte) 0x5D, (byte) 0x9D, (byte) 0x65, (byte) 0x73, (byte) 0x36, (byte) 0x86,
                        (byte) 0x5B, (byte) 0x8D, (byte) 0x30, (byte) 0x23, (byte) 0xD3, (byte) 0xD6, (byte) 0x45, (byte) 0x66,
                        (byte) 0x76, (byte) 0x25, (byte) 0xC9, (byte) 0xA0, (byte) 0x7A, (byte) 0x6B, (byte) 0x7A, (byte) 0x13,
                        (byte) 0x7C, (byte) 0xF0, (byte) 0xC6, (byte) 0x41, (byte) 0x98, (byte) 0xAE, (byte) 0x38, (byte) 0xFC,
                        (byte) 0x23, (byte) 0x80, (byte) 0x06, (byte) 0xFB, (byte) 0x26, (byte) 0x03, (byte) 0xF4, (byte) 0x1F,
                        (byte) 0x4F, (byte) 0x3B, (byte) 0xB9, (byte) 0xDA, (byte) 0x13, (byte) 0x47, (byte) 0x27, (byte) 0x0F,
                        (byte) 0x2F, (byte) 0x5D, (byte) 0x8C, (byte) 0x60, (byte) 0x6E, (byte) 0x42, (byte) 0x09, (byte) 0x58,
                        (byte) 0xC5, (byte) 0xF7, (byte) 0xD5, (byte) 0x0A, (byte) 0x71, (byte) 0xDE, (byte) 0x30, (byte) 0x14,
                        (byte) 0x2F, (byte) 0x70, (byte) 0xDE, (byte) 0x46, (byte) 0x88, (byte) 0x89, (byte) 0xB5, (byte) 0xE3,
                        (byte) 0xA0, (byte) 0x86, (byte) 0x95, (byte) 0xB9, (byte) 0x38, (byte) 0xA5, (byte) 0x0F, (byte) 0xC9,
                        (byte) 0x80, (byte) 0x39, (byte) 0x3A, (byte) 0x9C, (byte) 0xBC, (byte) 0xE4, (byte) 0x4A, (byte) 0xD2,
                        (byte) 0xD6, (byte) 0x4F, (byte) 0x63, (byte) 0x0B, (byte) 0xB3, (byte) 0x3A, (byte) 0xD3, (byte) 0xF5,
                        (byte) 0xF5, (byte) 0xFD, (byte) 0x49, (byte) 0x5D, (byte) 0x31, (byte) 0xF3, (byte) 0x78, (byte) 0x18,
                        (byte) 0xC1, (byte) 0xD9, (byte) 0x40, (byte) 0x71, (byte) 0x34, (byte) 0x2E, (byte) 0x07, (byte) 0xF1,
                        (byte) 0xBE, (byte) 0xC2, (byte) 0x19, (byte) 0x4F, (byte) 0x60, (byte) 0x35, (byte) 0xBA, (byte) 0x5D,
                        (byte) 0xED, (byte) 0x39, (byte) 0x36, (byte) 0x50, (byte) 0x0E, (byte) 0xB8, (byte) 0x2D, (byte) 0xFD,
                        (byte) 0xA6, (byte) 0xE8, (byte) 0xAF, (byte) 0xB6, (byte) 0x55, (byte) 0xB1, (byte) 0xEF, (byte) 0x3D,
                        (byte) 0x0D, (byte) 0x7E, (byte) 0xBF, (byte) 0x86, (byte) 0xB6, (byte) 0x6D, (byte) 0xD9, (byte) 0xF2,
                        (byte) 0x9F, (byte) 0x6B, (byte) 0x1D, (byte) 0x32, (byte) 0x4F, (byte) 0xE8, (byte) 0xB2, (byte) 0x6C,
                        (byte) 0xE3, (byte) 0x8A, (byte) 0xB2, (byte) 0x01, (byte) 0x3D, (byte) 0xD1, (byte) 0x3F, (byte) 0x61,
                        (byte) 0x1E, (byte) 0x7A, (byte) 0x59, (byte) 0x4D, (byte) 0x67, (byte) 0x5C, (byte) 0x44, (byte) 0x32,
                        (byte) 0x35, (byte) 0x0E, (byte) 0xA2, (byte) 0x44, (byte) 0xCC, (byte) 0x34, (byte) 0xF3, (byte) 0x87,
                        (byte) 0x3C, (byte) 0xBA, (byte) 0x06, (byte) 0x59, (byte) 0x29, (byte) 0x87, (byte) 0xA1, (byte) 0xD7,
                        (byte) 0xE8, (byte) 0x52, (byte) 0xAD, (byte) 0xC2, (byte) 0x2E, (byte) 0xF5, (byte) 0xA2, (byte) 0xEE,
                        (byte) 0x28, (byte) 0x13, (byte) 0x20, (byte) 0x31, (byte) 0xE4, (byte) 0x8F, (byte) 0x74, (byte) 0x03,
                        (byte) 0x7E, (byte) 0x3B, (byte) 0x34, (byte) 0xAB, (byte) 0x74, (byte) 0x7F, (byte) 0xDF, (byte) 0x04,
                        (byte) 0x01, (byte) 0x03, (byte) 0xDF, (byte) 0x03, (byte) 0x14, (byte) 0xF9, (byte) 0x10, (byte) 0xA1,
                        (byte) 0x50, (byte) 0x4D, (byte) 0x5F, (byte) 0xFB, (byte) 0x79, (byte) 0x3D, (byte) 0x94, (byte) 0xF3,
                        (byte) 0xB5, (byte) 0x00, (byte) 0x76, (byte) 0x5E, (byte) 0x1A, (byte) 0xBC, (byte) 0xAD, (byte) 0x72,
                        (byte) 0xD9};
                AppCenter.getInstance().KeyController().requestPbocSetPublicKey(operateCode, pbocPKData1);
            }
        }, 400);
    }

    /**
     * @param key
     * @deprecated 已弃用
     */
    public synchronized void InputAmount(String key) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCenter.getInstance().PeripheralController().requestAppointInputAmount(30, 0x02, 1, 1, 1);
            }
        }, 400);
    }

    /**
     * Mpos输入密码
     *
     * @param cardNum 卡号
     * @param money   金额
     */
    public synchronized void InputPIN(final String cardNum, String money) {
        Log.e(TAG, "InputPIN: " + money);
        MultiLineAttributes attr = new MultiLineAttributes(60, new MultiLineAttributes.MultiLineAttribute[]{
                new MultiLineAttributes.MultiLineAttribute(1, 1, 2, "金额:" + money),
                new MultiLineAttributes.MultiLineAttribute(2, 1, 2, "卡号:" + cardNum),
                new MultiLineAttributes.MultiLineAttribute(3, 1, 2, "请输密码")
        });
        AppCenter.getInstance().DeviceController().onShowMultiLine(attr);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCenter.getInstance().PeripheralController().requestAppointInputPIN(60, 3, 0, 1, 2, cardNum);
            }
        }, 400);
    }

}
