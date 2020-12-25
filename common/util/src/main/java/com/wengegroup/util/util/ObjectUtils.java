package com.wengegroup.util.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;


public class ObjectUtils {

	/**
	 * 判断map是否为空
	 * 
	 * @param map
	 * @return true 空 false 不为空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNullObj(Map map) {
		if (map == null) {
			return true;
		}
		return false;
	}

	/**
	 * 判断list 是否为空
	 * 
	 * @param list
	 * @return true 空 false 不为空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNullObj(List list) {
		if (list == null || list.isEmpty() || list.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断Object 是否为空
	 * 
	 * @param object
	 * @return true 空 false 不为空
	 */
	public static boolean isNullObj(Object object) {
		if (object == null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 判断数组是否为空
	 *
	 * @Author:suntongjie@itonghui.org
	 * @date: 2014-7-23 下午7:34:22
	 * @param args 数组
	 * @return boolean true 为空，false 不为空
	 */
	public static boolean isNullObj(String[] args) {
		if (args == null || args.length == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 对象转换成byte数组
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] objectToByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			objectOutputStream.flush();
			bytes = byteArrayOutputStream.toByteArray();

		} catch (IOException e) {
			// LOGGER.error("objectToByteArray failed, " + e);
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					// LOGGER.error("close objectOutputStream failed, " + e);
					e.printStackTrace();
				}
			}
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (IOException e) {
					// LOGGER.error("close byteArrayOutputStream failed, " + e);
					e.printStackTrace();
				}
			}

		}
		return bytes;
	}

	// 降序排序
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				int compare = (o1.getValue()).compareTo(o2.getValue());
				return -compare;
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	// 升序排序
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				int compare = (o1.getValue()).compareTo(o2.getValue());
				return compare;
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
