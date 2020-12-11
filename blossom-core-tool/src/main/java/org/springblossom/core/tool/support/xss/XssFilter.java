
package org.springblossom.core.tool.support.xss;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS过滤
 *
 * @author Chill
 */
public class XssFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletRequest requestWrapper = null;
		if (request instanceof HttpServletRequest &&  ((HttpServletRequest) request).getRequestURL().indexOf("feign-client") < 0 ) {
			requestWrapper = new XssHttpServletRequestWrapper((HttpServletRequest) request);
		}
		if (requestWrapper == null ) {
			chain.doFilter(request, response);
		} else {
			chain.doFilter(requestWrapper, response);
		}
	}

	@Override
	public void destroy() {

	}

}
