package com.loukou.mapi.auth.store.app;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.loukou.mapi.auth.contants.MDCConstants;
import com.loukou.mapi.auth.internal.InternalAuditor;
import com.loukou.mapi.auth.internal.InternalContext;

public class StoreAppInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(StoreAppInterceptor.class);
	public static final String ATTR_KEY_AUTH_CTX = "INTERNAL_AUTH_CONTEXT";
	private Set<String> whiteList = new HashSet<String>();
	
	@Autowired
	private StoreAppAuthenticator authenticator;
	
	@Autowired
	private InternalAuditor auditor;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) {

		String uri = request.getRequestURI();
		
		String method = request.getMethod();
		String clientIp = request.getRemoteAddr();
		String requestId = UUID.randomUUID().toString();

		//记录到mdc以影响后续打点
		MDC.put(MDCConstants.KEY_REQUEST_ID, requestId);
		MDC.put(MDCConstants.KEY_CLIENT_IP, clientIp);
		MDC.put(MDCConstants.KEY_URI, uri);
		//记录到context以便做audit
		InternalContext context = new InternalContext();
		context.setRequestId(requestId);
		context.setBegin(System.currentTimeMillis());
		context.setUri(uri);
		context.setMethod(method);
		context.setClientIp(clientIp);
		request.setAttribute(ATTR_KEY_AUTH_CTX, context);

		//不同来源的特有部分先清空
		MDC.put(MDCConstants.KEY_APP_ID, StringUtils.EMPTY);
		MDC.put(MDCConstants.KEY_CVS_ID, StringUtils.EMPTY);
		MDC.put(MDCConstants.KEY_MACHINE_ID, StringUtils.EMPTY);
		MDC.put(MDCConstants.KEY_CITY_ID, StringUtils.EMPTY);
		
		if (whiteList.contains(uri)) {
			return true;
		}
		else {
			//验证身份
			boolean authSucceed = authenticator.auth(request, context);
		
			if (!authSucceed) {
				//验证失败
				try {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid sign.");
				} catch (IOException e) {
					logger.error("fail to response.sendError(401)", e);
				}
				return false;
			} else {
				//验证成功
				//继续记录到mdc带入大点
				MDC.put(MDCConstants.KEY_APP_ID, context.getAppId());
				return true;
			}
		}
	}
		
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		InternalContext context = (InternalContext) request.getAttribute(ATTR_KEY_AUTH_CTX);
		context.setEnd(System.currentTimeMillis());
		auditor.audit(context);
	}

	public Set<String> getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(Set<String> whiteList) {
		this.whiteList = whiteList;
	}
}
