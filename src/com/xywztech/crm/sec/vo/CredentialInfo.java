package com.xywztech.crm.sec.vo;
/**
 * 认证策略信息类
 * @author wws
 * @date 2012-11-08
 * 
 **/
public class CredentialInfo {
	
	/**信息类型*/
	private String infoType;
	/**信息内容*/
	private String message;
	/**跳转URL*/
	private String redirectURL;

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}
	
	
}
