package com.xywztech.crm.sec.ctxsession;

import com.xywztech.bob.vo.AuthUser;

/**
 * 用户动态设置session级别信息管理器
 * @author wws
 * @date 2013-01-05
 * 
 **/
public interface ICtxSessionManager {
	
	/**增加参数方法*/
	public abstract void addCtxSessionParam (AuthUser authUser);
	
}
