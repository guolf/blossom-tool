package org.springblossom.core.file.service.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springblossom.core.file.config.QiniuProperties;
import org.springblossom.core.file.entity.FileInfoEntity;
import org.springblossom.core.file.model.BlossomFile;
import org.springblossom.core.file.model.OssFile;
import org.springblossom.core.file.qiniu.QiniuTemplate;
import org.springblossom.core.file.service.FileInfoService;
import org.springblossom.core.file.service.FileService;
import org.springblossom.core.secure.utils.SecureUtil;
import org.springblossom.core.tool.jackson.JsonUtil;
import org.springblossom.core.tool.utils.Func;
import org.springblossom.core.tool.utils.OkHttpUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 七牛文件管理
 *
 * @author guolf
 */
@Component
@AllArgsConstructor
@ConditionalOnProperty(prefix = "blossom.upload.qiniu", name = "enable", havingValue = "true")
public class QiniuFileServiceImpl implements FileService {

	private QiniuTemplate qiniuTemplate;
	private FileInfoService fileInfoService;
	private QiniuProperties qiniuProperties;

	/**
	 * 保存文件,并返回文件信息,如果存在相同的文件,则不会保存,而是返回已保存的文件
	 *
	 * @param fileStream 文件输入流
	 * @param fileName   文件名称
	 * @param type       文件类型(contentType)
	 * @return 文件信息
	 * @throws IOException
	 * @see FileInfoEntity
	 */
	@Override
	public FileInfoEntity saveFile(InputStream fileStream, String fileName, String type) {
		FileInfoEntity fileInfoEntity = new FileInfoEntity();
		BlossomFile blossomFile = qiniuTemplate.putFile(fileName, fileStream);
		OssFile ossFile = qiniuTemplate.statFile(blossomFile.getName());
		fileInfoEntity.setUrl(blossomFile.getLink());
		fileInfoEntity.setCreateUser(SecureUtil.getUserId());
		fileInfoEntity.setCreateTime(LocalDateTime.now());
		fileInfoEntity.setLocation(blossomFile.getLink());
		fileInfoEntity.setName(blossomFile.getName());
		fileInfoEntity.setSize(ossFile.getLength());
		fileInfoEntity.setMd5(ossFile.getHash());
		fileInfoEntity.setType(type);
		fileInfoService.save(fileInfoEntity);
		return fileInfoEntity;
	}

	/**
	 * 上传静态文件,并返回静态文件访问地址
	 *
	 * @param fileStream 文件输入流
	 * @param fileName   文件名
	 * @return 上传文件的访问地址
	 * @throws IOException
	 */
	@Override
	public BlossomFile saveStaticFile(InputStream fileStream, String fileName) {
		return qiniuTemplate.putFile(fileName, fileStream);
	}

	/**
	 * 根据文件名获取文件链接
	 *
	 * @param fileName
	 * @return
	 */
	@Override
	public String fileLink(String fileName) {
		if (qiniuProperties.getPublicSpace()) {
			return qiniuTemplate.fileLink(fileName);
		}
		return qiniuTemplate.filePrivateLink(fileName);
	}

	/**
	 * 视频时长
	 *
	 * @param fileName
	 * @return
	 */
	@Override
	public long duration(String fileName) {
		String fileUrl = fileLink(fileName + "?avinfo");
		String result = OkHttpUtil.get(fileUrl, null);
		if (Func.isNotBlank(result)) {
			Map map = JsonUtil.toMap(result);
			LinkedHashMap<String, String> mapResult = (LinkedHashMap<String, String>) map.get("format");
			String duration = mapResult.get("duration");
			return Func.toLong(StringUtils.substringBefore(duration, "."));
		}
		return 0;
	}
}
