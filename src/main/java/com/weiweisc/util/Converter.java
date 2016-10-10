package com.weiweisc.util;

import java.io.*;

/**
 * 字节转换器.可以通过转换器从字节数组转换为基本整形/长整形/短整形/系列化对象.
 * 
 * @author Relaxux.com
 * @version 1.0
 * @see
 */
public final class Converter {

	/**
	 * protected constructor.
	 */
	private Converter() {
	}

	/**
	 * 使用byte数组的某4个byte转换成整形.
	 * 
	 * @param array
	 * @return
	 */
	public static int bytesToInt(byte[] array, int off) {
		int b1, b2, b3, b4;

		b1 = (array[off++] << 24) & 0xFF000000;
		b2 = (array[off++] << 16) & 0x00FF0000;
		b3 = (array[off++] << 8) & 0x0000FF00;
		b4 = (array[off++] << 0) & 0x000000FF;

		return (b1 | b2 | b3 | b4);
	}

	/**
	 * 使用整形值转换成byte数组中的某4个byte.
	 * 
	 * @param value
	 * @param array
	 * @param offset
	 */
	public static void intToBytes(int value, byte[] array, int offset) {
		array[offset++] = (byte) ((value >>> 24) & 0xFF);
		array[offset++] = (byte) ((value >>> 16) & 0xFF);
		array[offset++] = (byte) ((value >>> 8) & 0xFF);
		array[offset++] = (byte) ((value >>> 0) & 0xFF);
	}

	/**
	 * 通过一个整形值返回一个4byte的数组,这个数组是整形的物理表达方式.
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] intToBytes(int value) {
		byte[] rtn = new byte[4];
		intToBytes(value, rtn, 0);
		return rtn;
	}

	/**
	 * 使用byte数组的某8个byte转换成长整形.
	 * 
	 * @param array
	 * @return
	 */
	public static long bytesToLong(byte[] array, int off) {
		long high = bytesToInt(array, off) & 0xFFFFFFFFL;
		long low = bytesToInt(array, off + 4) & 0xFFFFFFFFL;
		return (high << 32) | low;
	}

	/**
	 * 使用长整形值转换成byte数组中的某8个byte.
	 * 
	 * @param value
	 */
	public static void longToBytes(long value, byte[] array, int offset) {
		array[offset++] = (byte) ((value >>> 56) & 0xFF);
		array[offset++] = (byte) ((value >>> 48) & 0xFF);
		array[offset++] = (byte) ((value >>> 40) & 0xFF);
		array[offset++] = (byte) ((value >>> 32) & 0xFF);
		array[offset++] = (byte) ((value >>> 24) & 0xFF);
		array[offset++] = (byte) ((value >>> 16) & 0xFF);
		array[offset++] = (byte) ((value >>> 8) & 0xFF);
		array[offset++] = (byte) ((value >>> 0) & 0xFF);
	}

	/**
	 * 使用短整形值转换成byte数组中的某2个byte.
	 * 
	 * @param value
	 * @param array
	 * @param offset
	 */
	public static void shortToBytes(short value, byte[] array, int offset) {
		array[offset++] = (byte) ((value >>> 8) & 0xFF);
		array[offset++] = (byte) ((value >>> 0) & 0xFF);
	}

	/**
	 * 使用byte数组的某2个byte转换成短整形.
	 * 
	 * @param array
	 * @param offset
	 * @return
	 */
	public static short bytesToShort(byte[] array, int offset) {
		int high = array[offset] & 0xFF;
		int low = array[offset + 1] & 0xFF;
		return (short) ((high << 8) | low);
	}

	/**
	 * 把可序列化对象实例转变成byte数组.
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] objectToBytes(Object obj) throws IOException {
		/*
		 * if ( obj==null ) return new byte[]{ new Null().nullValue() } ; else
		 */if (obj instanceof Serializable) {
			ByteArrayOutputStream bout = new ByteArrayOutputStream(256);
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeObject(obj);
			byte rtn[] = bout.toByteArray();
			out.close();
			return rtn;
		} else
			throw new RuntimeException("Object[" + obj.getClass().getName()
					+ "] is not instance of Serializable");
	}

	/**
	 * 把一个序列化后的bytes数组转化成一个对象实例.
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static Object bytesToObject(byte[] stream) throws IOException,
			ClassNotFoundException {
		if (stream == null || stream.length == 0)
			return null;
		else {
			ByteArrayInputStream binput = new ByteArrayInputStream(stream);
			ObjectInputStream input = new ObjectInputStream(binput);
			Object obj = input.readObject();
			input.close();
			return obj;
		}
	}
	/*
	 * public static void main(String args[]) throws Exception { MsgPackage str
	 * = new MsgPackage() ; System.out.println( "before  str=[" +
	 * str.getTimestamp() + "]" );
	 * 
	 * byte[] data = objectToBytes( str ) ;
	 * 
	 * System.out.println( "data length=[" + data.length + "]" );
	 * 
	 * Object xstr = bytesToObject( data );
	 * 
	 * System.out.println( "after  str=[" + xstr.getClass().getName() + "]" ); }
	 */
}
