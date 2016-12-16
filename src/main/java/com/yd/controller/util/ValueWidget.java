package com.yd.controller.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueWidget {
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isMargin(String input) {
		if (null != input && "".endsWith(input)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isHasValue(String input) {
		if (null != input && !"".equals(input)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		
		return false;
	}

	/**
	 * decide whether has whitespace
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isHasWhiteSpace(String input) {
		String regex = " \t\r\n";
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (regex.indexOf(c) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 *   if the value is between -9223372036854775808 and
	 *        9223372036854775807, then return true
	 * @return
	 */
	public static boolean isValidLong(String value) {
		try {
			Long.parseLong(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isValidInt(String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 *  if the value is between 1 and 9223372036854775807, then return
	 *        true
	 * @return
	 */
	public static boolean isValidPositiveLong(String value) {
		try {
			Long i = Long.parseLong(value);
			if (i <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 *   if the value is between 1 and 2147483647, then return true
	 * @return
	 */
	public static boolean isValidPositiveInteger(String value) {
		try {
			int i = Integer.parseInt(value);
			if (i <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 *  if the value is between -9223372036854775808 and -1, then return
	 *        true
	 * @return
	 */
	public static boolean isValidNegativeLong(String value) {
		try {
			Long i = Long.parseLong(value);
			if (i >= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isValidDirectory(String path) {
		File file = new File(path);
		if (file.isDirectory()) {
			return true;
		}
		return false;
	}

	/**
	 * @param min
	 * @param max
	 * @param value
	 * @return if value is between min and max,then return true,else return
	 *         false
	 */
	public static boolean isBetweenPositiveInteger(int min, int max,
			String value) {
		if (isValidPositiveLong(value)) {
			Long temp = Long.parseLong(value);
			if (temp >= min && temp <= max) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param min
	 * @param max
	 * @param value
	 * @return if value is between min and max,then return true,else return
	 *         false
	 */
	public static boolean isBetweenInteger(int min, int max, String value) {
		if (isValidLong(value)) {
			Long temp = Long.parseLong(value);
			if (temp >= min && temp <= max) {
				return true;
			}
		}
		return false;
	}

	/**
	 * To determine whether the input string is a legal ip
	 * 
	 * ip
	 *            the ip to be checked
	 * @return true if the input string is a legal IP
	 * @author copy from StringParse.java
	 */
	public static boolean isValidIP(String IP) {
		if (IP == null || IP.length() < 1) {
			return false;
		}
		if (IP.indexOf(':') > -1) {
			return isValidV6IP(IP);
		} else {
			return isValidV4IP(IP);
		}
	}

	/**
	 * To determine whether the input string is a legal IPV6 ip
	 * 
	 * @param ip
	 *            the ip to be checked
	 * @return true if the input string is a legal IP
	 * @author copy from StringParse.java
	 */
	public static boolean isValidV6IP(String ip) {
		if (ip == null || ip.length() < 3) {
			return false;
		}
		int interfaceIndex = ip.lastIndexOf('%');
		if (interfaceIndex > -1) {
			String num = ip.substring(interfaceIndex + 1);
			try {
				Integer.parseInt(num);
			} catch (NumberFormatException e) {
				return false;
			}

			ip = ip.substring(0, interfaceIndex);
		}
		int singleIdx = ip.indexOf("::");
		int hasDouble = 0;
		ArrayList<String> tokens = new ArrayList<String>();
		if ((ip.startsWith(":") && (!ip.startsWith("::")))
				|| (ip.endsWith(":") && (!ip.endsWith("::")))) {
			return false;
		}
		if (singleIdx != -1) {
			hasDouble = 1;
			if (ip.indexOf("::", singleIdx + 1) != -1) {
				return false;
			}
		}
		StringTokenizer st = new StringTokenizer(ip, ":");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.length() > 4) {
				return false;
			}
			char[] chars = token.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (!(Character.isDigit(chars[i])
						|| (chars[i] >= 'a' && chars[i] <= 'f') || (chars[i] >= 'A' && chars[i] <= 'F'))) {
					return false;
				}
			}
			tokens.add(token);
		}

		if ((tokens.size() + hasDouble) > 8
				|| (tokens.size() < 8 && hasDouble == 0)) {
			return false;
		}

		return true;
	}

	/**
	 * To determine whether the input string is a legal IPV4 ip
	 * 
	 * @param ip
	 *            the ip to be checked
	 * @return true if the input string is a legal IP
	 * @author copy from StringParse.java
	 */
	public static boolean isValidV4IP(String ip) {
		if (ip == null) {
			return false;
		}
		if(ip.equals("localhost"))
		{
			return true;
		}
		if (ip.trim().indexOf("..") > -1 || ip.trim().startsWith(".")
				|| ip.trim().endsWith(".")) {
			return false;
		}

		StringTokenizer stringtokenizer = new StringTokenizer(ip, ".");
		if (stringtokenizer.countTokens() != 4) {
			return false;
		}

		try {
			int tempInt = 0;
			while (stringtokenizer.hasMoreTokens()) {
				tempInt = Integer.parseInt(stringtokenizer.nextToken());
				if (tempInt > 255 || tempInt < 0) {
					return false;
				}
			}
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	public static ArrayList<?> getArr4Collection(Collection<?> coll) {
		ArrayList<Object> arrs = new ArrayList<Object>();
		for (Object obj : coll) {
			arrs.add(obj);
		}
		return arrs;
	}

	/**
	 * ɾ��input�ַ��е�html��ʽ
	 * 
	 * @param input
	 * @param length
	 *            ��ʾ���ַ�ĸ���
	 * @return
	 */
	public static String splitAndFilterString(String input, int length) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// ȥ������htmlԪ��,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		int len = str.length();
		if (len <= length) {
			return str;
		} else {
			str = str.substring(0, length);
			str += "......";
		}
		return str;
	}

	/**
	 * ���ش��ı�
	 * 
	 * @param input
	 * @return
	 */
	public static String splitAndFilterString(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// ȥ������htmlԪ��,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		return str;
	}

	/**
	 * "abcd"-->"Abcd"
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/***
	 * String -->string
	 * 
	 * @param str
	 * @return
	 */
	public static String title(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	/***
	 * 判断 String 是否是 int
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isInteger(String input){
		Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
		return mer.find();
	}
	public static boolean isDouble(String input){
		Matcher mer = Pattern.compile("^[+-]?[0-9.]+$").matcher(input);
		return mer.find();
	}
	/***
	 * Get specified length of the string from the specified position.
	 * 
	 * @param data
	 * @param start
	 * @param len
	 * @return
	 */
	public static String subSpecifiedStr(String data, int start, int len) {
		return data.substring(start, start + len);
	}
	/***
	 * 类似于 linux sed命令的y参数
	 * @param source
	 * @param old
	 * @param target
	 * @return
	 */
	public static String replaceY(String source,String old,String target){
		if(old.length()!=target.length()){
			return null;
		}
		for(int i=0;i<old.length();i++){
			source=source.replace(old.charAt(i), target.charAt(i));
		}
		return source;
	}
	public static boolean isHexString(String hex){
		if(ValueWidget.isNullOrEmpty(hex)){
			return false;
		}
		return hex.matches("[\\dabcdefABCDEF]+");
	}
}
