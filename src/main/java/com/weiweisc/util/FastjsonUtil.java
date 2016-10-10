package com.weiweisc.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class FastjsonUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(FastjsonUtil.class);
	
	public static String toJson(Object obj){
		if(obj==null) return null;
		String result = null;
		try{
			result = JSON.toJSONString(obj);
		}catch(Exception e){
			LOG.error(e.getMessage(),e);
		}
		return result;
	}

	public static String wrapSuccess(String data){
		if(StringUtil.isEmpty(data)){
			data = "";
		}else{
			data = data.trim();
		}
		return "{\"code\":200,\"msg\":\"success\",\"data\":"+data+"}";
	}
	
	public static <T> T fromJson(String json,Class<T> valueType){
		if(StringUtil.isEmpty(json) || valueType==null) return null;
		T result = null;
		try{
			result = JSON.parseObject(json, valueType);
		}catch(Exception e){
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	public static <T> List<T> parseArray(String json,Class<T> valueType){
		if(StringUtil.isEmpty(json)  || valueType==null) return null;
		List<T> result = null;
		try{
			result = JSON.parseArray(json, valueType);
		}catch(Exception e){
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	
	
}
