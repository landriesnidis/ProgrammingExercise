package pers.landriesnidis.calculate;

import java.util.regex.Pattern;

public class RegexUtils {
	// ������ʽ����������
	private final static Pattern PATTERN = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
	
	/**
	 * �ж��ַ����Ƿ�����ֵ
	 * @param str 
	 * @return
	 */
	public static boolean isValue(String str){
		return PATTERN.matcher(str).matches();
	}
}
