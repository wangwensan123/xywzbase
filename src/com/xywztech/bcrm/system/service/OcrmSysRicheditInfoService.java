package com.xywztech.bcrm.system.service;

import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.OcrmSysRicheditInfo;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;

/***
 * 基础示例
 * @author zhangmin
 *
 */
@Service
public class OcrmSysRicheditInfoService extends CommonService {
	public OcrmSysRicheditInfoService(){
		JPABaseDAO<OcrmSysRicheditInfo,Long> baseDAO = new JPABaseDAO<OcrmSysRicheditInfo,Long>(OcrmSysRicheditInfo.class);
		super.setBaseDAO(baseDAO);
	}
}
