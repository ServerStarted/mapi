package com.loukou.mapi.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.loukou.mapi.bizlog.BizLogProcessor;
import com.loukou.mapi.context.Context;
import com.loukou.mapi.context.ContextProcessor;
import com.loukou.mapi.utils.HttpUtils;

public class MapiDispatcherServlet extends DispatcherServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6919789016646365774L;
	public static final String MAPI_CONTEXT = "mapi_context";
	public static final String MAPI_TRANSACTION_NAME = "MAPI";
	private static final String SERVER_ERROR_MSG = "服务异常，请稍后重试 (500)";
	
	@Override
	protected void doDispatch(HttpServletRequest request,
			HttpServletResponse response){

		// 获取request header 中的公共参数 context
		Context context = ContextProcessor.getContext(request);
		request.setAttribute(MAPI_CONTEXT, context);

		// 打印请求的bizlog
		BizLogProcessor.print(context);

		// cat 监控
		Transaction t = Cat.getProducer().newTransaction(
				MAPI_TRANSACTION_NAME, context.getUri());
		try {
			super.doDispatch(request, response);
			t.setStatus(Transaction.SUCCESS);
		} catch (Exception e) {
			Cat.getProducer().logError(e);// 用log4j记录系统异常，以便在Logview中看到此信息
			t.setStatus(e);
			HttpUtils.sendErrors(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SERVER_ERROR_MSG);
		} finally {
			t.complete();
		}

	}
}
