package com.xywztech.crm.sec.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bcrm.common.service.SecGrantService;
import com.xywztech.bob.core.UserOnlineManager;
import com.xywztech.bob.vo.AuthUser;
import com.xywztech.crm.constance.OperateTypeConstant;
/**
 * 登录成功处理类
 * @author wws
 * @date 2012-11-08
 * */
public class SecAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private SecGrantService secGrantService;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  /**默认跳转策略类*/

	private String defaultSuccessUrl;  /**登录成功跳转URL*/
	
	private String defaultFailureUrl;  /**登录失败跳转URL*/
	
	public SecAuthenticationSuccessHandler() {
		 
	}
	/**登录成功处理方法*/
	@SuppressWarnings("unchecked")
	public void onAuthenticationSuccess(HttpServletRequest request,  
	            HttpServletResponse response, Authentication authentication)  
	            throws ServletException, IOException { 
		HttpSession session = request.getSession();  
		AuthUser auth = (AuthUser) authentication.getPrincipal();
  Map forbiddenUserMap = (Map) session.getAttribute("forbiddenUserMap"); 
        /**用户密码连续错误达到上限map是否包含当前用户*/
    boolean isIncludFlag = false;
    if (forbiddenUserMap != null) {
      	if (auth.getUserId().equals(forbiddenUserMap.get(auth.getUserId()))) {
      		isIncludFlag = true;
          	    }
        } 
        /**判断用户是手机端还是web角色*/
//    boolean flag = false; 
//    List roles = auth.getRolesInfo();
//    for( int i=0;i<roles.size();i++ ){
//      Map role = (Map)auth.getRolesInfo().get(i);
//      String roleCode = role.get("ROLE_CODE").toString();
//      if (roleCode.equals("other")){
//        flag = true; 
//        break;
//                  }
//      
//              }
        /**是否禁止用户登录*/
    if (isIncludFlag) {
//      if(flag){
//        redirectStrategy.sendRedirect(request, response, "/index.html");;
//      }else{
        redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
//              }
    } else {
      doLoginSuccess(request);

//      if(flag){
//        redirectStrategy.sendRedirect(request, response, "/webadmin/admin-index.html");;
//      }else{
        redirectStrategy.sendRedirect(request, response,defaultSuccessUrl);
//              }
      }
		
	}
	public String getDefaultSuccessUrl() {
		return defaultSuccessUrl;
	}
	public void setDefaultSuccessUrl(String defaultSuccessUrl) {
		this.defaultSuccessUrl = defaultSuccessUrl;
	} 
	/**登录成功业务处理*/
	public void doLoginSuccess(HttpServletRequest request) {
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		/**更新登录时间*/
		secGrantService.refreshLoginTime(auth);
		/**登录成功更新日志信息*/
		secGrantService.addLoginLogInfo(auth, OperateTypeConstant.LOGIN_SYS);
//		/***登录成功更新在线用户信息*/
//        ServletContext servletContext =  request.getSession().getServletContext();
//        Cache userOnlineCache = (Cache) servletContext.getAttribute(UserOnlineManager.cacheName);
//        Element element = userOnlineCache.get(UserOnlineManager.cacheKey);
//        List<AuthUser> list = new ArrayList();
//        
//        if (element == null) {
//        	element = new Element(UserOnlineManager.cacheKey, list);
//        } else {
//			list = (List<AuthUser>)element.getObjectValue();
//        }
//        list.add((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        userOnlineCache.put(element);
//        servletContext.setAttribute(UserOnlineManager.cacheName, userOnlineCache);
	}
	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}
	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

}
