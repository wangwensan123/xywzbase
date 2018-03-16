package com.ecc.echain.wf;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.echain.workflow.engine.EVO;
import com.xywztech.crm.constance.JdbcUtil;

public class CustManagerP {

	public void end(EVO vo){
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CM_CUST_MANAG_EVA_MGR set STATUS = '4' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
		

	}
	
public void toDudao(EVO vo){
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CM_CUST_MANAG_EVA_MGR set STATUS = '2' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
		

	}	


public void toZhuguan(EVO vo){
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CM_CUST_MANAG_EVA_MGR set STATUS = '0' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
		

	}

public void backToMgr(EVO vo){
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CM_CUST_MANAG_EVA_MGR set STATUS = '1' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
	

}	

public void back(EVO vo){
	String nodeString = vo.getNodeID();
	if (nodeString.equals("12_a4")) { //当前节点是主管评价，即退回给客户经理评估
		backToMgr(vo);
	}else if (nodeString.equals("12_a5")) { //当前节点是督导查看，即退回给主管评价
		backToZhuguan(vo);
	}


}

public void backToZhuguan(EVO vo){
	
	Connection conn = null;
	Statement stmt = null;
	try {
		conn = JdbcUtil.getConnection();
		stmt = conn.createStatement();
		String instanceid = vo.getInstanceID();
		String sql = " update OCRM_F_CM_CUST_MANAG_EVA_MGR set STATUS = '3' where id = "+instanceid+" ";
		stmt.executeUpdate(sql);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally{
		JdbcUtil.close(null, stmt, conn);
	}
	

}
}
