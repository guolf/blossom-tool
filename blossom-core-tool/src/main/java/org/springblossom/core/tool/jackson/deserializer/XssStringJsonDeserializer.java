package org.springblossom.core.tool.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springblossom.core.tool.utils.StringUtil;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * RequestBody 防XSS
 * @author guolf
 */
public class XssStringJsonDeserializer extends JsonDeserializer<String> {

	@Override
	public Class<String> handledType() {
		return String.class;
	}

	@Override
	public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		String value = jsonParser.getValueAsString();
		if(StringUtil.isBlank(value)) {
			return value;
		}
		return HtmlUtils.htmlEscape(value,"UTF-8");
	}
}
