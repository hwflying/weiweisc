package com.weiweisc.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JsonUtil {

	public static String toJson(Map<String,Object> map){
		StringBuilder result = new StringBuilder();
		result.append("{");
		
		if(map!=null){
			Set<Entry<String, Object>> entrySet = map.entrySet();
			int size = entrySet.size();
			int i = 0;
			for(Entry<String, Object> entry:entrySet){
				String val = null;
				if(entry.getValue()==null){
					val = "null";
				}else{
					val = "\""+entry.getValue()+"\"";
				}
				result.append("\""+entry.getKey()+"\":"+val);
				if(i!=size-1){
					result.append(",");
				}
				i++;
			}
		}
		result.append("}");
		return result.toString();
	}
	
	public static String toJson(List<String> list){
		StringBuilder result = new StringBuilder();
		result.append("[");
		
		if(list!=null){
			int size = list.size();
			for(int i=0;i<size;i++){
				result.append("\""+list.get(i)+"\"");
				if(i<size-1) result.append(",");
			}
		}
		result.append("]");
		return result.toString();
	}
}
