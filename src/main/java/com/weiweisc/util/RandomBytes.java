package com.weiweisc.util;

import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.net.InetAddress;

/**
 * 获取一个JVM实例中的随机byte数组. 本随机工具使用{@link SecureRandom}作为随机制造器,JVM系统时间和本地ip作为种子.
 * 
 */
public final class RandomBytes {

	private static SecureRandom random = null;
	private static byte[] seed = new byte[8];
	private static int ip = 0;
	// initialize
	static {
		long jvm = System.currentTimeMillis();
		try {
			ip = Converter.bytesToInt(InetAddress.getLocalHost().getAddress(),
					0);
		} catch (Exception e) {
			ip = 0;
		}
		jvm = jvm + ip;
		Converter.longToBytes(jvm, seed, 0);
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(seed);
		} catch (NoSuchAlgorithmException nsa_ex) {
			random = new SecureRandom(seed);
		}
	}

	/**
	 * protected constructor.
	 */
	private RandomBytes() {
	}

	/**
	 * 获取下一个随机byte数组.
	 * 
	 * @param len
	 *            需要数组的长度
	 * @return
	 */
	public static byte[] nextBytes(int len) {
		if (len < 1)
			throw new IllegalArgumentException("length[" + len + "]");

		byte[] rtn = new byte[len];
		random.nextBytes(rtn);

		return rtn;
	}
}
