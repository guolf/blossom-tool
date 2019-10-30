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
package org.springblossom.core.tool.support.xss;

import org.springblossom.core.tool.utils.StringPool;
import org.springblossom.core.tool.utils.StringUtil;
import org.springblossom.core.tool.utils.WebUtil;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * XSS过滤处理
 *
 * @author Chill
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private byte[] body;

	public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		body = WebUtil.getRequestBytes(request);
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if(StringUtil.isBlank(value)) {
			return value;
		}
		return htmlEscape(value);
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if(StringUtil.isBlank(value)) {
			return value;
		}
		return htmlEscape(value);
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null) {
			int length = values.length;
			String[] escapeValues = new String[length];
			for (int i = 0; i < length; i++) {
				escapeValues[i] = htmlEscape(values[i]);
			}
			return escapeValues;
		}
		return super.getParameterValues(name);
	}

	private String htmlEscape(String input) {
		return StringUtil.isBlank(input) ? input : HtmlUtils.htmlEscape(input,"UTF-8");
	}

	/**
	 * 主要是针对HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE 获取pathvalue的时候把原来的pathvalue经过xss过滤掉
	 */
	@Override
	public Object getAttribute(String name) {
		// 获取pathvalue的值
		if (HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE.equals(name)) {
			Map uriTemplateVars = (Map) super.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			if (Objects.isNull(uriTemplateVars)) {
				return null;
			}
			Map newMap = new LinkedHashMap<>();
			uriTemplateVars.forEach((key, value) -> {
				if (value instanceof String) {
					newMap.put(key, htmlEscape((String) value));
				} else {
					newMap.put(key, value);

				}
			});
			return newMap;
		} else {
			return super.getAttribute(name);
		}
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}

			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}
		};
	}
}
