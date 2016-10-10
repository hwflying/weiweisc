package com.weiweisc.util;

import java.util.Random;

/**
 * 随机子串产生器.
 */
public class RandomCharacter {

	private static Random random = new Random(System.currentTimeMillis());
	private static char[] items = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
			'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z' };

	/**
	 * 获取随机字窜.len<1时，返回空串.
	 * 
	 * @param len
	 * @return
	 */
	public synchronized static String getRandomString(int len) {
		if (len < 1)
			return "";

		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			int position = random.nextInt(62);
			sb.append(items[position]);
		}

		return sb.toString();
	}

	/**
	 * 获取随机字窜,只产生数字窜. len<1时，返回空串.
	 * 
	 * @param len
	 * @return
	 */
	public synchronized static String getRandomDigit(int len) {
		if (len < 1)
			return "";
		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			int position = random.nextInt(10);
			sb.append(items[position]);
		}

		return sb.toString();
	}

	/**
	 * 获取随机字串,只产生字母窜.len<1时，返回空串.
	 * 
	 * @param len
	 * @return
	 */
	public synchronized static String getRandomLetter(int len) {
		if (len < 1)
			return "";

		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			int position = random.nextInt(52);
			sb.append(items[position + 10]);
		}

		return sb.toString();
	}

	/**
	 * 获取随机整型数值.
	 * 
	 * @param num
	 * @return
	 */
	public synchronized static int randomInt(int num) {
		return random.nextInt(num);
	}

	/**
	 * 获取随机整型数值.
	 * 
	 * @return
	 */
	public synchronized static int randomInt() {
		return random.nextInt();
	}

	/**
	 * 获取随机长整型数值.
	 * 
	 * @return
	 */
	public synchronized static long randomLong() {
		return random.nextLong();
	}
	
	
	public static void main(String[] args) {
		for(int i=0;i<10;i++) {
			String code = RandomCharacter.getRandomDigit(4);
			System.out.println(code);
		}
	}
}
