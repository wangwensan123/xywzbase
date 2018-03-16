package com.xywztech.bcrm.system.service;

import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.AdminAuthAccount;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;

/**
 * 个人密码修改Service
 * @author wangwan
 * @since 2012-10-23 
 */
@Service
public class ChangePasswordService extends CommonService {
   
   public ChangePasswordService(){
		JPABaseDAO<AdminAuthAccount, Long>  baseDAO=new JPABaseDAO<AdminAuthAccount, Long>(AdminAuthAccount.class);  
		super.setBaseDAO(baseDAO);
	}
   
}
