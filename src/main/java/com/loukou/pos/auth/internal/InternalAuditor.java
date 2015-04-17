package com.loukou.pos.auth.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.dw.bizlog.BizLog;
import com.loukou.dw.bizlog.BizPrinter;
import com.loukou.dw.bizlog.IBizLog;

@Service
public final class InternalAuditor {

	@Autowired
	private BizPrinter bizPrinter;

	public void audit(InternalContext context) {
		IBizLog bizLog = new BizLog();

		bizLog.putString("requestid", context.getRequestId());
		bizLog.putLong("begin", context.getBegin());
		bizLog.putLong("end", context.getEnd());
		bizLog.putString("uri", context.getUri());
		bizLog.putString("method", context.getMethod());
		bizLog.putString("clientip", context.getClientIp());
		bizLog.putInt("appid", context.getAppId());
		
		bizPrinter.print(bizLog);
	}
}
