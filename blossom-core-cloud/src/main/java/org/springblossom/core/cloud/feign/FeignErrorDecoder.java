package org.springblossom.core.cloud.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.tool.api.R;

import java.io.IOException;

/**
 * feign异常处理，对于业务系统返回的异常，直接往外抛
 * HystrixBadRequestException 不触发熔断机制
 *
 * @author guolf
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Exception decode(String methodKey, Response response) {
		if (response.body() != null) {
			String body = null;
			R result = null;
			try {
				body = Util.toString(response.body().asReader());
				result = objectMapper.readValue(body, R.class);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
			if (result != null) {
				return new HystrixBadRequestException(result.getMsg());
			}
		}
		return FeignException.errorStatus(methodKey, response);
	}
}
