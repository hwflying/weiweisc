package com.weiweisc.util;

/**
 * 一个积压byte,byte[]进栈的工具.功能跟ByteArrayOutputStream功能很像. 但是不同的是它没有繁琐的流处理.
 * 
 * <pre>
 *      BytesReader reader = new BytesReader();
 *      reader.append( 0x67 );
 *      reader.append( new byte[]{0x20,0x78,0x32} );
 *      reader.append( new byte[]{0x20,0x78,0x32},0,1 );
 *      byte[] b = reader.getBytes();
 *      b == {0x67,0x20,0x78,0x32,0x20}
 * </pre>
 * 
 * @author Relaxux.com
 * @version 1.0
 * @see
 */
public class BytesReader {

	private byte[] buf;
	private int bufSize = 0;
	private int initSize = 2048; // 2k

	/**
	 * default constrator.
	 * 
	 * @param len
	 */
	public BytesReader(int len) {
		buf = new byte[len];
		initSize = len;
	}

	/**
	 * 获取数据堆里的字节数据.
	 * 
	 * @return
	 */
	public synchronized byte[] getBytes() {
		byte[] b = new byte[bufSize];
		System.arraycopy(buf, 0, b, 0, bufSize);
		return b;
	}

	/**
	 * 添加数据.返回添加进去的字节长度.
	 * 
	 * @param source
	 *            数据源
	 * @param off
	 *            偏移位
	 * @param len
	 *            添加长度
	 * @return
	 */
	public synchronized int append(byte[] source, int off, int len) {
		if ((off < 0) || (off > source.length) || (len < 0)
				|| ((off + len) > source.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		int newcount = bufSize + len;
		if (newcount > buf.length) {
			byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
			System.arraycopy(buf, 0, newbuf, 0, bufSize);
			buf = newbuf;
		}
		System.arraycopy(source, off, buf, bufSize, len);
		bufSize = newcount;
		return len;
	}

	/**
	 * 添加一个字节.
	 * 
	 * @param source
	 *            数据源
	 * @return
	 */
	public synchronized int append(byte source) {
		int newcount = bufSize + 1;
		if (newcount > buf.length) {
			byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
			System.arraycopy(buf, 0, newbuf, 0, bufSize);
			buf = newbuf;
		}
		buf[bufSize] = source;
		bufSize = newcount;
		return 1;
	}

	/**
	 * 添加整个字节数组.
	 * 
	 * @param source
	 * @return
	 */
	public synchronized int append(byte[] source) {
		return append(source, 0, source.length);
	}

	/**
	 * 返回当前数据堆中的数据字节数.
	 * 
	 * @return
	 */
	public synchronized int size() {
		return bufSize;
	}

	/**
	 * 重置数据堆,清空.
	 */
	public synchronized void reset() {
		bufSize = 0;
		if (buf.length > initSize) {
			buf = new byte[initSize];
		}
	}

	/**
	 * 废弃.
	 */
	public synchronized void close() {
		bufSize = 0;
		buf = null;
	}
}
