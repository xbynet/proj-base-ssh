/*package net.xby1993.common.webmagic.tb;

import net.xby1993.common.webmagic.downloader.PhantomjsWebDriverPool;
import net.xby1993.common.webmagic.downloader.WebDriverPool;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlimamaLoginUtil {
	
	private static final Logger log=LoggerFactory.getLogger(AlimamaLoginUtil.class);
	
	public static synchronized void doLogin(){
		WebDriverPool webDriverPool=new PhantomjsWebDriverPool(1, true);
		WebDriver webDriver=null;
		try {
			webDriver = webDriverPool.get();
			WebDriver.Options manage = webDriver.manage();
			manage.window().maximize();
			LoginAction loginAction=new LoginAction();
			loginAction.execute(webDriver);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("获取webdriver失败",e);
		}finally{
			if(webDriver!=null){
				webDriverPool.returnToPool(webDriver);
				webDriverPool.shutdown();
			}
		}
	}
}
*/