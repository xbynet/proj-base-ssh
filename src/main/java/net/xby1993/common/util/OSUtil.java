package net.xby1993.common.util;

public class OSUtil {
	
	public static boolean isWin(){
		String ostype=System.getProperty("os.name");
		if("windows".equalsIgnoreCase(ostype)){
			return true;
		}
		return false;
	}
	public static void main(String[] args) {
		System.out.println(System.getProperty("os.name"));
	}
}
