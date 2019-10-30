package org.springblossom.core.file.service;

import org.springblossom.core.file.entity.FileInfoEntity;
import org.springblossom.core.mp.base.BaseService;

/**
 * 文件信息
 *
 * @author guolf
 */
public interface FileInfoService extends BaseService<FileInfoEntity> {

	/**
	 * 根据MD5查询
	 * @param md5
	 * @return
	 */
	FileInfoEntity selectByMd5(String md5);

	/**
	 * 根据MD5或ID查询
	 * @param idOrMd5
	 * @return
	 */
	FileInfoEntity selectByIdOrMd5(String idOrMd5);


}
