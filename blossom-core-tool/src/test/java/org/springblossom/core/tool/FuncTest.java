package org.springblossom.core.tool;

import org.springblossom.core.tool.utils.Func;

public class FuncTest {

	public static void main(String[] args) {
		Boolean a = Boolean.TRUE;
		System.out.println("args = " + Func.toBoolean(a,false));
		System.out.println("args = " + Func.toBoolean(a));
	}

}
