package com.loukou.mapi.context;

public class Context {

	public static final String REQUEST_ID = "rid";
	public static final String URI = "uri";
	public static final String UUID = "u";
	public static final String OS = "os";
	public static final String DEVICE = "d";
	public static final String APP = "app";
	public static final String APP_VERSION = "av";
	public static final String CHANNEL = "c";
	
	/**
	 * 请求ID
	 */
	private String requestId = "";
	/**
	 * uri
	 */
	private String uri = "";
	/**
	 * 设备唯一标识, uuid
	 */
	private String uuid = "";
	/**
	 * 系统标识, 包含版本号
	 */
	private String os = "";
	/**
	 * 硬件标识
	 */
	private String device = "";
	/**
	 * app 版本号 (iOS 取bundle id, android 取package name)
	 */
	private String app = "";
	/**
	 * app 版本号
	 */
	private String appVersion = "";
	/**
	 * 下载渠道
	 */
	private String channel = "";
	
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
}