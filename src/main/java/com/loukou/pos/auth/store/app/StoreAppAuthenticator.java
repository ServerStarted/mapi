package com.loukou.pos.auth.store.app;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.pos.auth.internal.InternalAuthResultEnum;
import com.loukou.pos.auth.internal.InternalContext;
import com.loukou.pos.auth.processor.AuthAccountProcessor;
import com.loukou.pos.auth.util.HttpUtil;
import com.loukou.pos.auth.util.PosSignUtil;

@Service
public class StoreAppAuthenticator {

	private static final Logger logger = Logger.getLogger(StoreAppAuthenticator.class);

	@Autowired
	private AuthAccountProcessor processor;
	
	public static final String APPID = "APPID";
	//验证身份并且记录信息到context
	public boolean auth(HttpServletRequest request, InternalContext context) {
		Map<String, String> params = HttpUtil.parseParams(request);
		StringBuffer sb =  new StringBuffer();
		for (String key : params.keySet()) {
			sb.append(key+"="+params.get(key));
		}
		logger.info("params got in auth:"+sb.toString());
		//找到对应的appId和secret
		String appIdStr = params.get(PosSignUtil.KEY_APP_ID);
		String timeStr = params.get(PosSignUtil.KEY_TIME);
		//验证必要字段
		if(StringUtils.isBlank(appIdStr) || StringUtils.isBlank(timeStr)) {
			//验证失败：参数不带appId或timeId
			context.setAuthResult(InternalAuthResultEnum.RESULT_PARAMS_MISSING);
			logger.info(String.format(InternalAuthResultEnum.RESULT_PARAMS_MISSING.getMessage()));
			return false;
		}
		int appId = 0;
		try {
			appId = Integer.parseInt(appIdStr);
		} catch (NumberFormatException e) {
			//验证失败：不是合法的appid
			context.setAuthResult(InternalAuthResultEnum.RESULT_IDS_INVALID);
			logger.info(String.format("invalid appid[%s]", appIdStr));
			return false;
		}
		//验证时间
		long time = 0L;
		try {
			time = Long.parseLong(timeStr);
		} catch(Exception e) {
			//验证失败：时间格式不对
			context.setAuthResult(InternalAuthResultEnum.RESULT_BAD_FORMAT);
			logger.info(String.format("bad time format[%s]", timeStr));
			return false;
		}
		// TODO 校验时间
		// Date date = new Date(time);

		String secret = processor.getSecretByAppId(appId);
		if(StringUtils.isEmpty(secret)) {
			//验证失败：不是合法的cvsid+machineid
			context.setAuthResult(InternalAuthResultEnum.RESULT_IDS_INVALID);
			logger.info(String.format("invalid appid", appIdStr));
			return false;
		}
		//签名
		String signCaled = PosSignUtil.getSign(params, secret);
		if (StringUtils.isBlank(signCaled)) {
			//验证失败：签名失败
			context.setAuthResult(InternalAuthResultEnum.RESULT_SIGN_FAILED);
			logger.info(String.format("failed to sign with params"));
			return false;
		}
		String signGot = params.get(PosSignUtil.KEY_SIGN);
		if (StringUtils.isNotBlank(signGot) && signGot.equalsIgnoreCase(signCaled)) {
			//验证成功
			context.setAppId(appId);
			context.setAuthResult(InternalAuthResultEnum.RESULT_SUCCESS);
			request.setAttribute(APPID, appId);
			return true;
		} else {
			//验证失败：签名不匹配
			context.setAuthResult(InternalAuthResultEnum.RESULT_SIGN_UNMATCH);
			logger.info(String.format("unmatch sign got[%s] caled[%s]", signGot, signCaled));
			return false;
		}
	}

}
