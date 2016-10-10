package com.weiweisc.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

	/**
	 * 往 Response 中写入字符串
	 * @param content
	 * @throws IOException
	 */
	protected void write(HttpServletResponse response,String content) throws IOException{
		if(response!=null && content!=null){
			response.getWriter().write(content);
		}
	}
	
	
	protected void printRequestParams(HttpServletRequest request){
		if(request==null) return ;
		
		Enumeration<?> names = request.getParameterNames();
		while(names.hasMoreElements()){
			String name = String.valueOf(names.nextElement());
			String value = request.getParameter(name);
			System.out.println(name+"="+value);
		}
		
	}
}
