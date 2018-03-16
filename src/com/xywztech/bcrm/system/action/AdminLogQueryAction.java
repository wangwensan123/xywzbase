package com.xywztech.bcrm.system.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.xywztech.bob.action.BaseQueryAction;

/**
 * 日志查询
 * @author weijl
 * @since 2012-09-24
 */
@Action("/AdminLogQuery")
@Results({
    @Result(name="success", type="redirectAction", params = {"actionName" , "AdminLogQuery"})
})
public class AdminLogQueryAction extends BaseQueryAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源

	/**
	 * 创建查询SQL并为父类中相应属性赋值
	 */
	public void prepare() {
		
		String sortSql = "A.OPER_TIME DESC"; //默认排序（时间逆序）
		
		StringBuilder querySql = new StringBuilder("SELECT A.ID,"+ //创建查询SQL
				" A.CONTENT,"+
				" A.AFTER_VALUE,"+
				" A.LOGIN_IP,"+
				" DATE_FORMAT(A.OPER_TIME, '%Y-%m-%d %T') AS OPER_TIME,"+
				" SU.USER_NAME "+
				" FROM ADMIN_LOG_INFO A "+
				" LEFT JOIN ADMIN_AUTH_ACCOUNT SU"+
				" ON A.USER_ID = SU.ACCOUNT_NAME "+
				" WHERE 1>0 ");
		
		for (String key : this.getJson().keySet()) { //循环获取查询条件
			if (null != this.getJson().get(key) && !this.getJson().get(key).equals("")) {
				if (key.equals("logType")){ //事件类型
					querySql.append(" AND A.LOG_TYPE_ID = '" + this.getJson().get(key) + "'");
				} else if (key.equals("userName")){ //操作用户
					
					
				//	querySql.append(" AND SU.USERNAME LIKE '%" + this.getJson().get(key) + "%'");
					
					//按操作用户用户名查询
                	String mgr = this.getJson().get(key).toString();
                	String mgrNm[] = mgr.split(",");
                	StringBuilder mgrb = new StringBuilder();
                	for(int j=0;j<mgrNm.length;j++){
                		if(j==0)
                			mgrb.append("'"+mgrNm[j]+"'");
                		else
                			mgrb.append(",'"+mgrNm[j]+"'");
                	}
                	querySql.append(" AND SU.USER_NAME IN("+mgrb.toString()+") ");
                
					
				} else if (key.equals("startTime")){ //开始时间
					querySql.append(" AND A.OPER_TIME >= to_date('" + this.getJson().get(key).toString().substring(0, 10) + "','yyyy-mm-dd')");
				} else if (key.equals("endTime")){ //截止时间
					querySql.append(" AND A.OPER_TIME <= to_date('" + this.getJson().get(key).toString().substring(0, 10) + " 23:59:59','yyyy-mm-dd hh24:mi:ss')");
				} else if (key.equals("timesort")){ //时间排序
					if( Integer.parseInt((String) this.getJson().get(key))==1){
						sortSql = "A.OPER_TIME ASC";
					}else{
						sortSql = "A.OPER_TIME DESC";
					}
				}
			}
		}
		
		SQL = querySql.toString(); //为父类SQL属性赋值（设置查询SQL）
		
		setPrimaryKey(sortSql); //设置查询排序条件
		
		datasource = ds; //为父类数据源赋值
	}
}
