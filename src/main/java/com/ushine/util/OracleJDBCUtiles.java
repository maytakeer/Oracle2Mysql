package com.ushine.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 连接oracle数据库工具类
 * @author Administrator
 *
 */


public class OracleJDBCUtiles  { 
	
	// 获取jdbc的连接
		public static Connection getConnections() {
			Connection conn = null;
			String driverClass = null;
			String jdbcUrl = null;
			String user = null;
			String password = null;
			try {
				InputStream is = MySQLJDBCUtiles.class.getClassLoader()
						.getResourceAsStream("jdbc.properties");
				Properties properties = new Properties();
				properties.load(is);
				driverClass = properties.getProperty("oracle.driver");
				jdbcUrl = properties.getProperty("oracle.url");
				user = properties.getProperty("oracle.username");
				password = properties.getProperty("oracle.password");
				Class.forName(driverClass);
				conn = DriverManager.getConnection(jdbcUrl, user, password);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return conn;
		}

		// 关闭jdbc的链接
		/**
		 * 关闭statement和connection
		 * 
		 * @param ps
		 * @param conn
		 */
		public static void release(PreparedStatement ps, Connection conn) {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		public static void release(ResultSet result, PreparedStatement ps,
				Connection conn) {
			try {
				if (result != null) {
					result.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		}
}
