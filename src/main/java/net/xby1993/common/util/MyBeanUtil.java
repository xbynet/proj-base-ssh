package net.xby1993.common.util;

import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.beans.BeanCopier;

public class MyBeanUtil {
	// 使用多线程安全的Map来缓存BeanCopier，由于读操作远大于写，所以性能影响可以忽略
	public static ConcurrentHashMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<String, BeanCopier>();

	public static void copyProperties(Object source, Object target) {
		String beanKey = generateKey(source.getClass(), target.getClass());
		BeanCopier copier = null;
		copier = BeanCopier.create(source.getClass(), target.getClass(), false);
		beanCopierMap.putIfAbsent(beanKey, copier);// putIfAbsent已经实现原子操作了。
		copier = beanCopierMap.get(beanKey);
		copier.copy(source, target, null);
	}

	private static String generateKey(Class<?> class1, Class<?> class2) {
		return class1.toString() + class2.toString();
	}
}
