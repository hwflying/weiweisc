package com.weiweisc.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

public class JAXBUtil {

	public static void main(String[] args) {
		TestXmlBean bean = new TestXmlBean();
		bean.setName("张三");
		bean.setAge(18);
		bean.setId("id-123");
		bean.setHobby("");
		bean.setSex(1);
		
		String xml = objectToXml(bean);
		System.out.println(xml);
		
		String xml2 = "<user><name><![CDATA[张三]]></name><age><![CDATA[18]]></age><hobby><![CDATA[]]></hobby><sex><![CDATA[1]]></sex></user>";
		TestXmlBean bean2 = xmlToObject(xml2,TestXmlBean.class);
		System.out.println(bean2);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T xmlToObject(String xml,Class<T> clazz) {
		if(xml==null || clazz==null || (xml = xml.trim()).length()==0) return null;
		StringReader reader = null;
		try{
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		reader = new StringReader(xml);
		return (T) jaxbUnmarshaller.unmarshal(reader);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader!=null){
				reader.close();
			}
		}
		return null;
	}
	
	public static String objectToXml(Object obj) {
		if(obj==null) return "";
		
		StringWriter out = null;
		try{
			out = new StringWriter();
			JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			
			marshaller.marshal(obj, out);
			return out.toString();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {out.close();} catch (Exception e) {}
			}
		}
		return "";
	}
	
	@XmlRootElement(name="user")
	@XmlAccessorType(XmlAccessType.FIELD)
	static class TestXmlBean{
		
		@XmlCDATA
		private String name;
		@XmlCDATA
		private Integer age;
		@XmlCDATA
		private String hobby;
		@XmlCDATA
		private Integer sex;
		
		@XmlTransient
		private String id;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getHobby() {
			return hobby;
		}
		public void setHobby(String hobby) {
			this.hobby = hobby;
		}
		public Integer getSex() {
			return sex;
		}
		public void setSex(Integer sex) {
			this.sex = sex;
		}
		@Override
		public String toString() {
			return "TestXmlBean [name=" + name + ", age=" + age + ", hobby=" + hobby + ", sex=" + sex + ", id=" + id
					+ "]";
		}
	}
}
