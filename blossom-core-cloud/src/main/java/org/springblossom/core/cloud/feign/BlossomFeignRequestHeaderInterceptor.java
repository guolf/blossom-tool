
package org.springblossom.core.cloud.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign 传递Request header
 *
 * @author Chill
 */
@Slf4j
public class BlossomFeignRequestHeaderInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			// 将“隔离模式”调整为“信号量”，才可以透传
			// hystrix.command.default.execution.isolation.strategy=SEMAPHORE
			return;
		}
		HttpServletRequest request = attributes.getRequest();
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				String value = request.getHeader(name);
				requestTemplate.header(name, value);
			}
		}

	}

}
