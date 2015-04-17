package com.loukou.mapi.auth.internal;


public enum InternalAuthResultEnum {

	RESULT_UNKNOWN("unknown", -1),
	
	RESULT_SUCCESS("success", 0),
	
	RESULT_PARAMS_MISSING("appid/time missing", 1),

	RESULT_IDS_INVALID("appid/time invalid", 2),
	
	RESULT_BAD_FORMAT("bad param format", 3),
	
	RESULT_EXPIRED("request expired", 4),

	RESULT_SIGN_FAILED("sign failed", 5),
	
	RESULT_SIGN_UNMATCH("sign unmatch", 6);
	
	private String message;
	private int id;
	
	private InternalAuthResultEnum(String message, int id) {
		this.message = message;
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public int getId() {
		return id;
	}
}
