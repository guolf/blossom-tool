package org.springblossom.core.file.service;

import org.springblossom.core.file.entity.FileInfoEntity;
import org.springblossom.core.file.model.BlossomFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件服务,用户保存,读取文件信息
 *
 * @author guolf
 */
public interface FileService {

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
	FileInfoEntity saveFile(InputStream fileStream, String fileName, String type) throws IOException;

	/**
	 * 上传静态文件,并返回静态文件访问地址
	 *
	 * @param fileStream 文件输入流
	 * @param fileName   文件名
	 * @return 上传文件的访问地址
	 * @throws IOException
	 */
	BlossomFile saveFile(InputStream fileStream, String fileName) throws IOException;

	/**
	 * 根据文件名获取文件链接
	 *
	 * @param fileName
	 * @return
	 */
	String fileLink(String fileName);

	/**
	 * 视频时长
	 *
	 * @param fileName
	 * @return
	 */
	long duration(String fileName);

	/**
	 * 从其他云存储中迁移文件
	 * @param fileStream
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	BlossomFile transfer(InputStream fileStream, String fileName) throws IOException;

	/**
	 * 下载文件，支持断点续传
	 * @param idOrMd5
	 * @param paramOutputStream
	 * @param skip
	 * @throws IOException
	 */
	void writeFile(String idOrMd5, OutputStream paramOutputStream, long skip) throws IOException;
}
