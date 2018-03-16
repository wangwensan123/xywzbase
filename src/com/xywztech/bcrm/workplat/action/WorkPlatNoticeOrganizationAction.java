package com.xywztech.bcrm.workplat.action;

import java.util.Collection;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.xywztech.bcrm.workplat.model.WorkingplatformNoticeOrganizer;
import com.xywztech.bcrm.workplat.service.NoticeOrganizetionService;

/**
 * @describe 公告接收机构
 * @author WillJoe
 *
 */
@SuppressWarnings("serial")
@Action("/workplatnoticeorg")
@Results({
    @Result(name="success", type="redirectAction", params = {"actionName" , "workplatnoticeorg"})
})
public class WorkPlatNoticeOrganizationAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable{
	
	private WorkingplatformNoticeOrganizer wno = new WorkingplatformNoticeOrganizer();
	private Collection<WorkingplatformNoticeOrganizer> wnoc;
	private Long id;
	
    @Autowired
    private NoticeOrganizetionService nos;
	
    /**
     * 单条数据展示.
     * HTTP:GET方法
     * URL:/actionName/$id;
     */
    public HttpHeaders show() {
    	wno = nos.find(id);
    	
    	/**
    	 * setUserId,this needs session;
    	 * TODO String userId = "";
    	 */    	
        return new DefaultHttpHeaders("show");
    }

    /**
     * 数据列表查询包括全部数据，或者待条件查询。
     *  HTTP:GET方法 
     *  URL:/actionName；
     */
    public HttpHeaders index() {
    	wnoc = nos.findAll();
        return new DefaultHttpHeaders("index")
            .disableCaching();
    }
    
    /**
     * 请求数据编辑页面跳转。
     * HTTP:GET方法
     * URL:/actionName/$id/edit;
     */
    public String edit() {
        return "edit";
    }

    /**
     * 新增页面请求
     * HTTP:GET方法
     * URL:/actionName/new
     */
    public String editNew() {
        return "editNew";
    }

    /**
     * 请求删除页面
     * HTTP:GET方法
     * URL:/actionName/$id/deleteContirm
     */
    public String deleteConfirm() {
        return "deleteConfirm";
    }

    /**
     * 数据删除提交
     * HTTP:DELETE方法
     * URL:/actionName/$id
     */
    public String destroy() {
    	nos.remove(id);
        return "success";
    }

    /**
     * 数据新增提交
     * HTTP:POST方法
     * URL:/actionName
     */
    public HttpHeaders create() {
    	nos.save(wno);
        return new DefaultHttpHeaders("success")
            .setLocationId(wno.getNoticeId());
    }

    /**
     * 数据修改提交
     * HTTP:PUT方法
     * URL:/WorkPlatNotice/$id
     */
    public String update() {
       	nos.save(wno);
        return "success";
    }

    /**
     * 数据验证方法
     */
    public void validate() {
    	/**
    	 * TODO validate bussness logic.
    	 */
    }

    /**
     * ID参数获取方法
     * @param id
     */
    public void setId(Long id) {
        if (id != null) {
            this.wno = nos.find(id);
        }
        this.id = id;
    }
    
    /**
     * 
     */
    public Object getModel() {
        return (wnoc != null ? wnoc : wno);
    }
    

}
