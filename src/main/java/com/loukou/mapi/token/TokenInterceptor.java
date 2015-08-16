package com.loukou.mapi.token;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.loukou.mapi.utils.AESUtils;
import com.loukou.mapi.utils.HttpUtils;

public class TokenInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(HandlerInterceptorAdapter.class);
	
	private static final String HEADER_TOKEN_NAME = "t";
	private static final String KEY = "70QuJbTMlI8KB4rJ7bmLQA==";
	private static final String TOKEN_SEPARATOR = ",";
	private static final long TOKEN_TIMEOUT = (long)30 * 24 * 60 * 60 * 1000; 	// token 超时时间, 30天
	public static final String TOKEN_DATA = "TOKEN_DATA";
	
	private static final String UNAUTHORIZED_REQUEST_MSG = "未授权的请求 (401)";
	
	private Set<String> whiteList = new HashSet<String>();

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) {
		// 白名单
		String uri = request.getRequestURI();
		if (whiteList.contains(uri)) {
			return true;
		}
		
		// 获取token
		String token = request.getHeader(HEADER_TOKEN_NAME);
		if (token != null && !token.isEmpty()) {
			
			// 解密
			String tokenStr = null;
			try {
				tokenStr = AESUtils.decrypt(token, KEY);
			}
			catch (Exception e) {
				logger.warn("parse token dateStr FAILED.", e);
			}
			
			// 解析token 内容
			if (tokenStr != null) {
				try {
					String[] strs = separateToken(tokenStr);
					// 获取tokenData
					String tokenData = strs[0];
					request.setAttribute(TOKEN_DATA, tokenData);
										
					// 获取/校验 时间
					String dateStr = strs[1];
					Date d = new Date(Long.parseLong(dateStr));
					if (d != null) {
						Date now = new Date();
						// 超时检验
						if (now.getTime() - d.getTime() > TOKEN_TIMEOUT) {
							return true;
						}
					}
				} catch (Exception e) {
					logger.warn("parse dateStr Failed. dateStr="+tokenStr, e);
				}
			}
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
	
	public static String getToken(String tokenData) {
		if (tokenData == null) {
			tokenData = StringUtils.EMPTY;
		}
		long dateLong = new Date().getTime();
		String token = AESUtils.encrypt(tokenData+TOKEN_SEPARATOR+dateLong, KEY);
		
		return token;
	}
	
	private String[] separateToken(String data) {
		int index = data.lastIndexOf(TOKEN_SEPARATOR);
		String tokenData = data.substring(0, index);
		String dateStr = data.substring(index+1, data.length());
		
		return new String[] {tokenData, dateStr};
	}

	public Set<String> getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(Set<String> whiteList) {
		this.whiteList = whiteList;
	}
	
	public static void main(String [] args) {
		String token = getToken("10106");
		System.out.println(token);
	}
}
