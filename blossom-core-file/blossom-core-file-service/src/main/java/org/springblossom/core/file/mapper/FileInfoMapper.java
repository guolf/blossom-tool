package org.springblossom.core.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springblossom.core.file.entity.FileInfoEntity;

/**
 * 文件信息Mapper
 *
 * @author guolf
 */
public interface FileInfoMapper extends BaseMapper<FileInfoEntity> {

	/**
	 * 根据MD5查询
	 *
	 * @param md5
	 * @return
	 */
	FileInfoEntity selectByMd5(@Param("md5") String md5);

	/**
	 * 根据MD5或ID查询
	 *
	 * @param idOrMd5
	 * @return
	 */
	FileInfoEntity selectByIdOrMd5(@Param("idOrMd5") String idOrMd5);
}
