package com.loukou.mapi.auth.cvs;



public class CvsContext {

	//请求开始时间
	private long begin;
	//请求结束时间
	private long end;
	//请求uri
	private String uri;
	//请求http方法
	private String method;
	//请求源ip
	private String clientIp;
	//请求来自哪个城市的便利店
	private int cityId;
	//请求来自验证过身份的便利店id
	private int cvsId;
	//请求来自验证过身份的便利店机器id
	private String machineId;
	//请求的guid
	private String requestId;
	//身份验证结果
	private CvsAuthResultEnum authResult = CvsAuthResultEnum.RESULT_UNKNOWN;

	public long getBegin() {
		return begin;
	}
	public void setBegin(long begin) {
		this.begin = begin;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getCvsId() {
		return cvsId;
	}
	public void setCvsId(int cvsId) {
		this.cvsId = cvsId;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public CvsAuthResultEnum getAuthResult() {
		return authResult;
	}
	public void setAuthResult(CvsAuthResultEnum authResult) {
		this.authResult = authResult;
	}
}
