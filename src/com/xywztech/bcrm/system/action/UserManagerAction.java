package com.xywztech.bcrm.system.action;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bcrm.system.model.AdminAuthAccount;
import com.xywztech.bcrm.system.service.UserManagerService;
import com.xywztech.bob.common.CommonAction;
import com.xywztech.bob.core.QueryHelper;
import com.xywztech.bob.vo.AuthUser;
import com.xywztech.crm.constance.EndecryptUtils;
import com.xywztech.crm.constance.SystemConstance;

/*
 * 用户管理Action,维护用户信息
 * @author wangwan
 * @since 2012-10-08
 */

@SuppressWarnings("serial")
@Action("/userManager")
public class UserManagerAction extends CommonAction {
	
	@Autowired
	private UserManagerService service;//定义UserManagerService属性
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性
	
	@Autowired
	public void init() {
		model = new AdminAuthAccount();
		setCommonService(service);
		needLog = true;//新增修改删除记录是否记录日志,默认为false，不记录日志
	}
	/**
	 * 用户查询拼装SQL
	 */
	public void prepare() {
    	StringBuffer sb = new StringBuffer("SELECT DISTINCT t1.*, t2.ORG_NAME  FROM ADMIN_AUTH_ACCOUNT t1"+
   	         " LEFT JOIN ADMIN_AUTH_ORG t2 ON t1.ORG_ID = t2.ORG_ID"+
   	         " LEFT JOIN ADMIN_AUTH_ACCOUNT_ROLE t3 ON t1.ID = t3.ACCOUNT_ID"+
   				" WHERE t1.APP_ID = ");
    	sb.append(SystemConstance.LOGIC_SYSTEM_APP_ID);
		
    	for(String key : this.getJson().keySet()){
    		if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){	
    			if(null!=key&&key.equals("ACCOUNT_NAME")){
    				sb.append("  AND (t1.ACCOUNT_NAME like"+" '%"+this.getJson().get(key)+"%') OR (t1.USER_NAME like '%"+this.getJson().get(key)+"%')");
    			}
    			else if (key.equals("TREE_STORE")){
    				sb.append("  AND (t1.ORG_ID IN (SELECT UNITID FROM SYS_UNITS WHERE UNITSEQ LIKE (SELECT UNITSEQ FROM SYS_UNITS WHERE UNITID='"+
    						(String)this.getJson().get(key)+"')||'%'))");
    			}else if (key.equals("userId")){
    				sb.append("  AND (t1.ID like"+" '%"+this.getJson().get(key)+"%')");
    			}
    		}
    	}
   	
    	addOracleLookup("SEX","DEM0100005");
    	addOracleLookup("USER_STATE","SYS_USER_STATE");
    	SQL=sb.toString();
    	datasource = ds;
    	try{
    		json=new QueryHelper(SQL, ds.getConnection()).getJSON();
    	}catch(Exception e){}
	}	

	/**
	 * 新增用户基本信息方法
	 * @return
	 * @throws ParseException 
	 * @throws Exception
	 */
	public String save() throws ParseException
	 {
		 ActionContext ctx = ActionContext.getContext();
         request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
         String accountName = request.getParameter("accountName");
         String birthday = request.getParameter("birthday");
         String deadline = request.getParameter("deadline");
         String email = request.getParameter("email");
         String mobilephone = request.getParameter("mobilephone");
         String officetel = request.getParameter("officetel");
         String orgId = request.getParameter("orgId");
         String userState = request.getParameter("userState");
         String password = request.getParameter("password");
         String sex = request.getParameter("sex");
         String userName = request.getParameter("userName");
         String updateUser = request.getParameter("updateUser");
         String offenIP = request.getParameter("offenIP");
         String dirId = request.getParameter("dirId");

         AdminAuthAccount aaa= service.saveBaseInfo(accountName, email, mobilephone, officetel, orgId,userState, password, sex, userName,birthday,deadline,offenIP,updateUser,dirId);
         AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         auth.setPid(  aaa.getId().toString());
		 return "";
	 }
	
	
	/**
	 * 修改密码方法
	 * @return
	 * @throws Exception
	 */
	public String updateNew() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			long idStr = Long.parseLong((request.getParameter("idStr")));//获取用户信息主键ID
			String key = request.getParameter("password");//获取用户密码
			String password=EndecryptUtils.encrypt(key);
			String jql = "update  AdminAuthAccount a set a.password ='"+password+"'" +
					" where a.id in ("+idStr+")";
			Map<String,Object> values = new HashMap<String,Object>();
			super.executeUpdate(jql, values);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "success";
	}
	/**
	 * 修改启用停用状态方法
	 * @return
	 * @throws Exception
	 */
	public String updateState() throws Exception{
		
		try{
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			String idStr = request.getParameter("idStr");//获取用户信息主键ID
			String state = request.getParameter("userState");//获取用户状态
			String jql = "update  AdminAuthAccount a set a.userState ='"+state+"'" +
					" where a.id in ("+idStr+")";
			Map<String,Object> values = new HashMap<String,Object>();
			super.executeUpdate(jql, values);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "success";
	}
	 /**
	  * （自定义）批量删除，根据前台传递的idStr删除相应用户信息
	  * @return 成功标识
	  */
    public String batchDestroy(){
	   	ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		String jql="DELETE FROM AdminAuthAccount C WHERE C.id IN ("+idStr+")";
		Map<String,Object> values=new HashMap<String,Object>();
		service.batchUpdateByName(jql, values);
		addActionMessage("batch removed successfully");
		
        return "success";
    }
}


