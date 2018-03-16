package com.xywztech.crm.dataauth.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
import com.xywztech.bob.core.QueryHelper;
import com.xywztech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value = "/queryCustViewAuthorize", results = { @Result(name = "success", type = "json"), })
public class QueryCustViewAuthorizeAction {
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	private HttpServletRequest request;

	private Map<String, Object> JSON;

	public Map<String, Object> getJSON() {
		return JSON;
	}

	public void setJSON(Map<String, Object> jSON) {
		JSON = jSON;
	}
    /**
     * 查询所有的授权信息
     * @return
     */
	public String index() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		try {
			StringBuilder sb = new StringBuilder(
					"select o.* from OCRM_SYS_VIEW_MANAGER o  order by o.orders");
			setJSON(new QueryHelper(sb.toString(), ds.getConnection())
					.getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}
    /**
     * 查询角色保存授权信息
     * @return
     */
	public String queryAuthorizeData() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		try {
			StringBuilder sb = new StringBuilder(
					"select o.view_id from OCRM_SYS_VIEW_USER_RELATION o  where 1=1");
			sb.append(" and role_id='" + request.getParameter("role_id")+"'");
			setJSON(new QueryHelper(sb.toString(), ds.getConnection())
					.getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}
    /**
     * 查询客户视图树形菜单
     * @return
     */
	public String queryCustViewTree() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		auth.getAuthorities();
		StringBuilder roleIdSb = new StringBuilder("");
		for (int i = 0; i < auth.getAuthorities().size(); i++) {
			if (!"".equals(auth.getAuthorities().get(i).toString())
					&& auth.getAuthorities().get(i) != null) {
				if (i == 0)
					roleIdSb.append("'"
							+ auth.getAuthorities().get(i).toString() + "'");
				else
					roleIdSb.append(",'"
							+ auth.getAuthorities().get(i).toString() + "'");
			}

		}

		Map numMap = searchCustViewTree(roleIdSb.toString());
		List numList = (List) numMap.get("data");
		Map numMap2 = (Map) numList.get(0);
		StringBuilder sb = new StringBuilder("");
		try {
			if (!"0".equals(numMap2.get("NUM_ID").toString())) {
				sb
						.append("select distinct o1.* from OCRM_SYS_VIEW_MANAGER o1 inner join OCRM_SYS_VIEW_USER_RELATION o2  on  o1.ID=o2.VIEW_ID  where 1=1");

				sb.append(" and o2.ROLE_ID IN (");
				sb.append(roleIdSb.toString());
				sb.append(")");
				sb.append(" and viewtype=" + request.getParameter("viewtype"));
				sb.append(" order by o1.orders");
			} else {
				sb.append("select * from OCRM_SYS_VIEW_MANAGER o where 1=1");
				sb.append(" and viewtype=" + request.getParameter("viewtype"));
				sb.append(" order by o.orders");
			}
			setJSON(new QueryHelper(sb.toString(), ds.getConnection()).getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}
	/**
	 * 查角色是否被授权过
	 * @param useId
	 * @return
	 */
	public Map<String, Object> searchCustViewTree(String useId) {
		String s = " select count(t1.id)as num_id  from OCRM_SYS_VIEW_USER_RELATION t1  where t1.role_id in("
				+ useId + ")";
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, ds.getConnection());
			return qh.getJSON();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

}
