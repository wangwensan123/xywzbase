package com.xywztech.crm.dataauth.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the AUTH_SYS_FILTER_AUTH database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AUTH_SYS_FILTER_AUTH")
public class AuthSysFilterAuth implements Serializable {
	@SuppressWarnings("unused")
    private static final Long serialVersionUID = 1L;


    @Id
    @GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.SEQUENCE)
	private Long id;

    @Temporal( TemporalType.DATE)
	@Column(name="AUTH_DATE")
	private Date authDate;

	@Column(name="FILTER_ID")
	private String filterId;

	@Column(name="ROLE_ID")
	private String roleId;

    public AuthSysFilterAuth() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAuthDate() {
		return this.authDate;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}

	public String getFilterId() {
		return this.filterId;
	}

	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}