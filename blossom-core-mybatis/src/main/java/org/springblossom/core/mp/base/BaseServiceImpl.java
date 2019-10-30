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
package org.springblossom.core.mp.base;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springblossom.core.secure.BlossomUser;
import org.springblossom.core.secure.utils.SecureUtil;
import org.springblossom.core.tool.constant.BlossomConstant;
import org.springblossom.core.tool.utils.BeanUtil;
import org.springblossom.core.tool.utils.ObjectUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 业务封装基础类
 *
 * @param <M> mapper
 * @param <T> model
 * @author Chill
 */
@Validated
@Transactional(rollbackFor = Exception.class)
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

	private Class<T> modelClass;

	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		Type type = this.getClass().getGenericSuperclass();
		this.modelClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[1];
	}

	@Override
	public boolean save(T entity) {
		initSaveEntity(entity);
		return super.save(entity);
	}

	/**
	 * 保存，初始化数据
	 * @param entity
	 * @return
	 */
	private T initSaveEntity(T entity) {
		BlossomUser user = SecureUtil.getUser();
		if(ObjectUtil.isEmpty(user)) {
			user = new BlossomUser();
			user.setUserId(0);
		}
		LocalDateTime now = LocalDateTime.now();
		entity.setCreateUser(user.getUserId());
		entity.setCreateTime(now);
		entity.setUpdateUser(user.getUserId());
		entity.setUpdateTime(now);
		if(ObjectUtil.isEmpty(entity.getStatus())) {
			entity.setStatus(BlossomConstant.DB_STATUS_NORMAL);
		}
		entity.setIsDeleted(BlossomConstant.DB_NOT_DELETED);
		return entity;
	}

	private T initUpdateEntity(T entity) {
		BlossomUser user = SecureUtil.getUser();
		entity.setUpdateUser(user.getUserId());
		entity.setUpdateTime(LocalDateTime.now());
		return entity;
	}

	/**
	 * 批量插入
	 *
	 * @param entityList ignore
	 * @param batchSize  ignore
	 * @return ignore
	 */
	@Override
	public boolean saveBatch(Collection<T> entityList, int batchSize) {
		String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			int i = 0;
			for (T anEntityList : entityList) {
				initSaveEntity(anEntityList);
				batchSqlSession.insert(sqlStatement, anEntityList);
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
				i++;
			}
			batchSqlSession.flushStatements();
		}
		return true;
	}

	@Override
	public boolean updateById(T entity) {
		BlossomUser user = SecureUtil.getUser();
		entity.setUpdateUser(user.getUserId());
		entity.setUpdateTime(LocalDateTime.now());
		return super.updateById(entity);
	}

	@Override
	public boolean deleteLogic(@NotEmpty List<Integer> ids) {
		BlossomUser user = SecureUtil.getUser();
		T entity = BeanUtil.newInstance(modelClass);
		entity.setUpdateUser(user.getUserId());
		entity.setUpdateTime(LocalDateTime.now());
		return super.update(entity, Wrappers.<T>update().lambda().in(T::getId, ids)) && super.removeByIds(ids);
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
		Assert.notEmpty(entityList, "error: entityList must not be empty");
		Class<?> cls = currentModelClass();
		TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
		Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
		String keyProperty = tableInfo.getKeyProperty();
		Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			int i = 0;
			for (T entity : entityList) {
				Object idVal = ReflectionKit.getMethodValue(cls, entity, keyProperty);
				if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
					initSaveEntity(entity);
					batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), entity);
				} else {
					initUpdateEntity(entity);
					MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
					param.put(Constants.ENTITY, entity);
					batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
				}
				// 不知道以后会不会有人说更新失败了还要执行插入 😂😂😂
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
				i++;
			}
			batchSqlSession.flushStatements();
		}
		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateBatchById(Collection<T> entityList, int batchSize) {
		Assert.notEmpty(entityList, "error: entityList must not be empty");
		String sqlStatement = sqlStatement(SqlMethod.UPDATE_BY_ID);
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			int i = 0;
			for (T anEntityList : entityList) {
				initUpdateEntity(anEntityList);
				MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
				param.put(Constants.ENTITY, anEntityList);
				batchSqlSession.update(sqlStatement, param);
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
				i++;
			}
			batchSqlSession.flushStatements();
		}
		return true;
	}

}
