package com.xywztech.crm.dataauth.managerment;

import java.util.ArrayList;
import java.util.List;

import com.xywztech.bob.vo.AuthUser;
import com.xywztech.crm.dataauth.model.AuthSysFilter;
import com.xywztech.crm.dataauth.model.AuthSysFilterAuth;
import com.xywztech.crm.dataauth.service.FilterLoader;

public class DataAuthManager {
	
	private static DataAuthManager instance;
	
	private List<AuthSysFilter> filterList = new ArrayList<AuthSysFilter>();
	
	public static synchronized DataAuthManager getInstance(){
		if (instance != null) {
            return instance;
        } else {
            instance = new DataAuthManager();
        }
        return instance;
	}
	
	public void initialize(FilterLoader filterLoader){
		filterList = filterLoader.LoadFilters();
	}
	
	/**
	 * 通过用户信息获取该用的所有的数据权限信息
	 * @param ia
	 * @return
	 */
	/**
	 * 通过用户信息获取该用的所有的数据权限信息
	 * @param ia
	 * @return
	 */
	public List<AuthSysFilter> getDataAuthInfo(AuthUser ia){
		//List<AuthSysFilterAuth> filterAuthList = dataAuthInfo.LoadAuthInfo(ia);
		List<AuthSysFilter> tmp = new ArrayList<AuthSysFilter>();
		for(AuthSysFilterAuth filterAuth: ia.getAuthInfos()){
			AuthSysFilter tmpFilter = findFilterById(filterAuth.getFilterId());
			if(null!=tmpFilter){
				tmp.add(tmpFilter);
			}
		}
		return tmp;
	}
	
	private AuthSysFilter findFilterById(String id){
		for(AuthSysFilter tmpFilter : filterList){
			if(tmpFilter.getId().toString().equals(id)){
				return tmpFilter;
			}
		}
		return null;
	}
	
}
