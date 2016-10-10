package com.weiweisc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.weiweisc.entity.MusicEntity;

public class MusicUtil {
	
	private static final String apikey = "5973e5dfc11489fc82aa5d393696ac3a";
	private static final String BASE_URL = "http://apis.baidu.com/geekery/";
	private static final String MUSIC_QUERY_URL = BASE_URL+"music/query";
	private static final String MUSIC_PLAYINFO_URL = BASE_URL+"music/playinfo";
	
	private static final String ENCODE = "UTF-8";
	
	
	public static String query(String keyword,Integer page,Integer size){
		String result = null;
		try{
			String httpArg = "s="+URLEncoder.encode(keyword,ENCODE)+"&size="+size+"&page="+page;
			result = request(MUSIC_QUERY_URL,httpArg);
		}catch(Exception e){}
		return result;
	}
	
	public static MusicEntity playinfo(String hash){
		
		if(StringUtil.isEmpty(hash)) return null;
		
		String json = request(MUSIC_PLAYINFO_URL,"hash="+hash);
		
		System.out.println(json);
		
		MusicEntity result = FastjsonUtil.fromJson(json, MusicEntity.class);
		
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		
		System.out.println(query("你好明天",1,10));
		System.out.println(playinfo("081a2b72cfecaebbaab70d6b5a05ca6a"));
		
		/*
		String httpUrl = "http://apis.baidu.com/geekery/music/query";
		String httpArg = "s=%E5%8D%81%E5%B9%B4&size=10&page=1";
		String jsonResult = request(httpUrl, httpArg);
		System.out.println(jsonResult);
		*/
		
		/*
		String httpUrl = "http://apis.baidu.com/geekery/music/playinfo";
		String httpArg = "hash=548f977f0afe54d043c828f4de70da1a";
		String jsonResult = request(httpUrl, httpArg);
		System.out.println(jsonResult);
		
		String ascii="\u9648\u5955\u8fc5 - \u5341\u5e74 - CCTV\u7cbe\u5f69\u97f3\u4e50\u6c47\u73b0\u573a1";
		System.out.println(ascii); 
		*/
		
	}

	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public static String request(String httpUrl, String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?" + httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty("apikey",  apikey);
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
}
