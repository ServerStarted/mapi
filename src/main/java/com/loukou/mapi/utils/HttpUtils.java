package com.loukou.mapi.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public final class HttpUtils {
	
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
	
}
