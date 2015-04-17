package com.loukou.pos.auth.cvs;


public enum CvsAuthResultEnum {

	RESULT_UNKNOWN("unknown", -1),
	
	RESULT_SUCCESS("success", 0),
	
	RESULT_PARAMS_MISSING("cvsid/machineid/cityid/time missing", 1),

	RESULT_IDS_INVALID("cvsid/machineid/cityid invalid", 2),
	
	RESULT_BAD_FORMAT("bad param format", 3),
	
	RESULT_EXPIRED("request expired", 4),

	RESULT_SIGN_FAILED("sign failed", 5),
	
	RESULT_SIGN_UNMATCH("sign unmatch", 6);
	
	
	private String message;
	private int id;
	
	private CvsAuthResultEnum(String message, int id) {
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
