package com.esapos.lib.Utils;

public class ConvertUtil {
	
//	/**
//	   * new 
//	   * @param str
//	   * @return
//	   */
//	  public static byte[] stringToHex(String str){ 
//		  char[] chars = str.toCharArray(); 
//		  byte []t = new byte[chars.length];
//		  for(int i = 0; i < chars.length; i++){ 
//			t[i] = Byte.parseByte(Integer.toHexString((int)chars[i]));
//		  } 
//		  return t; 
//	  } 

	/**
	 * new
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] stringToHex(String str) {
		char[] chars = str.toCharArray();
		byte[] t = new byte[chars.length];
		for (int i = 0; i < chars.length; i++) {
			t[i] = Byte.parseByte(Integer.toHexString((int) chars[i]));
		}
		return t;
	}
	
	public static String hexToStr(byte[] buf) {
		return hexToStr(buf, 0, buf.length);
	}
	
	public static String hexToStr(byte[] buf, int idx, int len) {
		StringBuffer sb = new StringBuffer();
		int n;
		for (int i = 0; i < len; i++) {
			n = buf[i + idx] & 0xff;
			if (n < 0x10) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(n));
		}

		return sb.toString();
	}
	
	/**
	   * new
	   * @param hex
	   * @return
	   */
	  public static String hexToString(byte[] t){ 
		  StringBuilder sb = new StringBuilder(); 
		  for( int i=0; i<t.length; i++ ){ 
			  String tt = t[i]+"";
		      int decimal = Integer.parseInt(tt, 16); 
		      sb.append((char)decimal); 
		  } 
		  return sb.toString(); 
	  }
	
	/**
	 * java二进制,字节数组,字符,十六进制,BCD编码转换 把16进制字符串转换成字节数组
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) ("0123456789ABCDEF".indexOf(achar[pos])<< 4|"0123456789ABCDEF".indexOf(achar[pos+1]));
		}
		return result;
	}
}
