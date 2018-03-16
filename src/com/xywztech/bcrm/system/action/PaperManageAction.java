package com.xywztech.bcrm.system.action;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bcrm.system.model.OcrmFSmPaper;
import com.xywztech.bcrm.system.service.PaperManageService;
import com.xywztech.bob.common.CommonAction;
import com.xywztech.bob.core.QueryHelper;
import com.xywztech.crm.exception.BizException;

/**
 * 问卷管理Action
 * @author wangwan
 * @since 2012-12-18
 */

@SuppressWarnings("serial")
@Action("/paperManage")
public class PaperManageAction extends CommonAction {
	
	@Autowired
	private PaperManageService service;//定义UserManagerService属性
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性
	
	@Autowired
	public void init() {
		model = new OcrmFSmPaper();
		setCommonService(service);
		needLog = true;//新增修改删除记录是否记录日志,默认为false，不记录日志
	}
	/**
	 * 用户查询拼装SQL
	 */
	public void prepare() {
		StringBuffer sb = new StringBuffer("SELECT t.* from OCRM_F_SM_PAPERS t WHERE 1=1");
		
		for(String key : this.getJson().keySet()){
			if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){	
				if(null!=key&&key.equals("paperName2")){
					sb.append("  AND (t.PAPER_NAME like"+" '%"+this.getJson().get(key)+"%')");
				}else if (key.equals("optionType2")){
					sb.append("  AND (t.OPTION_TYPE like"+" '%"+this.getJson().get(key)+"%')");
				}else if (key.equals("available2")){
					sb.append("   AND (t.AVAILABLE like"+" '%"+this.getJson().get(key)+"%')");
				}
			}
		}
   		addOracleLookup("optionType","OPTION_TYPE");
   		addOracleLookup("optionType2","OPTION_TYPE");
 		addOracleLookup("available","IF_FLAG");
        addOracleLookup("available2","IF_FLAG");
        SQL=sb.toString();
        datasource = ds;
        try{
        	json=new QueryHelper(SQL, ds.getConnection()).getJSON();
        }catch(Exception e){
        	e.printStackTrace();
			throw new BizException(1,2,"1002","查询问卷信息出错");
        }
	}	

	/**
	 * 新增问卷方法
	 * @return
	 * @throws ParseException 
	 * @throws Exception
	 */
	 public String save() 
	 {
		 ActionContext ctx = ActionContext.getContext();
         request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
         String paperName = request.getParameter("paperName");
         String optionType = request.getParameter("optionType");
         String remark = request.getParameter("remark");
         String available = request.getParameter("available");
         String creator = request.getParameter("creator");
         String creator_org = request.getParameter("creator_org");
         service.save(paperName,optionType,remark,available,creator,creator_org);
 		
		 return "";
	 }
	 
	 public DefaultHttpHeaders saveQuestion(){
		 
		 try{
			 ActionContext ctx = ActionContext.getContext();
			 this.request = ((HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest"));
			 
			 String s3 = this.request.getParameter("addArray");//
			 String s4 = this.request.getParameter("deleteArray");//
			 if(!(s3.equals("[]")))
			 {
				 JSONArray jarray = JSONArray.fromObject(s3);
				 this.service.saveQ(jarray);
			 } 
			 if(!(s4.equals("[]"))){
		  	    	JSONArray jarray2 = JSONArray.fromObject(s4);
		  	    	this.service.removeQ(jarray2);
			 }
		 }catch(Exception e){
			 e.printStackTrace();
			 throw new BizException(1,2,"1002",e.getMessage());
		 }
		 return new DefaultHttpHeaders("success");
		   
	 }
		/**
		 * 修改停启用状态方法
		 * @return
		 * @throws Exception
		 */
	 public String updateState() throws Exception{
			
		 try{
			 ActionContext ctx = ActionContext.getContext();
			 request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			 long idStr = Long.parseLong((request.getParameter("id")));//获取用户信息主键ID
			 String available = request.getParameter("available");//获取用户密码
			 String jql = "update  OcrmFSmPaper a set a.available ='"+available+"'" +
			 " where a.id in ("+idStr+")";
			 Map<String,Object> values = new HashMap<String,Object>();
			 super.executeUpdate(jql, values);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return "success";
	 }
}


