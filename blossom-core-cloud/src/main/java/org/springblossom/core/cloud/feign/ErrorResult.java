package org.springblossom.core.cloud.feign;

import lombok.Data;

/**
 * 异常错误封装
 * @see org.springblossom.core.tool.api.R
 *
 * @author guolf
 */
@Data
public class ErrorResult {

	private int code;

	private boolean success;

	private String msg;

}
