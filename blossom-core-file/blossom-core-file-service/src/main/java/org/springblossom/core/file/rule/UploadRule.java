package org.springblossom.core.file.rule;

/**
 * 云存储文件命名规则
 *
 * @author guolf
 */
public interface UploadRule {

	/**
	 * 获取存储桶规则
	 *
	 * @param bucketName 存储桶名称
	 * @return String
	 */
	default String bucketName(String bucketName) {
		return bucketName;
	}

	;

	/**
	 * 获取文件名规则
	 *
	 * @param originalFilename 文件名
	 * @return String
	 */
	String fileName(String originalFilename);

}
