
package com.xywztech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bcrm.system.model.OcrmSysViewManager;
import com.xywztech.bcrm.system.service.CustViewMaintainService;
import com.xywztech.bob.common.CommonAction;

/**
 * 客户视图项维护的新增修改和删除
 * @author zhangsxin
 * @since 2012-12-5
 */

@SuppressWarnings("serial")
@Action("/CustViewMaintainInfo-action")
public class CustViewMaintainInformationAction  extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	
	@Autowired
	private CustViewMaintainService custViewMaintainService;
 
	
	@Autowired
	public void init() {
		model = new OcrmSysViewManager();
		setCommonService(custViewMaintainService);
	}

	/**
	 * 客户视图项维护的查询
	 */
	public void prepare() {
		
		StringBuilder sb = new StringBuilder("SELECT A.*,B.NAME AS PARENT_NAME FROM OCRM_SYS_VIEW_MANAGER A ")
		.append("	LEFT JOIN OCRM_SYS_VIEW_MANAGER B ON A.PARENTID=B.ID")
		.append("  WHERE 1=1");
		
		for (String key : this.getJson().keySet()) { //循环获取查询条件
			if (null != this.getJson().get(key) && !this.getJson().get(key).equals("")) {
				if (key.equals("name")){ //视图项名称
					sb.append(" AND A.NAME like '%" + this.getJson().get(key) + "%'");
				} 
				else if(key.equals("viewtype")){//客户视图项类型
					sb.append(" AND A.VIEWTYPE like '%" + this.getJson().get(key) + "%'");
				}
			}
		}
		addOracleLookup("VIEWTYPE", "PAR0100021");//对照类型
		
		SQL = sb.toString(); //为父类SQL属性赋值（设置查询SQL）
		datasource = ds; //为父类数据源赋值
	}
	
   /**
    * 数据删除提交
    * HTTP:DELETE方法
    * URL:/actionName/$id
    */
   public String destroy() {
       ActionContext ctx = ActionContext.getContext();
       request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
       String idStr = request.getParameter("idStr"); //获取需要删除的控制id
       custViewMaintainService.remove(Long.valueOf(idStr));
       return "success";
   }	
}
