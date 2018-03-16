package com.xywztech.bcrm.system.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.AdminAuthAccountRole;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;

/**
 * 用户管理Service
 * @author wangwan
 * @since 2012-10-08 
 */
@Service
public class UserManagerForAuthService extends CommonService {
   
   public UserManagerForAuthService(){
		JPABaseDAO<AdminAuthAccountRole, Long>  baseDAO=new JPABaseDAO<AdminAuthAccountRole, Long>(AdminAuthAccountRole.class);  
		super.setBaseDAO(baseDAO);
	}
   /**
    * 保存用户授权角色信息
    * @param jarray
    * @return
    */
	public void save( JSONArray jarray){
		if (jarray.size() > 0){
			for (int i = 0; i < jarray.size(); ++i){
				JSONObject wa = (JSONObject)jarray.get(i);
				AdminAuthAccountRole ws = new AdminAuthAccountRole();
				ws.setAccountId(Long.valueOf((String) wa.get("accountId")));
				ws.setRoleId(Long.valueOf((String) wa.get("roleId")));
				ws.setAppId((String) wa.get("appId"));

				this.em.persist(ws);
        }
	}	
}
	 /**
	    * 删除用户授权角色信息
	    * @param jarray2
	    * @return
	    */
  public void remove(JSONArray jarray2){
  if (jarray2.size() > 0){
	  for (int i = 0; i < jarray2.size(); ++i) {
		  
		  JSONObject wb = (JSONObject)jarray2.get(i);
		  String t = (String)wb.get("id");
		  AdminAuthAccountRole ws2 = (AdminAuthAccountRole)this.em.find(AdminAuthAccountRole.class,Long.valueOf(t));
	        if (ws2 != null){
	        this.em.remove(ws2);
	        }
	  	}}
    	}
  
}
