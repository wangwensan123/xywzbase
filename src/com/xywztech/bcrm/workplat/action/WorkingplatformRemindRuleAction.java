package com.xywztech.bcrm.workplat.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.xywztech.bcrm.workplat.model.WorkingplatformRemindRule;
import com.xywztech.bcrm.workplat.service.WorkingplatformRemindRuleService;
import com.xywztech.bob.vo.AuthUser;

/**
 * @author km
 * 
 */
@SuppressWarnings("serial")
@Action("/workplatremindrule")
@Results({ @Result(name = "success", type = "redirectAction", params = {
        "actionName", "workplatremindrule" }) })
public class WorkingplatformRemindRuleAction extends ValidationAwareSupport
        implements ModelDriven<Object>, Validateable {

    private WorkingplatformRemindRule wrr = new WorkingplatformRemindRule();
    private Collection<WorkingplatformRemindRule> wrrc;
    private Long id;

    @Autowired
    private WorkingplatformRemindRuleService wrrs;

    /**
     * 单条数据展示. HTTP:GET方法 URL:/actionName/$id;
     */
    public HttpHeaders show() {
        wrr = wrrs.find(id);
        return new DefaultHttpHeaders("show");
    }

    /**
     * 数据列表查询包括全部数据，或者待条件查询。 HTTP:GET方法 URL:/actionName；
     */
    public HttpHeaders index() {
        // wrrc = wrrs.findAll();
        return new DefaultHttpHeaders("index").disableCaching();
    }

    /**
     * 请求数据编辑页面跳转。 HTTP:GET方法 URL:/actionName/$id/edit;
     */
    public String edit() {
        return "edit";
    }

    /**
     * 新增页面请求 HTTP:GET方法 URL:/actionName/new
     */
    public String editNew() {
        return "editNew";
    }

    /**
     * 请求删除页面 HTTP:GET方法 URL:/actionName/$id/deleteContirm
     */
    public String deleteConfirm() {
        return "deleteConfirm";
    }

    /**
     * 数据删除提交 HTTP:DELETE方法 URL:/actionName/$id
     */
    public String destroy() {
        wrrs.remove(id);
        return "success";
    }

    // 获取系统当前时间
    public Date getCurrentDate() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD");
        String date = format.format(new java.util.Date()).toString();
        return (Date) format.parse(date);

    }

    /**
     * 数据新增提交 HTTP:POST方法 URL:/actionName
     */
    public HttpHeaders create() {
        AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String currenUserId = auth.getUserId();
        // String currenUserName = auth.getUsername();
        // try {
        // Date date = getCurrentDate();
        // wrr.setCREATE_TIME(date);
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        wrr.setCREATOR(currenUserId);
        wrr.setCREATE_ORG(auth.getUnitId());
        wrrs.save(wrr);
        return new DefaultHttpHeaders("success")
                .setLocationId(wrr.getRULE_ID());
    }

    /**
     * 数据修改提交 HTTP:PUT方法 URL:/WorkPlatNotice/$id
     */
    public String update() {
        wrrs.save(wrr);
        return "success";
    }

    public HttpHeaders initRule() {
        AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String currenUserId = auth.getUserId();
        int isCustMgr = 0;
        @SuppressWarnings("rawtypes")
        List list = (List) auth.getAuthorities();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toString().equals("2")) {
                isCustMgr = 1;
            }
            
        }
        BigDecimal changeAmt = new BigDecimal(1000);
        wrr.setCREATOR(currenUserId);
        wrr.setCREATE_ORG(auth.getUnitId());
        wrr.setIS_CUST_MGR(isCustMgr+"");
        for (int i = 1; i < 11; i++) {
        	if(i<10){
        		wrr.setSECTION_TYPE("100000000" + i);
        	}else{
        		wrr.setSECTION_TYPE("10000000" + i);
        	}
            wrr.setRULE_NAME("0");
            wrr.setRULE_ID(null);
            if (i == 5) {
                wrr.setCHANGE_AMOUNT(changeAmt);
                    wrr.setBEFOREHEAD_DAY(15);
            } else {
                wrr.setBEFOREHEAD_DAY(15);
                wrr.setCHANGE_AMOUNT(null);
            }
            wrrs.save(wrr);
        }

        return new DefaultHttpHeaders("success")
                .setLocationId(wrr.getRULE_ID());
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
     * 
     * @param id
     */
    public void setId(Long id) {
        if (id != null) {
            this.wrr = wrrs.find(id);
        }
        this.id = id;
    }

    /**
     * 
     */
    public Object getModel() {
        return (wrrc != null ? wrrc : wrr);
    }

}
