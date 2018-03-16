package com.xywztech.bcrm.workplat.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bcrm.workplat.model.WorkingplatformInfo;
import com.xywztech.bcrm.workplat.service.WorkingplatformInfo_Service;
import com.xywztech.bob.common.CommonAction;

@SuppressWarnings("serial")
@Action("/workingplatformInfo")
public class WorkingplatformInfoAction extends CommonAction {
	@Autowired
	private WorkingplatformInfo_Service service;

	@Autowired
	public void init() {
		model = new WorkingplatformInfo();
		setCommonService(service);
		needLog = false;;
	}

	public HttpHeaders findWithType() throws Exception {
		try {
			StringBuilder sb = new StringBuilder(
					"select c from  WorkingplatformInfo c where 1=1 ");
			Map<String, Object> values = new HashMap<String, Object>();
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			if (request.getParameter("start") != null)
				start = new Integer(request.getParameter("start")).intValue();
			if (request.getParameter("limit") != null)
				limit = new Integer(request.getParameter("limit")).intValue();
			String messageType = request.getParameter("messageType");
			if (messageType != null&&messageType.equals("root")==false) {
				if (messageType.length() > 0) {
					sb.append("and c.messageType = :messageType");
					values.put("messageType", messageType);
				}
			} else {
				this.setJson(request.getParameter("ddddd"));
				for (String key : this.getJson().keySet()) {
					sb.append(" and c." + key + " = :" + key);
					values.put(key, this.getJson().get(key));
				}
			}
			
			/**-添加知识库发布 查看详情-start*/
			String msgId = request.getParameter("msgId");
			if(msgId !=null && !"".equals(msgId)){
				sb.append(" and c.messageId = '" +msgId+ "' ");
			}
			/**-添加知识库发布 查看详情-end*/

			return super.indexPageByJql(sb.toString(), values);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// 删除
	public String batchDestroy() {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			long idStr = Long.parseLong(request.getParameter("messageId"));
			String jql = "delete from WorkingplatformInfo c where c.messageId in ("
					+ idStr + ")";
			Map<String, Object> values = new HashMap<String, Object>();
			service.batchUpdateByName(jql, values);
			addActionMessage("batch removed successfully");
			return "success";
	}
	//修改栏目属性表
	public String update_productType() throws Exception {
		try {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
			.get(ServletActionContext.HTTP_REQUEST);
			String messageType = request.getParameter("messageType");
			String productType = request.getParameter("productType");
			String jql = "update WorkingplatformInfo c set c.productType ='"+productType+"' where c.messageType = ("+ messageType + ")";
			Map<String, Object> values = new HashMap<String, Object>();
			service.batchUpdateByName(jql, values);
			addActionMessage("batch removed successfully");
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
