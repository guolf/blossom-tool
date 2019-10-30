package org.springblossom.core.tool;

import com.google.common.collect.Lists;
import org.springblossom.core.tool.utils.CollectionUtil;

import java.util.List;

public class CollectionUtilTest {

	public static void main(String[] args) {
		List list = Lists.newArrayList("1","2","3");
		System.out.println("list = " + CollectionUtil.convertToString(list,","));
	}

	public static void test(String... args) {

	}
}
