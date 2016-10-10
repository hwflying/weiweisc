package com.weiweisc.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonpUtil {
	
	private static final String CONTENT_TYPE="application/x-javascript";
	private static final String DEFAULT_CALLBACK="callback";

	public static void jsonp(HttpServletResponse response,String callback,Object data) throws IOException{
		if(response==null) return ;
		response.setContentType(CONTENT_TYPE);
		if(StringUtil.isEmpty(callback)) callback = DEFAULT_CALLBACK;
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(data);
		if(json==null) json = "";
		response.getWriter().write(callback+"("+json+");");
		om = null;
	}
	public static void main(String[] args) {
		
	}
}
