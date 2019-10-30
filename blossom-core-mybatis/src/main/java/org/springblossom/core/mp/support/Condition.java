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
package org.springblossom.core.mp.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.springblossom.core.mp.base.DynamicQueryEntity;
import org.springblossom.core.tool.jackson.JsonUtil;
import org.springblossom.core.tool.utils.BeanUtil;
import org.springblossom.core.tool.utils.Func;
import org.springblossom.core.tool.utils.StringUtil;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * 分页工具
 *
 * @author Chill
 */
public class Condition {

	private static List symbolList = Lists.newArrayList("=", "≠", "like", ">", "≥", "<", "≤");


	/**
	 * 转化成mybatis plus中的Page
	 *
	 * @param query
	 * @return
	 */
	public static <T> IPage<T> getPage(Query query) {
		Page<T> page = new Page<>(Func.toInt(query.getCurrent(), 1), Func.toInt(query.getSize(), 10));
		page.setAsc(Func.toStrArray(query.getAscs()));
		page.setDesc(Func.toStrArray(query.getDescs()));
		return page;
	}

	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
		return new QueryWrapper<>(entity);
	}

	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param query
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
		query.remove("current");
		query.remove("size");
		QueryWrapper<T> qw = new QueryWrapper<>();
		qw.setEntity(BeanUtil.newInstance(clazz));
		if (Func.isNotEmpty(query)) {
			query.forEach((k, v) -> {
				if (Func.isNotEmpty(v)) {
					qw.like(StringUtil.humpToUnderline(k), v);
				}
			});
		}
		return qw;
	}

	/**
	 * 动态查询构造
	 *
	 * @param dynamicQueryStr
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> QueryWrapper<T> getDynamicQueryWrapper(String dynamicQueryStr, Class<T> clazz) {
		// "=", "≠", "like", ">", "≥", "<", "≤"
		QueryWrapper<T> qw = new QueryWrapper<>();
		qw.setEntity(BeanUtil.newInstance(clazz));
		if (Func.isNotEmpty(dynamicQueryStr)) {
			String strQuery = URLDecoder.decode(dynamicQueryStr);
			if (Func.isEmpty(strQuery)) {
				return qw;
			}
			List<DynamicQueryEntity> dynamicQueryEntityList = JsonUtil.parseArray(strQuery, DynamicQueryEntity.class);
			if (Func.isEmpty(dynamicQueryEntityList)) {
				return qw;
			}

			for (DynamicQueryEntity entity : dynamicQueryEntityList) {
				if (symbolList.contains(entity.getSymbol())) {
					String column = StringUtil.humpToUnderline(entity.getKey());

					switch (entity.getSymbol()) {
						case "like":
							qw.like(column, entity.getValue());
							break;
						case "=":
							qw.eq(column, entity.getValue());
							break;
						case "≠":
							qw.ne(column, entity.getValue());
							break;
						case ">":
							qw.gt(column, entity.getValue());
							break;
						case "≥":
							qw.ge(column, entity.getValue());
							break;
						case "<":
							qw.lt(column, entity.getValue());
							break;
						case "≤":
							qw.le(column, entity.getValue());
							break;
					}
				}
			}
		}
		return qw;
	}

}
