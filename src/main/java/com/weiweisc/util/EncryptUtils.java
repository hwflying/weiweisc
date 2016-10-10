package com.weiweisc.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {
	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static String saltValue = "B602CDB2BB5EEEB3187BCCE5CAAE9D11";

	private static String encrypt(String message, String type) {
		try {
			if (message == null || message.trim().isEmpty() || "".equals(message.trim())){
				throw new IllegalArgumentException("请输入内容：");
			}
			MessageDigest digest = MessageDigest.getInstance(type);
			String newMessagg = getMessageAndSalt(message);
			digest.update(newMessagg.getBytes("UTF-8"));
			byte[] b = digest.digest();
			return new String(hex(b));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return message;
	}

	public static String getMessageAndSalt(String message) {
		if (message != null && !message.trim().isEmpty()) {
			return message + "{" + saltValue + "}";
		} else {
			return "内容不能为空";
		}
	}

	public static String sha(String str) {
		return encrypt(str, "sha-256");
	}
	
	public static String sha1(String str) {
	    if (str == null) {  
            return null;  
        }  
        try {  
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");  
            messageDigest.update(str.getBytes());  
            return getFormattedText(messageDigest.digest());  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
	}
	
	
    private static String getFormattedText(byte[] bytes) {  
        int len = bytes.length;  
        StringBuilder buf = new StringBuilder(len * 2);  
        // 把密文转换成十六进制的字符串形式  
        for (int j = 0; j < len; j++) {  
            buf.append(HEX[(bytes[j] >> 4) & 0x0f]);  
            buf.append(HEX[bytes[j] & 0x0f]);  
        }  
        return buf.toString();  
    }  
  
	

	private static char[] hex(byte[] bytes) {
		final int nBytes = bytes.length;
		char[] result = new char[2 * nBytes];
		int j = 0;
		for (int i = 0; i < nBytes; i++) {
			result[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
			result[j++] = HEX[(0x0F & bytes[i])];
		}
		return result;
	}
	public static String md5(String str) {
		return encrypt(str, "md5");
	}

	public static void main(String[] args){
		String pwd = EncryptUtils.sha("521happy125");

		System.out.println(pwd);
		System.out.println("6d32f534151c72a42da66ff900554d76cddbef4e41cf4869f9b81b73e4c73898".equals(pwd));
		System.out.println(pwd.length());
	}

}
