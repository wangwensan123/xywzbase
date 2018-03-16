package com.xywztech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bob.action.BaseQueryAction;
import com.xywztech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value="/myCustomerGroupQueryAction", results={
    @Result(name="success", type="json"),
})
public class MyCustomerGroupQueryAction extends BaseQueryAction{

	@Autowired
	@Qualifier("dsOracle")	
	private DataSource dsOracle;  
	private HttpServletRequest request;
	
 	public void prepare() {	
 		ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = auth.getUserId();
        StringBuilder sb = new StringBuilder("select a.id,a.id as cust_id,a.group_name as name,'0' as parent_id,'0' as cust_typ from OCRM_F_CM_ACCORDION_GROUP a ");
        sb.append("where a.user_id = '"+userId+"' ");
        sb.append("union all ");
        sb.append("select to_char(b.id),b.cust_id,d.CUST_ZH_NAME,b.group_id as parent_id,d.cust_typ from OCRM_F_CM_ACCORDION_CUST b ");
        sb.append("left join  OCRM_F_CI_CUST_DESC d on d.CUST_ID = b.CUST_ID ");
		SQL = sb.toString();
		datasource = dsOracle;
	}
}
