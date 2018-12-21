package com.ushine.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
/**
 * 取出properties文件中的键值对
 * @author Administrator
 *
 */
public class ShowProperties {
	
	/**
	 * 遍历取出properties文件中的所有值
	 * @param properties
	 * @return
	 */
	public List<String> showValues(Properties properties){
		List<String> values = new ArrayList<String>();
		Enumeration<Object> enu = properties.elements();	
		while (enu.hasMoreElements()) {
			Object value = enu.nextElement();
	//		System.out.println(value);
			values.add(value.toString());
		}
		return values;
	}
	
	/**
	 * 遍历取出properties文件中的所有键
	 * @param properties
	 * @return
	 */
	public  List<String> showKey(Properties properties){
		List<String> keys = new ArrayList<String>();
		Enumeration<?> enu = properties.propertyNames();
		while(enu.hasMoreElements()){
			Object key = enu.nextElement();
		//	System.out.println(key);
			keys.add(key.toString());
		}
		return keys;
	}		
}
