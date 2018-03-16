package com.xywztech.crm.sec.credentialstrategy;


import com.xywztech.bob.vo.AuthUser;
import com.xywztech.crm.sec.common.SystemUserConstance;
/**
 * 首次登录策略类
 * @author wws
 * @date 2012-11-05
 * 
 **/
public class FirstLoginStrategy extends CredentialStrategy {
	
	FirstLoginStrategy () {
		CreStrategyID = SystemUserConstance.CS_FIRST_LOGIN_ID;
	}
	
	public void setCreStrategyID (String ID) {
		CreStrategyID = ID;
	}
	
	public boolean doCredentialStrategy (AuthUser userDetails, boolean isAuthenticationChecked) {
		boolean isCredentialStrategy = false;
		if (isAuthenticationChecked) {
			//首次登录逻辑判断
			if ("".equals(userDetails.getLastLoginTime())
					|| null == userDetails.getLastLoginTime()) {
				doActionType(ActionType, "请注意及时更新密码。", userDetails);
				isCredentialStrategy = true;
			}
		}
		return isCredentialStrategy;
	}

}

