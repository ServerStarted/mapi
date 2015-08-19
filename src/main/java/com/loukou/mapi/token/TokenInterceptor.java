package com.loukou.mapi.token;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.loukou.mapi.utils.HttpUtils;
import com.serverstarted.user.api.UserService;
import com.serverstarted.user.resp.dto.UserTokenContext;

public class TokenInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(HandlerInterceptorAdapter.class);
	
	private static final String TOKEN_PARAM_NAME = "tk";
	private static final String USER_ID_PARAM_NAME = "uid";
	public static final String TOKEN_CONTEXT = "TOKEN_CONTEXT";
	
	private static final String UNAUTHORIZED_REQUEST_MSG = "未授权的请求 (401)";
	
	private Set<String> whiteList = new HashSet<String>();	// 不需要token 验证的url
	private Set<String> blackList = new HashSet<String>();	// 需要token 验证的url

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) {
		// 白名单
		String uri = request.getRequestURI();
		if (whiteList.contains(uri)) {
			return true;
		}
		if (!blackList.contains(uri)) {
			return true;
		}
		
		try {
			// 获取token, userId
			String token = request.getParameter(TOKEN_PARAM_NAME);
			int userId = Integer.parseInt(request.getParameter(USER_ID_PARAM_NAME));
			if (userId > 0 && !StringUtils.isEmpty(token)) {
				UserTokenContext context = userService.getTokenContext(userId, token);
				if (context != null) {
					request.setAttribute(TOKEN_CONTEXT, context);
					return true;
				}
			}
		}
		catch (Exception e) {
			logger.warn("getTokenContext FAILED.", e);
		}
		
		logger.warn("invalid token.");
		HttpUtils.sendErrors(response, HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_REQUEST_MSG);

		return false;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}
	

	public Set<String> getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(Set<String> whiteList) {
		this.whiteList = whiteList;
	}
	
	public void setBlackList(Set<String> blackList) {
		this.blackList = blackList;
	}
	
	public static void main(String [] args) {
//		String token = getToken(10106);
//		System.out.println(token);
	}
}
