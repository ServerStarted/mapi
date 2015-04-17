package com.loukou.pos.auth.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public final class HttpUtil {

	private static final Logger logger = Logger.getLogger(HttpUtil.class);
	private static final String DEFAULT_CHARSET = "UTF-8";

	public static String chainParams(Map<String, Object> params) {
		return chainParams(params, DEFAULT_CHARSET);
	}
	
	public static String chainParams(Map<String, Object> params, String charset) {
		List<String> parStrs = new ArrayList<String>();
		if (params != null) {
			for (String key : params.keySet()) {
				try {
					parStrs.add(key+"="+URLEncoder.encode(getParameter(params, key).toString(), charset));
				} catch (UnsupportedEncodingException e) {
					logger.error(String.format("url encode fail [%s]", params.get(key).toString()), e);
				}
			}
		}
		return StringUtils.join(parStrs, "&");
	}
	
    public static Map<String, String> paramMapConvert(Map<String, Object> paramMap) {
		Map<String, String> paramStrMap = new HashMap<String, String>();
		if (paramMap != null) {
			for (String key : paramMap.keySet()) {
				Object val = paramMap.get(key);
				if (val instanceof Object[]) {
					Object[] values = (Object[])val; 
					if (values != null && values.length > 0) {
						val = values[0];
					} else {
						val = null;
					}
				}
				paramStrMap.put(key, String.valueOf(val));
			}
		}
		return paramStrMap;
    }
	
	public static Object getParameter(Map<String, Object> paramMap, String key) {
		Object val = paramMap.get(key);
		if (val instanceof Object[]) {
			Object[] values = (Object[])val; 
			if (values != null && values.length > 0) {
				val = values[0];
			} else {
				val = null;
			}
		}
		return val;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> parseParams(
			HttpServletRequest request) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Enumeration<String> paramsE = request.getParameterNames();
		while(paramsE.hasMoreElements()) {
		       String paramName = paramsE.nextElement();
		       paramsMap.put(paramName, request.getParameter(paramName));
		}
		return paramMapConvert(paramsMap);
	}
	
    public static String byte2hex(byte[] b) {
        StringBuffer buf = new StringBuffer();
        int i;

        for (int offset=0; offset<b.length; offset++) {
            i = b[offset];
            if(i<0)
                i += 256;
            if(i<16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }
}
