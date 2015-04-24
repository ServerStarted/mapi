package com.loukou.mapi.auth.header;

public class Context {

	/**
	 * 设备唯一标识, uuid
	 */
	private String u = "";
	/**
	 * 系统标识, 包含版本号
	 */
	private String os = "";
	/**
	 * 硬件标识
	 */
	private String d = "";
	/**
	 * app 版本号
	 */
	private String av = "";
	/**
	 * 下载渠道
	 */
	private String c = "";
	
	
	public String getU() {
		return u;
	}
	public void setU(String u) {
		this.u = u;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public String getAv() {
		return av;
	}
	public void setAv(String av) {
		this.av = av;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
}
