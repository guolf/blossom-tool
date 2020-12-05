package org.springblossom.core.boot.filter;

import org.slf4j.MDC;
import org.springblossom.core.tool.constant.BlossomConstant;
import org.springblossom.core.tool.utils.Func;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 日志链路追踪过滤器
 *
 * @author guolf
 * @date 2020-11-07
 */
@ConditionalOnClass(Filter.class)
public class TraceFilter extends OncePerRequestFilter {

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return true;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws IOException, ServletException {
		try {
			String traceId = request.getHeader(BlossomConstant.TRACE_ID_HEADER);
			if (Func.isEmpty(traceId)) {
				traceId = UUID.randomUUID().toString();
			}
			MDC.put(BlossomConstant.LOG_TRACE_ID, traceId);

			filterChain.doFilter(request, response);
		} finally {
			MDC.clear();
		}
	}
}
