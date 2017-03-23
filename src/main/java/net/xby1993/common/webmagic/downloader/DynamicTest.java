package net.xby1993.common.webmagic.downloader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.xpath.functions.FuncExtElementAvailable;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author taojw
 *
 */
public class DynamicTest implements PageProcessor {
	
	private static Site site=Site.me().setCharset("UTF-8").setUserAgent(
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
//		Reader r=new StringReader(page.getRawText());
		File f=new File("D:\\data\\test.html");
		BufferedWriter writer=null;
		try {
			writer=new BufferedWriter(new FileWriter(f));
			IOUtils.write(page.getRawText(), writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(writer);
		}
	}
	public  void start() {
		
    	Spider cnSpider = Spider.create(this).setDownloader(new SeleniumDownloader(5000,null,new TestAction()))
//    			.addUrl("https://shop34068488.taobao.com/?spm=a230r.7195193.1997079397.2.JLFlPa")
    			.addUrl("https://item.taobao.com/item.htm?spm=a1z10.5-c.w4002-7276872102.21.w5Qfbd&id=539743000108");
//    			.addPipeline(new JsonFilePipeline("D:\\data\\webmagicfile.json"))
    	
    	//SpiderMonitor.instance().register(cnSpider);
    	cnSpider.run();
	}
	public static void main(String[] args) {
		new DynamicTest().start();
	}
	
	private class TestAction implements SeleniumAction{

		@Override
		public void execute(WebDriver driver) {
			WindowUtil.loadAll(driver);
			try {
				Thread.sleep(5000);
				
				WebDriverWait wait = new WebDriverWait(driver, 10);
		        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("J_PromoPriceNum")));
				File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("D:\\data\\111.png"));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
