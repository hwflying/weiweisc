package com.weiweisc.util;

import java.io.Serializable;
import java.util.RandomAccess;

/**
 * String 类型的堆栈实现(stack).主要为了效率.
 */
public class StringStack implements RandomAccess, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2643675761496546439L;
	private int count = 0;
	private int increase = 0;
	private String entry[] = null;
	private int initSize = 10;

	/**
	 * constructor.
	 */
	public StringStack() {
		this(10, 10);
	}

	public StringStack(int init) {
		this(init, 10);
	}

	/**
	 * initialize the stack.
	 * 
	 * @param init
	 * @param inc
	 */
	public StringStack(int init, int inc) {
		if (init < 1)
			throw new IllegalArgumentException("Illegal Capacity: " + init);

		initSize = init;
		entry = new String[init];
		if (inc < 1)
			increase = 10;
		else
			increase = inc;

		count = 0;
	}

	/**
	 * 判断堆栈中是否有条目.
	 * 
	 * @return
	 */
	public boolean empty() {
		synchronized (entry) {
			return (count == 0);
		}
	}

	/**
	 * 判断堆栈中是否没条目.
	 * 
	 * @return
	 */
	public boolean notEmpty() {
		synchronized (entry) {
			return (count > 0);
		}
	}

	/**
	 * 压栈动作.
	 * 
	 * @param item
	 */
	public void push(String item) {
		if (item == null)
			return;

		synchronized (entry) {
			ensureCapacity();
			entry[count] = item;
			count++;
		}
	}

	/**
	 * this method need synchronized where it be overrided.
	 */
	protected void ensureCapacity() {
		if ((entry.length <= count) || (entry.length > (count + increase * 2))) {
			String temp[] = entry;
			entry = new String[count + increase];
			System.arraycopy(temp, 0, entry, 0, count);
			temp = null;
		}
	}

	/**
	 * 出栈动作.
	 * 
	 * @return
	 */
	public String pop() {
		String item = null;
		synchronized (entry) {
			if (count == 0)
				return null;
			item = entry[count - 1];
			entry[count - 1] = null;
			count--;
			ensureCapacity();
		}
		return item;
	}

	/**
	 * release the stack.
	 */
	public void clear() {
		synchronized (entry) {
			for (int i = 0; i < entry.length; i++)
				entry[i] = null;
			count = 0;
			entry = new String[initSize];
		}
	}

	/**
	 * get index element.
	 * 
	 * @param index
	 * @return
	 */
	public String get(int index) {
		if ((index < 0) || (index >= count))
			throw new ArrayIndexOutOfBoundsException("Illegal index of stack:"
					+ index);

		synchronized (entry) {
			return entry[index];
		}
	}

	/**
	 * return the size of stack.
	 * 
	 * @return
	 */
	public int size() {
		synchronized (entry) {
			return count;
		}
	}

	/**
	 * 硬拷贝数据对象.
	 * 
	 * @return
	 */
	public String[] toStringArray() {
		if (count == 0)
			return new String[0];

		synchronized (entry) {
			String rtn[] = new String[count];
			System.arraycopy(entry, 0, rtn, 0, count);
			return rtn;
		}
	}

	/**
	 * toString() override.
	 * 
	 * @return
	 */
	public String toString() {
		StringBuffer rtn = new StringBuffer();
		synchronized (entry) {
			rtn.append("entry.length=[");
			rtn.append(entry.length);
			rtn.append("],count=[");
			rtn.append(count);
			rtn.append("],initSize=[");
			rtn.append(initSize);
			rtn.append("],increase=[");
			rtn.append(increase);
			rtn.append("]");
		}

		return rtn.toString();
	}

	/**
	 * to override the super.hashCode.
	 * 
	 * @return
	 */
	public int hashCode() {
		int total = 37 * 17;
		total = total * 17 + count;
		total = total * 17 + initSize;
		total = total * 17 + increase;
		if (count > 0) {
			synchronized (entry) {
				for (int i = 0; i < count; i++)
					total = total * 17 + entry[i].hashCode();
			}
		}
		return total;
	}

	/**
	 * to override the super.equals.
	 * 
	 * @return
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj == this)
			return true;
		else
			return false;
	}

	/**
	 * peek
	 * 
	 * @return
	 */
	public String peek() {
		synchronized (entry) {
			if (count == 0)
				return null;
			else
				return entry[count - 1];
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
		synchronized (entry) {
			s.defaultWriteObject();
		}
	}
}
