package com.xywztech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bob.action.BaseQueryAction;
import com.xywztech.crm.constance.SystemConstance;

/**
 * 反选菜单栏查询
 * @author 
 * @since 2012-10-9
 */
@ParentPackage("json-default")
@Action(value="/roleMenuQuery", results={
    @Result(name="success", type="json"),
})
public class RoleMenuQueryAction extends BaseQueryAction{
	
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	
	private HttpServletRequest request;
	
	/**
	 * 反选菜单栏SQL
	 * @author wz
	 * @since 2012-10-17
	 */
	public void prepare(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String roleId = request.getParameter("roleId");
		StringBuffer sb = new StringBuffer();
		sb.append("select '1' as TABLE_NUM, m.ID, m.APP_ID,  m.RES_ID, m.ATTR_ID, m.RES_CODE,m.ATTR_CODE, m.OPERATE_KEY " +
				"from auth_res_attr_data m where 1=1 and m.app_id = " + SystemConstance.LOGIC_SYSTEM_APP_ID );
		sb.append(" and m.attr_code = '" + roleId +"'");
		sb.append(" union all ");
		sb.append("select '2' as TABLE_NUM, t.ID, t.APP_ID,t.RES_ID, t.ATTR_ID, t.RES_CODE, t.ATTR_CODE,t.OPERATE_KEY " +
				" from AUTH_RES_CONTROL_ATTR_DATA t " +
				" where t.attr_code = '" + roleId +"' and t.app_id = " + SystemConstance.LOGIC_SYSTEM_APP_ID);
		   setPrimaryKey("table_num");
		SQL=sb.toString();
		datasource = ds;
		this.limit= 10000000;
	}
}	







