package com.weiweisc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("weixin")
public class WeixinController extends BaseController{

	
	@RequestMapping(value="notice.wx",method={RequestMethod.POST,RequestMethod.GET})
	public void notice(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		this.printRequestParams(request);
		
		String signature=request.getParameter("signature");  //微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String timestamp=request.getParameter("timestamp"); 	//时间戳
		String nonce=request.getParameter("nonce");   //随机数
		String echostr=request.getParameter("echostr"); //随机字符串 
		
		
		
		this.write(response,echostr);
	}
}
