package com.xywztech.bcrm.system.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bcrm.common.CommonContance;
import com.xywztech.bcrm.common.service.OrgSearchService;
import com.xywztech.bcrm.system.model.AdminAuthDpt;
import com.xywztech.bcrm.system.service.DepartmentManagerService;
import com.xywztech.bob.common.CommonAction;
import com.xywztech.bob.vo.AuthUser;
/**
 * 部门管理
 * @author changzh@xywztech.com
 * @since 2012-11-21
 */

@SuppressWarnings("serial")
@Action("/departmentManagerAction")
public class DepartmentManagerAction  extends CommonAction{
	
	//private static Logger log = Logger.getLogger(DepartmentManagerAction.class);
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds; //声明数据源
	
	@Autowired
	OrgSearchService oss;
	
    @Autowired
    private DepartmentManagerService departmentManagerService ;
    @Autowired
	public void init(){
	  	model = new AdminAuthDpt(); 
		setCommonService(departmentManagerService);
		//新增修改删除记录是否记录日志,默认为false，不记录日志
		//needLog=true;
	}
   //（自定义）批量删除
    public String batchDestroy(){

	   	ActionContext ctx = ActionContext.getContext();
	    request           = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String      idStr = request.getParameter("idStr");
		String        jql = "delete from AdminAuthDpt d where d.id in (" + idStr + ")";
		
		Map<String,Object> values = new HashMap<String,Object>();
		departmentManagerService.batchUpdateByName(jql, values);
		addActionMessage("batch removed successfully");
		
	    return "success";

    }
    
    //新增或修改方法
    public String saveData(){
    	
        
		departmentManagerService.saveData( model);
		addActionMessage("saveData successfully");
		
	    return "success";

    }

    //分页查询
    public HttpHeaders indexPageByModel() throws Exception {
    	try{	
    		StringBuilder      sb     = new StringBuilder("select d from AdminAuthDpt d where 1=1 ");
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
			        if(key.equals("dptName")){
			            sb.append(" and d.dptName like :dptName");
			            values.put("dptName", "%" + (String) this.getJson().get(key) + "%");
			        } else if(key.equals("id")){
	                    sb.append(" and d.id = :id");
	                    values.put("id", Long.parseLong((String) this.getJson().get(key)));
	                } else{
			        	sb.append(" and d." + key + " = :" + key);
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
    
  //查询上级部门
    public HttpHeaders getDptParents() throws Exception {
    	try{	
    		StringBuilder      sb     = new StringBuilder("select d from AdminAuthDpt d where 1=1 ");
			Map<String,Object> values = new HashMap<String,Object>();
			ActionContext      ctx    = ActionContext.getContext();
			request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
			
			start = new Integer(0).intValue();
			limit = new Integer(1000000).intValue();
        
			String dptId  = request.getParameter("dptId");	
			String belongOrgId  = request.getParameter("belongOrgId");
			
		    if(null != dptId && !dptId.equals("")){
		    	sb.append(" and d.dptId <> :dptId");
		    	values.put("dptId", dptId);
		    } else {
		    	sb.append(" and 1=2");
		    }
		    if(null != belongOrgId && !belongOrgId.equals("")){
	        	sb.append(" and d.belongOrgId = :belongOrgId");
	            values.put("belongOrgId", belongOrgId);
		    } else {
		    	sb.append(" and 1=2");
		    }
			
			return super.indexPageByJql(sb.toString(), values);
			
    	} catch (Exception e){
    		e.printStackTrace();
    		throw e;
    	}
	}
    
    /***查询子组织*/
	@SuppressWarnings("unchecked")
	public String QueryOrgs() throws Exception{
		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String orgId = auth.getUnitId();
		String flag = (String)this.getJson().get("searchType");
		String condiOrg = (String)this.getJson().get("orgId");
		if(condiOrg!=null){
			orgId = condiOrg;
		}
		String condiRole = (String)this.getJson().get("roleId");
		switch(CommonContance.ORG_SEAR.indexOf(flag)){
			case 0 : setJson(oss.searchSubOrgTree(orgId)); break;
			case 1 : setJson(oss.searchSubOrgs(orgId)); break;
			case 2 : setJson(oss.searchParentOrg(orgId)); break;
			case 3 : setJson(oss.searchPathInOrgTree(orgId)); break;
			case 4 : setJson(oss.searchAllOrgs()); break;
			case 5 : 
				if(condiRole!=null)
					setJson(oss.SearchOrgUsers(orgId,condiRole));
				else
					setJson(oss.SearchOrgUsers(orgId,this.getJson()));
				break;
			case 6 : 
				if(condiRole!=null)
					setJson(oss.SearchSubOrgUsers(orgId, false,condiRole));
				else
					setJson(oss.SearchSubOrgUsers(orgId, false)); 
				break;
			case 7 : 
				if(condiRole!=null)
					setJson(oss.SearchSubOrgUsers(orgId, true,condiRole));
				else	
					setJson(oss.SearchSubOrgUsers(orgId, true)); 
				break;
			case 8 : 
				if(condiRole!=null)
					setJson(oss.searchAllUsers(condiRole));
				else
					setJson(oss.searchAllUsers()); 
				break;
			case 9 : 
				if(condiOrg!=null){
					Map tmp = new HashMap<String,Object>();
					tmp.put("ORGNAME", oss.orgName(condiOrg));
					setJson(tmp);
				} 
			default : setJson(new HashMap<String,Object>());
		}
		return "success";
	}
    /**
	 * 设置查询SQL并为父类相关属性赋值
	 */
	public void prepare() {

		String sortStr = "D.DPT_ID ASC"; //设置默认排序
		
		StringBuilder queryStr = new StringBuilder("SELECT " +
				" D.ID, D.APP_ID, D.DPT_ID, D.DPT_NAME, D.DPT_PARENT_ID,P.DPT_NAME as DPT_PARENT_NAME, D.DPT_TYPE," +
				" D.BELONG_ORG_ID, D.INCLUDE_ORG_IDS,'' AS INCLUDE_ORG_NAMES, D.REMARK,O.ORG_NAME " +
				" FROM ADMIN_AUTH_DPT D LEFT JOIN ADMIN_AUTH_ORG O ON D.BELONG_ORG_ID= O.ORG_ID " +
				"	LEFT JOIN  ADMIN_AUTH_DPT P ON " +
				"	D.DPT_PARENT_ID = P.DPT_ID and D.BELONG_ORG_ID = P.BELONG_ORG_ID " +
				" WHERE 1>0 ");

		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key)
					&& !this.getJson().get(key).equals("")) {
				if (key.equals("dptName")) {
					queryStr.append(" AND D.DPT_NAME LIKE '%" + this.getJson().get(key) + "%'");
				} else if (key.equals("selectOrgName")) {
					queryStr.append(" AND O.ORG_NAME LIKE '%" + this.getJson().get(key) + "%'");
				} else if(key.equals("id")){
					queryStr.append(" AND D.ID =" + Long.parseLong((String) this.getJson().get(key)));
                } 				
			}
		}
	    setBranchFileldName("D.BELONG_ORG_ID");
		SQL = queryStr.toString();
		setPrimaryKey(sortStr);
		datasource = ds;
	}
	
	/*
     * 基于JDBC分页查询入口，默认进入index方法
     */
    @SuppressWarnings("unchecked")
	public String index()  {
    	super.index();
    	List list = (List)((Map)json.get("json")).get("data");
    	for (int i = 0; i < list.size(); i ++ ) {
    		Map map = (Map)list.get(i);
    		map.put("COUNT_INCLUDE_ORGS", ((String)map.get("INCLUDE_ORG_IDS")).split(",").length);
    	}
        return "success";
    }
    
}