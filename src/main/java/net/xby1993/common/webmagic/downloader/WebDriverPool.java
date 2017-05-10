package net.xby1993.common.webmagic.downloader;

import org.openqa.selenium.WebDriver;

public interface WebDriverPool {
	WebDriver get() throws InterruptedException;
	void returnToPool(WebDriver webDriver);
	void close(WebDriver webDriver);
	void shutdown();
}
