package com.weiweisc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SensitiveKeywordFilter { 	 
	//词库文本文件地址
	private static String textFilePath="";
	//敏感词库
	@SuppressWarnings("rawtypes")
	private static HashMap sensitiveWordMap = null;
	//默认编码格式
	private static final String ENCODING = "utf-8";

	public static int minMatchTYpe = 1;      //最小匹配规则
	public static int maxMatchType = 2;      //最大匹配规则

	public static String getTextFilePath() {
		return textFilePath;
	}

	public static void setTextFilePath(String textFilePath) {
		SensitiveKeywordFilter.textFilePath = textFilePath;
	}


	//初始化敏感词库
	private static void init() {		
		sensitiveWordMap = initKeyWord();		 
	}

	@SuppressWarnings("rawtypes")
	public static HashMap initKeyWord(){
		try {
			//读取敏感词库
			Set<String> keyWordSet = readSensitiveWordFile();
			//将敏感词库加入到HashMap中
			addSensitiveWordToHashMap(keyWordSet);
			//spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveWordMap;
	}

	//读取敏感词库中的内容，将内容添加到set集合中	
	@SuppressWarnings("resource")
	private static Set<String> readSensitiveWordFile() throws Exception{
		Set<String> set = null;

		File file = new File(textFilePath);    //读取文件
		InputStreamReader read = new InputStreamReader(new FileInputStream(file),ENCODING);
		try {
			if(file.isFile() && file.exists()){      //文件流是否存在
				set = new HashSet<String>();
				BufferedReader bufferedReader = new BufferedReader(read);
				String txt = null;
				while((txt = bufferedReader.readLine()) != null){    //读取文件，将文件内容放入到set中
					set.add(txt);
				}
			}
			else{         //不存在抛出异常信息
				throw new Exception("敏感词库文件不存在");
			}
		} catch (Exception e) {
			throw e;
		}finally{
			read.close();     //关闭文件流
		}
		return set;
	}



	/**
	 * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型： 	
	 * @param keyWordSet  敏感词库    
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
		String key = null;  
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		//迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while(iterator.hasNext()){
			key = iterator.next();    //关键字
			nowMap = sensitiveWordMap;
			for(int i = 0 ; i < key.length() ; i++){
				char keyChar = key.charAt(i);       //转换成char型
				Object wordMap = nowMap.get(keyChar);       //获取

				if(wordMap != null){        //如果存在该key，直接赋值
					nowMap = (Map) wordMap;
				}
				else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String,String>();
					newWorMap.put("isEnd", "0");     //不是最后一个
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}

				if(i == key.length() - 1){
					nowMap.put("isEnd", "1");    //最后一个
				}
			}
		}
	}



	/**
	 * 检查文字中是否包含敏感字符，检查规则如下：<br>     
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return，如果存在，则返回敏感词字符的长度，不存在返回0      
	 */
	@SuppressWarnings({ "rawtypes"})
	public static int CheckSensitiveWord(String txt,int beginIndex,int matchType){
		boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
		int matchFlag = 0;     //匹配标识数默认为0
		char word = 0;
		Map nowMap = sensitiveWordMap;
		for(int i = beginIndex; i < txt.length() ; i++){
			word = txt.charAt(i);
			nowMap = (Map) nowMap.get(word);     //获取指定key
			if(nowMap != null){     //存在，则判断是否为最后一个
				matchFlag++;     //找到相应key，匹配标识+1 
				if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
					flag = true;       //结束标志位为true   
					if(SensitiveKeywordFilter.minMatchTYpe == matchType){    //最小规则，直接返回,最大规则还需继续查找
						break;
					}
				}
			}
			else{     //不存在，直接返回
				break;
			}
		}
		if(matchFlag < 2 && !flag){     
			matchFlag = 0;
		}
		return matchFlag;
	}


	/**
	 * 判断文字是否包含敏感字符	 
	 * @param txt  文字
	 * @param matchType  匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
	 * @return 若包含返回true，否则返回false	  
	 */
	public boolean isContaintSensitiveWord(String txt,int matchType){
		boolean flag = false;
		for(int i = 0 ; i < txt.length() ; i++){
			int matchFlag = SensitiveKeywordFilter.CheckSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
			if(matchFlag > 0){    //大于0存在，返回true
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取文字中的敏感词	  
	 * @param txt 文字
	 * @param matchType 匹配规则：1：最小匹配规则，2：最大匹配规则
	 * @return	  
	 */
	public static Set<String> getSensitiveWord(String txt , int matchType){
		Set<String> sensitiveWordList = new HashSet<String>();

		for(int i = 0 ; i < txt.length() ; i++){
			int length = CheckSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
			if(length > 0){    //存在,加入list中
				sensitiveWordList.add(txt.substring(i, i+length));
				i = i + length - 1;    //减1的原因，是因为for会自增
			}
		}

		return sensitiveWordList;
	}

	/**
	 * 替换敏感字字符	 
	 * @param txt
	 * @param matchType
	 * @param replaceChar 替换字符，默认*	 
	 */
	public String replaceSensitiveWord(String txt,int matchType,String replaceChar){
		String resultTxt = txt;
		Set<String> set = getSensitiveWord(txt, matchType);     //获取所有的敏感词
		Iterator<String> iterator = set.iterator();
		String word = null;
		String replaceString = null;
		while (iterator.hasNext()) {
			word = iterator.next();
			replaceString = getReplaceChars(replaceChar, word.length());
			resultTxt = resultTxt.replaceAll(word, replaceString);
		}

		return resultTxt;
	}

	/**
	 * 获取替换字符串
	 * @param replaceChar
	 * @param length
	 * @return	  
	 */
	private String getReplaceChars(String replaceChar,int length){
		String resultReplace = replaceChar;
		for(int i = 1 ; i < length ; i++){
			resultReplace += replaceChar;
		}		
		return resultReplace;
	}


	public static void main(String[] arge){

		SensitiveKeywordFilter.setTextFilePath("/Users/huwei/Documents/tv_sys_sensitive.txt");
		SensitiveKeywordFilter.init();
		System.out.println("敏感词的数量：" + SensitiveKeywordFilter.sensitiveWordMap.size());
		String string = " 情人，av,二奶，主人公尝试着去用某然后法轮功 我们的扮演的角色法轮大法女人就是小姐就是坏人就喜欢上床就喜欢仿真枪睡觉就是跟随着主人公的喜红客联盟 "
				+ "然后法轮功 我们的扮演的角色法轮大法就是跟随着催情粉主人公的兽交喜红客联盟 怒哀乐而过于牵强的把自己的中共情感也附加于银幕情节中，然后感动就流泪，"
				+ "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人小姐一杯红酒兼职小姐一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
		
		//string = "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器情人av二奶主人公尝试着去用情人二级三级某";
		System.out.println("待检测语句字数：" + string.length());
		long beginTime = System.currentTimeMillis();
		Set<String> set = SensitiveKeywordFilter.getSensitiveWord(string, 2);
		long endTime = System.currentTimeMillis();
		System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
		System.out.println("总共消耗时间为：" + (endTime - beginTime));



		System.exit(0);


	}


}