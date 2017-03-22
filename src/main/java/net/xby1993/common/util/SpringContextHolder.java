package net.xby1993.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringContextHolder implements ApplicationContextAware{

    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }
    public static Object getBean(String name){
    	return context.getBean(name);
    }
    public static <T> T getBean(Class<T> clz){
    	return context.getBean(clz);
    }
    public static <T> T getBean(String name , Class<T> clz){
    	return context.getBean(name, clz);
    }
    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        context = ac;
    }
}