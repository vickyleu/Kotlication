package com.esapos.lib.Utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class CryptUtil1 {

//	public static void main(String[] args) {
//		String key = "31313131313131313131313131313131";
//		String jiami = encryptString("43EA9E64A843AE6DC462BF9E86A16ECB", key);
//		System.out.println("加密：" + jiami);
//
//		String jiemi = decryptString(jiami, key);
//		System.out.println("解密：" + jiemi);
//	}


	public static String getAscii(String str) {
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (c > 255) {
				sb.append("//u");
				j = (c >>> 8);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1)
					sb.append("0");
				sb.append(tmp);
				j = (c & 0xFF);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1)
					sb.append("0");
				sb.append(tmp);
			} else {
				sb.append(c);
			}

		}
		return (new String(sb));
	}

	/**
	 * Convert byte[] to hex string. 把字节数组转化为字符串
	 * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src
	 *            byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}

	/**
	 * Convert hex string to byte[] 把为字符串转化为字节数组
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String encryptString(String mw, String my) {
		byte[] text = hexStringToBytes(mw);
		byte[] password = hexStringToBytes(my);
		byte[] encryp = encryptoECBKey2(text, password);
		String en = bytesToHexString(encryp);
		return en;
	}

	public static String decryptString(String psw, String key) {
		byte[] text = hexStringToBytes(psw);
		byte[] password = hexStringToBytes(key);
		byte[] decrypt = decryptECBKey2(text, password);
		String decr = bytesToHexString(decrypt);
		return decr;
	}
	public static String decryptString(String psw) {
		return decryptString(psw,"31313131313131313131313131313131");
	}

	public static String AsciiStringToString(String content) {
		String result = "";
		int length = content.length() / 2;
		for (int i = 0; i < length; i++) {
			String c = content.substring(i * 2, i * 2 + 2);
			int a = hexStringToAlgorism(c);
			char b = (char) a;
			String d = String.valueOf(b);
			result += d;
		}
		return result;
	}

	public static int hexStringToAlgorism(String hex) {
		hex = hex.toUpperCase();
		int max = hex.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = hex.charAt(i - 1);
			int algorism = 0;
			if (c >= '0' && c <= '9') {
				algorism = c - '0';
			} else {
				algorism = c - 55;
			}
			result += Math.pow(16, max - i) * algorism;
		}
		return result;
	}

	public static byte[] hexStr2ByteArray(String hexString) {
		if (TextUtils.isEmpty(hexString))
			throw new IllegalArgumentException(
					"this hexString must not be empty");

		hexString = hexString.toLowerCase();
		final byte[] byteArray = new byte[hexString.length() / 2];
		int k = 0;
		for (int i = 0; i < byteArray.length; i++) {
			// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
			// 将hex 转换成byte "&" 操作为了防止负数的自动扩展
			// hex转换成byte 其实只占用了4位，然后把高位进行右移四位
			// 然后“|”操作 低四位 就能得到 两个 16进制数转换成一个byte.
			//
			byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
			byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
			byteArray[i] = (byte) (high << 4 | low);
			// log.info( byteArray[i]);
			k += 2;
		}
		return byteArray;
	}

	public final static String DES_TYPE = "DES/ECB/NoPadding";// DES加密类型

	/**
	 * DES加密过程
	 * 
	 * @param src
	 * @param password
	 * @return
	 */
	public static byte[] encryptoECB(byte[] src, byte[] password) {
		try {
			// return DESCryption.encryptionECB(datasource, password);
			if (src.length % 8 > 0) {// 补足8字节整数倍
				byte[] temp = new byte[(src.length / 8 + 1) * 8];
				// 不足8位补F, Start
				// for (int i = 0; i < temp.length; i++) {
				// temp[i] = (byte) 0XFF;
				// }
				// 不足8位补F, End
				System.arraycopy(src, 0, temp, 0, src.length);
				src = temp;
//				log.info("temp===" + temp);
			}
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(DES_TYPE);
			// 用密匙初始化Cipher对象 DES/ECB/NoPadding
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(src);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * DES解密过程
	 * 
	 * @param src
	 * @param password
	 * @return
	 */
	public static byte[] decryptECB(byte[] src, byte[] password) {
		try {
			// return DESCryption.discryptionECB(src, password);
			if (src.length % 8 > 0) {// 补足8字节整数倍
				byte[] temp = new byte[(src.length / 8 + 1) * 8];
				System.arraycopy(src, 0, temp, 0, src.length);
				src = temp;
			}
			// DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			// 创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(password);
			// 创建一个密匙工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(DES_TYPE);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			// 真正开始解密操作
			return cipher.doFinal(src);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 3DES 加密
	 * 
	 * @param decode
	 * @param keystr
	 * @return
	 */
	public static String threeDesEncrypt(String decode, String keystr) {
		// String str = "1111111122222222";
		// String sourcestr = "45678901";
		byte[] keyHigh = keystr.substring(0, 8).getBytes();
		byte[] keyLow = keystr.substring(8, keystr.length()).getBytes();
		byte[] source = decode.getBytes();
		// 加密1
		byte[] encodeResult01 = encryptoECB(source, keyHigh);
		// 解密1
		byte[] decodeResult = decryptECB(encodeResult01, keyLow);
		// 加密2
		byte[] encodeResultFinal = encryptoECB(decodeResult, keyHigh);

		return new String(encodeResultFinal);
	}

	/**
	 * 3DES 解密
	 * 
	 * @param encode
	 * @param keystr
	 * @return
	 */
	public static String threeDesDecrypt(String encode, String keystr) {

		byte[] keyHigh = ConvertUtil.stringToHex(keystr.substring(0, 8));
		byte[] keyLow = ConvertUtil.stringToHex(keystr.substring(8,
				keystr.length()));
		byte[] source = ConvertUtil.hexStringToByte(encode);
		// 解密1
		byte[] decodeResult01 = decryptECB(source, keyHigh);
		// 加密1
		byte[] encodeResult = encryptoECB(decodeResult01, keyLow);
		// 解密2
		byte[] encodeResultFinal = decryptECB(encodeResult, keyHigh);
		return ConvertUtil.hexToString(encodeResultFinal);
	}

	/**
	 * ansi 9.9 mac算法 1. 以8位拆分字符串, 不足8位补0 2.
	 * 
	 * @param str
	 * @param key
	 */
	public static String ansi99Mac(String str, String key) throws Exception {
		byte[] source = str.getBytes();
		byte[] keyByte = key.getBytes();
		System.out.println("data:" + ConvertUtil.hexToStr(source));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// ByteArrayOutputStream baoKey = new ByteArrayOutputStream();
		try {
			baos.write(source);
			// 不满8的补0x00
			if (source.length % 8 != 0) {
				baos.write(new byte[8 - source.length % 8]);
			}

			// baoKey.write(keyByte);
			// // 不满8的补0x00
			// if (keyByte.length % 8 != 0) {
			// baoKey.write(new byte[8 - keyByte.length % 8]);
			// }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(baos.size());
		// 每8字节做异或。
		byte[] data = baos.toByteArray();
		// keyByte = baoKey.toByteArray();

		byte[] tmp = new byte[8];
		int cont = 0;
		int dt = 0;
		for (int i = 0; i < data.length; i += 8) {
			for (int j = 0; j < 8; j++) {
				if (tmp[j] < 0) {
					cont = tmp[j] + 256;
					tmp[j] = (byte) cont;
					dt = data[i + j];
					cont = (cont ^ dt);
					tmp[j] = (byte) cont;
				} else {
					tmp[j] = (byte) (tmp[j] ^ data[i + j]);
				}

			}
			tmp = encryptoECB(tmp, keyByte);
		}
		byte[] high = ConvertUtil.hexToStr(tmp, 0, 4).toUpperCase().getBytes();
		// byte[] low = ConvertUtil.hexToStr(tmp, 4,
		// 4).toUpperCase().getBytes();

		// return Base64.encodeToString(high, Base64.DEFAULT);
		return ConvertUtil.hexToStr(high);
	}

	/**
	 * 
	 * 双倍长密钥加密 3DES加密过程为：C=Ek3(Dk2(Ek1(P)))
	 * 
	 * K1=K3，但不能K1=K2=K3（如果相等的话就成了DES算法了）
	 */
	public static byte[] encryptoECBKey2(byte[] text, byte[] password) {
		if (password.length != 16)
			return null;
		// byte[] result = new
		// byte[text.length%8==0?text.length:(text.length/8+1)*8];
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		System.arraycopy(password, 0, key1, 0, 8);
		System.arraycopy(password, 8, key2, 0, 8);
		byte[] result = encryptoECB(text, key1);
		result = decryptECB(result, key2);
		result = encryptoECB(result, key1);
		return result;
	}

	/**
	 * 
	 * 双倍长密钥解密 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
	 * 
	 * K1=K3，但不能K1=K2=K3（如果相等的话就成了DES算法了）
	 */
	public static byte[] decryptECBKey2(byte[] text, byte[] password) {
		if (password.length != 16)
			return null;
		// byte[] result = new
		// byte[text.length%8==0?text.length:(text.length/8+1)*8];
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		System.arraycopy(password, 0, key1, 0, 8);
		System.arraycopy(password, 8, key2, 0, 8);
		byte[] result = decryptECB(text, key1);
		result = encryptoECB(result, key2);
		result = decryptECB(result, key1);
		return result;
	}

	/**
	 * 
	 * 双倍长密钥加密 3DES加密过程为：C=Ek3(Dk2(Ek1(P)))
	 * 
	 * K1=K3，但不能K1=K2=K3（如果相等的话就成了DES算法了）
	 */
	public static String encryptoECBKey2(String source, String password) {
		if (password.length() != 16)
			return null;
		byte[] key1 = password.substring(0, 8).getBytes();
		byte[] key2 = password.substring(8, password.length()).getBytes();
		byte[] text = source.getBytes();
		byte[] result = encryptoECB(text, key1);
		result = decryptECB(result, key2);
		result = encryptoECB(result, key1);

		return Base64.encodeToString(result, Base64.DEFAULT);
	}

	/**
	 * 
	 * 双倍长密钥解密 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
	 * 
	 * K1=K3，但不能K1=K2=K3（如果相等的话就成了DES算法了）
	 */
	public static String decryptECBKey2(String source, String password)
			throws Exception {
		if (password.length() != 16)
			return null;
		byte[] key1 = password.substring(0, 8).getBytes();
		byte[] key2 = password.substring(8, password.length()).getBytes();
		byte[] text = Base64.decode(source,Base64.DEFAULT);
		byte[] result = decryptECB(text, key1);
		result = encryptoECB(result, key2);
		result = decryptECB(result, key1);
		return new String(result);
	}

	public static String decryptECBKey3(String source, String password)
			throws Exception {
		if (password.length() != 32)
			return null;
		byte[] key1 = password.substring(0, 16).getBytes();
		byte[] key2 = password.substring(16, password.length()).getBytes();
		byte[] text = Base64.decode(source,Base64.DEFAULT);

		byte[] result = decryptECB(text, key1);
		result = encryptoECB(result, key2);
		result = decryptECB(result, key1);
		return new String(result);
	}

	public static String encryptoECBKey3(String source, String password) {
		if (password.length() != 16)
			return null;
		byte[] key1 = password.substring(0, 16).getBytes();
		byte[] key2 = password.substring(16, password.length()).getBytes();
		byte[] text = source.getBytes();
		byte[] result = encryptoECB(text, key1);
		result = decryptECB(result, key2);
		result = encryptoECB(result, key1);
		return Base64.encodeToString(result,Base64.DEFAULT);
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public static byte[] tCountMACx9_9(byte[] tKey, byte[] tBuffer,
			int iOffset, int iLength) {
		byte[] tResult = null;
		Vector vctBlk = new Vector();
		byte[] tTmp, tBlk, tXor, tDes;
		int iNum, iLen, iPos, iN, i, j;

		if (tKey == null || tBuffer == null)
			return tResult;

		if (iOffset < 0)
			iOffset = 0;
		if (iLength < 0)
			iLength = tBuffer.length - iOffset;

		// 拆分数据（8字节块/Block）
		iLen = 0;
		iPos = iOffset;
		while (iLen < iLength && iPos < tBuffer.length) {
			tBlk = new byte[8];
			for (i = 0; i < tBlk.length; i++)
				tBlk[i] = (byte) 0; // clear(0x00)
			for (i = 0; i < tBlk.length && iLen < iLength
					&& iPos < tBuffer.length; i++) {
				tBlk[i] = tBuffer[iPos++];
				iLen++;
			}
			vctBlk.addElement(tBlk); // store (back)
		}

		// 循环计算（XOR + DES）
		tDes = new byte[8]; // 初始数据
		for (i = 0; i < tDes.length; i++)
			tDes[i] = (byte) 0; // clear(0x00)

		iNum = vctBlk.size();
		for (iN = 0; iN < iNum; iN++) {
			tBlk = (byte[]) vctBlk.elementAt(iN);
			if (tBlk == null)
				continue;

			tXor = new byte[Math.min(tDes.length, tBlk.length)];
			for (i = 0; i < tXor.length; i++)
				tXor[i] = (byte) (tDes[i] ^ tBlk[i]); // 异或(Xor)

			tTmp = encryptoECB(tKey, tXor); // DES加密

			for (i = 0; i < tDes.length; i++)
				tDes[i] = (byte) 0; // clear
			for (i = 0; i < Math.min(tDes.length, tTmp.length); i++)
				tDes[i] = tTmp[i]; // copy / transfer
		}

		vctBlk.removeAllElements(); // clear

		tResult = tDes;

		return tResult;
	}

	/**
	 * 采用x9.9算法计算MAC (Count MAC by ANSI-x9.9).
	 *
	 * @param tKey
	 *            密钥数据
	 * @param tBuffer
	 *            待计算的缓冲区
	 * @param iOffset
	 *            数据的偏移量(0,1,...，即起始位置)
	 * @param iLength
	 *            数据的长度(<0 - 默认值，即整个长度)
	 * @return MAC值(null -- 无效)
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public static byte[] tCountMAC(byte[] tKey, byte[] tBuffer, int iOffset,
			int iLength) {
		byte[] tResult = null;
		Vector vctBlk = new Vector();
		byte[] tTmp, tBlk, tXor, tDes;
		int iNum, iLen, iPos, iN, i, j;

		if (tKey == null || tBuffer == null)
			return tResult;

		if (iOffset < 0)
			iOffset = 0;
		if (iLength < 0)
			iLength = tBuffer.length - iOffset;

		// 拆分数据（8字节块/Block）
		iLen = 0;
		iPos = iOffset;
		while (iLen < iLength && iPos < tBuffer.length) {
			tBlk = new byte[8];
			for (i = 0; i < tBlk.length; i++)
				tBlk[i] = (byte) 0; // clear(0x00)
			for (i = 0; i < tBlk.length && iLen < iLength
					&& iPos < tBuffer.length; i++) {
				tBlk[i] = tBuffer[iPos++];
				iLen++;
			}
			vctBlk.addElement(tBlk); // store (back)
		}

		// 循环计算（XOR + DES）
		tDes = new byte[8]; // 初始数据
		for (i = 0; i < tDes.length; i++)
			tDes[i] = (byte) 0; // clear(0x00)

		iNum = vctBlk.size();
		for (iN = 0; iN < iNum; iN++) {
			tBlk = (byte[]) vctBlk.elementAt(iN);
			if (tBlk == null)
				continue;

			tXor = new byte[Math.min(tDes.length, tBlk.length)];
			for (i = 0; i < tXor.length; i++)
				tXor[i] = (byte) (tDes[i] ^ tBlk[i]); // 异或(Xor)

			tTmp = encryptoECB(tXor, tKey); // DES加密

			for (i = 0; i < tDes.length; i++)
				tDes[i] = (byte) 0; // clear
			for (i = 0; i < Math.min(tDes.length, tTmp.length); i++)
				tDes[i] = tTmp[i]; // copy / transfer
		}

		vctBlk.removeAllElements(); // clear

		tResult = tDes;

		return tResult;
	}

	@SuppressWarnings("static-access")
	public static String StringTo16String(String st) {
		String str = "";
		try {
			byte[] b = st.getBytes();

			for (int i = 0; i < b.length; i++) {
				Integer I = new Integer(b[i]);
				String strTmp = I.toHexString(b[i]);
				if (strTmp.length() > 2)
					strTmp = strTmp.substring(strTmp.length() - 2);
				str = str + strTmp;
			}
			System.out.println(str.toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;

	}
}
