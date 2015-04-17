package com.loukou.mapi.auth.internal;



public class InternalContext {

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
	private int appId;
	//请求的guid
	private String requestId;
	//身份验证结果
	private InternalAuthResultEnum authResult = InternalAuthResultEnum.RESULT_UNKNOWN;


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
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public InternalAuthResultEnum getAuthResult() {
		return authResult;
	}
	public void setAuthResult(InternalAuthResultEnum authResult) {
		this.authResult = authResult;
	}
	
	@Override
	public String toString() {
		return "InternalContext [begin=" + begin + ", end=" + end + ", uri="
				+ uri + ", method=" + method + ", clientIp=" + clientIp
				+ ", appId=" + appId + ", requestId=" + requestId
				+ ", authResult=" + authResult + "]";
	}
}
