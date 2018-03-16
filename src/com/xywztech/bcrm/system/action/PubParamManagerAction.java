package com.xywztech.bcrm.system.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bcrm.system.model.FwSysProp;
import com.xywztech.bcrm.system.service.PubParamManagerService;
import com.xywztech.bob.common.CommonAction;
/**
 * 公共参数管理
 * @author changzh@xywztech.com
 * @since 2012-11-19
 */

@SuppressWarnings("serial")
@Action("/pubParamManagerAction")
public class PubParamManagerAction  extends CommonAction{
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds; //声明数据源
	
    @Autowired
    private PubParamManagerService pubParamManagerService ;
    
    @Autowired
	public void init(){
	  	model = new FwSysProp(); 
		setCommonService(pubParamManagerService);
		//新增修改删除记录是否记录日志,默认为false，不记录日志
		//needLog=true;
	}
   //（自定义）批量删除
    public String batchDestroy(){

	   	ActionContext ctx = ActionContext.getContext();
	    request           = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String      idStr = request.getParameter("idStr");
		String        jql = "delete from FwSysProp p where p.id in (" + idStr + ")";
		
		Map<String,Object> values = new HashMap<String,Object>();
		pubParamManagerService.batchUpdateByName(jql, values);
		addActionMessage("batch removed successfully");
		
	    return "success";

    }
    
    //新增或修改方法
    public String saveData(){

	   	 
		pubParamManagerService.saveData((FwSysProp) model);
		addActionMessage("saveData successfully");
		
	    return "success";

    }
    
    /**
	 * 设置查询SQL并为父类相关属性赋值
	 */
	public void prepare() {

		String sortStr = "D.ID DESC"; //设置默认排序
		
		StringBuilder queryStr = new StringBuilder("SELECT * " +
				" FROM FW_SYS_PROP D " +
				" WHERE 1>0 and D.PROP_NAME NOT LIKE 'CustOnwerPara%'");

		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key)
					&& !this.getJson().get(key).equals("")) {
				if (key.equals("propName")) {
					queryStr.append(" AND D.PROP_NAME LIKE '%" + this.getJson().get(key) + "%'");
				} else if (key.equals("propDesc")) {
					queryStr.append(" AND D.PROP_DESC LIKE '%" + this.getJson().get(key) + "%'");
				} else if(key.equals("id")){
					queryStr.append(" AND D.ID =" + Long.parseLong((String) this.getJson().get(key)));
                } 				
			}
		}
		
		SQL = queryStr.toString();
		setPrimaryKey(sortStr);
		datasource = ds;
	}

    //分页查询
    public HttpHeaders indexPage() throws Exception {
    	try{	
    		StringBuilder      sb     = new StringBuilder("select p from FwSysProp p where 1=1 ");
			Map<String,Object> values = new HashMap<String,Object>();
			ActionContext      ctx    = ActionContext.getContext();
			request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
			
			if(request.getParameter("start") != null) {
				start = new Integer(request.getParameter("start")).intValue();
			}
			if(request.getParameter("limit") != null){
				limit = new Integer(request.getParameter("limit")).intValue();
			}
        
			this.setJson(request.getParameter("condition"));
			for(String key:this.getJson().keySet()){
			    if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){
			        if(key.equals("propName")){
			            sb.append(" and p.propName like :propName");
			            values.put("propName", "%" + (String) this.getJson().get(key) + "%");
			        }
			        else if(key.equals("p.propDesc")){
			            sb.append(" and p.propDesc like :propDesc");
			            values.put("propDesc", "%" + (String)this.getJson().get(key) + "%");
			        } else if(key.equals("id")){
	                    sb.append(" and p.id = :id");
	                    values.put("id", Long.parseLong((String) this.getJson().get(key)));
	                } else{
			        	sb.append(" and p." + key + " = :" + key);
			        	values.put(key, this.getJson().get(key));
			        }
			    }
			}
			
			return super.indexPageByJql(sb.toString(), values);
			
    	} catch (Exception e){
    		e.printStackTrace();
    		throw e;
    	}
	}
    
}