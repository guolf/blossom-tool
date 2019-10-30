/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblossom.core.boot.ctrl;

import org.springblossom.core.boot.file.BlossomFile;
import org.springblossom.core.boot.file.BlossomFileUtil;
import org.springblossom.core.secure.BlossomUser;
import org.springblossom.core.secure.utils.SecureUtil;
import org.springblossom.core.tool.api.R;
import org.springblossom.core.tool.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Blossom控制器封装类
 *
 * @author Chill
 */
public class BlossomController {

	/**
	 * ============================     REQUEST    =================================================
	 */

	@Autowired
	private HttpServletRequest request;

	/**
	 * 获取request
	 *
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 * 获取当前用户
	 *
	 * @return BlossomUser
	 */
	public BlossomUser getUser() {
		return SecureUtil.getUser();
	}

	/** ============================     API_RESULT    =================================================  */

	/**
	 * 返回ApiResult
	 *
	 * @param data 数据
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	public <T> R<T> data(T data) {
		return R.data(data);
	}

	/**
	 * 返回ApiResult
	 *
	 * @param data 数据
	 * @param msg  消息
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	public <T> R<T> data(T data, String msg) {
		return R.data(data, msg);
	}

	/**
	 * 返回ApiResult
	 *
	 * @param data 数据
	 * @param msg  消息
	 * @param code 状态码
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	public <T> R<T> data(T data, String msg, int code) {
		return R.data(code, data, msg);
	}

	/**
	 * 返回ApiResult
	 *
	 * @param msg 消息
	 * @return R
	 */
	public R success(String msg) {
		return R.success(msg);
	}

	/**
	 * 返回ApiResult
	 *
	 * @param msg 消息
	 * @return R
	 */
	public R fail(String msg) {
		return R.fail(msg);
	}

	/**
	 * 返回ApiResult
	 *
	 * @param flag 是否成功
	 * @return R
	 */
	public R status(boolean flag) {
		return R.status(flag);
	}


	/**============================     FILE    =================================================  */

	/**
	 * 获取BlossomFile封装类
	 *
	 * @param file 文件
	 * @return BlossomFile
	 */
	public BlossomFile getFile(MultipartFile file) {
		return BlossomFileUtil.getFile(file);
	}

	/**
	 * 获取BlossomFile封装类
	 *
	 * @param file 文件
	 * @param dir  目录
	 * @return BlossomFile
	 */
	public BlossomFile getFile(MultipartFile file, String dir) {
		return BlossomFileUtil.getFile(file, dir);
	}

	/**
	 * 获取BlossomFile封装类
	 *
	 * @param file        文件
	 * @param dir         目录
	 * @param path        路径
	 * @param virtualPath 虚拟路径
	 * @return BlossomFile
	 */
	public BlossomFile getFile(MultipartFile file, String dir, String path, String virtualPath) {
		return BlossomFileUtil.getFile(file, dir, path, virtualPath);
	}

	/**
	 * 获取BlossomFile封装类
	 *
	 * @param files 文件集合
	 * @return BlossomFile
	 */
	public List<BlossomFile> getFiles(List<MultipartFile> files) {
		return BlossomFileUtil.getFiles(files);
	}

	/**
	 * 获取BlossomFile封装类
	 *
	 * @param files 文件集合
	 * @param dir   目录
	 * @return BlossomFile
	 */
	public List<BlossomFile> getFiles(List<MultipartFile> files, String dir) {
		return BlossomFileUtil.getFiles(files, dir);
	}

	/**
	 * 获取BlossomFile封装类
	 *
	 * @param files       文件集合
	 * @param dir         目录
	 * @param path        目录
	 * @param virtualPath 虚拟路径
	 * @return BlossomFile
	 */
	public List<BlossomFile> getFiles(List<MultipartFile> files, String dir, String path, String virtualPath) {
		return BlossomFileUtil.getFiles(files, dir, path, virtualPath);
	}

	/**
	 * 时间范围查询
	 * @param param
	 * @return
	 */
	public LocalDateTime[] getSearchDateTime(String param) {
		Assert.notNull(param,"参数不能为空");
		String[] dateStrArray = getRequest().getParameterMap().get(param);
		if(null == dateStrArray || dateStrArray.length != 2) {
			return null;
		}
		LocalDateTime[] localDateTimes = new LocalDateTime[2];
		for (int i = 0; i < dateStrArray.length; i++) {
			Date date =  DateUtil.parse(dateStrArray[i], "yyyy-MM-dd HH:mm:ss");
			localDateTimes[i] = LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault());
		}
		return localDateTimes;

	}


}
