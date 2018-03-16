package com.xywztech.crm.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * CRM SESSION超时过滤器，当SESSION超时之后，将由此过滤器返回一个异常编码：600.
 * @author wws
 */
public class CrmSessionFilter implements Filter {

	public void destroy() {}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		//HttpSession session = request.getSession();
		if(!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String))
			arg2.doFilter(request, response);
		else
			response.sendError(600,"session out");
	}
	public void init(FilterConfig arg0) throws ServletException {}

}
