/**
 * @author Quinn He
 * @dateTime 2010-8-1 下午08:16:34
 */
package com.tgb.util;

import java.security.MessageDigest;

/**
 * @author Quinn He
 * @dateTime 2012-6-4 下午10:03:33
 */
public final class MD5 {

	/**
	 * 使用的固定数字编码
	 * 
	 * @author Quinn He
	 * @dateTime 2012-6-4 下午10:03:42
	 */
	private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获得指定字串的MD5编码
	 * 
	 * @author Quinn He
	 * @dateTime 2012-6-4 下午10:03:42
	 * @param target 目标字串
	 * @return MD5编码
	 */
	public final static String getMD5(final String target) {
		try {
			// 使用MD5创建MessageDigest对象
			final MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			final byte[] md = mdTemp.digest(target.getBytes("UTF-8"));
			final int j = md.length;
			final char str[] = new char[j * 2];
			byte b;
			for (int i = 0, k = 0; i < j; i++) {
				b = md[i];
				str[k++] = MD5.hexDigits[(b >> 4) & 0xf];
				str[k++] = MD5.hexDigits[b & 0xf];
			}
			return new String(str);
		} catch (final Exception e) {
			return null;
		}
	}
}
