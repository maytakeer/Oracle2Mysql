package com.ushine.project;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ForkJoinPool.ManagedBlocker;

import com.ushine.util.MySQLJDBCUtiles;
import com.ushine.util.ShowProperties;

public class CreateTable {
	public void createTables(){
		Connection conn = null;
//		PreparedStatement ps = null;
		Statement stmt = null;
		Statement stmt2 = null;
		conn = MySQLJDBCUtiles.getConnections();
		Properties properties = new Properties();
		InputStream in = CreateTable.class.getClassLoader().getResourceAsStream("table.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ShowProperties sp = new ShowProperties();
		List<String> key = sp.showKey(properties);
		for (int i = 0; i < key.size(); i++) {
			 String sql =  "DROP TABLE IF EXISTS "+key.get(i);
			 System.out.println(sql);
			 try {
				stmt2 = conn.createStatement();
				stmt2.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		if (stmt2!=null) {
			try {
				stmt2.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Enumeration<Object> enu = properties.elements();	
		while (enu.hasMoreElements()) {
			Object value = enu.nextElement();
			 try {
				
				 stmt =  conn.createStatement();
				stmt.executeUpdate(value.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
		MySQLJDBCUtiles.release(stmt, conn);
	}
	public static void main(String[] args) {
		new CreateTable().createTables();
	}
}
