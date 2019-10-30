package org.springblossom.core.mp.support;

import com.google.common.collect.Lists;
import org.springblossom.core.tool.jackson.JsonUtil;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

public class ConditionTest {


	public static void main(String[] args) {
		List symbolList = Lists.newArrayList("=", "≠", "like", ">", "≥", "<", "≤");
		String str = "[{\"key\":\"createTime\",\"symbol\":\"between\",\"value\":[\"2019-06-01\",\"2019-06-29\"]},{\"key\":\"id\",\"symbol\":\"=\",\"value\":\"1000\"}]";
		System.out.println("str = " + str);

//		Map map = JsonUtil.parse(str,Map.class);
//		System.out.println("map = " + map);

		List<DynamicQuery> dynamicQueryList = JsonUtil.parseArray(str, DynamicQuery.class);
		System.out.println("dynamicQueryList = " + dynamicQueryList);
		for (DynamicQuery dynamicQuery : dynamicQueryList) {
			System.out.println("dynamicQuery = " + dynamicQuery);
		}


	}
}
