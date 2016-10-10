package com.weiweisc.util;

import java.io.File;
import java.util.Calendar;

/**
 * Created by huwei on 15/12/26 下午4:34.
 */
public class FileUploadUtil {

    public static String buildHtml(String data,String callback){
        StringBuilder html = new StringBuilder();

        html.append("<script>");
        html.append("parent."+callback+"("+data+");");
        html.append("</script>");

        return html.toString();
    }

    public static void main(String[] args) {
		System.out.println(genImageDir());
		System.out.println(genVideoDir());
		System.out.println(genMusicDir());
	}

    //图片目录  每小时一个目录 
    public static String genImageDir(){
        Calendar calendar = Calendar.getInstance();
        char c = 'a';
        StringBuilder str = new StringBuilder();
        str.append((char)(calendar.get(Calendar.YEAR)%2000%26+c));
        str.append((char)(calendar.get(Calendar.MONTH)+c));
        str.append(File.separatorChar);
        str.append((calendar.get(Calendar.DAY_OF_MONTH)));
        str.append(File.separatorChar);
        str.append((char)(calendar.get(Calendar.HOUR_OF_DAY)+c));
        return str.toString();
    }
    
    //视频 每天一个目录
    public static String genVideoDir(){
        Calendar calendar = Calendar.getInstance();
        char c = 'a';
        StringBuilder str = new StringBuilder();
        str.append((char)(calendar.get(Calendar.YEAR)%2000%26+c));
        str.append((char)(calendar.get(Calendar.MONTH)+c));
        str.append(File.separatorChar);
        str.append((calendar.get(Calendar.DAY_OF_MONTH)));
        return str.toString();
    }
    
    //音乐 每天一个目录
    public static String genMusicDir(){
        Calendar calendar = Calendar.getInstance();
        char c = 'a';
        StringBuilder str = new StringBuilder();
        str.append((char)(calendar.get(Calendar.YEAR)%2000%26+c));
        str.append((char)(calendar.get(Calendar.MONTH)+c));
        str.append(File.separatorChar);
        str.append(calendar.get(Calendar.DAY_OF_MONTH));
        return str.toString();
    }
    
    public static boolean contentType(String fileType,String allowTypes){
		if(StringUtil.isBlank(fileType) || StringUtil.isBlank(allowTypes)){
			return false;
		}
		String[] types = allowTypes.split(",");
		for(String type:types){
			if(!StringUtil.isBlank(type) && type.equalsIgnoreCase(fileType)){
				return true;
			}
		}
		return false;
	}
}
