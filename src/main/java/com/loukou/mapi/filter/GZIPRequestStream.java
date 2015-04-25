package com.loukou.mapi.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;

public class GZIPRequestStream extends ServletInputStream {

	/**
	 * Default encoding that is used when post parameters are parsed.
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	private final Logger logger = Logger.getLogger(this.getClass());
	private ByteArrayInputStream sourceStream = null;
	private boolean isFinished = false;
	private boolean isReady = false;

	public GZIPRequestStream(InputStream inputStream) throws IOException {
		InputStream in = new GZIPInputStream(inputStream);
		byte[] bytes = IOUtils.toByteArray(in);
		logger.info("Get unzip request data:" + new String(bytes, DEFAULT_ENCODING));
		
		sourceStream = new ByteArrayInputStream(bytes);
		isFinished = true;
		isReady = true;
	}

	public int read() throws IOException {
		return sourceStream.read();
	}

	public void close() throws IOException {
		super.close();
		sourceStream.close();
	}

	@Override
	public boolean isFinished() {
		return isFinished;
	}

	@Override
	public boolean isReady() {
		return isReady;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
	}
};