package com.xywztech.bob.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Action(value="/orgusermanage", results={
    @Result(name="success", type="json"),
})
public class OrgUserManageAction  extends BaseQueryAction {
	@Autowired
	@Qualifier("dsOracle")	
	private DataSource ds;  
	private HttpServletRequest request;
    public void prepare () {
    		
    	ActionContext ctx = ActionContext.getContext();
    	request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);

    	  String role_id = request.getParameter("role_id");
    	  String org_id = request.getParameter("org_id");
        	StringBuffer sb = new StringBuffer("SELECT DISTINCT t3.ID,t2.ORG_ID,t3.USER_NAME,t3.USER_CODE,t1.ROLE_NAME,t1.ROLE_CODE,t2.ORG_NAME,t3.ACCOUNT_NAME,t4.ROLE_ID "+ 
        	"FROM ADMIN_AUTH_ACCOUNT t3 "+
			"left join ADMIN_AUTH_ACCOUNT_ROLE t4 on t3.ID = t4.ACCOUNT_ID "+
			"left join ADMIN_AUTH_ORG t2 on t3.ORG_ID = t2.ORG_ID "+
			"left join ADMIN_AUTH_ROLE t1 on t4.ROLE_ID = t1.ID WHERE 1=1 "+
			"and (t2.ORG_ID IN (SELECT UNITID FROM SYS_UNITS WHERE UNITSEQ LIKE (SELECT UNITSEQ FROM SYS_UNITS WHERE UNITID='"+
			org_id+"')||'%'))");
        	if(!role_id.equals("")){
        		sb.append("and t4.ROLE_ID in ("+ role_id+")");
        	}
        	
        	for(String key : this.getJson().keySet()){
        		if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){	
					if(null!=key&&key.equals("USER_NAME")){
						sb.append("  AND (t3.USER_NAME like '"+this.getJson().get(key)+"')");
					}
					else if (key.equals("ROLE_ID")){
						sb.append("  AND (t4.ROLE_ID in ( "+this.getJson().get(key)+"))");
					}	
					else if (key.equals("TREE_STORE")){
						sb.append("  AND (t2.ORG_ID IN (SELECT UNITID FROM SYS_UNITS WHERE UNITSEQ LIKE (SELECT UNITSEQ FROM SYS_UNITS WHERE UNITID='"+
								(String)this.getJson().get(key)+"')||'%'))");
					}
					else if (key.equals("ROLE_ID2")){
						sb.append("  AND (t4.ROLE_ID in ( "+this.getJson().get(key)+"))");
					}
					else if (key.equals("searchForRoleType")){
						sb.append("  AND (t4.ROLE_ID in ( "+this.getJson().get(key)+"))");
					}
					else if (key.equals("ORG_ID")){
						sb.append("  AND (t2.ORG_ID in ( '"+this.getJson().get(key)+"'))");
					}
        		}
			}

        	
        	SQL=sb.toString();
			datasource = ds;
    }
    

    
}
