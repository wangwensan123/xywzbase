package com.xywztech.bob.core;

import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.xywztech.bob.upload.ImportTradeManage;
import com.xywztech.crm.dataauth.managerment.DataAuthManager;
import com.xywztech.crm.dataauth.service.FilterLoader;
import com.xywztech.crm.ehcache.EhCacheManager;

public class StartupServletContextListener implements ServletContextListener{

    private static Logger log = Logger.getLogger(StartupServletContextListener.class);
    
	private FilterLoader filterLoader;
	
	public ApplicationContext ac;
	
    public void contextDestroyed(ServletContextEvent event) {
        log.info("Destorying the CRM context listerner...");
        CorebankingQueue.getInstance().stopQueryThread();
    }

    public void contextInitialized(ServletContextEvent event)  {
    	
//    	if(!LicenseManager.verifyLicense(event.getServletContext().getInitParameter("licenseFile").toString())){
//    		log.info("请联系CRM产品中心获取最新License文件");
//    		throw new NullPointerException();
//    	}
    	ac = ContextLoaderListener.getCurrentWebApplicationContext();
    	
    	filterLoader = (FilterLoader)ac.getBean("filterLoader");
        log.info("Initializing the CRM context listerner...");
        CacheManager cacheManager = new CacheManager();
        EhCacheManager.getInstance().setCacheManager(cacheManager);
		      log.info("Initializing the CRM cacheManager completely.");
        LookupManager.getInstance().initialize(ac);
        FilterManager.getInstance().initialize(ac);
        MenuManager.getInstance().initialize(ac);
        log.info("Data auth filters load completely!");
        //CorebankingQueue.getInstance().startQueryThread();
//        String s_xmlpath="ImportTradeDefine.xml"; 
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        InputStream is=classLoader.getResourceAsStream(s_xmlpath); 
//        ImportTradeManage.getInstance().initialize(is);
//        ExportImportManager.getInstance();
//        log.info("Initializing the CRM data auth filters...");
//        DataAuthManager.getInstance().initialize(filterLoader);
//        log.info("Initializing the CRM CustBelongParams...");
//        CustBelongParamManager.getInstance().initialize(ac);
//        log.info("Initializing the CRM MktActivityParams...");
//        MktActivityParamManager.getInstance().initialize(ac);
//        MktActivityParamManager1.getInstance().initialize(ac);
//        log.info("Data auth filters load completely!");
    }


}
