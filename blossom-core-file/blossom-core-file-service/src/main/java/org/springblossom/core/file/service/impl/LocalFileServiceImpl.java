package org.springblossom.core.file.service.impl;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springblossom.core.file.config.UploadProperties;
import org.springblossom.core.file.entity.FileInfoEntity;
import org.springblossom.core.file.model.BlossomFile;
import org.springblossom.core.file.rule.UploadRule;
import org.springblossom.core.file.service.FileInfoService;
import org.springblossom.core.file.service.FileService;
import org.springblossom.core.tool.utils.Func;
import org.springblossom.core.tool.utils.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.security.MessageDigest;

/**
 * 本地文件服务
 *
 * @author guolf
 */
@Slf4j
@Service
@Primary
@ConditionalOnProperty(prefix = "blossom.upload.local", name = "enable", havingValue = "true")
public class LocalFileServiceImpl implements FileService {

	@Autowired
	private FileInfoService fileInfoService;
	@Autowired
	private UploadProperties uploadProperties;
	@Autowired
	private UploadRule uploadRule;

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
	public FileInfoEntity saveFile(InputStream fileStream, String fileName, String type) throws IOException {
		//配置中的文件上传根路径
		String fileBasePath = uploadProperties.getBasePath();
		//文件存储的相对路径，以日期分隔，每天创建一个新的目录
		String filePath = uploadRule.fileName(fileName);

		//文件存储绝对路径
		String absPath = fileBasePath.concat(StringPool.SLASH).concat(getFilePath(filePath));
		File path = new File(absPath);
		if (!path.exists()) {
			//创建目录
			path.mkdirs();
		}
		String fileAbsName = absPath.concat(StringPool.SLASH).concat(fileName);
		int fileSize;
		MessageDigest digest = DigestUtils.getMd5Digest();
		try (InputStream proxyStream = new InputStream() {
			@Override
			public int read(byte[] b, int off, int len) throws IOException {
				int l = fileStream.read(b, off, len);
				digest.update(b, off, len);
				return l;
			}

			@Override
			public void close() throws IOException {
				fileStream.close();
				super.close();
			}

			@Override
			public int available() throws IOException {
				return fileStream.available();
			}

			@Override
			public int read() throws IOException {
				return fileStream.read();
			}
		};
			 FileOutputStream os = new FileOutputStream(fileAbsName)) {
			int remainBytes = fileSize = proxyStream.available();
			byte[] buff = new byte[remainBytes > 1024 * 10 ? 1024 * 10 : remainBytes];
			int bytes;
			log.debug("开始写出文件:{}到:{}, size: {} bytes", fileName, fileAbsName, fileSize);
			while (remainBytes > 0) {
				bytes = proxyStream.read(buff, 0, remainBytes > buff.length ? buff.length : remainBytes);
				os.write(buff, 0, bytes);
				remainBytes -= bytes;
				log.debug("写出文件:{}:{},剩余数据量: {} bytes", fileName, fileAbsName, remainBytes);
			}
		}

		String md5 = Hex.encodeHexString(digest.digest());

		File newFile = new File(fileAbsName);
		//获取文件的md5值
		//判断文件是否已经存在
		FileInfoEntity fileInfo = fileInfoService.selectByMd5(md5);
		if (fileInfo != null) {
			log.info("文件:{}已上传过", fileAbsName);
			if (new File(uploadProperties.getBasePath() + StringPool.SLASH + fileInfo.getLocation()).exists()) {
				//文件已存在则删除临时文件不做处理
				newFile.delete();
			} else {
				newFile.renameTo(new File(absPath.concat(StringPool.SLASH).concat(md5)));
			}
			return fileInfo;
		} else {
			log.info("上传文件{}完成:{}->{}", fileName, fileAbsName, absPath.concat(StringPool.SLASH).concat(md5));
			newFile.renameTo(new File(absPath.concat(StringPool.SLASH).concat(md5)));
		}
		FileInfoEntity infoEntity = new FileInfoEntity();
		infoEntity.setLocation(absPath);
		infoEntity.setName(fileName);
		infoEntity.setType(type);
		infoEntity.setSize((long) fileSize);
		infoEntity.setMd5(md5);
		fileInfoService.save(infoEntity);
		infoEntity.setUrl(uploadProperties.getEndpoint() + StringPool.SLASH + filePath);
		fileInfoService.updateById(infoEntity);
		return infoEntity;
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
	public BlossomFile saveFile(InputStream fileStream, String fileName) throws IOException {
		BlossomFile blossomFile = new BlossomFile();
		try {
			String filePath = uploadRule.fileName(fileName);
			//文件存储绝对路径
			String absPath = uploadProperties.getBasePath().concat(StringPool.SLASH).concat(getFilePath(filePath));
			File path = new File(absPath);
			if (!path.exists()) {
				//创建目录
				path.mkdirs();
			}

			String fileAbsName = uploadProperties.getBasePath() + StringPool.SLASH + filePath;
			log.debug("fileAbsName = {}", fileAbsName);
			try (FileOutputStream out = new FileOutputStream(fileAbsName)) {
				StreamUtils.copy(fileStream, out);
			}

			//响应上传成功的资源信息
			blossomFile.setLink(uploadProperties.getEndpoint().concat(StringPool.SLASH).concat(filePath));
			blossomFile.setName(filePath);
			return blossomFile;
		} finally {
			fileStream.close();
		}
	}

	/**
	 * 根据文件名获取文件链接
	 *
	 * @param fileName
	 * @return
	 */
	@Override
	public String fileLink(String fileName) {
		return uploadProperties.getEndpoint().concat(StringPool.SLASH).concat(fileName);
	}

	/**
	 * 视频时长
	 *
	 * @param fileName
	 * @return
	 */
	@Override
	public long duration(String fileName) {
		Encoder encoder = new Encoder();
		try {
			MultimediaInfo multimediaInfo = encoder.getInfo(new File(uploadProperties.getBasePath() + StringPool.SLASH + fileName));
			return multimediaInfo.getDuration();
		} catch (Exception ex) {
			log.error("获取视频时长出错", ex);
		}
		return 0;
	}

	private String getFilePath(String filePath) {
		if (Func.isBlank(filePath)) {
			return null;
		}
		return filePath.substring(0, filePath.lastIndexOf(StringPool.SLASH));
	}

	/**
	 * 从其他云存储中迁移文件
	 *
	 * @param fileStream
	 * @param fileName
	 */
	@Override
	public BlossomFile transfer(InputStream fileStream, String fileName) throws IOException {
		BlossomFile blossomFile = new BlossomFile();
		try {
			String filePath = fileName;
			//文件存储绝对路径
			String absPath = uploadProperties.getBasePath().concat(StringPool.SLASH).concat(getFilePath(filePath));
			File path = new File(absPath);
			if (!path.exists()) {
				//创建目录
				path.mkdirs();
			}

			String fileAbsName = uploadProperties.getBasePath() + StringPool.SLASH + filePath;
			log.debug("fileAbsName = {}", fileAbsName);
			try (FileOutputStream out = new FileOutputStream(fileAbsName)) {
				StreamUtils.copy(fileStream, out);
			}

			//响应上传成功的资源信息
			blossomFile.setLink(uploadProperties.getEndpoint().concat(StringPool.SLASH).concat(filePath));
			blossomFile.setName(filePath);
			return blossomFile;
		} finally {
			fileStream.close();
		}
	}
}
