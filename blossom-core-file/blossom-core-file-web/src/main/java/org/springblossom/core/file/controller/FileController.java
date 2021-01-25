package org.springblossom.core.file.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblossom.core.boot.ctrl.BlossomController;
import org.springblossom.core.file.entity.FileInfoEntity;
import org.springblossom.core.file.model.BlossomFile;
import org.springblossom.core.file.service.FileInfoService;
import org.springblossom.core.file.service.FileService;
import org.springblossom.core.tool.api.R;
import org.springblossom.core.tool.exception.BlossomBaseException;
import org.springblossom.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Pattern;
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
	@Autowired
	private FileInfoService fileInfoService;

	private static final Pattern fileNameKeyWordPattern = Pattern.compile("(\\\\)|(/)|(:)(|)|(\\?)|(>)|(<)|(\")");

	/**
	 * 上传文件,支持多文件上传.获取到文件流后,调用{@link org.springblossom.core.file.service.FileService#saveFile(InputStream, String, String)}进行文件保存
	 * 上传成功后,将返回资源信息如:[{"id":"fileId","name":"fileName","md5":"md5"}]
	 *
	 * @param files 上传的文件
	 * @return 文件上传结果.
	 */
	@PostMapping(value = "/upload-multi")
	@ApiOperation("上传多个文件")
	@Cacheable
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
			throw new BlossomBaseException("save file error");
		}
		return R.data(fileInfo);
	}

	@PostMapping(value = "/upload-static")
	@ApiOperation(value = "上传静态文件", notes = "上传后响应结果的result字段为文件的访问地址")
	public R<BlossomFile> uploadStatic(@RequestPart("file") MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			return R.fail("文件不能为空");
		}
		return R.data(fileService.saveFile(file.getInputStream(), file.getOriginalFilename()));
	}

	@RequestMapping(value = {"/download/{id}/{name:.+}"}, method = {RequestMethod.GET})
	@ApiOperation("指定文件名下载文件")
	public void restDownLoad(@ApiParam("文件的ID或MD5") @PathVariable("id") String id, @ApiParam("文件名") @PathVariable("name") String name, @ApiParam(hidden = true) HttpServletResponse response, @ApiParam(hidden = true) HttpServletRequest request) throws IOException {
		downLoad(id, name, response, request);
	}

	@GetMapping({"/download/{id}"})
	@ApiOperation("下载文件")
	public void downLoad(@ApiParam("文件的ID或MD5") @PathVariable("id") String idOrMd5, @ApiParam(value = "文件名，如果未指定，默认未上传时的文件名", required = false) @RequestParam(value = "name", required = false) String name, @ApiParam(hidden = true) HttpServletResponse response, @ApiParam(hidden = true) HttpServletRequest request) throws IOException {
		FileInfoEntity fileInfo = fileInfoService.selectByIdOrMd5(idOrMd5);
		if (fileInfo == null) {
			throw new BlossomBaseException("文件不存在");
		}
		String fileName = fileInfo.getName();
		String suffix = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf("."), fileName.length()) : "";
		String contentType = (fileInfo.getType() == null) ? MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileName) : fileInfo.getType();
		if (StringUtils.isBlank(name)) {
			name = fileInfo.getName();
		}
		if (!name.contains(".")) {
			name = name.concat(".").concat(suffix);
		}
		name = fileNameKeyWordPattern.matcher(name).replaceAll("");
		int skip = 0;
		long fSize = fileInfo.getSize().longValue();
		try {
			String range = request.getHeader("Range").replace("bytes=", "").replace("-", "");
			skip = Func.toInt(range);
		} catch (Exception exception) {
			log.error("download fail",exception);
		}
		response.setContentLength((int) fSize);
		response.setContentType(contentType);
		response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));
		if (skip > 0) {
			response.setStatus(206);
			String contentRange = "bytes " + skip + "-" + (fSize - 1L) + "/" + fSize;
			response.setHeader("Content-Range", contentRange);
		}
		this.fileService.writeFile(idOrMd5, (OutputStream) response.getOutputStream(), skip);
	}
}
