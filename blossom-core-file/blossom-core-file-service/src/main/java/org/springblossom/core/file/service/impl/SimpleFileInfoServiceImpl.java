package org.springblossom.core.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblossom.core.file.entity.FileInfoEntity;
import org.springblossom.core.file.mapper.FileInfoMapper;
import org.springblossom.core.file.service.FileInfoService;
import org.springblossom.core.secure.BlossomUser;
import org.springblossom.core.secure.utils.SecureUtil;
import org.springblossom.core.tool.constant.BlossomConstant;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件信息
 *
 * @author guolf
 */
@Service
public class SimpleFileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfoEntity> implements FileInfoService {

	@Override
	public boolean save(FileInfoEntity entity) {
		BlossomUser user = SecureUtil.getUser();
		LocalDateTime now = LocalDateTime.now();
		entity.setCreateUser(user.getUserId());
		entity.setCreateTime(now);
		entity.setIsDeleted(BlossomConstant.DB_NOT_DELETED);
		return super.save(entity);
	}

	/**
	 * 根据MD5查询
	 *
	 * @param md5
	 * @return
	 */
	@Override
	public FileInfoEntity selectByMd5(String md5) {
		return baseMapper.selectByMd5(md5);
	}

	/**
	 * 根据MD5或ID查询
	 *
	 * @param idOrMd5
	 * @return
	 */
	@Override
	public FileInfoEntity selectByIdOrMd5(String idOrMd5) {
		return baseMapper.selectByIdOrMd5(idOrMd5);
	}

	@Override
	public boolean deleteLogic(List<Integer> ids) {
		return baseMapper.deleteBatchIds(ids) == ids.size();
	}
}
