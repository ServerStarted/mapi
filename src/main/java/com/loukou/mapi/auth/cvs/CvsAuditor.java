package com.loukou.mapi.auth.cvs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.dw.bizlog.BizLog;
import com.loukou.dw.bizlog.BizPrinter;
import com.loukou.dw.bizlog.IBizLog;

@Service
public final class CvsAuditor {

	@Autowired
	private BizPrinter bizPrinter;

	public void audit(CvsContext context) {
		IBizLog bizLog = new BizLog();

		bizLog.putString("requestid", context.getRequestId());
		bizLog.putLong("begin", context.getBegin());
		bizLog.putLong("end", context.getEnd());
		bizLog.putString("uri", context.getUri());
		bizLog.putString("method", context.getMethod());
		bizLog.putString("clientip", context.getClientIp());
		bizLog.putInt("cityid", context.getCityId());
		bizLog.putInt("cvsid", context.getCvsId());
		bizLog.putString("machineid", context.getMachineId());
		
		bizPrinter.print(bizLog);
	}
}
