package com.xywztech.bcrm.echain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.xywztech.crm.constance.JdbcUtil;

public class EChainCallbackCommon {

	protected String SQL;

	public String execteSQL() {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			stmt.execute(SQL);
			System.out.println("执行SQL:"+SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, stmt, conn);
		}
		return "success";
	}

}
