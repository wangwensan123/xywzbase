package com.xywztech.bcrm.system.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.AuthResController;
import com.xywztech.bcrm.system.model.FwFunction;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;

/**
 * @describe 模块管理服务
 * @author GUOCHI
 * @since 2012-10-12
 */
@Service
public class FwFunctionManagerService extends CommonService {
	public FwFunctionManagerService() {
		JPABaseDAO<FwFunction, Long> baseDAO = new JPABaseDAO<FwFunction, Long>(
				FwFunction.class);
		super.setBaseDAO(baseDAO);

	}
	
	// 删除一条记录及其连带按钮控制

    @SuppressWarnings("unchecked")
		public void remove(Object id) {
    	 Object obj = find(id);
        	if (obj != null) {
        		baseDAO.remove(obj);
       		JPABaseDAO<AuthResController, Long> baseDAO2 = new JPABaseDAO<AuthResController, Long>(AuthResController.class);
    		baseDAO2.setEntityManager(em); 
    		if(id != null&&id!=""){
    			Map<String,Object> values=new HashMap<String,Object>();
				String attrDataDelJQL = "delete from AuthResController a where a.fwFunId='"+id+"'";
				baseDAO2.batchExecuteWithNameParam(attrDataDelJQL, values);
			}
		}
   }
	/**
	 * 保存：包括新增和修改
	 * @param ws
	 */
	public void save(FwFunction ws) {
		if (ws.getId() == null) {
			ws.setVersion(0);
			em.persist(ws);
			em.merge(ws);
		} else {
			ws.setVersion(0);
			em.merge(ws);
		}
	}
}
