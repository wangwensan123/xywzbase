package com.xywztech.crm.sec;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class SecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource{
	
	@Autowired
    @Qualifier("dsOracle")
    private DataSource dataSource;
	
	public SecurityMetadataSourceImpl(){}
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
		return configAttributes;
	}

	public Collection<ConfigAttribute> getAttributes(Object url)
			throws IllegalArgumentException {
		Connection conn = null;
		Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
		try {
			conn = dataSource.getConnection();
//			String sql = "SELECT R.ROLE_CODE FROM PIC_ROLE_GRANT G LEFT JOIN ADMIN_AUTH_ROLE R on G.ROLE_ID = R.ID WHERE G.URL='"+((FilterInvocation) url).getRequestUrl()+"'";
//			
//			Statement stsm = conn.createStatement();
//			ResultSet rs = stsm.executeQuery(sql);
//			while(rs.next()){
//				if(null!=rs.getString("ROLE_CODE") && !rs.getString("ROLE_CODE").equals("")){
//					ConfigAttribute configAttribute = new SecurityConfig(rs.getString("ROLE_CODE"));
//					if(!configAttributes.contains(configAttribute)){
//						configAttributes.add(configAttribute);
//					}
//				}
//			}
//			
//			ConfigAttribute configAttribute = new SecurityConfig("ROLE_ADMIN");
//			configAttributes.add(configAttribute);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return configAttributes;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

}
