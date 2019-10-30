package org.springblossom.core.tool.jackson.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 将字符串转为Long
 *
 * @author guolf
 */
public class LongJsonDeserializer extends JsonDeserializer<Long> {

	@Override
	public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		String value = jsonParser.getText();
		return value == null ? null : Long.parseLong(value);
	}

}
