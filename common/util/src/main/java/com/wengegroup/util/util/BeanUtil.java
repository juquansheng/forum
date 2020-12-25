package com.wengegroup.util.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;

import java.util.*;


public class BeanUtil {


	private static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

	/**
	 * 对象转map
	 * @param bean
	 * @param <T>
	 * @return
	 */
	public static <T> Map<String, Object> beanToMap(T bean) {
		Map<String, Object> map = new HashMap<>();
		if (bean != null) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				if(beanMap.get(key) != null){
					map.put(key + "", beanMap.get(key));
				}
			}
		}
		return map;
	}

	/**
	 * 对象转String
	 */
	public static <T> String beanToString(T value) {
		if (value == null) {
			return null;
		}
		Class<?> clazz = value.getClass();
		if (clazz == int.class || clazz == Integer.class) {
			return "" + value;
		} else if (clazz == String.class) {
			return (String) value;
		} else if (clazz == long.class || clazz == Long.class) {
			return "" + value;
		} else {
			return JSON.toJSONString(value);
		}
	}

	/**
	 * String转对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T stringToBean(String str, Class<T> clazz) {
		if (str == null || str.length() <= 0 || clazz == null) {
			return null;
		}
		if (clazz == int.class || clazz == Integer.class) {
			return (T) Integer.valueOf(str);
		} else if (clazz == String.class) {
			return (T) str;
		} else if (clazz == long.class || clazz == Long.class) {
			return (T) Long.valueOf(str);
		} else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}


	/**
	 * 批量对象转换
	 * @param sourceList 要求集合元素类型一致
	 * @param targetClass
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> beanBatchCopy(List sourceList, Class<T> targetClass) throws IllegalAccessException, InstantiationException {
		ArrayList<T> targetList = new ArrayList<>();
		if (sourceList == null || sourceList.size() == 0) {
			return targetList;
		}

		for (Object o : sourceList) {
			T target = targetClass.newInstance();
			beanCopy(o,target);
			targetList.add(target);
		}

		return targetList;
	}

	/**
	 * 对象拷贝，只拷贝完全一致的属性
	 * @param source
	 * @param targetClass 目标对象class
	 */
	public static <T> T beanCopy(Object source,Class<T> targetClass) {
		String simpleName = targetClass.getSimpleName();
		T instance = null;
		try {
			instance = targetClass.newInstance();
			beanCopy(source,instance,null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(simpleName + "反射创建对象失败");
		}
		return instance;
	}


	/**
	 * 对象拷贝，只拷贝完全一致的属性
	 * @param source
	 * @param target
	 */
	public static void beanCopy(Object source,Object target) {
		beanCopy(source,target,null);
	}

	/**
	 * 对象拷贝，根据自定义Converter 实现对象拷贝，适合复杂对象拷贝
	 * @param source
	 * @param target
	 * @param converter
	 */
	public static void beanCopy(Object source, Object target, Converter converter) {
		if (source == null) {
			logger.warn("beanCopy,source is null");
			return;
		}

		boolean usrConverter = true;
		if (converter == null) {
			usrConverter = false;
		}
		BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), usrConverter);
		copier.copy(source, target, converter);
	}

	public static Map<String, Object> bean2Map(Object obj) {
		Map<String,Object> map = new LinkedHashMap<>();
		if (obj == null) {
			return map;
		}
		//使用fastJson自动去除值为null的字段，obj属性最好都为引用类型
		JSONObject json = JSONObject.parseObject(JSON.toJSONString(obj));

		json.forEach(map::put);
		return map;
	}
}

