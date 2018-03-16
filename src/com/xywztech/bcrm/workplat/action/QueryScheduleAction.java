package com.xywztech.bcrm.workplat.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bob.action.BaseQueryAction;
import com.xywztech.bob.core.PagingInfo;
import com.xywztech.bob.core.QueryHelper;
import com.xywztech.bob.vo.AuthUser;

/**
 * 日程安排查询
 * @author WILLJOE
 * @since 2012-11-15 
 */
@ParentPackage("json-default")
@Action(value="/scheduleforcal", results={ 
            @Result(name="success", type="json") ,
        })
public class QueryScheduleAction extends BaseQueryAction {

    @Autowired
    @Qualifier("dsOracle")
    private DriverManagerDataSource dsOracle;
    
    public String index() throws Exception {
    	
        AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId();  
    	ActionContext ctx = ActionContext.getContext();
    	HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
    	Connection conn = dsOracle.getConnection();
    	StringBuilder sb = new StringBuilder(
    			"select SCHEDULE_ID,CREATE_ORG,IS_REMIND," +
    			"CASE WHEN IS_PROCESS = 1 THEN 'TRUE' ELSE 'FALSE' END AS IS_PROCESS," +
    			"RELATION_CUST,t.cust_zh_name as relation_cust_name,REMIND_CYCLE,REMIND_END_TIME,REMIND_STSRT_TIME,REMIND_TYPE," +
    			"SCHEDULE_CONTENT,SCHEDULE_TITLE,TO_CHAR(START_DATE,'yyyy-mm-dd') || ' ' || START_TIME AS START_DATE,START_TIME,END_TIME,BEFOREHEAD_DAY," +
    			"TO_CHAR(END_DATE,'yyyy-mm-dd') || ' ' || END_TIME AS END_DATE,REMIND_BELONG,CREATE_DATE,IS_TEAM from OCRM_F_WP_SCHEDULE " +
    			" left join OCRM_F_CI_CUST_DESC t on t.cust_id = relation_cust " +
    			" where CREATOR='"+userId+"' AND START_DATE is not null and END_DATE is not null AND START_TIME IS NOT NULL AND END_TIME IS NOT NULL ");
    	QueryHelper qh ;
    	if(this.getLimit()<10){//首页日程展示
    		sb.append(" AND END_DATE>=to_date(TO_CHAR(SYSDATE,'yyyy-mm-dd'),'yyyy-mm-dd') ORDER BY END_DATE ASC");//修改
    		PagingInfo pi = new PagingInfo(this.getLimit(),0);
    		qh = new QueryHelper(sb.toString(), conn, pi);
    	}else{//日程安排功能
    		sb.append(" AND END_DATE <= to_date('"+request.getParameter("end").toString()+"','yyyy-mm-dd') ");
    		sb.append(" AND START_DATE >= to_date('"+request.getParameter("start").toString()+"','yyyy-mm-dd') ");
    		qh = new QueryHelper(sb.toString(), conn);
    	}
    	qh.setPrimaryKey("SCHEDULE_ID");
    	this.setJson(qh.getJSON());
    	return "success";
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<Map> getEventJson(){
    	ArrayList<Map> res = (ArrayList<Map>)super.getJson().get("data");
    	for(Map r : res){
    		r.put("ALL_DAY", Boolean.valueOf((String)r.get("IS_PROCESS")));
    	}
    	return res;
    }
    
    public void setEventJson(String eventjson){
    }
    
    public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    public void setLimit(String limit){
        this.limit = Integer.valueOf(limit);
    }

	public Map<String, Object> getJson() {
        return super.getJson();
    }
    public void setStart(String start){
    	
    }

	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

}
