package com.xywztech.crm.sec.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.xywztech.bcrm.common.service.SecGrantService;
import com.xywztech.bob.vo.AuthUser;
import com.xywztech.crm.constance.OperateTypeConstant;
/**
 * 登出成功处理类
 * @author wws
 * @date 2012-11-12
 * */
public class SecLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	
	@Autowired
	private SecGrantService secGrantService;
	
	public SecLogoutSuccessHandler()
    {
    }
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  

	private String defaultLogoutUrl; 

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException
    {
        /**登出成功更新日志信息*/
    	doLogoutSuccess(authentication);
      if (authentication != null) {
        AuthUser auth = (AuthUser) authentication.getPrincipal();
                /**判断用户是手机端还是web角色*/
            boolean flag = false; 
            List roles = auth.getRolesInfo();
            for( int i=0;i<roles.size();i++ ){
              Map role = (Map)auth.getRolesInfo().get(i);
              String roleCode = role.get("ROLE_CODE").toString();
              if (roleCode.equals("other")){
                flag = true; 
                break;
                          }
              
                      }
            defaultLogoutUrl="/index.html";
                }
        redirectStrategy.sendRedirect(request, response, defaultLogoutUrl);
    }
    
    /**登出成功业务处理*/
	public void doLogoutSuccess(Authentication authentication) {
		//是否正常退出
		if (authentication != null) {
			 AuthUser auth = (AuthUser) authentication.getPrincipal();
			 secGrantService.addLoginLogInfo(auth, OperateTypeConstant.LOGOUT_SYS);
		}
	}

	public String getDefaultLogoutUrl() {
		return defaultLogoutUrl;
	}

	public void setDefaultLogoutUrl(String defaultLogoutUrl) {
		this.defaultLogoutUrl = defaultLogoutUrl;
	}

}
