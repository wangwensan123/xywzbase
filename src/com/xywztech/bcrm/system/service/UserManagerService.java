package com.xywztech.bcrm.system.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.AdminAuthAccount;
import com.xywztech.bcrm.system.model.AdminAuthPasswordLog;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;
import com.xywztech.crm.constance.EndecryptUtils;
import com.xywztech.crm.constance.SystemConstance;

/**
 * 用户管理Service
 * @author wangwan
 * @since 2012-10-08 
 */
@Service
public class UserManagerService extends CommonService {
   
	public UserManagerService(){
		JPABaseDAO<AdminAuthAccount, Long>  baseDAO = new JPABaseDAO<AdminAuthAccount, Long>(AdminAuthAccount.class);  
		super.setBaseDAO(baseDAO);
	}


   /*
    * 保存用户基本信息方法
    * @return
    * @throws Exception
    */
	public AdminAuthAccount saveBaseInfo( String accountName, String email, String mobilephone,
			String officetel, String orgId,String userState, String password, String sex,
			String userName, String birthday, String deadline,String offenIP,String updateUser,String dirId) throws ParseException {
		if(!(password.equals(""))){//若已为新增用户设置密码，则将新密码保存至密码修改历史表
			String passwordEncode=EndecryptUtils.encrypt(password);
			AdminAuthPasswordLog ws = new AdminAuthPasswordLog();//保存密码至历史密码记录表
			ws.setUpdateUser(updateUser);
			ws.setUserId(accountName);
			ws.setPswdUped(passwordEncode);
			ws.setPswdUpTime(new Date(System.currentTimeMillis()));
			em.persist(ws);
		
			AdminAuthAccount wm = new AdminAuthAccount();
			wm.setAccountName(accountName);
			wm.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
			if(!(birthday.equals(""))){//若传入生日日期不为空，则进行日期格式强制转换
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date birthdayDate = (Date) format.parse(birthday);
				wm.setBirthday(birthdayDate);
			}
		
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//日期格式强制转换
			Date deadlineDate = (Date) format.parse(deadline);
			wm.setDeadline(deadlineDate);
		
			wm.setPassword(passwordEncode);
			wm.setEmail(email);
			wm.setMobilephone(mobilephone);
			wm.setOffenIP(offenIP);
			wm.setOfficetel(officetel);
			wm.setOrgId(orgId);
			wm.setUserState(userState);
			wm.setSex(sex);
			wm.setUserName(userName);
			wm.setDirId(dirId);
		
			em.persist(wm);
			return wm;
		}else{
			AdminAuthAccount wm = new AdminAuthAccount();
			wm.setAccountName(accountName);
			wm.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
			wm.setEmail(email);
			wm.setMobilephone(mobilephone);
			wm.setOffenIP(offenIP);
			wm.setOfficetel(officetel);
			wm.setOrgId(orgId);
			wm.setUserState(userState);
			wm.setSex(sex);
			wm.setUserName(userName);
			if(!(birthday.equals(""))){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date birthdayDate = (Date) format.parse(birthday);
				wm.setBirthday(birthdayDate);
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//日期格式强制转换
			Date deadlineDate = (Date) format.parse(deadline);
			wm.setDeadline(deadlineDate);
			em.persist(wm);
			return wm;
		}
	}
}
