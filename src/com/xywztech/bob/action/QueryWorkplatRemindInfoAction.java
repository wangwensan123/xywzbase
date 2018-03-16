package com.xywztech.bob.action;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.context.SecurityContextHolder;

import com.xywztech.bob.core.QueryHelper;
import com.xywztech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value="/queryremindinfoind", results={
    @Result(name="success", type="json")
})
public class QueryWorkplatRemindInfoAction extends BaseAction{
        
    @Autowired
    @Qualifier("dsOracle")
    private DriverManagerDataSource dsOracle;
    
    public String index() throws Exception {
        AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId(); 
        String roleLvl="0";
        String roleCode="";
        List userCode=auth.getRolesInfo();
        for( int i=0;i<userCode.size();i++ ){
        	Map role = (Map)auth.getRolesInfo().get(i);
        	roleCode = role.get("ROLE_CODE").toString();
        	if (roleCode.equals("admin") || roleCode.equals("zhhz") ){
        		roleLvl="1" ;
        		break;
        	}
        	
        }
        
        Connection conn = dsOracle.getConnection();
        String sqlLayout ="";
    	if (roleLvl.equals("1")){
            sqlLayout = "SELECT COUNT(1) COUNT,REM_NAME FROM xywz_sysm_msg_rmnd WHERE VALID_FLAG <> '0' and READ_FLAG<>'1' GROUP BY REM_NAME "; 
    	}else
        {
    		sqlLayout ="SELECT COUNT(1) AS COUNT,REM_NAME FROM xywz_sysm_msg_rmnd WHERE VALID_FLAG <> '0' and READ_FLAG<>'1' and  " +
    		"RECV_CSTID = '" +
    		roleCode+"' GROUP BY REM_NAME ";; 
        };
        
        QueryHelper qhRemind = new QueryHelper(sqlLayout, conn);        
        this.setJson(qhRemind.getJSON());        
        return "success";
    }
}
