package org.springblossom.core.file.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springblossom.core.file.config.UploadProperties;
import org.springblossom.core.file.entity.FileInfoEntity;
import org.springblossom.core.file.service.FileInfoService;
import org.springblossom.core.file.service.FileService;
import org.springblossom.core.log.exception.ServiceException;
import org.springblossom.core.tool.utils.DateUtil;
import org.springblossom.core.tool.utils.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.security.MessageDigest;
import java.util.Date;

/**
 * 本地文件服务
 *
 * @author guolf
 */
@Slf4j
@Service
public class LocalFileServiceImpl implements FileService {

	@Autowired
	private FileInfoService fileInfoService;
	@Autowired
	private UploadProperties uploadProperties;

	/**
	 * 根据文件id或者md5获取文件流,如果文件不存在,将抛出异常
	 *
	 * @param fileIdOrMd5 文件id或者md5值
	 * @return 文件流
	 */
	@Override
	public InputStream readFile(String fileIdOrMd5) {
		FileInfoEntity fileInfo = fileInfoService.selectByIdOrMd5(fileIdOrMd5);
		if (fileInfo == null) {
			throw new ServiceException("file not found or disabled");
		}
		//配置中的文件上传根路径
		String filePath = uploadProperties.getFilePath() + StringPool.SLASH + fileInfo.getLocation();
		File file = new File(filePath);
		if (!file.exists()) {
			throw new ServiceException("file not found");
		}
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException ignore) {
			throw new ServiceException("file not found");
		}
	}

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
		String fileBasePath = uploadProperties.getFilePath();
		//文件存储的相对路径，以日期分隔，每天创建一个新的目录
		String filePath = DateUtil.format(new Date(), "yyyyMMdd");
		//文件存储绝对路径
		String absPath = fileBasePath.concat(StringPool.SLASH).concat(filePath);
		File path = new File(absPath);
		if (!path.exists()) {
			//创建目录
			path.mkdirs();
		}
		//临时文件名 ,纳秒的md5值
		String newName = String.valueOf(System.nanoTime());
		String fileAbsName = absPath.concat(StringPool.SLASH).concat(newName);
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
			if (new File(uploadProperties.getFilePath() + StringPool.SLASH + fileInfo.getLocation()).exists()) {
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
		infoEntity.setLocation(filePath.concat(StringPool.SLASH).concat(md5));
		infoEntity.setName(fileName);
		infoEntity.setType(type);
		infoEntity.setSize((long) fileSize);
		infoEntity.setMd5(md5);
		fileInfoService.save(infoEntity);

		infoEntity.setUrl(uploadProperties.getDownloadPrefix() + uploadProperties.getDownloadPath() + infoEntity.getId());
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
	public String saveStaticFile(InputStream fileStream, String fileName) throws IOException {
		try {
			//文件后缀
			String suffix = fileName.contains(".") ?
				fileName.substring(fileName.lastIndexOf(".")) : "";

			//以日期划分目录
			String filePath = DateUtil.format(new Date(), "yyyyMMdd");

			//创建目录
			new File(uploadProperties.getStaticFilePath() + StringPool.SLASH + filePath).mkdirs();

			// 存储的文件名
			String realFileName = System.nanoTime() + suffix;

			String fileAbsName = uploadProperties.getStaticFilePath() + StringPool.SLASH + filePath + StringPool.SLASH + realFileName;
			log.debug("fileAbsName = {}",fileAbsName);
			try (FileOutputStream out = new FileOutputStream(fileAbsName)) {
				StreamUtils.copy(fileStream, out);
			}

			//响应上传成功的资源信息
			return uploadProperties.getStaticLocation() + filePath + StringPool.SLASH + realFileName;
		} finally {
			fileStream.close();
		}
	}

	/**
	 * 将已上传的文件写出到指定的输出流
	 *
	 * @param fileId 已上传的文件id
	 * @param out    要写出的流
	 * @param skip   跳过写出 {@link InputStream#skip(long)}
	 * @throws IOException
	 */
	@Override
	public void writeFile(String fileId, OutputStream out, long skip) throws IOException {
		try (InputStream inputStream = readFile(fileId)) {
			if (skip > 0) {
				long len = inputStream.skip(skip);
				log.info("skip write stream {},{}", skip, len);
			}
			StreamUtils.copy(inputStream, out);
		}
	}
}
