package com.xywztech.crm.sec.credentialstrategy;


import com.xywztech.bob.vo.AuthUser;
import com.xywztech.crm.sec.common.SystemUserConstance;
/**
 * 登录时间限制策略类
 * @author wws
 * @date 2012-11-05
 **/
public class LoginTimeStrategy extends CredentialStrategy {
	
	LoginTimeStrategy () {
		CreStrategyID = SystemUserConstance.CS_LOGIN_TIME_ID;
	}
	
	public void setCreStrategyID (String ID) {
		CreStrategyID = ID;
	}
	
	public boolean doCredentialStrategy (AuthUser userDetails, boolean isAuthenticationChecked) {
		boolean isCredentialStrategy = false;
		if (isAuthenticationChecked) {
			//登录时间逻辑判断
			if (this.CreStrategyDetail != null) {
				//throw new BadCredentialsException("登录时间不符合，请联系管理员", null);
				//doActionType(ActionType, "登录时间策略警告", userDetails);
				isCredentialStrategy = true;
			}
		}
		
		return isCredentialStrategy;
	}

}

