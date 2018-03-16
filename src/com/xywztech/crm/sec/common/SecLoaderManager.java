package com.xywztech.crm.sec.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.xywztech.crm.sec.ctxsession.ICtxSessionManager;

/***
 * 认证加载管理器
 * 目前登录时加载session用户自定义参数管理类；
 * 可扩展用于登录时业务逻辑。
 * @author wws
 * @since 2013-01-06
 */ 
public class SecLoaderManager {
    
	/**单例对象*/
    private static SecLoaderManager instance; 
    
    /**日志*/
    private static Logger log = Logger.getLogger(SecLoaderManager.class);
    /**用户自定义参数管理类list*/
    private List<ICtxSessionManager> ctxSessionManager = new ArrayList<ICtxSessionManager>();
    /**saltSource*/
    private SaltSource saltSource = null;
    
    /**saltSource*/
    private PasswordEncoder passwordEncoder = null;
    
    /**构造方法*/
    private SecLoaderManager() {
    	ctxSessionManager = new ArrayList<ICtxSessionManager>();
    }
    /**获取单例对象*/
    public static synchronized SecLoaderManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new SecLoaderManager();
        }
        return instance;
    }
    /**
	 * 初始化方法
	 * @param ApplicationContext
	 */ 
    public void initialize(List<ICtxSessionManager> list) {
    	this.setCtxSessionManager(list);
    }
    
    /**
	 * 取得客户归属参数信息
	 * @param paramName
	 * @return list
	 */
	public List<ICtxSessionManager> getCtxSessionManager(){
		return this.ctxSessionManager;
	}
	/**
	 * 用户自定义参数管理类list
	 * @param list
	 */
	public void setCtxSessionManager(List<ICtxSessionManager> ctxSessionManager){
		if (ctxSessionManager != null) {			
			log.info("开始加载用户自定义参数管理类list……");
			this.ctxSessionManager = ctxSessionManager;
			log.info("完成加载用户自定义参数管理类list，共计：" + ctxSessionManager.size());
		}
	}
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}
	public SaltSource getSaltSource() {
		return saltSource;
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

}
