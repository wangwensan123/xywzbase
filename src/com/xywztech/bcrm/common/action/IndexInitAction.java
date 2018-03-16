package com.xywztech.bcrm.common.action;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.xywztech.bob.action.BaseAction;
import com.xywztech.bob.core.QueryHelper;
import com.xywztech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value="/indexinit", results={
    @Result(name="success", type="json"),
})
public class IndexInitAction extends BaseAction{
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource dsOracle;

	public String index() throws Exception{
		
		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
		Map<String,Object> menus = auth.getGrant();
		
		StringBuffer sb = new StringBuffer();
		
		for(String key : menus.keySet()){
			if(sb.toString().equals("")){
				sb.append(key);
			}else{
				sb.append(","+key);
			}
		}
		sb.insert(0, "SELECT * FROM CNT_MENU C LEFT JOIN FW_FUNCTION F ON C.MOD_FUNC_ID = F.ID WHERE C.ID IN (");
		sb.append(") ORDER BY C.ORDER_ ASC ");
		
		QueryHelper indexInit = new QueryHelper(sb.toString(), dsOracle.getConnection());
		this.setJson(indexInit.getJSON());
		return "success";
	}
	
	
}
