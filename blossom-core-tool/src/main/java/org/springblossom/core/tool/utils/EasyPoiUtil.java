//package org.springblossom.core.tool.utils;
//
//import cn.afterturn.easypoi.excel.ExcelExportUtil;
//import cn.afterturn.easypoi.excel.ExcelImportUtil;
//import cn.afterturn.easypoi.excel.entity.ExportParams;
//import cn.afterturn.easypoi.excel.entity.ImportParams;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.util.Collection;
//import java.util.List;
//import java.util.NoSuchElementException;
//
///**
// * poi 工具类
// *
// * @author guolf
// */
//public class EasyPoiUtil {
//
//	public static void exportExcel(String fileName, HttpServletResponse response, ExportParams entity, Class<?> pojoClass, Collection<?> dataSet) {
//		try {
//			Workbook workbook = ExcelExportUtil.exportExcel(entity, pojoClass, dataSet);
//			response.setCharacterEncoding("UTF-8");
//			response.setHeader("content-Type", "application/vnd.ms-excel");
//			response.setHeader("Content-Disposition",
//				"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//			workbook.write(response.getOutputStream());
//		} catch (IOException e) {
//			throw new IllegalArgumentException(e.getMessage());
//		}
//	}
//
//	public static void exportExcel(String fileName, HttpServletResponse response, Workbook workbook) {
//		try {
//			response.setCharacterEncoding("UTF-8");
//			response.setHeader("content-Type", "application/vnd.ms-excel");
//			response.setHeader("Content-Disposition",
//				"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//			workbook.write(response.getOutputStream());
//		} catch (IOException e) {
//			throw new IllegalArgumentException(e.getMessage());
//		}
//	}
//
//	public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
//		if (StringUtils.isBlank(filePath)) {
//			return null;
//		}
//		ImportParams params = new ImportParams();
//		params.setTitleRows(titleRows);
//		params.setHeadRows(headerRows);
//		List<T> list;
//		try {
//			list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
//		} catch (NoSuchElementException e) {
//			throw new IllegalArgumentException("模板不能为空");
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e.getMessage());
//		}
//		return list;
//	}
//
//	public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
//		if (ObjectUtil.isEmpty(file)) {
//			return null;
//		}
//		ImportParams params = new ImportParams();
//		params.setTitleRows(titleRows);
//		params.setHeadRows(headerRows);
//		List<T> list;
//		try {
//			list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
//		} catch (NoSuchElementException e) {
//			throw new IllegalArgumentException("excel文件不能为空");
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage());
//		}
//		return list;
//	}
//
//}
