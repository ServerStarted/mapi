package com.loukou.pos.auth.processor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.pos.proxy.service.cvs.entity.CvsAuthAccount;
import com.loukou.pos.proxy.service.cvs.entity.InternalAuthAccount;
import com.loukou.pos.proxy.service.cvs.service.impl.CvsAuthAccountService;

@Service
public class AuthAccountProcessor {

	@Autowired
	private CvsAuthAccountService authAccountService;
	
	public String getSecretByCvsIdAndMachineId(int cvsId, String machineId) {
		String secret = StringUtils.EMPTY;
		CvsAuthAccount account = authAccountService.getAccountByCvsIdAndMachineId(cvsId, machineId);
		if (account != null && account.getStatus() == 10) {
			secret = account.getSecret();
		}
		return secret;
	}

	public String getSecretByAppId(int appId) {
		String secret = StringUtils.EMPTY;
		InternalAuthAccount account = authAccountService.getAccountByAppId(appId);
		if (account != null && account.getStatus() == 10) {
			secret = account.getSecret();
		}
		return secret;
	}

}
