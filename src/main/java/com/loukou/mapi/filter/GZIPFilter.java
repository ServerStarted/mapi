package com.loukou.mapi.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;


public class GZIPFilter implements Filter {
	
	private static final String CONTENT_ENCODING_GZIP = "gzip";

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) req;
			
			// ungzip request
			String contentEncoding = request.getHeader(HttpHeaders.CONTENT_ENCODING);
		    if (contentEncoding!= null && contentEncoding.contains(CONTENT_ENCODING_GZIP)) {
		        request = new GZIPRequestWrapper(request);
		    }

		    // gzip response
			HttpServletResponse response = (HttpServletResponse) res;
			String acceptEncoding = request.getHeader("accept-encoding");
			if (acceptEncoding != null && acceptEncoding.contains(CONTENT_ENCODING_GZIP)) {
				GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);
				chain.doFilter(request, wrappedResponse);
				wrappedResponse.finishResponse();
			}
			else {
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}
}
