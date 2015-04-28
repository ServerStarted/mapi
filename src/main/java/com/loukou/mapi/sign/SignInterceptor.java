package com.loukou.mapi.sign;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.loukou.mapi.utils.HttpUtils;
import com.loukou.mapi.utils.SignUtils;
import com.loukou.pos.proxy.service.cvs.entity.InternalAuthAccount;
import com.loukou.pos.proxy.service.cvs.service.impl.InternalAuthAccountService;

public class SignInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(SignInterceptor.class);
	public static final String ATTR_KEY_AUTH_CTX = "INTERNAL_AUTH_CONTEXT";
	// 白名单，不需要验签
	private Set<String> whiteList = new HashSet<String>();
	
	@Autowired
	private InternalAuthAccountService internalAuthAccountService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) {

		String uri = request.getRequestURI();
		
		if (whiteList.contains(uri)) {
			return true;
		}
		else {
			// 验签
			Map<String, String> params = HttpUtils.parseParams(request);
			StringBuffer sb =  new StringBuffer();
			for (String key : params.keySet()) {
				sb.append(key+"="+params.get(key));
			}
			logger.info("params got in auth:"+sb.toString());
			//找到对应的appId和secret
			String appIdStr = params.get(SignUtils.KEY_APP_ID);
			String timeStr = params.get(SignUtils.KEY_TIME);
			//验证必要字段
			if(StringUtils.isBlank(appIdStr) || StringUtils.isBlank(timeStr)) {
				//验证失败：参数不带appId或timeId
				logger.info("appid/time is missing.");
				return false;
			}
			int appId = 0;
			try {
				appId = Integer.parseInt(appIdStr);
			} catch (NumberFormatException e) {
				//验证失败：不是合法的appid
				logger.info("invalid appid, appid="+appIdStr);
				return false;
			}
			//验证时间
			long time = 0L;
			try {
				time = Long.parseLong(timeStr);
			} catch(Exception e) {
				//验证失败：时间格式不对
				logger.info("invalid time, time="+timeStr);
				return false;
			}
			// TODO 校验时间
			// Date date = new Date(time);

			InternalAuthAccount account = internalAuthAccountService.findByAppId(appId);
			String secret = account == null ? StringUtils.EMPTY : account.getSecret();
			if(StringUtils.isEmpty(secret)) {
				// 验证失败：secret 不存在
				logger.info("no secret for appid="+appId);
				return false;
			}
			//签名
			String signCaled = SignUtils.getSign(params, secret);
			if (StringUtils.isBlank(signCaled)) {
				//验证失败：签名失败
				logger.info(String.format("failed to sign with params"));
				return false;
			}
			String signGot = params.get(SignUtils.KEY_SIGN);
			if (StringUtils.isNotBlank(signGot) && signGot.equalsIgnoreCase(signCaled)) {
				//验证成功
				return true;
			} else {
				//验证失败：签名不匹配
				logger.info(String.format("unmatch sign got[%s] caled[%s]", signGot, signCaled));
				try {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid sign.");
				} catch (IOException e) {
					logger.error("fail to response.sendError(401)", e);
				}
				return false;
			}
		}
	}
	
		
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
}
