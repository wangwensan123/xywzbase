package com.xywztech.bcrm.system.service;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.FwSysProp;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;
import com.xywztech.crm.exception.BizException;
/**
 * 公共参数管理
 * @author changzh@xywztech.com
 * @since 2012-11-19
 */
@Service
public class PubParamManagerService extends CommonService{
	
	 public PubParamManagerService(){
		   JPABaseDAO<FwSysProp, Long>  baseDAO = new JPABaseDAO<FwSysProp, Long>(FwSysProp.class);  
		   super.setBaseDAO(baseDAO);
	 }
	 
	 public Object saveData(FwSysProp p) {
		 try {
			 StringBuffer searchSql = new StringBuffer("select p from FwSysProp p where p.propName =?1 ");
			 if (p.getId() != null) {
				 searchSql.append(" and p.id <>" + p.getId());
			 }
			 	
			Query query = em.createQuery(searchSql.toString());
			query.setParameter(1, p.getPropName());
			query.setFirstResult(0);
				
			List<FwSysProp> result = (List<FwSysProp>) query.getResultList();
			if (result != null)	 {
				/**参数名称是否重复*/
				if (result.size() > 0) {
					throw new BizException(1,0,"0001", "参数名称重复,请重新输入");
				} 
			}
				 
			return baseDAO.save(p);
			 
		 } catch (Exception e) {			 
			 throw new BizException(1,0,"0001",e.getMessage());
		 }		 
	 }
}
