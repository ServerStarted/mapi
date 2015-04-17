package com.loukou.pos.auth.cvs;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.pos.auth.processor.AuthAccountProcessor;
import com.loukou.pos.auth.util.HttpUtil;
import com.loukou.pos.auth.util.PosSignUtil;

@Service
public class CvsAuthenticator {

	private static final Logger logger = Logger.getLogger(CvsAuthenticator.class);
	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormat.forPattern(PosSignUtil.DATE_TIME_FORMAT);    

	@Autowired
	private AuthAccountProcessor processor;
	
	//验证身份并且记录信息到context
	public boolean auth(HttpServletRequest request, CvsContext context) {
		Map<String, String> params = HttpUtil.parseParams(request);
		//找到对应的cvsid+machineId和secret
		String cvsIdStr = params.get(PosSignUtil.KEY_CVS_ID);
		String machineIdStr = params.get(PosSignUtil.KEY_MACHINE_ID);
		String cityIdStr = params.get(PosSignUtil.KEY_CITY_ID);
		String timeStr = params.get(PosSignUtil.KEY_TIME);
		//验证必要字段
		if(StringUtils.isBlank(cvsIdStr) || StringUtils.isBlank(machineIdStr)
				 || StringUtils.isBlank(cityIdStr) || StringUtils.isBlank(timeStr)) {
			//验证失败：参数不带cvsid/machineid/cityid
			context.setAuthResult(CvsAuthResultEnum.RESULT_PARAMS_MISSING);
			logger.info(String.format("cvsid/machineid/cityid/time missing"));
			return false;
		}
		int cvsId = 0;
		int cityId = 0;
		try {
			cvsId = Integer.parseInt(cvsIdStr);
			cityId = Integer.parseInt(cityIdStr);
		} catch (NumberFormatException e) {
			//验证失败：不是合法的cvsid+cityId
			context.setAuthResult(CvsAuthResultEnum.RESULT_IDS_INVALID);
			logger.info(String.format("invalid cvsid[%s]+cityId[%s]", 
					cvsIdStr, cityIdStr));
			return false;
		}
		//验证时间
		DateTime time = new DateTime();
		try {
			time = DATE_TIME_FORMAT.parseDateTime(timeStr);
		} catch(IllegalArgumentException e) {
			//验证失败：时间格式不对
			context.setAuthResult(CvsAuthResultEnum.RESULT_BAD_FORMAT);
			logger.info(String.format("bad time format[%s]", timeStr));
			return false;
		}
//		DateTime now = new DateTime();
//		Duration duration = new Duration(time, now);
//		if (Math.abs(duration.getMillis()) > ProxySignUtil.VALID_SPAN_MS) {
//			//验证失败：请求过期
//			context.setAuthResult(AuthResultEnum.RESULT_BAD_FORMAT);
//			logger.info(String.format("request expired[%s] valid span[%d]", timeStr, ProxySignUtil.VALID_SPAN_MS));
//			return false;
//		}
		String secret = processor.getSecretByCvsIdAndMachineId(cvsId, machineIdStr);
		if(StringUtils.isEmpty(secret)) {
			//验证失败：不是合法的cvsid+machineid
			context.setAuthResult(CvsAuthResultEnum.RESULT_IDS_INVALID);
			logger.info(String.format("invalid cvsid[%s]+machineid[%s]+cityId[%s]", 
					cvsIdStr, machineIdStr, cityIdStr));
			return false;
		}
		//签名
		String signCaled = PosSignUtil.getSign(params, secret);
		if (StringUtils.isBlank(signCaled)) {
			//验证失败：签名失败
			context.setAuthResult(CvsAuthResultEnum.RESULT_SIGN_FAILED);
			logger.info(String.format("failed to sign with params"));
			return false;
		}
		String signGot = params.get(PosSignUtil.KEY_SIGN);
		if (StringUtils.isNotBlank(signGot) && signGot.equalsIgnoreCase(signCaled)) {
			//验证成功
			context.setCvsId(cvsId);
			context.setMachineId(machineIdStr);
			context.setCityId(cityId);
			context.setAuthResult(CvsAuthResultEnum.RESULT_SUCCESS);
			return true;
		} else {
			//验证失败：签名不匹配
			context.setAuthResult(CvsAuthResultEnum.RESULT_SIGN_UNMATCH);
			logger.info(String.format("unmatch sign got[%s] caled[%s]", signGot, signCaled));
			return false;
		}
	}

}
