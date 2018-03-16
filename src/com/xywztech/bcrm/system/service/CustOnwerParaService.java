package com.xywztech.bcrm.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.FwSysProp;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;
import com.xywztech.crm.constance.SystemConstance;
/***
 * 客户归属参数设置
 * @author zhangmin
 *
 */
@Service
public class CustOnwerParaService extends CommonService {
	
	public CustOnwerParaService(){
		JPABaseDAO<FwSysProp,Long> baseDao = new JPABaseDAO<FwSysProp,Long>(FwSysProp.class);
		super.setBaseDAO(baseDao);
	}
	/***
	 * 保存用户认证策略
	 * @param DataStr 策略内容
	 * @return
	 */
	public List<FwSysProp> saveData(String DataStr){
		// 删除表中已存在的参数设置
		String jql="delete from FwSysProp T where T.propName LIKE 'CustOnwerPara%'";
		Map<String,Object> values=new HashMap<String,Object>();
		super.batchUpdateByName(jql, values);
		
		// 判断删除结果执行保存操作
		String[] tacticsArr = DataStr.split(";");
		List<FwSysProp> belongParamsList = new ArrayList<FwSysProp>();
		for (int i = 0; i < tacticsArr.length; i++) {
			String[] itemsArr = tacticsArr[i].split(",");
			
				
			FwSysProp o = new FwSysProp();
			o.setId(Long.parseLong("0"));
			o.setVersion(4);
			o.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
			o.setPropName(itemsArr[0]);
			o.setPropDesc(itemsArr[1]);
			o.setPropValue(itemsArr[2]);
			o.setRemark(itemsArr[3]);
			belongParamsList.add(o);
			em.persist(o);
			
		}
		return belongParamsList;
	}
}

