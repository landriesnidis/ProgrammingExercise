package pers.landriesnidis.calculate;

import java.util.regex.Pattern;

public class RegexUtils {
	// 正则表达式（浮点数）
	private final static Pattern PATTERN = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
	
	/**
	 * 判断字符串是否是数值
	 * @param str 
	 * @return
	 */
	public static boolean isValue(String str){
		return PATTERN.matcher(str).matches();
	}
}
