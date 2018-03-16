package com.ecc.echain.wf;

import com.ecc.echain.workflow.engine.EVO;
import com.xywztech.bcrm.echain.EChainCallbackCommon;


/**
 * @author km 存款业绩分配流程回调类 Dep -- Deposit; Dstr -- Distribution; CB -- CallBack
 * 
 */
public class DepDstrCB extends EChainCallbackCommon {

	/**
	 * 发起流程
	 * @param vo
	 */
	public void start(EVO vo) {
		System.out.println("########################Run start(EVO vo)#####################");
		String instanceId = vo.getInstanceID(); //vo为流程实例对象,存储着流程实例的相关信息
		String type="";//type可以在vo中获取
		//更新申请表的状态为提交绩效管理员
		if(type=="01"){
			SQL ="update PMA_DR_DEP_ACCT_DSTR_APL pa set pa.apply_sts='01' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}else if(type=="02"){
			SQL ="update PMA_DR_LON_ACCT_DSTR_APL pa set pa.apply_sts='01' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}else if(type=="03"){
			SQL ="update PMA_DR_MID_BUSS_DSTR_APL pa set pa.apply_sts='01' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}
	}
	
	/**
	 * 审批通过
	 * @param vo
	 */

	/**
	 * 审批驳回
	 * @param vo
	 */
	public void endReject(EVO vo) {
		System.out.println("########################Run start(EVO vo)#####################");
		String instanceId = vo.getInstanceID(); //vo为流程实例对象,存储着流程实例的相关信息
		String type="";//type可以在vo中获取
		//更新申请表的状态为审批驳回
		if(type=="01"){
			SQL ="update PMA_DR_DEP_ACCT_DSTR_APL pa set pa.apply_sts='00' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}else if(type=="02"){
			SQL ="update PMA_DR_LON_ACCT_DSTR_APL pa set pa.apply_sts='00' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}else if(type=="03"){
			SQL ="update PMA_DR_MID_BUSS_DSTR_APL pa set pa.apply_sts='00' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}
	}

	/**
	 * 审批异常
	 * @param vo
	 */
	public void endException(EVO vo) {
		System.out.println("########################Run start(EVO vo)#####################");
		String instanceId = vo.getInstanceID(); //vo为流程实例对象,存储着流程实例的相关信息
		String type="";//type可以在vo中获取
		//更新申请表的状态为审批异常
		if(type=="01"){
			SQL ="update PMA_DR_DEP_ACCT_DSTR_APL pa set pa.apply_sts='11' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}else if(type=="02"){
			SQL ="update PMA_DR_LON_ACCT_DSTR_APL pa set pa.apply_sts='11' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}else if(type=="03"){
			SQL ="update PMA_DR_MID_BUSS_DSTR_APL pa set pa.apply_sts='11' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}
	}

	/**
	 * 提交支行督导
	 * @param vo
	 */
	public void toZhiHangDudao(EVO vo) {
		System.out.println("########################Run start(EVO vo)#####################");
		String instanceId = vo.getInstanceID(); //vo为流程实例对象,存储着流程实例的相关信息
		String type="";//type可以在vo中获取
		//更新申请表的状态为提交支行督导
		if(type=="01"){
			SQL ="update PMA_DR_DEP_ACCT_DSTR_APL pa set pa.apply_sts='02' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}else if(type=="02"){
			SQL ="update PMA_DR_LON_ACCT_DSTR_APL pa set pa.apply_sts='02' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}else if(type=="03"){
			SQL ="update PMA_DR_MID_BUSS_DSTR_APL pa set pa.apply_sts='02' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}
	}

	/**
	 * 提交总行督导
	 * @param vo
	 */
	public void toZongHangDudao(EVO vo) {
		System.out.println("########################Run start(EVO vo)#####################");
		String instanceId = vo.getInstanceID(); //vo为流程实例对象,存储着流程实例的相关信息
		String type="";//type可以在vo中获取
		//更新申请表的状态为提交总行督导
		if(type=="01"){
			SQL ="update PMA_DR_DEP_ACCT_DSTR_APL pa set pa.apply_sts='03' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}else if(type=="02"){
			SQL ="update PMA_DR_LON_ACCT_DSTR_APL pa set pa.apply_sts='03' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}else if(type=="03"){
			SQL ="update PMA_DR_MID_BUSS_DSTR_APL pa set pa.apply_sts='03' where pa.apply_id='"+instanceId+"'";
			execteSQL();//执行SQL
		}
	}


}
