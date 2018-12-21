package com.ushine.project;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import com.ushine.util.MySQLJDBCUtiles;
import com.ushine.util.OracleJDBCUtiles;
import com.ushine.util.ShowProperties;

public class Perform {
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		//生成表格
		new CreateTable().createTables();
		ShowProperties sp = new ShowProperties();
		Properties properties = new Properties();
		InputStream is = Perform.class.getClassLoader().getResourceAsStream("table.properties");
		try {
			properties.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//获取所有表格名
		List<String> showKey = sp.showKey(properties);
		Connection con = null;
		PreparedStatement statement = null;
		Statement sm = null;
		PreparedStatement statement2 = null;
		Statement sm2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		con = OracleJDBCUtiles.getConnections();
		Connection mysqlconn = null;
		PreparedStatement mysqlps = null;
		Statement mysqlsm = null;
		mysqlconn = MySQLJDBCUtiles.getConnections();
		//循环所有表格
		for (int k = 0; k < showKey.size(); k++) {
			Map<Integer,Map<String, String>> tableAndSqls = new HashMap<Integer, Map<String,String>>();
			//查询数据条数
			String sqltotalnumber = "select count(*) from " + showKey.get(k);
		
			//查询所有数据
			String sql="select * from "+ showKey.get(k);
			//
//			String delsql = "DROP TABLE IF EXISTS "+showKey.get(k);
//			Connection con = null;
//			PreparedStatement statement = null;
//			PreparedStatement statement2 = null;
//			ResultSet rs = null;
//			ResultSet rs2 = null;
			long rowCount = 0;
//			con = OracleJDBCUtiles.getConnections();
			try {

				/*sm = con.createStatement();
				
				sm2 = con.createStatement();*/
			//	sm2.executeUpdate(sql);
				statement = con.prepareStatement(sql);
				statement2 = con.prepareStatement(sqltotalnumber);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs = statement.executeQuery();
				rs2 = statement2.executeQuery();
			/*	rs = sm.executeQuery(sql);
				rs2 = sm2.executeQuery(sqltotalnumber);*/
				//取得数据总条数
				while(rs2.next()){
					rowCount = rs2.getLong(1);
				}
				System.out.println("rowCount"+rowCount);
				//数据为空跳出本次循环
				if (rowCount == 0) {
					continue;
				}
				ResultSetMetaData metaData = rs.getMetaData();
				HashMap<String, Object> data = new HashMap<String, Object>();
				//得到表中数据字段总数
				int count = metaData.getColumnCount();
				System.out.println("count: "+count);
				String resultRow = "";
				//循环所有数据，将数据以键值对的形式保存在tableAndSqls集合中
				for (int j = 0; j < rowCount; j++) {
					Map<String, String> tableAndSql = new HashMap<String, String>();
					if (rs.next()) {					
						for (int i = 0; i < count; i++) {
							resultRow += rs.getString(i+1)+";";
							tableAndSql.put(metaData.getColumnName(i+1), rs.getString(i+1));					
						}
					}
					tableAndSqls.put(j, tableAndSql);				
				}
	//			System.out.println(resultRow);
				
				/*for (Entry<Integer, Map<String, String>> entry : tableAndSqls.entrySet()) { 
					  System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
					}
				
				Iterator<String> iterable =  data.keySet().iterator();
				while (iterable.hasNext()) {
					String key = iterable.next();
				//	System.err.println(key+"\t");
					System.out.println(data.get(key)+"\t"+"\n");
				}*/
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//表中列的名字
			Object[] columname = new String[50];
			try {
				columname= tableAndSqls.get(0).keySet().toArray();
				int columnumber = columname.length;				
	//			System.out.println(columnumber);
			} catch (Exception e) {
				// TODO: handle exception
			}
			String jointcolum = "";
			//循环表中列名
			for (int i = 0; i < columname.length; i++) {
				if (i==columname.length-1) {
					jointcolum += columname[i];
				}else {
					jointcolum += columname[i]+",";
				}
				
			}
			
			System.out.println("jointcolum"+jointcolum);
			//写入到mysql数据库
//			Connection mysqlconn = null;
//			PreparedStatement mysqlps = null;
//			Statement mysqlsm = null;
//			mysqlconn = MySQLJDBCUtiles.getConnections();
			
            int total = tableAndSqls.size();
            //用StringBuilder拼接sql语句，10000个数据执行一次
            for (int m = 0; m < total/10000+1; m++) {
            	StringBuilder sb = new StringBuilder();  
                sb.append("insert into "+showKey.get(k)+ " ("+jointcolum+") values"); 
            	int totalnum;
            	if (m==total/10000) {
					totalnum = total;
				}else {
					totalnum = 10000*(m+1);
				}
            	for (int i = 0+10000*m; i < totalnum; i++) {
        //    		System.out.println("i********"+i);
      //      		System.out.println(tableAndSqls);
    				Object[] tabledb = tableAndSqls.get(i).values().toArray();			
    				String linedb = "";				
    				if (i>0+10000*m) {
    					sb.append(",");
    				}
    	            for (int j = 0; j < tabledb.length; j++) {
    	            	if (j==0) {
    						sb.append("(");
    					}
    	            	if (tabledb[j] == null || "".equals(tabledb[j])) {
    						if (j == tabledb.length-1) {
    							sb.append("null)");
    						}else {
    							sb.append("null,");
    						}
    						
    					}
    					else if(j==tabledb.length-1){
    						sb.append("'"+tabledb[j]+"')");
    					}else {
    						sb.append("'"+tabledb[j]+"',");
    					}
    	            }

    			}
    			try {
    			/*	mysqlsm =  mysqlconn.createStatement();			
    				System.out.println(sb.toString());
    				mysqlsm.executeUpdate(sb.toString());	*/
    				mysqlps = mysqlconn.prepareStatement(sb.toString());
    				mysqlps.executeUpdate();
    	//			sb = null;
    			} catch (SQLException e) {
    				
    				e.printStackTrace();
    			}finally{
    				try {
    					if (mysqlps!=null) {
    						mysqlps.close();
						}    					
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
			}
//			for (int i = 0; i < tableAndSqls.size(); i++) {
//				Object[] tabledb = tableAndSqls.get(i).values().toArray();			
//				String linedb = "";				
//				if (i>0) {
//					sb.append(",");
//				}
//	            for (int j = 0; j < tabledb.length; j++) {
//	            	if (j==0) {
//						sb.append("(");
//					}
//	            	if (tabledb[j] == null || "".equals(tabledb[j])) {
//						if (j == tabledb.length-1) {
//				//			linedb += "null";
//							sb.append("null)");
//						}else {
//			//				linedb += "null,";
//							sb.append("null,");
//						}
//						
//					}
//					else if(j==tabledb.length-1){
//			//			linedb += "'"+tabledb[j]+"'";
//						sb.append("'"+tabledb[j]+"')");
//					}else {
//			//			linedb += "'"+tabledb[j]+"',";
//						sb.append("'"+tabledb[j]+"',");
//					}
//	            }
//	//			String insertSql = "insert into " + showKey.get(k) +" ("+jointcolum+") values("+linedb+")";
//	//			System.out.println("insertSql:   "+insertSql);
////				try {
////					mysqlsm =  mysqlconn.createStatement();
////					/*mysqlps = mysqlconn.prepareStatement(insertSql);
////					mysqlps.executeUpdate();*/
////					System.out.println(sb.toString());
////					mysqlsm.executeUpdate(sb.toString());	
////				} catch (SQLException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//			}
//			try {
//				mysqlsm =  mysqlconn.createStatement();			
//				System.out.println(sb.toString());
//				mysqlsm.executeUpdate(sb.toString());	
//				sb = null;
//			} catch (SQLException e) {
//	
//				e.printStackTrace();
//			}
		}
		OracleJDBCUtiles.release(statement, con);
		OracleJDBCUtiles.release(statement2, con);
		MySQLJDBCUtiles.release(mysqlsm, mysqlconn);
		long endTime = System.currentTimeMillis();
		System.out.println("程序运行时间："+(endTime-startTime)+" 毫秒");
	}
}
