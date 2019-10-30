package org.springblossom.core.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordTest {

	public static void main(String[] args) {
		System.out.println("1234 = " + checkPwdFormat("1234"));
		System.out.println("12345678 = " + checkPwdFormat("12345678"));
		System.out.println("a2345678 = " + checkPwdFormat("a2345678"));
	}

	public static boolean checkPwdFormat(String psw)
	{
		if(psw == null || psw.isEmpty()){
			return false;
		}
		// 密码验证的正则表达式:由数字和字母组成，并且要同时含有数字和字母，且长度要在8-16位之间。
		String regex = "";
		Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
		Matcher m = p.matcher(psw);
		return m.matches();
	}
}
