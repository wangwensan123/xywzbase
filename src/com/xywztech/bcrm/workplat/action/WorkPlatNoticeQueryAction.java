package com.xywztech.bcrm.workplat.action;

import java.util.ArrayList;
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
import com.xywztech.bcrm.common.service.OrgSearchService;
import com.xywztech.bob.action.BaseQueryAction;
import com.xywztech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value="/noticequery", results={
    @Result(name="success", type="json"),
})
public class WorkPlatNoticeQueryAction extends BaseQueryAction{
    
    private List<String> orgIds = new ArrayList<String>();
    @Autowired
    @Qualifier("dsOracle")
    private DataSource ds;
    @Autowired
    OrgSearchService oss;
    
    private HttpServletRequest request;
    /**
     * @describe Create sql for search and export.
     */
    public void prepare(){
        AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId();
        String orgId = auth.getUnitId();
        
        ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String dateStr = (String)request.getParameter("cal_curdate");
		List orgsPath=new ArrayList();
		
		try {
			orgsPath =  (List) oss.searchPathInOrgTree(orgId).get("data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //inOrgs(orgId);
        StringBuilder sb = new StringBuilder("");
        sb.append("Select n.NOTICE_ID,n.NOTICE_TITLE,n.NOTICE_CONTENT,n.NOTICE_LEVEL,(SELECT u2.USERNAME FROM SYS_USERS u2 WHERE u2.USERID=n.PUBLISHER) AS PUBLISHER_NAME,n.PUBLISHER,(SELECT u1.UNITNAME FROM SYS_UNITS u1 WHERE u1.UNITID=n.PUBLISH_ORG) AS PUB_ORG_NAME, n.PUBLISH_ORG,n.IS_TOP,n.PUBLISH_TIME,n.MOD_TYPE,n.PUBLISHED,n.ACTIVE_DATE,n.TOP_ACTIVE_DATE,n.NOTICE_TYPE,(SELECT u2.USERNAME FROM SYS_USERS u2 WHERE u2.USERID=n.CREATOR) AS CREATOR_NAME,n.CREATOR,"
                +"(CASE WHEN EXISTS (SELECT 1 FROM OCRM_F_WP_NOTICE_READ r1 where r1.USER_ID='"+userId+"' and r1.NOTICE_ID=n.NOTICE_ID) THEN 'red001' ELSE 'red002' END) AS IS_READ, "
                +"(SELECT COUNT(1) FROM OCRM_F_WP_ANNEXE an WHERE an.RELATION_INFO=to_char(n.NOTICE_ID) AND an.RELATIOIN_MOD='notice') as ANN_COUNT "
                +" from ocrm_f_wp_notice n where (n.CREATOR='"+userId+"' ");
        sb.append(" OR (n.PUBLISHED='pub001' AND n.PUBLISH_ORG in ('"+orgId+"'");
        
        
        
        for(Object oId : orgsPath){
        	Map uniMap =(Map)oId;
        	String[] paths = ((String)uniMap.get("UNITSEQ")).split(",");
        	for(String o : paths)
        		sb.append(",'"+o+"'");
        }
        sb.append(")");
        sb.append(")");
        sb.append(")");
        if(!"".equals(dateStr) && dateStr != null){
        	//若当前日期不为空
        	sb.append("and n.PUBLISH_TIME is not null ");
        	sb.append("and n.PUBLISH_TIME <= to_date('");//for db2 9.7
//        	sb.append("and n.PUBLISH_TIME <= '");//for db2 9.5
        	sb.append(dateStr);
        	sb.append("','yyyy-mm-dd') ");//for db2 9.7
//        	sb.append("'");//for db2 9.5
        	sb.append("and nvl(n.ACTIVE_DATE,to_date('2999-12-31','yyyy-mm-dd')) >= to_date('");//for db2 9.7
//        	sb.append("and nvl(n.ACTIVE_DATE,'2999-12-31') >= '");//for db2 9.5
        	sb.append(dateStr);
        	sb.append("','yyyy-mm-dd') ");//for db2 9.7
//        	sb.append("'");//for db2 9.5
        }
        for(String key:this.getJson().keySet()){ 
            if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){
                if(!key.toUpperCase().equals("ISREAD")){
                    if(!key.equals("PUBLISH_TIME_START")&&!key.equals("PUBLISH_TIME_END")){
                        if(key.equals("NOTICE_TITLE")){
                            sb.append(" and n."+key+" like '%"+this.getJson().get(key)+"%' ");
                        }else 
                        {if(key.equals("PUBLISHER")){
                        	String ss = (String) this.getJson().get(key);
                        	String bb = ss.replace(",", "','");
                            sb.append(" and n."+key+" in ('"+bb+"') ");
                        }
                        }
                        }
                        
                    else if(key.equals("PUBLISH_TIME_START")){
                        String st = (String)this.getJson().get(key);
                        sb.append(" and n.PUBLISH_TIME>=to_date('"+st.substring(0, 10)+"','yyyy-MM-dd') ");//for db2 9.7
//                      sb.append(" and n.PUBLISH_TIME>= '"+st.substring(0, 10)+"'");//for db2 9.5
                    }
                    else if(key.equals("PUBLISH_TIME_END")){
                        String et = (String)this.getJson().get(key);
                        sb.append(" and n.PUBLISH_TIME<=to_date('"+et.substring(0, 10)+"','yyyy-MM-dd') ");//for db2 9.7
//                        sb.append(" and n.PUBLISH_TIME<= '"+et.substring(0, 10)+"'");//for db2 9.5
                    }
                }else{
                    if(this.getJson().get(key).equals("red001"))
                        sb.append(" AND EXISTS (SELECT 1 FROM OCRM_F_WP_NOTICE_READ r WHERE n.notice_id=r.notice_id and r.user_id='"+userId+"')");
                    else if(this.getJson().get(key).equals("red002"))
                        sb.append(" AND NOT EXISTS (SELECT 1 FROM OCRM_F_WP_NOTICE_READ r WHERE n.notice_id=r.notice_id and r.user_id='"+userId+"')");
                    else continue;
                }
            }
        }
        SQL=sb.toString();
        setPrimaryKey("n.NOTICE_ID , n.PUBLISH_TIME ,n.ACTIVE_DATE DESC");
        addOracleLookup("NOTICE_LEVEL", "NOTICE_LEV");
        addOracleLookup("IS_READ", "READ_FLAG");
        datasource = ds;
    }
}
