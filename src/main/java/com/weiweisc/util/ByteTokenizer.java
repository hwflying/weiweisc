package com.weiweisc.util;

import java.util.Enumeration;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * 参照java.util.StringTokenizer所作,功能跟它很像,只是这是针对字节数组byte[].
 * 
 * @author Relaxux.com
 * @version 1.0
 * @see
 */
public class ByteTokenizer implements Enumeration<byte[]> {

	private byte[] delimiters;
	private byte[] source;
	private int current;
	private int newPos;
	private int end;
	//
	private byte maxDelim = 0;

	/**
	 * constructor.
	 * 
	 * @param src
	 * @param delims
	 */
	public ByteTokenizer(byte[] src, int off, int xlen, byte[] delims) {
		source = src;
		delimiters = delims;
		current = off;
		if (current < 0)
			throw new RuntimeException("offset is negative");

		end = off + xlen;
		//
		if (off + xlen > source.length)
			throw new RuntimeException("off+xlen>source.length");
		//
		if (delimiters == null || delimiters.length == 0)
			throw new NullPointerException("delimiters is empty");
		// 排序
		if (delimiters.length > 1)
			Arrays.sort(delimiters);

		maxDelim = delimiters[delimiters.length - 1];
	}

	/**
	 * constructor.
	 * 
	 * @param src
	 * @param off
	 * @param xlen
	 * @param delim
	 */
	public ByteTokenizer(byte[] src, int off, int xlen, byte delim) {
		this(src, off, xlen, new byte[] { delim });
	}

	/**
	 * constructor.
	 * 
	 * @param src
	 * @param delims
	 */
	public ByteTokenizer(byte[] src, byte[] delims) {
		this(src, 0, src.length, delims);
	}

	/**
	 * constructor.
	 * 
	 * @param src
	 * @param delim
	 */
	public ByteTokenizer(byte[] src, byte delim) {
		this(src, 0, src.length, delim);
	}

	/**
	 * 是否还有下一个内容.
	 * 
	 * @return
	 */
	public boolean hasMoreTokens() {
		newPos = skipDelimiters(current);
		return (newPos < end);
	}

	/**
	 * 获取下一个内容.
	 * 
	 * @return
	 */
	public byte[] nextToken() {
		current = skipDelimiters(current);
		if (current >= end)
			throw new NoSuchElementException();

		int start = current;
		current = scanToken(current);
		byte[] rtn = new byte[current - start];
		System.arraycopy(source, start, rtn, 0, rtn.length);
		return rtn;
	}

	/**
	 * 检索下一个分隔符的位置.
	 * 
	 * @param index
	 * @return
	 */
	private int scanToken(int index) {
		int position = index;
		while (position < end) {
			byte b = source[position];
			// 如果遇到分隔符,退出
			if (b <= maxDelim && Arrays.binarySearch(delimiters, b) >= 0)
				break;
			position++;
		}

		return position;
	}

	/**
	 * 跳过所有连续的分隔符.
	 * 
	 * @param index
	 * @return
	 */
	private int skipDelimiters(int index) {
		int position = index;
		while (position < end) {
			byte b = source[position];
			// 如果不是分隔符(delimiter),退出
			if (b > maxDelim || Arrays.binarySearch(delimiters, b) < 0)
				break;
			position++;
		}
		return position;
	}

	/**
	 * 返回数组中分割子段的个数.
	 * 
	 * @return
	 */
	public int countTokens() {
		int count = 0;
		int currpos = current;
		while (currpos < end) {
			currpos = skipDelimiters(currpos);
			if (currpos >= end)
				break;
			currpos = scanToken(currpos);
			count++;
		}
		return count;
	}

	/**
	 * implement Enumeration.hasMoreElements.
	 * 
	 * @return hasMoreTokens()
	 */
	public boolean hasMoreElements() {
		return hasMoreTokens();
	}

	/**
	 * implement Enumeration.nextElement.
	 * 
	 * @return byte[]
	 */
	public byte[] nextElement() {
		return nextToken();
	}
}
