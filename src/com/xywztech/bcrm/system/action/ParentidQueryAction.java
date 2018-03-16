package com.xywztech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bob.common.CommonAction;


@SuppressWarnings("serial")
@Action("/parentidQuery")
public class ParentidQueryAction extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	private HttpServletRequest request;

	/**
     * 查询客户视图树形菜单
     * @return
     */
	@Override
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		
		String viewtype1 = request.getParameter("viewtype");
		StringBuilder sb = new StringBuilder("");
			sb.append("select * from OCRM_SYS_VIEW_MANAGER where 1=1");
				if (viewtype1!=null && !viewtype1.equals("")){
					sb.append(" and VIEWTYPE='"+viewtype1+"'");
				}
		SQL=sb.toString();
		datasource = ds;	
	}
}
