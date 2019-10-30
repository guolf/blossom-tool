package org.springblossom.core.oss;

import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springblossom.core.oss.config.OssProperties;
import org.springblossom.core.oss.model.BlossomFile;
import org.springblossom.core.oss.model.OssFile;
import org.springblossom.core.oss.rule.OssRule;
import org.springblossom.core.tool.utils.CollectionUtil;
import org.springblossom.core.tool.utils.Func;
import org.springblossom.core.tool.utils.StringPool;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 七牛存储 调用模板方法
 *
 * @author guolf
 */
@AllArgsConstructor
public class QiniuTemplate {

	private Auth auth;
	private UploadManager uploadManager;
	private BucketManager bucketManager;
	private OssProperties ossProperties;
	private OssRule ossRule;


	@SneakyThrows
	public void makeBucket(String bucketName) {
		if (!CollectionUtil.contains(bucketManager.buckets(), getBucketName(bucketName))) {
			bucketManager.createBucket(getBucketName(bucketName), Zone.zone0().getRegion());
		}
	}


	@SneakyThrows
	public void removeBucket(String bucketName) {
		bucketManager.deleteBucket(getBucketName(bucketName));
	}


	@SneakyThrows
	public boolean bucketExists(String bucketName) {
		return CollectionUtil.contains(bucketManager.buckets(), getBucketName(bucketName));
	}


	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName) {
		bucketManager.copy(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
	}


	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
		bucketManager.copy(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
	}

	/**
	 * 获取文件信息
	 *
	 * @param fileName 文件名称
	 * @return
	 */
	@SneakyThrows
	public OssFile statFile(String fileName) {
		return statFile(ossProperties.getBucketName(), fileName);
	}

	/**
	 * 获取文件信息
	 *
	 * @param bucketName 存储空间名称
	 * @param fileName   文件名称
	 * @return
	 */
	@SneakyThrows
	public OssFile statFile(String bucketName, String fileName) {
		FileInfo stat = bucketManager.stat(getBucketName(bucketName), fileName);
		OssFile ossFile = new OssFile();
		ossFile.setName(stat.key);
		ossFile.setName(Func.isEmpty(stat.key) ? fileName : stat.key);
		ossFile.setLink(fileLink(ossFile.getName()));
		ossFile.setHash(stat.hash);
		ossFile.setLength(stat.fsize);
		ossFile.setPutTime(new Date(stat.putTime / 10000));
		ossFile.setContentType(stat.mimeType);
		return ossFile;
	}


	@SneakyThrows
	public String filePath(String fileName) {
		return getBucketName().concat(File.separator).concat(fileName);
	}

	@SneakyThrows
	public String filePath(String bucketName, String fileName) {
		return getBucketName(bucketName).concat(File.separator).concat(fileName);
	}

	/**
	 * 文件外链 - 私有空间
	 *
	 * @param fileName
	 * @return
	 */
	@SneakyThrows
	public String filePrivateLink(String fileName) {
		return auth.privateDownloadUrl(fileLink(fileName));
	}

	/**
	 * 文件外链 - 公有空间
	 *
	 * @param fileName 文件名称
	 * @return
	 */
	@SneakyThrows
	public String fileLink(String fileName) {
		return ossProperties.getEndpoint().concat(StringPool.SLASH).concat(fileName);
	}


	@SneakyThrows
	public String fileLink(String bucketName, String fileName) {
		return ossProperties.getEndpoint().concat(StringPool.SLASH).concat(fileName);
	}

	/**
	 * 上传文件
	 * @param file
	 * @return
	 */
	@SneakyThrows
	public BlossomFile putFile(MultipartFile file) {
		return putFile(ossProperties.getBucketName(), file.getOriginalFilename(), file);
	}

	/**
	 * 上传文件
	 * @param fileName
	 * @param file
	 * @return
	 */
	@SneakyThrows
	public BlossomFile putFile(String fileName, MultipartFile file) {
		return putFile(ossProperties.getBucketName(), fileName, file);
	}

	/**
	 * 上传文件
	 * @param bucketName
	 * @param fileName
	 * @param file
	 * @return
	 */
	@SneakyThrows
	public BlossomFile putFile(String bucketName, String fileName, MultipartFile file) {
		return putFile(bucketName, fileName, file);
	}


	@SneakyThrows
	public BlossomFile putFile(String fileName, InputStream stream) {
		return putFile(ossProperties.getBucketName(), fileName, stream);
	}


	@SneakyThrows
	public BlossomFile putFile(String bucketName, String fileName, InputStream stream) {
		return put(bucketName, stream, fileName, false);
	}

	@SneakyThrows
	public BlossomFile put(String bucketName, InputStream stream, String key, boolean cover) {
		makeBucket(bucketName);
		key = getFileName(key);
		// 覆盖上传
		if (cover) {
			uploadManager.put(stream, key, getUploadToken(bucketName, key), null, null);
		} else {
			Response response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
			int retry = 0;
			int retryCount = 5;
			while (response.needRetry() && retry < retryCount) {
				response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
				retry++;
			}
		}
		BlossomFile file = new BlossomFile();
		file.setName(key);
		file.setLink(fileLink(bucketName, key));
		return file;
	}


	@SneakyThrows
	public void removeFile(String fileName) {
		bucketManager.delete(getBucketName(), fileName);
	}


	@SneakyThrows
	public void removeFile(String bucketName, String fileName) {
		bucketManager.delete(getBucketName(bucketName), fileName);
	}


	@SneakyThrows
	public void removeFiles(List<String> fileNames) {
		fileNames.forEach(this::removeFile);
	}


	@SneakyThrows
	public void removeFiles(String bucketName, List<String> fileNames) {
		fileNames.forEach(fileName -> removeFile(getBucketName(bucketName), fileName));
	}

	/**
	 * 根据规则生成存储桶名称规则
	 *
	 * @return String
	 */
	private String getBucketName() {
		return getBucketName(ossProperties.getBucketName());
	}

	/**
	 * 根据规则生成存储桶名称规则
	 *
	 * @param bucketName 存储桶名称
	 * @return String
	 */
	private String getBucketName(String bucketName) {
		return ossRule.bucketName(bucketName);
	}

	/**
	 * 根据规则生成文件名称规则
	 *
	 * @param originalFilename 原始文件名
	 * @return string
	 */
	private String getFileName(String originalFilename) {
		return ossRule.fileName(originalFilename);
	}

	/**
	 * 获取上传凭证，普通上传
	 */
	public String getUploadToken(String bucketName) {
		return auth.uploadToken(getBucketName(bucketName));
	}

	/**
	 * 获取上传凭证，覆盖上传
	 */
	private String getUploadToken(String bucketName, String key) {
		return auth.uploadToken(getBucketName(bucketName), key);
	}
}
