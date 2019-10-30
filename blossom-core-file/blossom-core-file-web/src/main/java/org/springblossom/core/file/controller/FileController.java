package org.springblossom.core.file.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblossom.core.boot.ctrl.BlossomController;
import org.springblossom.core.file.entity.FileInfoEntity;
import org.springblossom.core.file.service.FileInfoService;
import org.springblossom.core.file.service.FileService;
import org.springblossom.core.log.exception.ServiceException;
import org.springblossom.core.tool.api.R;
import org.springblossom.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
	 * 构建一个文本文件,并下载.支持GET,POST请求
	 *
	 * @param name     文件名
	 * @param text     文本内容
	 * @param response {@link HttpServletResponse}
	 * @throws IOException 写出文本内容错误
	 */
	@RequestMapping(value = "/download-text/{name:.+}", method = {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation("构建文本文件并下载")
	public void downloadTxt(@ApiParam("文件名") @PathVariable("name") String name,
							@ApiParam("文本内容") @RequestParam("text") String text,
							HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=utf-8");
		response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));
		response.getWriter().write(text);
	}

	/**
	 * 使用restful风格,通过文件ID下载已经上传的文件,支持断点下载
	 * 如: http://host:port/file/download/aSk2a/file.zip 将下载 ID为aSk2a的文件.并命名为file.zip
	 *
	 * @param id       文件ID
	 * @param name     文件名
	 * @param response {@link HttpServletResponse}
	 * @param request  {@link HttpServletRequest}
	 * @return 下载结果, 在下载失败时, 将返回错误信息
	 * @throws IOException 读写文件错误
	 */
	@RequestMapping(value = "/download/{id}/{name:.+}", method = RequestMethod.GET)
	@ApiOperation("指定文件名下载文件")
	public void restDownLoad(@ApiParam("文件的id或者md5") @PathVariable("id") String id,
							 @ApiParam("文件名") @PathVariable("name") String name,
							 @ApiParam(hidden = true) HttpServletResponse response,
							 @ApiParam(hidden = true) HttpServletRequest request) throws IOException {

		downLoad(id, name, response, request);
	}

	/**
	 * 通过文件ID下载已经上传的文件,支持断点下载
	 * 如: http://host:port/file/download/aSk2a/file.zip 将下载 ID为aSk2a的文件.并命名为file.zip
	 *
	 * @param idOrMd5  要下载资源文件的id或者md5值
	 * @param name     自定义文件名，该文件名不能存在非法字符.如果此参数为空(null).将使用文件上传时的文件名
	 * @param response {@link javax.servlet.http.HttpServletResponse}
	 * @param request  {@link javax.servlet.http.HttpServletRequest}
	 * @return 下载结果, 在下载失败时, 将返回错误信息
	 * @throws IOException 读写文件错误
	 */
	@GetMapping(value = "/download/{id}")
	@ApiOperation("下载文件")
	public void downLoad(@ApiParam("文件的id或者md5") @PathVariable("id") String idOrMd5,
						 @ApiParam(value = "文件名,如果未指定,默认为上传时的文件名", required = false) @RequestParam(value = "name", required = false) String name,
						 @ApiParam(hidden = true) HttpServletResponse response, @ApiParam(hidden = true) HttpServletRequest request)
		throws IOException {
		FileInfoEntity fileInfo = fileInfoService.selectByIdOrMd5(idOrMd5);
		if (fileInfo == null) {
			throw new ServiceException("文件不存在");
		}
		String fileName = fileInfo.getName();

		String suffix = fileName.contains(".") ?
			fileName.substring(fileName.lastIndexOf("."), fileName.length()) :
			"";
		//获取contentType
		String contentType = fileInfo.getType() == null ?
			MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileName) :
			fileInfo.getType();
		//未自定义文件名，则使用上传时的文件名
		if (StringUtils.isBlank(name)) {
			name = fileInfo.getName();
		}
		//如果未指定文件拓展名，则追加默认的文件拓展名
		if (!name.contains(".")) {
			name = name.concat(".").concat(suffix);
		}
		//关键字剔除
		name = fileNameKeyWordPattern.matcher(name).replaceAll("");
		int skip = 0;
		long fSize = fileInfo.getSize();
		//尝试判断是否为断点下载
		try {
			//获取要继续下载的位置
			String range = request.getHeader("Range").replace("bytes=", "").replace("-", "");
			skip = Func.toInt(range);
		} catch (Exception ignore) {
		}
		//文件大小
		response.setContentLength((int) fSize);
		response.setContentType(contentType);
		response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));
		//断点下载
		if (skip > 0) {
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			String contentRange = "bytes " + skip + "-" + (fSize - 1) + "/" + fSize;
			response.setHeader("Content-Range", contentRange);
		}
		fileService.writeFile(idOrMd5, response.getOutputStream(), skip);
	}

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
	public R<String> uploadStatic(@RequestPart("file") MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			return R.fail("文件不能为空");
		}
		return R.data(fileService.saveStaticFile(file.getInputStream(), file.getOriginalFilename()));
	}

	@GetMapping(value = "/md5/{md5}")
	@ApiOperation("根据MD5获取文件信息")
	public R<FileInfoEntity> uploadStatic(@PathVariable String md5) throws IOException {
		FileInfoEntity fileInfoEntity = fileInfoService.selectByMd5(md5);
		if (fileInfoEntity == null) {
			throw new ServiceException("文件不存在");
		}
		return R.data(fileInfoEntity);
	}
}
