package org.springblossom.core.file.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.boot.ctrl.BlossomController;
import org.springblossom.core.file.constant.FileConstant;
import org.springblossom.core.file.entity.FileInfoEntity;
import org.springblossom.core.file.service.FileInfoService;
import org.springblossom.core.mp.support.Condition;
import org.springblossom.core.mp.support.Query;
import org.springblossom.core.tool.api.R;
import org.springblossom.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文件信息
 */
@RestController
@RequestMapping("${blossom.web.mappings.fileinfo:fileinfo}")
@Api(value = "文件信息", tags = "文件管理-文件信息")
@Slf4j
public class FileInfoController extends BlossomController {

	@Autowired
	private FileInfoService fileInfoService;

	@ApiModelProperty("根据文件名ID集合查询文件信息")
	@GetMapping("/getFileInfoByIds")
	public R<List<FileInfoEntity>> getFileInfoByIds(@RequestParam("ids") String ids) {
		if (StringUtil.isBlank(ids)) {
			return R.fail("ids不能为空");
		}
		String[] arrayIds = ids.split(",");
		List<FileInfoEntity> list = Lists.newArrayList();
		for (String id : arrayIds) {
			FileInfoEntity fileInfoEntity = fileInfoService.getById(id);
			if (fileInfoEntity != null) {
				list.add(fileInfoEntity);
			}
		}
		return R.data(list);
	}

	@ApiOperation(value = "根据用户ID查询文件列表", notes = "文件类型，图片：pic、文档：file、视频：video")
	@GetMapping("/getFileInfoByUser")
	public R<IPage<FileInfoEntity>> getFileInfoByIds(String userId, @RequestParam(required = false) String fileType, Query query) {
		String type = null;
		if (FileConstant.TYPE_PIC.equals(fileType)) {
			type = "image";
		} else if (FileConstant.TYPE_FILE.equals(fileType)) {
			type = "application/vnd";
		} else if (FileConstant.TYPE_VIDEO.equals(fileType)) {
			type = "video";
		}
		return R.data(fileInfoService.page(Condition.getPage(query),
			new LambdaQueryWrapper<FileInfoEntity>().eq(FileInfoEntity::getCreateUser, userId)
				.likeRight(StringUtil.isNotBlank(fileType), FileInfoEntity::getType, type)
		));
	}
}
