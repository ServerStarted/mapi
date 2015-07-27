package com.loukou.mapi.context;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class ContextProcessor {

	public static Context getContext(HttpServletRequest request) {
		Context context = new Context();
		String uri = request.getRequestURI();
		
		context.setUri(uri);
		context.setOs(getHeader(request, Context.OS));
		context.setDevice(getHeader(request, Context.DEVICE));
		context.setChannel(getHeader(request, Context.CHANNEL));
		context.setIp(getHeader(request, Context.IP));
		context.setLat(getHeader(request, Context.LAT));
		context.setLng(getHeader(request, Context.LNG));
		context.setNetwork(getHeader(request, Context.NETWORK));
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
