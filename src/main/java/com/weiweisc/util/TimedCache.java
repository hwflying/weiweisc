package com.weiweisc.util;

import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个ObjectCache.对于一些对时间上要求比较过期时间的cache,可以使用此类.
 */
public class TimedCache {

	/* default cache for half en hour */
	private final static int DEFAULT_MAX_SIZE = 1000;
	/* default cache for half en hour */
	private final static int DEFAULT_INTERVAL_TIME = 10 * 60 * 1000;

	private ConcurrentHashMap<Object, TimedObject> cache = null;

	private int INTERVAL_TIME = 0;
	private int MAX_SIZE = 0;

	/**
	 * default constructor.
	 */
	public TimedCache() {
		init();
	}

	/**
	 * constructor.
	 * 
	 * @param size
	 *            max size of cache
	 * @param interval
	 *            interval time of refresh:second
	 */
	public TimedCache(int size, int interval) {
		MAX_SIZE = size;
		INTERVAL_TIME = interval * 1000;
		init();
	}

	/**
	 * initialize the cache.
	 */
	private void init() {
		if (MAX_SIZE < 1)
			MAX_SIZE = DEFAULT_MAX_SIZE;

		if (INTERVAL_TIME < 1000)
			INTERVAL_TIME = DEFAULT_INTERVAL_TIME;

		cache = new ConcurrentHashMap<Object, TimedObject>(MAX_SIZE);
	}

	/**
	 * add key,value to the cache.
	 * 
	 * @param key
	 * @param value
	 */
	public void add(Object key, Object value) {
		if (key == null || value == null)
			return;

//		synchronized (cache) {
		if (cache.size() < MAX_SIZE) {
			cache.put(key, new TimedObject(value));
			return;
		} else
			refresh();
		//
		if (cache.size() < MAX_SIZE)
			cache.put(key, new TimedObject(value));
//		}
	}

	/**
	 * get value from cache use key.
	 * 
	 * @param key
	 * @return
	 */
	public Object get(Object key) {
//		synchronized (cache) {
		TimedObject obj = (TimedObject) cache.get(key);
		if (obj != null) {
			if (System.currentTimeMillis() - obj.getTime() > INTERVAL_TIME) {
				cache.remove(key);
				return null;
			} else {
				obj.setTime(System.currentTimeMillis());
				return obj.getValue();
			}
		} else
			return null;
//		}
	}

	/**
	 * refresh the cache.
	 */
	private void refresh() {
		Set<Map.Entry<Object, TimedObject>> entrys = cache.entrySet();
		Iterator<Map.Entry<Object, TimedObject>> it = entrys.iterator();
		for (; it.hasNext();) {
			Map.Entry<Object, TimedObject> entry = it.next();
			TimedObject obj = entry.getValue();
			if (obj == null
					|| System.currentTimeMillis() - obj.getTime() > INTERVAL_TIME) {
				it.remove();
			}
		}
	}

	/**
	 * return the cache current size.
	 */
	public int size() {
//		synchronized (cache) {
			return cache.size();
//		}
	}

	/**
	 * 对象时间戳,通过对象存储的时间戳进行更新.
	 */
	static class TimedObject {

		Object obj = null;
		long time = System.currentTimeMillis();

		public TimedObject(Object x) {
			obj = x;
		}

		public Object getValue() {
			return obj;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long x) {
			time = x;
		}
	}
}
