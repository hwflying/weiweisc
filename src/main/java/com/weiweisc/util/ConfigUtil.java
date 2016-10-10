package com.weiweisc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
	
	private static final Properties props = new Properties();
	
	static{
		try {
			InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static String get(String key){
		return get(key,"");
	}
	
	public static String get(String key,String defaultValue){
		
		String result = props.getProperty(key, defaultValue);
		
		return result;
		
		/*
		String result = defaultValue;
		try {
			String val = get("config.properties",key,defaultValue);
			if(val!=null){
				result = val;
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return result;
		*/
	}
	
	public static String get(String fileName,String key,String defaultValue) throws IOException{
		if(StringUtil.isBlank(fileName)||StringUtil.isBlank(key)){
			return null;
		}
		
		//InputStream is = ClassLoader.getSystemResourceAsStream(fileName);
		
		InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream(fileName);
		
		Properties props =new Properties();
		props.load(is);
		return props.getProperty(key, defaultValue);
	}
}
