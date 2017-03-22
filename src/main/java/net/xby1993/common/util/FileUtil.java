package net.xby1993.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	private static  final Logger log=LoggerFactory.getLogger(FileUtil.class);
	private static final ConcurrentHashMap<String,Properties> props=new ConcurrentHashMap<String,Properties>();
	/**
	 * 以MB返回文件大小
	 * @param path
	 * @return
	 */
	public static int getFileSizeMB(String path){
		File file=new File(path);
		if(file!=null&&file.exists()){
			long len=file.length();
			int size=(int)(len/(1024*1024));
			return size;
		}
		return 0;
	}
	public static Properties loadPropertyFile(String clzPath){
		if(props.containsKey(clzPath)){
			return props.get(clzPath);
		}
		Properties prop=new Properties();
		InputStream ins=FileUtil.class.getClassLoader().getResourceAsStream(clzPath);
		try {
			prop.load(ins);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(ins);
		}
		props.put(clzPath, prop);
		return prop;
	}

	public static String getCommonProp(String key){
		Properties prop=loadPropertyFile("common.properties");
		return prop.getProperty(key);
	}
	
	public static void main(String[] args) {
		System.out.println(getCommonProp("phantomjs.path"));
	}

}