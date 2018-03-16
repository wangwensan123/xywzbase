
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
import com.xywztech.bob.common.CommonAction;
/**
 * 产品放大镜查询
 * @author songxs
 * @since 2012-12-21
 *
 */
@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value="comProductTree-action", results={@Result(name="success", type="json")})
public class ComProductTreeAction  extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")	
	private DataSource ds;  
	
	public void prepare(){
		String idStrs = "";
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
	
			idStrs = request.getParameter("idStr");		
		
		StringBuffer sb  = new StringBuffer("select a.*,b.catl_name　from ocrm_f_pd_prod_info a inner join ocrm_f_pd_prod_catl_view b on a.catl_code=b.catl_code where 1=1 ");
		if(null!=idStrs&&!idStrs.equals("")){
			String[] strArray = idStrs.split(","); 
			sb.append(" and ( ");
			for(int i=0;i<strArray.length;i++){	
				sb.append(" b.catlseq like (select catlseq from ocrm_f_pd_prod_catl_view where catl_code in "+strArray[i]+")||'%'");
				if(i<strArray.length-1){
					sb.append(" or ");
				}
			}
			sb.append(" ) ");
		}
		for(String key : this.getJson().keySet()){
			if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){	
				if(null!=key&&key.equals("CATL_CODE")){
					sb.append("  and b.catlseq like (select catlseq from ocrm_f_pd_prod_catl_view where catl_code="
							+this.getJson().get(key)
							+")||'%'");
				}else if(null!=key&&key.equals("PRODUCT_ID")){
					sb.append("  and a.PRODUCT_ID like '%"+this.getJson().get(key)+"%'  ");
				}else if(null!=key&&key.equals("PROD_NAME")){
					sb.append("  and a.PROD_NAME like '%"+this.getJson().get(key)+"%'  ");
				}	
			}
		}
		SQL=sb.toString();
		datasource = ds;
	}


}

