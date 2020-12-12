package org.springblossom.core.tool.exception;

import lombok.Getter;
import org.springblossom.core.tool.api.IResultCode;
import org.springblossom.core.tool.api.ResultCode;

/**
 * blossom 异常基类
 *
 * @author guolf
 */
public class BlossomBaseException extends RuntimeException {

	@Getter
	private final IResultCode resultCode;

	public BlossomBaseException(String message) {
		super(message);
		this.resultCode = ResultCode.INTERNAL_SERVER_ERROR;
	}

	public BlossomBaseException(IResultCode resultCode) {
		super(resultCode.getMessage());
		this.resultCode = resultCode;
	}

	public BlossomBaseException(IResultCode resultCode, Throwable cause) {
		super(cause);
		this.resultCode = resultCode;
	}

	/**
	 * 提高性能
	 *
	 * @return Throwable
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

	public Throwable doFillInStackTrace() {
		return super.fillInStackTrace();
	}

}
