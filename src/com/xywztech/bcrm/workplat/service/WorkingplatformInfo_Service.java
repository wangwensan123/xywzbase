package com.xywztech.bcrm.workplat.service;



import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bcrm.workplat.model.WorkingplatformInfo;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;
import com.xywztech.bob.vo.AuthUser;

@Service
public class WorkingplatformInfo_Service extends CommonService{
//	
	public WorkingplatformInfo_Service(){
		
		JPABaseDAO<WorkingplatformInfo, Long>  baseDAO=new JPABaseDAO<WorkingplatformInfo, Long>(WorkingplatformInfo.class);  
		   super.setBaseDAO(baseDAO);
	}
	
	public Object save(Object model){
		WorkingplatformInfo workingplatformInfo = (WorkingplatformInfo)model;
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		workingplatformInfo.setMessageSummary(request.getParameter("messageIntroduce"));
		workingplatformInfo.setMessageTitle(request.getParameter("messageTitle"));
		workingplatformInfo.setMessageIntroduce(request.getParameter("messageIntroduce"));
		workingplatformInfo.setPublishUser(request.getParameter("publishUser"));
		workingplatformInfo.setMessageType(request.getParameter("messageType"));
		workingplatformInfo.setProductType(request.getParameter("productType"));
		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//根据Id是否为空判断是新增还是 更改
		if(workingplatformInfo.getMessageId()==null){
			workingplatformInfo.setPublishDate(new Date());
			workingplatformInfo.setPublishUser(auth.getUserId());
			workingplatformInfo.setPublishOrg(auth.getUnitId());
		}else{
			workingplatformInfo.setPublishDate(new Date());
			workingplatformInfo.setPublishUser(auth.getUsername());
			workingplatformInfo.setPublishOrg(auth.getUnitId());
			workingplatformInfo.setMessageId(Long.parseLong(request.getParameter("messageId")));
		}
		return super.save(workingplatformInfo);
		
	}
}
