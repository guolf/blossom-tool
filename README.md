## 简介
blossom-tool 是一个基于 Spring Boot 2 & Spring Cloud Finchley ，封装组合大量组件，用于快速构建中大型API、RESTful API项目的核心包。


## 工程结构
``` 
blossom-tool
├── blossom-core-boot -- 业务包综合模块
├── blossom-core-launch -- 基础启动模块
├── blossom-core-log -- 日志封装模块 
├── blossom-core-mybatis -- mybatis拓展封装模块 
├── blossom-core-secure -- 安全模块 
├── blossom-core-swagger -- swagger拓展封装模块 
└── blossom-core-tool -- 工具包模块 
	 
```


扩展 `DefaultWebSessionManager`,重写`getSessionId`方法

    /** 
	 * 获取session id
	 * 前后端分离将从请求头中获取jsesssionid
	 */
	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		// 从请求头中获取token
		String token = WebUtils.toHttp(request).getHeader("Authorization");
		// 判断是否有值
		if (StringUtils.isNoneBlank(token)) {
			// 设置当前session状态
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url"); 
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);  
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);  
			return token;
		}
		// 若header获取不到token则尝试从cookie中获取
		return super.getSessionId(request, response);
	}
