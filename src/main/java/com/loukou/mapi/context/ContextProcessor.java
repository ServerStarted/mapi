package com.loukou.mapi.context;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class ContextProcessor {

	public static Context getContext(HttpServletRequest request) {
		Context context = new Context();
		String uri = request.getRequestURI();
		String requestId = getHeader(request, Context.REQUEST_ID);
		if (StringUtils.isEmpty(requestId)) {
			requestId = UUID.randomUUID().toString();
		}
		
		context.setUri(uri);
		context.setRequestId(requestId);
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
