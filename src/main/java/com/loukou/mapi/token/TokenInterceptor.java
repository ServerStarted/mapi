package com.loukou.mapi.token;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.loukou.mapi.utils.AESUtils;
import com.loukou.pos.proxy.service.cvs.constants.CvsInfoStatus;
import com.loukou.pos.proxy.service.cvs.entity.CvsInfo;
import com.loukou.pos.proxy.service.cvs.entity.CvsStaff;
import com.loukou.pos.proxy.service.cvs.service.impl.CvsInfoService;
import com.loukou.pos.proxy.service.cvs.service.impl.CvsStaffService;

public class TokenInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(HandlerInterceptorAdapter.class);
	
	private static final String HEADER_TOKEN_NAME = "t";
	private static final String KEY = "70QuJbTMlI8KB4rJ7bmLQA==";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String TOKEN_SEPARATOR = ",";
	private static final int TOKEN_TIMEOUT = 30 * 24 * 60 * 60 * 1000; 	// token 超时时间, 30天
	public static final String USER_ID = "USER_ID";
	
	private Set<String> whiteList = new HashSet<String>();
	
	@Autowired
	private CvsStaffService cvsStaffService;
	@Autowired
	private CvsInfoService cvsInfoService;

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
					String[] strs = tokenStr.split(TOKEN_SEPARATOR);
					// 获取userId
					int userId = Integer.parseInt(strs[0]);
					request.setAttribute(USER_ID, userId);
					
					// 验证店铺是否 审核未通过
					CvsStaff cvsStaff = cvsStaffService.findStaffById(userId);
					if (cvsStaff != null) {
						CvsInfo cvsInfo = cvsInfoService.getCvsInfo(cvsStaff.getCvsId());
						if (cvsInfo != null && (cvsInfo.getStatus() == CvsInfoStatus.AUDIT_FAILURE || cvsInfo.getStatus() == CvsInfoStatus.CLOSED)) {
							// 店铺审核未通过
							logger.info("the store audit NOT PASS.");
							try {
								response.sendError(HttpServletResponse.SC_FORBIDDEN);
							} catch (IOException e) {
								logger.error("fail to response.sendError(403)", e);
							}
							return false;
						}
					} 
										
					// 获取/校验 时间
					String dateStr = strs[1];
					DateFormat dateFormator = new SimpleDateFormat(DATE_FORMAT);
					Date d = dateFormator.parse(dateStr);
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
		
		logger.info("invalid token.");
		try {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid token.");
		} catch (IOException e) {
			logger.error("fail to response.sendError(401)", e);
		}
		return false;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}
	
	public static String getToken(int userId) {
		DateFormat dateFormator = new SimpleDateFormat(DATE_FORMAT);
		String dateStr = dateFormator.format(new Date());
		String token = AESUtils.encrypt(userId+TOKEN_SEPARATOR+dateStr, KEY);
		
		return token;
	}

	public Set<String> getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(Set<String> whiteList) {
		this.whiteList = whiteList;
	}
	
	public static void main(String [] args) {
		String token = getToken(10106);
		System.out.println(token);
	}
}
