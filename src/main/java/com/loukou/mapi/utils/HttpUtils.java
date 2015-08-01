package com.loukou.mapi.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public final class HttpUtils {
	private static final Logger LOGGER = Logger.getLogger(HttpUtils.class);
	
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
	
	/**
	 * http response 返回错误，并写入错误信息
	 * @param response
	 * @param sc
	 * @param message
	 */
	public static void sendErrors(HttpServletResponse response, int sc, String message) {

		response.setContentType("text/html;charset=UTF-8");
		response.setStatus(sc);
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.print(message);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			LOGGER.error("Get response writer FAILED.", e);
		}
	}
}
