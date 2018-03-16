package com.xywztech.bcrm.workplat.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xywztech.bcrm.workplat.model.WorkingplatformRemindRule;

/**
 * 提醒规则服务
 * 
 * @author WillJoe
 * 
 */
@Service
@Transactional(value="postgreTransactionManager")
public class WorkingplatformRemindRuleService {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     * 保存：包括新增和修改
     * 
     * @param wrr
     */
    public void save(WorkingplatformRemindRule wrr) {
        if (wrr.getRULE_ID() == null) {
            em.persist(wrr);
        } else{
            wrr.setIS_CUST_MGR(em.find(WorkingplatformRemindRule.class, wrr.getRULE_ID()).getIS_CUST_MGR());
            em.merge(wrr);
        }
    }

    /**
     * 移除记录
     * 
     * @param id
     */
    public void remove(Long id) {
        em.remove(em.find(WorkingplatformRemindRule.class, id));
    }

    /**
     * 查看记录
     * 
     * @param id
     * @return
     */
    public WorkingplatformRemindRule find(Long id) {
        return em.find(WorkingplatformRemindRule.class, id);
    }

    // /**
    // * 查看所有记录
    // * @return
    // */
    // @SuppressWarnings("unchecked")
    // public List<WorkingplatformRemindRule> findAll(){
    // AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
    // .getAuthentication().getPrincipal();
    // String currenUserId = auth.getUserId();
    // String wrrFindAll =
    // "select wrr from WorkingplatformRemindRule wrr where wrr.creator = '"+currenUserId+"' ";
    // Query wrrQuery = em.createQuery(wrrFindAll);
    // return wrrQuery.getResultList();
    // }
}
