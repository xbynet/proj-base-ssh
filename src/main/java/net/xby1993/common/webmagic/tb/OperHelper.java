package net.xby1993.common.webmagic.tb;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class OperHelper {
	/**
	 * 检查二维码是否失效
	 */
	public static String checkQrCode(WebDriver driver){
		InputStream ins=OperHelper.class.getResourceAsStream("checkQrCode.js");
		String externalJS="";
		try {
			externalJS = IOUtils.toString(ins,"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		IOUtils.closeQuietly(ins);
		Object ignore = ((JavascriptExecutor) driver).executeScript(externalJS);
		return (String)ignore;
	}
}
