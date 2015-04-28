package com.loukou.mapi.context;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class ContextProcessor {

	public static Context getContext(HttpServletRequest request) {
		Context context = new Context();
		
		String requestId = UUID.randomUUID().toString();
		String uri = request.getRequestURI();
		
		context.setRequestId(requestId);
		context.setUri(uri);
		context.setUuid(getHeader(request, Context.UUID));
		context.setOs(getHeader(request, Context.OS));
		context.setDevice(getHeader(request, Context.DEVICE));
		context.setApp(getHeader(request, Context.APP));
		context.setAppVersion(getHeader(request, Context.APP_VERSION));
		context.setChannel(getHeader(request, Context.CHANNEL));
		return context;
	}
	
	private static String getHeader(HttpServletRequest request, String headerName) {
		String headerValue = request.getHeader(headerName);
		if (headerValue == null) {
			return StringUtils.EMPTY;
		}
		else {
			return headerValue;
		}
	}
}
