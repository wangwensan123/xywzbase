package com.xywztech.bcrm.workplat.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the OCRM_F_WP_REMIND_RULE database table.
 * 
 * FDM(基础数据层)，归属于工作平台的第二主题，数据为自动提醒规则信息
 */
@Entity
@Table(name = "OCRM_F_WP_REMIND_RULE")
public class WorkingplatformRemindRule implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3493436177693214236L;

    /** 规则id */
    @Id
    @GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.SEQUENCE)
    @Column(name = "RULE_ID", nullable = false)
    private Long RULE_ID;

    /** 提前天数 */
    @Column(name = "BEFOREHEAD_DAY")
    private Integer BEFOREHEAD_DAY;

    /** 创建机构id */
    @Column(name = "CREATE_ORG", length = 32)
    private String CREATE_ORG;

    /** 创建机构名称 */
    @Column(name = "CREATE_ORG_NAME", length = 100)
    private String CREATE_ORG_NAME;

    /** 生效开始时间 */
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_TIME")
    private Date CREATE_TIME;

    /** 创建人id */
    @Column(length = 30)
    private String CREATOR;

    /** 变动余额 */
    @Column(name = "CHANGE_AMOUNT", precision = 24, scale = 6)
    private BigDecimal CHANGE_AMOUNT;

    /** 创建人名称 */
    @Column(name = "CREATOR_NAME", length = 200)
    private String CREATOR_NAME;

    /** 提醒频率-默认：每天 */
    @Column(name = "CYCLE_TIME")
    private Long CYCLE_TIME;

    /** 提醒方式 */
    @Column(name = "REMIND_MODE", length = 20)
    private String REMIND_MODE;

    /** 生效截止时间 */
    @Temporal(TemporalType.DATE)
    @Column(name = "REMIND_TIME")
    private Date REMIND_TIME;

    /** 提醒类别 */
    @Column(name = "REMIND_TYPE", length = 20)
    private String REMIND_TYPE;

    /** 规则名称 --启用停用状态*/
    @Column(name = "RULE_NAME", length = 100)
    private String RULE_NAME;

    /** 功能类型 */
    @Column(name = "SECTION_TYPE", length = 20)
    private String SECTION_TYPE;

    /** 阀值 */
    @Column(name = "THRESHHOLD", precision = 24, scale = 6)
    private BigDecimal THRESHHOLD;
    
    /** 是否客户经理 */
    @Column(length=20)
    private String IS_CUST_MGR;

    public String getIS_CUST_MGR() {
        return IS_CUST_MGR;
    }

    public void setIS_CUST_MGR(String iS_CUST_MGR) {
        IS_CUST_MGR = iS_CUST_MGR;
    }

    public Long getRULE_ID() {
        return RULE_ID;
    }

    public void setRULE_ID(Long rULE_ID) {
        RULE_ID = rULE_ID;
    }

    public Integer getBEFOREHEAD_DAY() {
        return BEFOREHEAD_DAY;
    }

    public void setBEFOREHEAD_DAY(Integer bEFOREHEAD_DAY) {
        BEFOREHEAD_DAY = bEFOREHEAD_DAY;
    }

    public String getCREATE_ORG() {
        return CREATE_ORG;
    }

    public void setCREATE_ORG(String cREATE_ORG) {
        CREATE_ORG = cREATE_ORG;
    }

    public String getCREATE_ORG_NAME() {
        return CREATE_ORG_NAME;
    }

    public void setCREATE_ORG_NAME(String cREATE_ORG_NAME) {
        CREATE_ORG_NAME = cREATE_ORG_NAME;
    }

    public Date getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(Date cREATE_TIME) {
        CREATE_TIME = cREATE_TIME;
    }

    public String getCREATOR() {
        return CREATOR;
    }

    public void setCREATOR(String cREATOR) {
        CREATOR = cREATOR;
    }

    public BigDecimal getCHANGE_AMOUNT() {
        return CHANGE_AMOUNT;
    }

    public void setCHANGE_AMOUNT(BigDecimal cHANGE_AMOUNT) {
        CHANGE_AMOUNT = cHANGE_AMOUNT;
    }

    public String getCREATOR_NAME() {
        return CREATOR_NAME;
    }

    public void setCREATOR_NAME(String cREATOR_NAME) {
        CREATOR_NAME = cREATOR_NAME;
    }

    public Long getCYCLE_TIME() {
        return CYCLE_TIME;
    }

    public void setCYCLE_TIME(Long cYCLE_TIME) {
        CYCLE_TIME = cYCLE_TIME;
    }

    public String getREMIND_MODE() {
        return REMIND_MODE;
    }

    public void setREMIND_MODE(String rEMIND_MODE) {
        REMIND_MODE = rEMIND_MODE;
    }

    public Date getREMIND_TIME() {
        return REMIND_TIME;
    }

    public void setREMIND_TIME(Date rEMIND_TIME) {
        REMIND_TIME = rEMIND_TIME;
    }

    public String getREMIND_TYPE() {
        return REMIND_TYPE;
    }

    public void setREMIND_TYPE(String rEMIND_TYPE) {
        REMIND_TYPE = rEMIND_TYPE;
    }

    public String getRULE_NAME() {
        return RULE_NAME;
    }

    public void setRULE_NAME(String rULE_NAME) {
        RULE_NAME = rULE_NAME;
    }

    public String getSECTION_TYPE() {
        return SECTION_TYPE;
    }

    public void setSECTION_TYPE(String sECTION_TYPE) {
        SECTION_TYPE = sECTION_TYPE;
    }

    public BigDecimal getTHRESHHOLD() {
        return THRESHHOLD;
    }

    public void setTHRESHHOLD(BigDecimal tHRESHHOLD) {
        THRESHHOLD = tHRESHHOLD;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}