package com.xywztech.bcrm.workplat.action;


import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.xywztech.bob.action.BaseQueryAction;

/**
 * 日程管理-日程查询
 * @author songxs
 * @since 2012-12-6
 */
@ParentPackage("json-default")
@Action(value="/scheduleQuery-action", results={
    @Result(name="success", type="json")
})
public class ScheduleQueryAction extends BaseQueryAction{
    
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
		
	

    @Override
    public void prepare() {
        StringBuffer builder = new StringBuffer("select c.schedule_id,  c.schedule_title,c.relation_cust," +
        		"c.creator,c.remind_belong,c.is_team, c.mkt_team_id, c.start_date,c.end_date,t.cust_zh_name as relation_cust_name,t1.user_name as creator_name," +
        		"case when c.start_date> sysdate then '未开始'  " +
        		"when c.start_date<sysdate and sysdate<c.end_date then '进行中'" +
        		"when sysdate >c.end_date then '已过期' end STATE," +
        		"to_char(c.start_date,'yyyy-mm-dd') || '   '||c.start_time as star_dt,to_char(c.end_date,'yyyy-mm-dd') || '   '|| c.end_time as end_dt " +
        		"from OCRM_F_WP_SCHEDULE c " +
        		"LEFT JOIN OCRM_F_CI_CUST_DESC t on t.cust_id = c.relation_cust " +
        		"LEFT JOIN admin_auth_account t1 on t1.account_name = c.creator where 1=1 ");

        for (String key : getJson().keySet()){
            String value = getJson().get(key).toString();
            if (! "".equals(value)) {
                if("RELATION_CUST".equals(key))
                    builder.append(" and c." + key + " like " + "'%" + value + "%'");
                else if("CREATOR".equals(key))
                    builder.append(" and c." + key + " like " + "'%" + value +"%'");
                else if("START_DATE_FROM".equals(key))
                    builder.append(" and c.START_DATE >= to_date('" + value + "', 'YYYY-MM-DD') ");  
                else if("END_DATE_TO".equals(key))
                    builder.append(" and c.START_DATE <= to_date('" + value + "', 'YYYY-MM-DD') ");    
            }
        }
        builder.append("order by c.schedule_id desc");
      //  setPrimaryKey("c.schedule_id");
        // 设置父类中的信息
        SQL = builder.toString();
        datasource = ds;
    }
    
}
