package com.weiweisc.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class HttpTools {

	public static void main(String[] args) {
		//http://m.kuaidi100.com/query?type=shunfeng&postid=606080205425&id=1&valicode=&temp=0.2738252424822816
		String url = "http://m.kuaidi100.com/query";
		NameValuePair[] params = {
				new NameValuePair("type","shunfeng"),
				new NameValuePair("postid","606080205425"),
				new NameValuePair("id","1"),
				new NameValuePair("temp",String.valueOf(new Random().nextDouble())),
				};
		String str = doMyGet(url, params);
		System.out.println(str.replaceAll("\"shunfeng\"","\"发沙发沙发\""));
		
	}

    //执行一个HTTP GET请求，返回请求响应的HTML
    public static String doMyGet(String url, NameValuePair[] params) {
        String response = "";
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        client.getHttpConnectionManager().getParams().setSoTimeout(1000);
        HttpMethod method = new GetMethod(url);
      
        try {
            if (params!=null && params.length>0){
            	method.setQueryString(params);
            }
            method.setRequestHeader("charset","UTF-8");	            
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
            	 response = new String(method.getResponseBody(),"UTF-8");
            }  
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    
    //执行一个HTTP POST请求，返回请求响应的HTML
    public static  String doMyPost(String url, Map<String, String> params) {
        String response = "";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);       
        method.setRequestHeader("charset","iso-8859-1");	 
        if (params!=null && params.size()>0) {             
            for (Map.Entry<String, String> entry : params.entrySet()) {                
                method.addParameter(entry.getKey(), entry.getValue());              
            }                
        }
        
        try {        	 
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                response = new String(method.getResponseBody(),"UTF-8");
            }
        } catch (IOException e) {
        	//LOGGER.error("执行HTTP Post请求" + url + "时，发生异常！");        	
        	e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return response;
    } 
    
    
    public static String doMySend(String url,String data){           
        //创建httpclient工具对象   
        HttpClient client = new HttpClient();    
        //创建post请求方法   
        PostMethod myPost = new PostMethod(url);    
        //设置请求超时时间           
        String responseString = null;       
        
        try{                 	
            //设置请求头部类型   
            myPost.setRequestHeader("Content-Type","text/xml");  
            myPost.setRequestHeader("charset","utf-8");            
            myPost.setRequestEntity(new StringRequestEntity(data,"text/xml","utf-8"));
           
            int statusCode = client.executeMethod(myPost); 
            //System.out.println("doMySend statusCode>>>"+statusCode);
            if(statusCode == HttpStatus.SC_OK){    
                BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());    
                byte[] bytes = new byte[1024];    
                ByteArrayOutputStream bos = new ByteArrayOutputStream();    
                int count = 0;    
                while((count = bis.read(bytes))!= -1){    
                    bos.write(bytes, 0, count);    
                }    
                byte[] strByte = bos.toByteArray();    
                responseString = new String(strByte,0,strByte.length,"utf-8");    
                bos.close();    
                bis.close();    
            }    
        }catch (Exception e) {    
            e.printStackTrace();    
        }finally{
        	myPost.releaseConnection();         	
        }          
        return responseString;    
    }    
    
    public static String doMySend(String url,String data,String type){           
        //创建httpclient工具对象   
        HttpClient client = new HttpClient();    
        //创建post请求方法   
        PostMethod myPost = new PostMethod(url);    
        //设置请求超时时间           
        String responseString = null;       
        
        try{  
        	type=(type==null || type.isEmpty()) ?  "html" : type;
        	type=type.toLowerCase();
        	
            //设置请求头部类型   
        	myPost.setRequestHeader("charset","utf-8");
        	if("html".equals(type)){
        		  myPost.setRequestHeader("Content-Type","text/html");                         
                  myPost.setRequestEntity(new StringRequestEntity(data,"text/html","utf-8"));
        	}else if("xml".equals(type)){
        		  myPost.setRequestHeader("Content-Type","text/xml");                         
                  myPost.setRequestEntity(new StringRequestEntity(data,"text/xml","utf-8"));
        	}else if("json".equals(type)){	
        		  myPost.setRequestHeader("Content-Type","application/json");                         
                  myPost.setRequestEntity(new StringRequestEntity(data,"application/json","utf-8"));
        	}else if("text".equals(type)){	
        		  myPost.setRequestHeader("Content-Type","text/html");                         
                  myPost.setRequestEntity(new StringRequestEntity(data,"text/html","utf-8"));
        	}else{
        		  myPost.setRequestHeader("Content-Type","text/html");                         
                  myPost.setRequestEntity(new StringRequestEntity(data,"text/html","utf-8"));
        	}
          
           
            int statusCode = client.executeMethod(myPost); 
            //System.out.println("doMySend statusCode>>>"+statusCode);
            if(statusCode == HttpStatus.SC_OK){    
                BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());    
                byte[] bytes = new byte[1024];    
                ByteArrayOutputStream bos = new ByteArrayOutputStream();    
                int count = 0;    
                while((count = bis.read(bytes))!= -1){    
                    bos.write(bytes, 0, count);    
                }    
                byte[] strByte = bos.toByteArray();    
                responseString = new String(strByte,0,strByte.length,"utf-8");    
                bos.close();    
                bis.close();    
            }    
        }catch (Exception e) {    
            e.printStackTrace();    
        }finally{
        	myPost.releaseConnection();         	
        }          
        return responseString;    
    }    
    
    
    
    
   
}
