package org.springblossom.core.file.rule;

import lombok.AllArgsConstructor;
import org.springblossom.core.tool.utils.DateUtil;
import org.springblossom.core.tool.utils.StringPool;
import org.springblossom.core.tool.utils.StringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Date;

/**
 * 云存储默认文件命名方式
 *
 * @author guolf
 */
@AllArgsConstructor
public class BlossomOssRule implements UploadRule {

	/**
	 * 获取文件名规则
	 *
	 * @param originalFilename 文件名
	 * @return String
	 */
	@Override
	public String fileName(String originalFilename) {
		return "upload" + StringPool.SLASH + DateUtil.format(new Date(), "yyyyMMdd") + StringPool.SLASH + StringUtil.randomUUID() + StringPool.DOT + getFileExtension(originalFilename);
	}

	private String getFileExtension(String fullName) {
		Assert.notNull(fullName, "file fullName is null.");
		String fileName = new File(fullName).getName();
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}
}
