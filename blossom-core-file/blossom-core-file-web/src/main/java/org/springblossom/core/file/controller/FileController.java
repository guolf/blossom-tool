package org.springblossom.core.file.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.boot.ctrl.BlossomController;
import org.springblossom.core.file.entity.FileInfoEntity;
import org.springblossom.core.file.model.BlossomFile;
import org.springblossom.core.file.service.FileService;
import org.springblossom.core.log.exception.ServiceException;
import org.springblossom.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文件操作控制器，提供文件上传下载等操作
 *
 * @author guolf
 */
@RestController
@RequestMapping("${blossom.web.mappings.file:file}")
@Api(value = "文件管理", tags = "文件管理-文件操作")
@Slf4j
public class FileController extends BlossomController {

	@Autowired
	private FileService fileService;

	/**
	 * 上传文件,支持多文件上传.获取到文件流后,调用{@link org.springblossom.core.file.service.FileService#saveFile(InputStream, String, String)}进行文件保存
	 * 上传成功后,将返回资源信息如:[{"id":"fileId","name":"fileName","md5":"md5"}]
	 *
	 * @param files 上传的文件
	 * @return 文件上传结果.
	 */
	@PostMapping(value = "/upload-multi")
	@ApiOperation("上传多个文件")
	public R<List<FileInfoEntity>> upload(@RequestPart("files") MultipartFile[] files) {
		List fileInfoEntityList = Stream.of(files)
			.map(this::upload)
			.map(R::getData)
			.collect(Collectors.toList());
		return R.data(fileInfoEntityList);
	}

	/**
	 * 上传单个文件
	 *
	 * @param file 上传文件
	 * @return 上传结果
	 */
	@PostMapping(value = "/upload")
	@ApiOperation("上传单个文件")
	public R<FileInfoEntity> upload(@RequestPart("file") MultipartFile file) {
		if (file.isEmpty()) {
			return R.fail("文件不能为空");
		}
		String fileName = file.getOriginalFilename();
		FileInfoEntity fileInfo;
		try {
			fileInfo = fileService.saveFile(file.getInputStream(), fileName, file.getContentType());
		} catch (IOException e) {
			throw new ServiceException("save file error");
		}
		return R.data(fileInfo);
	}

	@PostMapping(value = "/upload-static")
	@ApiOperation(value = "上传静态文件", notes = "上传后响应结果的result字段为文件的访问地址")
	public R<BlossomFile> uploadStatic(@RequestPart("file") MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			return R.fail("文件不能为空");
		}
		return R.data(fileService.saveStaticFile(file.getInputStream(), file.getOriginalFilename()));
	}
}
