package org.springblossom.core.boot.filter;

import org.slf4j.MDC;
import org.springblossom.core.secure.utils.SecureUtil;
import org.springblossom.core.tool.constant.BlossomConstant;
import org.springblossom.core.tool.utils.Func;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 日志链路追踪过滤器
 *
 * 启动器添加
 * @ServletComponentScan(basePackages = "org.springblossom.core.boot.filter")
 *
 * @author guolf
 * @date 2020-11-07
 */
@Order(100000)
@WebFilter(filterName = "traceFilter",urlPatterns = "/*")
public class TraceFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws IOException, ServletException {
		try {
			String traceId = request.getHeader(BlossomConstant.TRACE_ID_HEADER);
			if (Func.isEmpty(traceId)) {
				traceId = UUID.randomUUID().toString().replace("-","");
			}
			MDC.put(BlossomConstant.LOG_TRACE_ID, traceId);

			// todo fix 取不到值
			MDC.put("userId",Func.toStr(SecureUtil.getUserId()));

			filterChain.doFilter(request, response);
		} finally {
			MDC.clear();
		}
	}
}
