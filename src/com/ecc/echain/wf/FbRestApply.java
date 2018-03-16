package com.ecc.echain.wf;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.echain.workflow.engine.EVO;
import com.xywztech.crm.constance.JdbcUtil;

public class FbRestApply {

	public void toZhuguan(EVO vo){
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CM_ABSENT_INFO set ABSENT_STAT = '1' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
		

	}

	public void toHangzhang(EVO vo){
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CM_ABSENT_INFO set ABSENT_STAT = '2' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
		

	}

	public void end(EVO vo){
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CM_ABSENT_INFO set ABSENT_STAT = '3' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
		

	}
}
