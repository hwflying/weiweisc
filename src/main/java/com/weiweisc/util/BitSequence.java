package com.weiweisc.util;

import java.math.BigInteger;
import java.io.Serializable;

/**
 * 一个bit序列封装.理论上支持无线位数的bit值. 但是对于无限大的位数对我们的实际应用应该也是没意义的. 在应用中,我们还是很多情况都会使用位列操作的.
 * 这个封装跟 {@link java.util.BitSet} 有很多相似的地方,但是BitSequence的实现简单,
 * 而且占用内存少,因为它是使用真正的物理bit位来表示<b>bit</b>的. 本封装使用 boolean:<b>true</b>|<b>false</b>
 * 来表示 bit:<b>1</b>|<b>0</b> .
 * 
 * <pre>
 *   例如:
 *      BitSequence bits = new BitSequence();
 *      bits.set( 8 );
 *      bits.set( 2 ) ;
 *      bits.get( 8 ) == true ;
 *      bits.get( 0 ) == false ;
 * 
 *      bits.getBigInteger().intValue() == 260 ;
 * 
 *      bits.or( otherBitSequence )  this | other ;
 *      bits.and( otherBitSequence ) this & other ;
 *      bits.xor( otherBitSequence ) this ^ other ;
 * </pre>
 * 
 * @author Relaxux.com
 * @version 1.0
 * @see
 */
@SuppressWarnings("rawtypes")
public class BitSequence implements Cloneable, Comparable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1878612239467317967L;
	private transient byte[] bits = null;
	private static short HI_MASK = 0x80;
	/**
	 * a zero BitSequence instance.
	 */
	public final static BitSequence ZERO = new BitSequence();

	/**
	 * default constructor.
	 */
	public BitSequence() {
		bits = new byte[4];
	}

	/**
	 * 
	 * @param value
	 */
	private BitSequence(byte[] value) {
		if (value == null)
			throw new NullPointerException();

		if (value.length == 0)
			bits = new byte[4];
		else {
			bits = new byte[value.length];
			System.arraycopy(value, 0, bits, 0, value.length);
		}
	}

	/**
	 * 返回当前实例中位(bit)的长度.
	 * 
	 * @return
	 */
	public int length() {
		return bits.length * 8;
	}

	/**
	 * 判断某一个位是否为'1'.
	 * 
	 * @param index
	 * @return
	 */
	public boolean get(int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException("index < 0 :" + index);

		if (index >= length())
			return false;

		byte unit = unitIndex(index);
		short value = indexValue(index);

		return (value & unit) > 0;
	}

	/**
	 * 设置某一个位为'1'.
	 * 
	 * @param index
	 */
	public void set(int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException("index < 0 :" + index);
		//
		ensureCapacity(index);
		//
		byte unit = unitIndex(index);
		unit = (byte) (unit | indexValue(index));
		int off = (length() - index - 1) / 8;
		bits[off] = unit;
	}

	/**
	 * 设置某一个位为'0'.
	 * 
	 * @param index
	 */
	public void unset(int index) {
		if (get(index)) {
			ensureCapacity(index);
			byte unit = unitIndex(index);
			unit = (byte) (unit & (~indexValue(index)));
			int off = (length() - index - 1) / 8;
			bits[off] = unit;
		}
	}

	/**
	 * <b>与</b>操作.
	 * 
	 * @param bs
	 */
	public void and(BitSequence bs) {
		int off = bits.length - bs.bits.length;
		if (off <= 0) {
			for (int i = 0; i < bits.length; i++) {
				bits[i] = (byte) (bits[i] & bs.bits[i - off]);
			}
		} else // if ( off>0 )
		{
			for (int i = 0; i < bits.length; i++) {
				if (i < off)
					bits[i] = 0;
				else
					bits[i] = (byte) (bits[i] & bs.bits[i - off]);
			}
		}
	}

	/**
	 * <b>或</b>操作.
	 * 
	 * @param bs
	 */
	public void or(BitSequence bs) {
		// 如果被操作数比操作数长
		if ((bits.length - bs.bits.length) < 0) {
			byte[] temp = new byte[bs.bits.length];
			System.arraycopy(bits, 0, temp, temp.length - bits.length,
					bits.length);
			bits = temp;
		}
		int off = bits.length - bs.bits.length;
		for (int i = off; i < bits.length; i++) {
			bits[i] = (byte) (bits[i] | bs.bits[i - off]);
		}
	}

	/**
	 * <b>异或</b>操作.
	 * 
	 * @param bs
	 */
	public void xor(BitSequence bs) {
		int off = bits.length - bs.bits.length;
		byte[] temp = null;
		if (off == 0) {
			for (int i = off; i < bits.length; i++) {
				bits[i] = (byte) (bits[i] ^ bs.bits[i]);
			}
		} else if (off > 0) {
			temp = new byte[bits.length];
			System.arraycopy(bs.bits, 0, temp, off, bs.bits.length);
			for (int i = off; i < temp.length; i++) {
				bits[i] = (byte) (bits[i] ^ temp[i]);
			}
		} else // off<0
		{
			temp = new byte[bs.bits.length];
			System.arraycopy(bits, 0, temp, off, bits.length);
			for (int i = off; i < temp.length; i++) {
				bits[i] = (byte) (bs.bits[i] ^ temp[i]);
			}
		}
	}

	/**
	 * 获取无限bit序列的Number表示,永远大于等于0.
	 * 
	 * @return BigInteger表示
	 * @see #getOriginalBigInteger()
	 */
	public BigInteger getBigInteger() {
		if (bits[0] < 0) {
			byte[] temp = new byte[bits.length + 1];
			System.arraycopy(bits, 0, temp, 1, bits.length);
			return new BigInteger(temp);
		} else
			return new BigInteger(bits);
	}

	/**
	 * 获取当前bit序列的Number表示,可能小于0 .
	 * 
	 * @return BigInteger表示
	 * @see #getBigInteger()
	 */
	public BigInteger getOriginalBigInteger() {
		return new BigInteger(bits);
	}

	/**
	 * 根据index 查找它所包括的在的byte[]数组中的byte位置.
	 * 
	 * @param index
	 * @return
	 */
	private byte unitIndex(int index) {
		int off = (length() - index - 1) / 8;
		return bits[off];
	}

	/**
	 * 根据index 计算出此位置的位值,使用short的是为了做补位操作.
	 * 
	 * @param index
	 * @return
	 */
	private short indexValue(int index) {
		short value = (short) ((length() - index - 1) % 8);
		short bit = HI_MASK;
		return (short) (bit >> value);
	}

	/**
	 * 本类实现动态配置高位数据,由于bit序列在本类中认为高位是无止境的。 所以对于高位可以一直使用'0',不需分配实际内存.
	 * 
	 * @param index
	 */
	private void ensureCapacity(int index) {
		if (index > length() - 1) {
			int len = index;
			if ((len % 8) == 0)
				len = len / 8;
			else
				len = len / 8 + 1;

			byte[] temp = new byte[len + 4];
			System.arraycopy(bits, 0, temp, temp.length - bits.length,
					bits.length);
			bits = temp;
		}
	}

	/**
	 * {@link Object#hashCode()} .
	 * 
	 * @return
	 */
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * {@link Object#equals(Object)} .
	 * 
	 * @param obj
	 * @return
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof BitSequence))
			return false;
		BitSequence sbit = (BitSequence) obj;

		return (compareTo(sbit) == 0);
	}

	/**
	 * {@link Object#toString()} .
	 * 
	 * @return
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		boolean ready = false;
		for (int i = length() - 1; i > -1; i--) {
			if (get(i)) {
				sb.append("1");
				ready = true;
			} else if (ready)
				sb.append("0");
		}
		if (sb.length() > 0)
			return sb.toString();
		else
			return "0";
	}

	/**
	 * {@link Comparable#compareTo(Object)} .
	 * 
	 * @param o
	 * @return
	 */
	public int compareTo(Object o) {
		return compareTo((BitSequence) o);
	}

	/**
	 * 比较.
	 * 
	 * @param bs
	 * @return 1,0,-1
	 */
	public int compareTo(BitSequence bs) {
		return getBigInteger().compareTo(bs.getBigInteger());
	}

	/**
	 * 根据一个长整形构建一个BitSequence 实例.
	 * 
	 * @param value
	 *            long value
	 * @return BitSequence
	 */
	public static BitSequence valueOf(long value) {
		if (value == 0)
			return new BitSequence();
		else if (value > 0) {
			byte[] b = new byte[8];
			Converter.longToBytes(value, b, 0);
			return new BitSequence(b);
		} else {
			byte[] b = new byte[12];
			Converter.longToBytes(value, b, 4);
			return new BitSequence(b);
		}
	}

	/**
	 * instance to a stream .
	 * 
	 * @param s
	 * @throws java.io.IOException
	 */
	private void writeObject(java.io.ObjectOutputStream s)
			throws java.io.IOException {
		s.defaultWriteObject();
		s.writeObject(bits);
	}

	/**
	 * instance from a stream .
	 * 
	 * @param s
	 * @throws java.io.IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream s)
			throws java.io.IOException, ClassNotFoundException {
		s.defaultReadObject();
		bits = (byte[]) s.readObject();
	}
}
