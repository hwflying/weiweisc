package com.weiweisc.util;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
/**视频上传时  通过url获取视频*/
public class HtmlParserUtil{

    public static void main(String[] args) throws Exception {
		//String test1 = getVideoFlashUrl("http://v.youku.com/v_show/id_XMTYzMDg5ODU4MA==.html?from=y1.3-tv-grid-1007-9910.86804.1-1");
	    //System.out.println(test1);
    	
    	//String aa = getaaa("http://www.kssnwl.com/jgcxshow/C2wI1u.html");
    	//System.out.println(aa);
    	/*
    	String html = getHtml("http://www.kssnwl.com/jgcx.html");
    	String[] pair = html.split("\\n");
    	for(int i=0;i<pair.length;i++){
    		String[] arr = pair[i].split("\\|");
    		//System.out.println("name:"+arr[0]+"\turl:"+arr[1]);
    		String aa = getaaa(arr[1]);
        	System.out.println(aa);
    	}
    	System.out.println();
    	*/
    	System.out.println(getIp("172.16.104.122"));
    }
    
    
    public static String getIp(String ip) throws ParserException{
    	
    	String url="http://www.ip138.com/ips138.asp?ip="+ip;
    	url = url + "&action=2";
    	String type = "";
    	String city = "";
    	String sheng = "";
    	String returndata = "";
    	
    	// 1、构造一个Parser 
    	Parser parser = new Parser(url); 
    				
		// 2.1、自定义一个Filter，用于过滤<input>标签
        NodeFilter frameNodeFilter = new NodeFilter(){  
        	
			private static final long serialVersionUID = 1L;
			
            public boolean accept(Node node){ 
				if (node.getText().startsWith("li")){ 
					return true;
                } else {  
                    return false;  
                } 
            }  
        };  
    		        //2.2、创建第二个Filter，过滤<a>标签  
    		        //NodeFilter aNodeFilter = new NodeClassFilter(LinkTag.class);  
    		          
    		        //2.3、净土上述2个Filter形成一个组合逻辑Filter。  
    		        //OrFilter linkFilter = new OrFilter(frameNodeFilter, aNodeFilter);  
    		          
    		        //3、使用parser根据filter来取得所有符合条件的节点  
        NodeList nodeList = parser.extractAllNodesThatMatch(frameNodeFilter); 
        //4、对取得的Node进行处理  
        for(int j = 0; j<nodeList.size();j++){ 
        	//获得每个节点
            Node node = nodeList.elementAt(j);  
            //获得每个节点的文本(字符串)  
            String content = node.getFirstChild().getText();
            if(content==null) content = "";
            if(content.startsWith("本站数据")){
            	content = content.replaceAll("本站数据：", "").replaceAll("市", "市  ").replaceAll("区", "区  ").replaceAll("省", "省  ");
            	System.out.println(content);
            	/*if(content.contains("全省共用出口"))
            	{
            		sheng = "北京市";
            		city = "北京市";
            		type = "移动";
            	}*/
            	
            	String[] arr = content.split(" +");
            	if(arr.length == 1)
            	{
            		sheng = "北京市";
            		city = "北京市";
            		type = "电信";
            	}
            	if(arr.length==2)
            	{
            		sheng = arr[0];
            		city = "city";
            		type = arr[1];
            	}
            	if(arr.length==3)
            	{  
            		sheng = arr[0];
            		city = arr[1];
            		type = arr[2];
            	}
            	if(arr.length==4)
            	{
            	    sheng = arr[0];
            		city = arr[1];
            		type = arr[3];
            	}
            	
            	if(arr.length==5)
            	{
            	    sheng = arr[0];
            		city = arr[1];
            		type = arr[4];
            	}
            }
        }
        returndata = sheng + ","+city+","+type;
        return returndata;
    }
    
    
    
    public static String getaaa(String url){
    	StringBuilder result = new StringBuilder();
    	
    	try{
			// 1、构造一个Parser 
			Parser parser = new Parser(url); 
			
			// 2.1、自定义一个Filter，用于过滤<input>标签
	        NodeFilter frameNodeFilter = new NodeFilter() {  
	        	
				private static final long serialVersionUID = 1L;
				
				@Override  
	            public boolean accept(Node node) {  
					if (node.getText().startsWith("tr")) {  
	                    return true;  
	                } else {  
	                    return false;  
	                } 
	            }  
	        };  
	          
	        //2.2、创建第二个Filter，过滤<a>标签  
	        //NodeFilter aNodeFilter = new NodeClassFilter(LinkTag.class);  
	          
	        //2.3、净土上述2个Filter形成一个组合逻辑Filter。  
	        //OrFilter linkFilter = new OrFilter(frameNodeFilter, aNodeFilter);  
	          
	        //3、使用parser根据filter来取得所有符合条件的节点  
	        NodeList nodeList = parser.extractAllNodesThatMatch(frameNodeFilter); 
	        //4、对取得的Node进行处理  
	        for(int i = 0; i<nodeList.size();i++){ 
	        	//获得每个节点
	            Node node = nodeList.elementAt(i);  
	            //获得每个节点的文本(字符串)  
	            //String text = node.getText();
	            //result.append(node.toHtml()+"\n");
	            
	            NodeList children = node.getChildren();
	            for(int j=0;j<children.size();j++){
	            	
	            	String s = children.elementAt(j).toHtml();
	            	s = s.replaceAll("\\s+", "").trim();
	            	if(s.length()==0) continue;
	            	s = s.substring(s.indexOf(">")+1);
	            	s = s.substring(0,s.indexOf("<"));
	            	result.append(s+"\t");
	            }
	            result.append("\n");
	            
		    }
		}catch(Exception e){
			//
		}
        return result.toString();
    }
    
    public static String getHtml(String url){
    	
    	StringBuilder result = new StringBuilder();
    	
    	try{
			// 1、构造一个Parser 
			Parser parser = new Parser(url); 
			
			// 2.1、自定义一个Filter，用于过滤<input>标签
	        NodeFilter frameNodeFilter = new NodeFilter() {  
	        	
				private static final long serialVersionUID = 1L;
				
				@Override  
	            public boolean accept(Node node) {  
					if (node.getText().startsWith("td")) {  
	                    return true;  
	                } else {  
	                    return false;  
	                } 
	            }  
	        };  
	          
	        //2.2、创建第二个Filter，过滤<a>标签  
	        //NodeFilter aNodeFilter = new NodeClassFilter(LinkTag.class);  
	          
	        //2.3、净土上述2个Filter形成一个组合逻辑Filter。  
	        //OrFilter linkFilter = new OrFilter(frameNodeFilter, aNodeFilter);  
	          
	        //3、使用parser根据filter来取得所有符合条件的节点  
	        NodeList nodeList = parser.extractAllNodesThatMatch(frameNodeFilter); 
	        //4、对取得的Node进行处理  
	        for(int i = 0; i<nodeList.size();i++){ 
	        	//获得每个节点
	            Node node = nodeList.elementAt(i);  
	            //获得每个节点的文本(字符串)  
	            String text = node.getText();
	            if(text.indexOf("width=\"15%\"")!=-1){
	            	NodeList children = node.getChildren();
	            		
//	            	Node an = children.elementAt(0).getChildren().elementAt(0);
//	            	an.getText();
//	            	Node a = an.getChildren().elementAt(0);
//	            	String n = a.getFirstChild().getText();
	            	String href = children.elementAt(2).getText();
	            	href = href.substring(href.indexOf("\"")+1,href.length()-1);
	            	String p = children.elementAt(2).getChildren().elementAt(0).getFirstChild().getText();
	            	
	            	result.append(p+"|http://www.kssnwl.com"+href+"\n");
	            }
		    }
		}catch(Exception e){
			//
		}
        return result.toString();
    }

	public static String getVideoFlashUrl(String url) {
		
		try{
			// 1、构造一个Parser 
			Parser parser = new Parser(url); 
			// 2.1、自定义一个Filter，用于过滤<input>标签
	        NodeFilter frameNodeFilter = new NodeFilter() {  
	        	
				private static final long serialVersionUID = 1L;
				
				@Override  
	            public boolean accept(Node node) {  
	            	if (node.getText().startsWith("input")) {  
	                    return true;  
	                } else {  
	                    return false;  
	                } 
	            }  
	        };  
	          
	        //2.2、创建第二个Filter，过滤<a>标签  
	        //NodeFilter aNodeFilter = new NodeClassFilter(LinkTag.class);  
	          
	        //2.3、净土上述2个Filter形成一个组合逻辑Filter。  
	        //OrFilter linkFilter = new OrFilter(frameNodeFilter, aNodeFilter);  
	          
	        //3、使用parser根据filter来取得所有符合条件的节点  
	        NodeList nodeList = parser.extractAllNodesThatMatch(frameNodeFilter); 
	        //4、对取得的Node进行处理  
	        for(int i = 0; i<nodeList.size();i++){ 
	        	//获得每个节点
	            Node node = nodeList.elementAt(i);  
	            //获得每个节点的文本(字符串)  
	            String text = node.getText();
	            //获得id=link2的input字符串
	            if(text.indexOf("id=\"link2\"")>0){
	            	int j = text.indexOf("value=");
	            	text = text.substring(j);
	            	text = text.substring(text.indexOf("\"")+1,text.lastIndexOf("\""));
	            	return text;
	            	
	            //System.out.println(i+"\t"+text);
	           }  
		    }
		}catch(Exception e){
			//
		}
        return null;
	}
}
