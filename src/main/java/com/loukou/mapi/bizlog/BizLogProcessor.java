package com.loukou.mapi.bizlog;

import com.loukou.dw.bizlog.BizLog;
import com.loukou.dw.bizlog.BizPrinter;
import com.loukou.dw.bizlog.LogTypeEnum;
import com.loukou.mapi.context.Context;

public class BizLogProcessor {
	private static final String BUSINESS = "mapi";
	private static final String FILENAME = "context";
	private static final LogTypeEnum TYPE = LogTypeEnum.FILE;
	
	private static BizPrinter bizPrinter = null;
	
	static {
		bizPrinter = new BizPrinter();
		bizPrinter.setBusiness(BUSINESS);
		bizPrinter.setFileName(FILENAME);
		bizPrinter.setType(TYPE);
		bizPrinter.init();
	}
	
	public static void print(Context context) {
		
		BizLog log = new BizLog();
		
		log.putString(Context.URI, context.getUri());
		log.putString(Context.OS, context.getOs());
		log.putString(Context.DEVICE, context.getDevice());
		log.putString(Context.IP, context.getIp());
		log.putString(Context.LAT, context.getLat());
		log.putString(Context.LNG, context.getLng());
		log.putString(Context.NETWORK, context.getNetwork());
		bizPrinter.print(log);
	}
}
