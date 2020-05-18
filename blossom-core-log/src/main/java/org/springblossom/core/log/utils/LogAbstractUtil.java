package org.springblossom.core.log.utils;

import org.springblossom.core.launch.props.BlossomProperties;
import org.springblossom.core.launch.server.ServerInfo;
import org.springblossom.core.log.model.LogAbstract;
import org.springblossom.core.secure.utils.SecureUtil;
import org.springblossom.core.tool.utils.Func;
import org.springblossom.core.tool.utils.StringPool;
import org.springblossom.core.tool.utils.UrlUtil;
import org.springblossom.core.tool.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class LogAbstractUtil {

	/**
	 * 向log中添加补齐request的信息
	 *
	 * @param request     请求
	 * @param logAbstract 日志基础类
	 */
	public static void addRequestInfoToLog(HttpServletRequest request, LogAbstract logAbstract) {
		if(Func.notNull(request)) {
			logAbstract.setRemoteIp(WebUtil.getIP(request));
			logAbstract.setUserAgent(request.getHeader(WebUtil.USER_AGENT_HEADER));
			logAbstract.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
			logAbstract.setMethod(request.getMethod());
			logAbstract.setParams(WebUtil.getRequestParamString(request));
			logAbstract.setCreateBy(SecureUtil.getUserAccount(request));
		}
	}

	/**
	 * 向log中添加补齐其他的信息（eg：blade、server等）
	 *
	 * @param logAbstract       日志基础类
	 * @param blossomProperties 配置信息
	 * @param serverInfo        服务信息
	 */
	public static void addOtherInfoToLog(LogAbstract logAbstract, BlossomProperties blossomProperties, ServerInfo serverInfo) {
		logAbstract.setServiceId(blossomProperties.getName());
		logAbstract.setServerHost(serverInfo.getHostName());
		logAbstract.setServerIp(serverInfo.getIpWithPort());
		logAbstract.setEnv(blossomProperties.getEnv());
		logAbstract.setCreateTime(new Date());

		//这里判断一下params为null的情况，否则blade-log服务在解析该字段的时候，可能会报出NPE
		if (logAbstract.getParams() == null) {
			logAbstract.setParams(StringPool.EMPTY);
		}
	}
}
