package com.xywztech.bcrm.system.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.xywztech.bob.action.BaseQueryAction;

/**
 * 模块功能查询
 * @author wz
 * @since 2012-11-16
 */

@ParentPackage("json-default")
@Action(value = "/functionQueryAction", results = { @Result(name = "success", type = "json") })

public class FunctionQueryAction extends BaseQueryAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源

	/**
	 * 创建查询SQL
	 */
	public void prepare() {
		
		StringBuilder querySql = new StringBuilder();
		querySql.append("select f.id, f.func_name ");
		querySql.append("from fw_function f ");
		querySql.append("where 1=1 ");
		
		SQL = querySql.toString(); //为父类SQL属性赋值（设置查询SQL）
		
		setPrimaryKey("f.id"); //设置查询排序条件
		
		datasource = ds; //为父类数据源赋值
	}
}
