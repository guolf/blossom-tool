package org.springblossom.core.mp.base;

import lombok.Data;

import java.util.List;

/**
 * 动态构造查询
 *
 * @author guolf
 */
@Data
public class DynamicQueryEntity {

	private String key;

	private String symbol;

	private List<String> value;

}
