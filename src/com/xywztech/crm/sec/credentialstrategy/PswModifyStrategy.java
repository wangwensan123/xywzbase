package com.xywztech.crm.sec.credentialstrategy;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.xywztech.bcrm.system.model.AdminAuthPasswordLog;
import com.xywztech.bcrm.system.service.PasswordChangeLogService;
import com.xywztech.bob.vo.AuthUser;
import com.xywztech.crm.sec.common.SystemUserConstance;
/**
 * 强制修改密码策略类
 * @author wws
 * @date 2012-11-05
 * 
 **/
public class PswModifyStrategy extends CredentialStrategy {
	
	@Autowired
	private PasswordChangeLogService passwordChangeLogService;
	
	PswModifyStrategy () {
		CreStrategyID = SystemUserConstance.CS_PSW_MODIFY_ID;
	}
	
	public void setCreStrategyID (String ID) {
		CreStrategyID = ID;
	}
	
	public boolean doCredentialStrategy (AuthUser userDetails, boolean isAuthenticationChecked) {
		boolean isCredentialStrategy = false;
		if (isAuthenticationChecked) {
			//强制修改密码逻辑
			String searchSql = "select n from AdminAuthPasswordLog n where n.userId =?1 order by n.id desc";
			Query query = passwordChangeLogService.getEntityManager() .createQuery(searchSql);
			query.setParameter(1, userDetails.getUserId());
			query.setFirstResult(0);
			query.setMaxResults(1);
			List<AdminAuthPasswordLog> result = (List<AdminAuthPasswordLog>)query.getResultList();
			Long modifyPeriod = Long.parseLong(this.CreStrategyDetail);
			long dateNum = 0;
			for(AdminAuthPasswordLog aapl : result){
				SimpleDateFormat formatDate  = new SimpleDateFormat("yyyyMMdd");
				Date lastUpdateDate = aapl.getPswdUpTime();
				String currentDate    = formatDate.format(new Date());
				String sLastUpdateDate = formatDate.format(lastUpdateDate);
				dateNum = Long.parseLong(sLastUpdateDate) - Long.parseLong(currentDate);
			}
			
			if (dateNum > modifyPeriod) {
				doActionType(ActionType, "您已经"+ this.CreStrategyDetail +"天没有修改密码了，请注意及时更新密码。", userDetails);
				isCredentialStrategy = true;
			}
		}  

		return isCredentialStrategy;
		 
	}

}

