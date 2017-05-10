package net.xby1993.common.webmagic.downloader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import net.xby1993.common.util.FileUtil;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChromeWebDriverPool implements WebDriverPool{
	private Logger logger = LoggerFactory.getLogger(getClass());

	private int CAPACITY = 5;
	private AtomicInteger refCount = new AtomicInteger(0);
	private static final String DRIVER_NAME = "chrome";

	/**
	 * store webDrivers available
	 */
	private BlockingDeque<WebDriver> innerQueue = new LinkedBlockingDeque<WebDriver>(
			CAPACITY);

	private static String DRIVER_PATH;
	private static DesiredCapabilities caps = DesiredCapabilities.chrome();
	static {
		DRIVER_PATH = FileUtil.getCommonProp("chrome.path");
		
		System.setProperty("webdriver.chrome.driver", FileUtil.getCommonProp("chrome.driver.path"));

		ChromeOptions options = new ChromeOptions();
//		options.addExtensions(new File("/path/to/extension.crx"))
//		options.setBinary(DRIVER_PATH); //注意chrome和chromeDirver的区别
		options.addArguments("test-type"); //ignore certificate errors
		options.addArguments("headless");// headless mode
		options.addArguments("disable-gpu"); 
//		options.addArguments("log-path=chromedriver.log");
//		options.addArguments("screenshot"); 没打开一个页面就截图
		//options.addArguments("start-maximized"); 最大化
		//Use custom profile
		Map<String, Object> prefs = new HashMap<String, Object>();
//		prefs.put("profile.default_content_settings.popups", 0);
		//http://stackoverflow.com/questions/28070315/python-disable-images-in-selenium-google-chromedriver
		prefs.put("profile.managed_default_content_settings.images",2); //禁止下载加载图片
		options.setExperimentalOption("prefs", prefs);
		
		
		caps.setJavascriptEnabled(true);
		caps.setCapability(ChromeOptions.CAPABILITY, options);		
//		caps.setCapability("takesScreenshot", false);
		
		/* Add the WebDriver proxy capability.
		Proxy proxy = new Proxy();
		proxy.setHttpProxy("myhttpproxy:3337");
		capabilities.setCapability("proxy", proxy);
		*/

	}

	public ChromeWebDriverPool() {
	}

	public ChromeWebDriverPool(int poolsize) {
		this.CAPACITY = poolsize;
		innerQueue = new LinkedBlockingDeque<WebDriver>(poolsize);
	}

	public WebDriver get() throws InterruptedException {
		WebDriver poll = innerQueue.poll();
		if (poll != null) {
			return poll;
		}
		if (refCount.get() < CAPACITY) {
			synchronized (innerQueue) {
				if (refCount.get() < CAPACITY) {

					WebDriver mDriver = new ChromeDriver(caps);
					// 尝试性解决：https://github.com/ariya/phantomjs/issues/11526问题
					mDriver.manage().timeouts()
							.pageLoadTimeout(60, TimeUnit.SECONDS);
					// mDriver.manage().window().setSize(new Dimension(1366,
					// 768));
					innerQueue.add(mDriver);
					refCount.incrementAndGet();
				}
			}
		}
		return innerQueue.take();
	}

	public void returnToPool(WebDriver webDriver) {
		// webDriver.quit();
		// webDriver=null;
		innerQueue.add(webDriver);
	}

	public void close(WebDriver webDriver) {
		refCount.decrementAndGet();
		webDriver.quit();
		webDriver = null;
	}

	public void shutdown() {
		try {
			for (WebDriver driver : innerQueue) {
				close(driver);
			}
			innerQueue.clear();
		} catch (Exception e) {
			// e.printStackTrace();
			logger.warn("webdriverpool关闭失败", e);
		}
	}
}
