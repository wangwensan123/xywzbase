package com.xywztech.bcrm.system.service;

import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.AdminAuthPasswordLog;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;

/**
 * 用户管理Service
 * @author wangwan
 * @since 2012-10-08 
 */
@Service
public class PasswordChangeLogService extends CommonService {
	
   
   public PasswordChangeLogService(){
		JPABaseDAO<AdminAuthPasswordLog, Long>  baseDAO=new JPABaseDAO<AdminAuthPasswordLog, Long>(AdminAuthPasswordLog.class);  
		super.setBaseDAO(baseDAO);
	}
}
