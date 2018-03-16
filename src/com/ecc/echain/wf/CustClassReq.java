package com.ecc.echain.wf;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.echain.workflow.engine.EVO;
import com.xywztech.crm.constance.JdbcUtil;

public class CustClassReq {

	public void endReject(EVO vo){//驳回申请结束
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CI_GRADE_APPLY set STATUS = '结束-申请被驳回' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
		

	}
	
public void endPass(EVO vo){//通过申请结束
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CI_GRADE_APPLY set STATUS = '结束-申请通过' where id = "+instanceid+" ";
			String sql2 = " update OCRM_F_CI_CUST_DESC a set a.CUST_LEV = (select b.TO_GRADE from " +
					"OCRM_F_CI_GRADE_APPLY b where b.id = "+instanceid+" ) " +
							"where a.cust_id = (select b.cust_id from " +
					"OCRM_F_CI_GRADE_APPLY b where b.id = "+instanceid+" )";
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
		

	}	


public void toHangzhang(EVO vo){//提交支行行长审批
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CI_GRADE_APPLY set STATUS = '行长审批' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
		

	}


public void toZonghang(EVO vo){//提交总行零售部审查人
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CI_GRADE_APPLY set STATUS = '总行零售部审批' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
		

	}

public void backToMgr(EVO vo){//退回客户经理
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String instanceid = vo.getInstanceID();
			String sql = " update OCRM_F_CI_GRADE_APPLY set STATUS = '支行行长退回客户经理' where id = "+instanceid+" ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcUtil.close(null, stmt, conn);
		}
	

}	

public void back(EVO vo){//退回操作
	String nodeString = vo.getNodeID();
	if (nodeString.equals("13_a4")) { //当前节点是支行行长，即退回给客户经理
		backToMgr(vo);
	}else if (nodeString.equals("13_a5")) { //当前节点是总行零售部审查人，即退回给支行行长
		backToHangzhang(vo);
	}


}

public void backToHangzhang(EVO vo){
	
	Connection conn = null;
	Statement stmt = null;
	try {
		conn = JdbcUtil.getConnection();
		stmt = conn.createStatement();
		String instanceid = vo.getInstanceID();
		String sql = " update OCRM_F_CI_GRADE_APPLY set STATUS = '总行退回支行行长' where id = "+instanceid+" ";
		stmt.executeUpdate(sql);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally{
		JdbcUtil.close(null, stmt, conn);
	}
	

}
}
