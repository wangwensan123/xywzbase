package com.xywztech.crm.ehcache;

import net.sf.ehcache.CacheManager;
/***
 * 缓存管理器接口
 * @author wws
 * @since 2013-03-07
 */
public interface IEhCacheManager {	 
    public static String CRMCacheName = "crmCache";
	public void setCacheManager(CacheManager cacheManager);
	
    public CacheManager getCacheManager();
	
}
