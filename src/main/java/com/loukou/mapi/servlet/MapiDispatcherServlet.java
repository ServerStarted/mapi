package com.loukou.mapi.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

import com.loukou.mapi.bizlog.BizLogProcessor;
import com.loukou.mapi.context.Context;
import com.loukou.mapi.context.ContextProcessor;

@SuppressWarnings("serial")
public class MapiDispatcherServlet extends DispatcherServlet {
	public static final String MAPI_CONTEXT = "mapi_context";

	@Override
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取request header 中的公共参数 context
		Context context = ContextProcessor.getContext(request);
		request.setAttribute(MAPI_CONTEXT, context);
		
		// 打印请求的bizlog
		BizLogProcessor.print(context);
		
		
		// TODO cat 监控
		
		
		super.doDispatch(request, response);
	}
}
