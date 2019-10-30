package org.springblossom.core.cloud.feign;

import com.fasterxml.jackson.databind.JsonNode;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.tool.api.R;
import org.springblossom.core.tool.api.ResultCode;
import org.springblossom.core.tool.jackson.JsonUtil;
import org.springblossom.core.tool.utils.ObjectUtil;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.*;

/**
 * feign 默认回调
 *
 * @author guolf
 * @param <T>
 */
@Slf4j
@AllArgsConstructor
public class BlossomFeignFallback<T> implements MethodInterceptor {
	private final Class<T> targetType;
	private final String targetName;
	private final Throwable cause;
	private final String code = "code";

	@Nullable
	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		String errorMessage = cause.getMessage();
		log.error("MicaFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, errorMessage);
		Class<?> returnType = method.getReturnType();
		// 集合类型反馈空集合
		if (List.class.isAssignableFrom(returnType)) {
			return Collections.emptyList();
		}
		if (Set.class.isAssignableFrom(returnType)) {
			return Collections.emptySet();
		}
		if (Map.class.isAssignableFrom(returnType)) {
			return Collections.emptyMap();
		}
		// 暂时不支持 flux，rx，异步等，返回值不是 R，直接返回 null。
		if (R.class != returnType) {
			return null;
		}
		// 非 FeignException，直接返回 500 请求被拒绝
		if (!(cause instanceof FeignException)) {
			return R.fail(ResultCode.INTERNAL_SERVER_ERROR, errorMessage);
		}
		FeignException exception = (FeignException) cause;
		byte[] content = exception.content();
		// 如果返回的数据为空
		if (ObjectUtil.isEmpty(content)) {
			return R.fail(ResultCode.FAILURE, errorMessage);
		}
		// 转换成 jsonNode 读取，因为直接转换，可能 对方放回的并 不是 R 的格式。
		JsonNode resultNode = JsonUtil.readTree(content);
		// 判断是否 R 格式 返回体
		if (resultNode.has(code)) {
			return JsonUtil.getInstance().convertValue(resultNode, R.class);
		}
		return R.fail(resultNode.toString());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BlossomFeignFallback<?> that = (BlossomFeignFallback<?>) o;
		return targetType.equals(that.targetType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetType);
	}
}
