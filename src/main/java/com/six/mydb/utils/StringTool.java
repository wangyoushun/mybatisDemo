package com.six.mydb.utils;

import java.util.Random;

/**
 * 
 * @ClassName: StringUtil
 * @Description: 字符串工具类
 * @author iwantfly
 * @date 2017年5月23日 下午9:43:34
 *
 */
public class StringTool {

	public static final String EMPTY = "";

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断字符串不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断字符串是空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		int strLen = 0;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串不为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	// trim start
	/**
	 * 去除字符串左右空字符
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	/**
	 * trim后为空转为null
	 * 
	 * @param str
	 * @return
	 */
	public static String trimToNull(String str) {
		String trim = trim(str);
		return isEmpty(trim) ? null : trim;
	}

	/**
	 * trim后为null转为空
	 * 
	 * @param str
	 * @return
	 */
	public static String trimToEmpty(String str) {
		return str == null ? EMPTY : trim(str);
	}

	// trim end

	/**
	 * 判断两个字符串是否相等
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	/**
	 * 忽视大小写判断两个字符串是否相等
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
	}

	/**
	 * 字符串转大写
	 * 
	 * @param str
	 * @return
	 */
	public static String upperCase(String str) {
		return str == null ? null : str.toUpperCase();
	}

	/**
	 * 字符串转小写
	 * 
	 * @param str
	 * @return
	 */
	public static String lowerCase(String str) {
		return str == null ? null : str.toLowerCase();
	}

	/**
	 * 字符串下划线转为峰拖发
	 * 
	 * @param param
	 * @return
	 */
	public static String underlineToCamel(String param) {
		char ch = '_';
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == ch) {
				if (++i < len) {
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 驼峰转下划线
	 * 
	 * @param param
	 * @return
	 */
	public static String camelTounderline(String param) {
		// a-z：97-122
		// A-Z：65-90
		// 0-9：48-57
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);

			if (i > 0 && c >= 65 && c <= 90) { // 大写字符
				if (i < len) {
					sb.append("_");
					sb.append(Character.toLowerCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString().toUpperCase();
	}
	
	/**
	 * 生成一个随机字符串
	 * @return
	 */
	public static String generateRandomName() {
		char[] charabc = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
				'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
				'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5',
				'6', '7', '8', '9'};
		Random random = new Random();
		int length = random.nextInt(10);
		String str="";
		for(int i=length; i>-1; i--){
			str += charabc[random.nextInt(62)];
		}
		return str;
	}
	
	/**
	 * 首字符大写
	 * @param str
	 * @return
	 */
	public static String firstToUp(String str){
		if(str==null || str.trim().length()==0){
			return "";
		}
		
		return str.substring(0,1).toUpperCase()+str.substring(1);
	}

}
