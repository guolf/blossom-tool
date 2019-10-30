package org.springblossom.core.tool;

import org.springframework.web.util.HtmlUtils;

public class XssTest {

	public static void main(String[] args) {
		String str = "省水开公司召开党风廉政建设工作专题会&暨“两学一做”第二专题学习会";
		System.out.println("HtmlUtils.htmlEscape = " + HtmlUtils.htmlEscape(str));
		System.out.println("HtmlUtils.htmlEscapeDecimal = " + HtmlUtils.htmlEscapeDecimal(str));
	}
}
