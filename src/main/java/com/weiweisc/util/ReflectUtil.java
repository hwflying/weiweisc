package com.weiweisc.util;

public class ReflectUtil {

	public static String getSetter(String fieldName){
		return "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}
	
	public static String getGetter(String fieldName,Class<?> clazz){
		String prefix = "get";
		if(clazz==Boolean.TYPE){
			prefix = "is";
		}
		return prefix + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}
	
	public static void main(String[] args) {
		System.out.println(getSetter("name"));
		System.out.println(getGetter("gender",boolean.class));
	}
}