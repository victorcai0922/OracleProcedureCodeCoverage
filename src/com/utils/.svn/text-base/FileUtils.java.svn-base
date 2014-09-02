/**
 * 
 */
package com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author caiql
 *
 */
public class FileUtils {
	
	/**
	 * read data source
	 * from properties file
	 * @return
	 */
	public static Map<String,String> readDataSourceFromPropertiesFile(){
		InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(
				"database.properties");
		Properties prop = new Properties();
		try {
			prop.load(in);
			String url = prop.getProperty("jdbc.url");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			String statistic = prop.getProperty("statistic");
			String schema = prop.getProperty("schema");
			Map<String,String> datasource = new HashMap<String, String>();
			datasource.put("url", url);
			datasource.put("username", username);
			datasource.put("password", password);
			datasource.put("statistic", statistic);
			datasource.put("schema", schema);
			return datasource;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			prop.clear();
		}
		return null;
	}
	
	public static void main(String[] args) {
		FileUtils.readDataSourceFromPropertiesFile();
	}

}
