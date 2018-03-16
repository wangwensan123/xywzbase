package com.xywztech.crm.constance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xywztech.crm.exception.BipExceptionController;

public final class JdbcUtil {
	private static String driver;
	private static String url;
	private static String user;
	private static String pwd;
	private static Logger log = Logger.getLogger(BipExceptionController.class);
	static {
		Properties props = new Properties();
		try {
			props.load(JdbcUtil.class.getClassLoader().getResourceAsStream(
					"bip.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (props != null) {
			driver = props.getProperty("jdbc.driverClassName");
			url = props.getProperty("jdbc.url");
			user = props.getProperty("jdbc.username");
			pwd = props.getProperty("jdbc.password");
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				log.error("无法加载驱动");
			}
		}
	}

	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (SQLException e) {
			log.error("数据库连接失败"+e);
		}
		return conn;
	}

	public static void close(ResultSet rs, Statement ps, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.error("ResultSet关闭错误"+e);
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				log.error("Statement关闭错误"+e);
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error("Connection关闭错误"+ e);
			}
		}
	}
}
