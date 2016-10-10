package com.weiweisc.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

	private static Map<String,Properties> propMap = new HashMap<String,Properties>();
	
	public static Properties getProperties(String fileName,boolean reload){
		if(StringUtil.isEmpty(fileName)){
			return null;
		}
		Properties prop = null;
		if(!reload){
			prop = propMap.get(fileName);
		}
		if(prop==null){
			try {
				prop = new Properties();
				prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(prop!=null){
			propMap.put(fileName, prop);
		}
		
		return prop;
	}
}
