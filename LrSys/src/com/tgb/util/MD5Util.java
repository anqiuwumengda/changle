package com.tgb.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class MD5Util {
	public static String sign(HttpServletRequest request) {
		try {
			Map<String, String[]> map = request.getParameterMap();
			if (null == map || map.size() <= 0) {
				return null;
			}
			Object[] keys = map.keySet().toArray();
			Arrays.sort(keys);
			StringBuffer sbuf = new StringBuffer();
			for (int i = 0; i < keys.length; i++) {
				String key = keys[i].toString().trim();
				if (key.equals("sign")) {
					continue;
				}
				sbuf.append(key);
				sbuf.append("=");
				sbuf.append(map.get(key).length > 0 ? map.get(key)[0] : "");
				sbuf.append("&");
			}
			if (sbuf.length() <= 0)
				return null;
			return MD5Util.getMd5Hex(sbuf
					.delete(sbuf.lastIndexOf("&"), sbuf.length()).toString().getBytes("UTF-8")
					);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	 public static String getMd5Hex(byte[] s) {
	        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	 
	        try {
	            byte[] strTemp = s;
	            MessageDigest mdTemp = MessageDigest.getInstance("md5");
	            mdTemp.update(strTemp);
	            byte[] md = mdTemp.digest();
	            int j = md.length;
	            char str[] = new char[j * 2];
	            int k = 0;
	            for (int i = 0; i < j; i++) {
	                byte byte0 = md[i];
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                str[k++] = hexDigits[byte0 & 0xf];
	            }
	            return new String(str);
	        } catch (Exception e) {
	            return null;
	        }
	    }
		public static void main(String[] arts) throws UnsupportedEncodingException{
			System.out.println(MD5Util.getMd5Hex("61555".getBytes("UTF-8")));
		}
}
