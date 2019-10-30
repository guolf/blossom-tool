package org.springblossom.core.mp.support;

import lombok.Data;

import java.util.List;

/**
 * 动态查询条件构造
 */
@Data
public class DynamicQuery {

	private String key;

	private String symbol;

	private List<String> value;

}
