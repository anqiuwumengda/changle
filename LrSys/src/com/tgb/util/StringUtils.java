/**
 * @author Quinn He
 * @dateTime 2012-2-15 下午2:32:01
 */
package com.tgb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Quinn He
 * @dateTime 2012-2-15 下午2:32:01
 */
public abstract class StringUtils {

	/**
	 * 获得一个UUID
	 * 
	 * @author Quinn He
	 * @dateTime 2012-2-15 下午2:34:53
	 * @return String
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 判断一个字符串是否为null或者为空字符串
	 * 
	 * @author Quinn He
	 * @dateTime 2012-2-15 下午2:36:46
	 * @param s
	 * @return Boolean
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s.trim());
	}

	/**
	 * 把一个字符串用逗号‘,’隔开,返回数组
	 * 
	 * @author Quinn He
	 * @dateTime 2012-2-20 下午5:28:17
	 * @param s
	 * @return java.lang.String[]
	 */
	public static String[] getStringArray(String s) {
		if (!StringUtils.isEmpty(s)) {
			return s.split(",");
		}
		return null;
	}

	/**
	 * 把一个字符串用逗号‘,’隔开,返回List
	 * 
	 * @author Quinn He
	 * @dateTime 2012-2-20 下午5:28:17
	 * @param s
	 * @return java.util.List<String>
	 */
	public static List<String> getStringList(String s) {
		if (!StringUtils.isEmpty(s)) {
			String[] ss = s.split(",");
			List<String> list = new ArrayList<String>();
			for (String p : ss) {
				list.add(p);
			}
			return list;
		}
		return null;
	}

	/**
	 * 如果一个字符串为null或者为一个空字符串,则返回一个null
	 * 
	 * @author Quinn He
	 * @dateTime 2012-2-20 下午5:37:17
	 * @param s
	 * @return java.lang.String
	 */
	public static String getNull(String s) {
		return s = (s == null || s.trim().isEmpty()) ? null : s;
	}

	/**
	 * @author Quinn He
	 * @dateTime 2012-2-23 下午3:50:22
	 * @param s
	 * @return java.util.List<Integer>
	 */
	public static List<Integer> getIntegerList(String s) {
		if (!isEmpty(s)) {
			String[] ss = s.split(",");
			List<Integer> list = new ArrayList<Integer>();
			for (String p : ss) {
				list.add(Integer.valueOf(p));
			}
			return list;
		}
		return null;
	}

	// **************************************************

	/**
	 * 根据用户名的不同长度，来进行替换 ，达到保密效果
	 * 
	 * @param userName
	 *            用户名或电话
	 * @return 替换后的用户名
	 */
	public static String userNameReplaceWithStar(String userName) {
		String userNameAfterReplaced = "";

		if (userName == null) {
			userName = "";
		}

		int nameLength = userName.length();

		if (nameLength <= 1) {
			userNameAfterReplaced = "*";
		} else if (nameLength == 2) {
			userNameAfterReplaced = replaceAction(userName,
					"(?<=\\d{0})\\d(?=\\d{1})");
		} else if (nameLength >= 3 && nameLength <= 6) {
			userNameAfterReplaced = replaceAction(userName,
					"(?<=\\d{1})\\d(?=\\d{1})");
		} else if (nameLength == 7) {
			userNameAfterReplaced = replaceAction(userName,
					"(?<=\\d{1})\\d(?=\\d{2})");
		} else if (nameLength == 8) {
			userNameAfterReplaced = replaceAction(userName,
					"(?<=\\d{2})\\d(?=\\d{2})");
		} else if (nameLength == 9) {
			userNameAfterReplaced = replaceAction(userName,
					"(?<=\\d{2})\\d(?=\\d{3})");
		} else if (nameLength == 10) {
			userNameAfterReplaced = replaceAction(userName,
					"(?<=\\d{3})\\d(?=\\d{3})");
		} else if (nameLength >= 11) {
			userNameAfterReplaced = replaceAction(userName,
					"(?<=\\d{3})\\d(?=\\d{4})");
		}

		return userNameAfterReplaced;

	}

	/**
	 * 实际替换动作
	 * 
	 * @param username
	 *            username
	 * @param regular
	 *            正则
	 * @return
	 */
	private static String replaceAction(String username, String regular) {
		return username.replaceAll(regular, "*");
	}

	/**
	 * 身份证号替换，保留前四位和后四位
	 * 
	 * 如果身份证号为空 或者 null ,返回null ；否则，返回替换后的字符串；
	 * 
	 * @param idCard
	 *            身份证号
	 * @return
	 */
	public static String idCardReplaceWithStar(String idCard) {

		if (idCard.isEmpty() || idCard == null) {
			return null;
		} else {
			return replaceAction(idCard, "(?<=\\d{4})\\d(?=\\d{4})");
		}
	}

	/**
	 * 银行卡替换，保留后四位
	 * 
	 * 如果银行卡号为空 或者 null ,返回null ；否则，返回替换后的字符串；
	 * 
	 * @param bankCard
	 *            银行卡号
	 * @return
	 */
	public static String bankCardReplaceWithStar(String bankCard) {

		if (bankCard.isEmpty() || bankCard == null) {
			return null;
		} else {
			return replaceAction(bankCard, "(?<=\\d{0})\\d(?=\\d{4})");
		}
	}

	// 判断一个字符串是否都为数字
	public static boolean isDigit2(String strNum) {
		return strNum.matches("[0-9]{1,}");
	}

	// 判断一个字符串是否都为数字
	public static boolean isDigit(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

	// 截取数字
	public static String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	// 截取非数字
	public static String splitNotNumber(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	// 判断一个字符串是否含有数字

	public static boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;

	}

	public static void main(String[] args) {
		String s = "13122223333";
		System.out.println(StringUtils.userNameReplaceWithStar(s));
	}
}
