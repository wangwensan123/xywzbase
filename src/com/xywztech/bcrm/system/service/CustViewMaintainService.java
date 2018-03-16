package com.xywztech.bcrm.system.service;


import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.OcrmSysViewManager;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;

/**
 * 客户视图项维护
 * @author zhangsxin
 * @since 2012-12-4
 */
@Service
public class CustViewMaintainService extends CommonService {
	public CustViewMaintainService(){
		   JPABaseDAO<OcrmSysViewManager, String>  baseDAO=new JPABaseDAO<OcrmSysViewManager, String>(OcrmSysViewManager.class);  
		   super.setBaseDAO(baseDAO);
	   }
	   /**
	    * 删除的方法
	    * @param idStr
	    */
	   public void remove(String idStr){
	       em.remove(em.find(OcrmSysViewManager.class, idStr));
	   }
}
