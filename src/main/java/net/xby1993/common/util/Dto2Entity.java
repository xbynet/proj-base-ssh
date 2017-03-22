package net.xby1993.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.beans.BeanCopier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dto2Entity {
	private static final Logger log = LoggerFactory.getLogger(Dto2Entity.class);
	// 使用多线程安全的Map来缓存BeanCopier，由于读操作远大于写，所以性能影响可以忽略
		public static ConcurrentHashMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<String, BeanCopier>();
		
		/**
		 * 通过cglib BeanCopier
		 * @param source
		 * @param target
		 */
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

	/**
	 * 通过反射形式
	 * DTO对象转换为实体对象。如命名不规范或其他原因导致失败返回null
	 * @param t
	 *            进行转换的对象
	 * @param e
	 *            进行转换的对象
	 * @return 实体对象
	 */
	@Deprecated
	public static <T, E> void transalteByReflection(T t, E e) {
		Method[] tms = t.getClass().getDeclaredMethods();
		Method[] tes = e.getClass().getDeclaredMethods();

		for (Method m1 : tms) {
			if (m1.getName().startsWith("get")||m1.getName().startsWith("is")) {
				String mNameSubfix="";
				if(m1.getName().startsWith("get")){
					mNameSubfix = m1.getName().substring(3);
				}else{
					mNameSubfix = m1.getName().substring(2);
				}
				String forName = "set" + mNameSubfix;
				for (Method m2 : tes) {
					if (m2.getName().equals(forName)) {
						// 如果参数类型一致，或者m1的返回类型是m2的参数类型的父类或接口
						boolean canContinue = m1.getReturnType().isAssignableFrom(m2.getParameterTypes()[0]);
						if (canContinue) {
							try {
								m2.invoke(e, m1.invoke(t));
								break;
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
								// TODO Auto-generated catch block
								log.debug("DTO 2 Entity转换失败");
								e1.printStackTrace();
							}
						}
					}
				}
			}

		}
		log.debug("转换完成");

	}
//	/**
//	 *为了性能考虑，采用cglib来创建动态代理对象
//	 * @param t
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public static <T> T generateProxy(T t){
//		Enhancer enhancer=new Enhancer();
//		enhancer.setSuperclass(t.getClass());
//		//直接调用方式的回调
//		enhancer.setCallback(NoOp.INSTANCE);
//		//设置类装载器
//		enhancer.setClassLoader(t.getClass().getClassLoader());
//		return (T)enhancer.create();
//	}
	public static void main(String[] args) {
//		GlobalConfig a=new GlobalConfig();
//		GlobalConfig b=new GlobalConfig();
//		a.setId("111");
//		a.setIsFirst(0);
//		long start=System.currentTimeMillis();
//		for(int i=0;i<1000*100*100;i++){
//			//测试1
//			//transalte(a, b); 
//			//测试2
//			/*BeanMap map1=BeanMap.create(a);
//			BeanMap map2=BeanMap.create(b);
//			for(Object key:map1.keySet()){
//				if(map2.containsKey(key)){
//					map2.put(key, map1.get(key));
//				}
//			}*/
//			
//			//测试3
//			copyProperties(a,b);
//
//		}
//		
//		long end=System.currentTimeMillis();
//		System.out.println(end-start);
//		System.out.println("id:"+b.getId());
	}
}
