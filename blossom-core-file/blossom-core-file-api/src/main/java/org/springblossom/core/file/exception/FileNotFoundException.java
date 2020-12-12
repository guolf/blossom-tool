package org.springblossom.core.file.exception;

import org.springblossom.core.tool.api.IResultCode;
import org.springblossom.core.tool.exception.BlossomBaseException;

/**
 * 文件不存在异常
 *
 * @author guolf
 */
public class FileNotFoundException extends BlossomBaseException {
	public FileNotFoundException(String message) {
		super(message);
	}

	public FileNotFoundException(IResultCode resultCode) {
		super(resultCode);
	}

	public FileNotFoundException(IResultCode resultCode, Throwable cause) {
		super(resultCode, cause);
	}
}
