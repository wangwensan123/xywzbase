package com.xywztech.bcrm.system.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.xywztech.bcrm.system.model.FwFunction;
import com.xywztech.bcrm.system.service.FwFunctionManagerService;
import com.xywztech.bob.common.CommonAction;

/**
 * 功能点管理
 * @author GUOCHI
 * @since 2012-10-09
 */
@SuppressWarnings("serial")
@Action("/Function-action")
public class FwFunctionManagerAction extends CommonAction{

	
	private FwFunction wi = new FwFunction();
	private Collection<FwFunction> wic;
	private HttpServletRequest request;
	private Long id;
	
    @Autowired
    private FwFunctionManagerService wis;
    @Autowired
	public void init() {//初始化module
		model = new FwFunction();
		setCommonService(wis);
		// 新增修改删除记录是否记录日志,默认为false，不记录日志
		needLog = false;;
	}
	
    /**
     * 单条数据展示.
     * HTTP:GET方法
     * URL:/actionName/$id;
     */
//    public HttpHeaders show() {
//    	wi = wis.find(id);    	
//        return new DefaultHttpHeaders("show");
//    }

    /**
     * 数据列表查询包括全部数据，或者待条件查询。
     *  HTTP:GET方法 
     *  URL:/actionName；
     */
//    public HttpHeaders index() {
//    	wic = wis.findAll();
//        return new DefaultHttpHeaders("index")
//            .disableCaching();
//    }
    
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
    	
    	 ActionContext ctx = ActionContext.getContext();
         request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
         String idStr = request.getParameter("idStr"); //获取需要删除的记录id
         wis.remove(Long.valueOf(idStr));
         return "success";
    	
    }

    /**
     * 数据新增提交
     * HTTP:POST方法
     * URL:/actionName
     */
    public DefaultHttpHeaders create() {
    	wis.save(wi);
        return new DefaultHttpHeaders("success")
            .setLocationId(wi.getId());
    }

    /**
     * 数据修改提交
     * HTTP:PUT方法
     * URL:/WorkPlatNotice/$id
     */
    public String update() {
       	wis.save(wi);
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
            this.wi = (FwFunction) wis.find(id);
        }
        this.id = id;
    }
    
    /**
     * 
     */
    public Object getModel() {
        return (wic != null ? wic : wi);
    }
}
