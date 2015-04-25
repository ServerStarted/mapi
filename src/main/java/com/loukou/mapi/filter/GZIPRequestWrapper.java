package com.loukou.mapi.filter;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

class GZIPRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * Constructs a request object wrapping the given request. In case if
	 * Content-Encoding contains "gzip" we wrap the input stream into byte array
	 * to original input stream has nothing in it but hew wrapped input stream
	 * always returns reproducible ungzipped input stream.
	 * 
	 * @param request
	 *            request which input stream will be wrapped.
	 * @throws java.io.IOException
	 *             when input stream reqtieval failed.
	 */
	public GZIPRequestWrapper(final HttpServletRequest request)
			throws IOException {
		super(request);
	}

	/**
	 * @return reproduceable input stream that is either equal to initial
	 *         servlet input stream(if it was not zipped) or returns unzipped
	 *         input stream.
	 * @throws IOException
	 *             if fails.
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		
		return new GZIPRequestStream(getRequest().getInputStream());
	}

}