package com.loukou.mapi.context;

public class Context {

	public static final String DEVICE = "d";
	public static final String OS = "os";
	public static final String IP = "ip";
	public static final String CHANNEL = "c";
	public static final String LAT = "lt";
	public static final String LNG = "ln";
	public static final String NETWORK = "n";
	public static final String URI = "uri";
	/**
	 * 系统标识, 包含版本号
	 */
	private String os = "";
	/**
	 * 硬件标识
	 */
	private String device = "";
	
	/**
	 * 下载渠道
	 */
	private String channel = "";
	
	/**
	 * IP地址
	 */
	private String ip = "";
	/**
	 * APP定位的纬度
	 */
	private String lat = "";
	/**
	 * APP定位的经度
	 */
	private String lng = "";
	/**
	 * APP的网络
	 */
	private String network = "";
	
	private String uri = "";
	
	
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
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

}