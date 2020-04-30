package org.springblossom.core.boot.logger.filter;

import org.slf4j.MDC;
import org.springblossom.core.secure.utils.SecureUtil;
import org.springblossom.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * slf4j MDC 过滤
 *
 * @author guolf
 */
@Component
public class LoggingFilter implements Filter {

	private static String LOG_MDC_KEY = "userId";

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		MDC.put(LOG_MDC_KEY, Func.toStr(SecureUtil.getUserId()));
		filterChain.doFilter(servletRequest, servletResponse);
		MDC.remove(LOG_MDC_KEY);
	}
}
