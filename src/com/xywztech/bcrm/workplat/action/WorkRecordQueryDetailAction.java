package com.xywztech.bcrm.workplat.action;

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


@ParentPackage("json-default")
@Action(value = "/workRecordQueryDetail", results = { @Result(name = "success", type = "json")})
public class WorkRecordQueryDetailAction extends BaseQueryAction {
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	private HttpServletRequest request;
	
    public void prepare() {
    	ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		StringBuilder sb = new StringBuilder(
				" select c.* from ACRM_F_CI_CUST_DAYLOG c " +
				" inner join OCRM_F_WP_WORKLOG WP on WP.USER_ID=c.OWENERID" +
				" where 1>0");
		if(!("").equals(request.getParameter("cust_name"))&&("").equals(request.getParameter("worklog_stat"))&&("").equals(request.getParameter("work_date")))
		{
		    sb.append(" and c.owenername = '" + request.getParameter("cust_name")+"'");
		    sb.append(" and WP.WORKLOG_STAT = '" + request.getParameter("worklog_stat")+"'");
		    sb.append(" and c.ETLDATE = '" + request.getParameter("work_date")+"'");
		}
		setPrimaryKey("c.LOGID");
        SQL=sb.toString();
        datasource = ds;
	}
}

