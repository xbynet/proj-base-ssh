/*package net.xby1993.common.webmagic.tb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import net.xby1993.common.util.concurrent.CountableThreadPool;
import net.xby1993.common.util.http.HttpReqUtil;
import net.xby1993.common.webmagic.downloader.PhantomjsWebDriverPool;
import net.xby1993.common.webmagic.downloader.SeleniumDownloader;
import net.xby1993.common.webmagic.downloader.WebDriverPool;
import net.xby1993.common.webmagic.downloader.WindowUtil;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;


public class AlimamaSpider implements PageProcessor {
	private Logger log = LoggerFactory.getLogger(AlimamaSpider.class);

	private static Site site = Site
			.me()
			.setCharset("gbk")
			.setUserAgent(
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
			.addHeader("Referer", "http://pub.alimama.com/");
	private String loginUrl = "https://login.taobao.com/member/login.jhtml?style=mini&newMini2=true&from=alimama&redirectURL=http%3A%2F%2Flogin.taobao.com%2Fmember%2Ftaobaoke%2Flogin.htm%3Fis_login%3d1&full_redirect=true&disableQuickLogin=true";

	private final BlockingQueue<String> queue=new ArrayBlockingQueue<String>(10000);
	private Spider spider=null;
	private AtomicBoolean addFinished=new AtomicBoolean(false);//保留，暂不对外开放
	private static AtomicBoolean running = new AtomicBoolean(false);
	private static AtomicBoolean shutDowned=new AtomicBoolean(false);
	private WebDriverPool webDriverPool=null;
	private CountableThreadPool threadPool=null;
	
	private AlimamaSpider(){
		webDriverPool=new PhantomjsWebDriverPool(5,false);
		threadPool=new CountableThreadPool(5);
	}
	public static AlimamaSpider get(){
		return SingleHolder.instance;
	}
	private static class SingleHolder{
		static AlimamaSpider instance=new AlimamaSpider();
	}
	
	public static boolean isRunning() {
		return running.get();
	}
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
	}

	public void start() {
		if (running.compareAndSet(false, true)) {
			log.info("开始alimamaSpider");
			try {
				WebDriverPool wdpool = new PhantomjsWebDriverPool(1, true);
				spider = Spider
						.create(this)
						// .setScheduler(scheduler)
						.setDownloader(
								new SeleniumDownloader(1000, wdpool,
										new TestAction())).addUrl(loginUrl)
						.thread(1);
				shutDowned.set(false);
				spider.start();
				queryAndPushUrls();
				while(spider.getStatus()!=Spider.Status.Stopped){
					HttpReqUtil.sleep(5000);
				}
			} finally {
				running.set(false);
				log.info("alimamaSpider结束");
			}
		}
	}

	boolean doDirectPromo(String processUrl){
		WebDriver driver=null;
		try {
			driver = webDriverPool.get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			log.error("",e1);
			return false;
		}
		WebDriverWait wait = new WebDriverWait(driver, 3);
		//如果加载不出元素，则手动打开新tab
//		String nzjhWH=WindowUtil.openNewTab(driver, "http://pub.alimama.com/promo/item/channel/index.htm?channel=nzjh");
//		HttpReqUtil.sleep(1000);
//		WindowUtil.changeWindowTo(driver, nzjhWH);
//		HttpReqUtil.sleep(2000);
		driver.get("http://pub.alimama.com/promo/item/channel/index.htm?channel=nzjh");
		HttpReqUtil.sleep(1500);
		WindowUtil.setCookies(driver, AlimamaCookieHolder.getCookies());
		driver.get("http://pub.alimama.com/promo/item/channel/index.htm?channel=nzjh&tttt="+System.currentTimeMillis());
		HttpReqUtil.sleep(2000);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#J_category_filter")));
		
		try {
			StoreManager storeManager=GlobeContext.getAc().getBean(StoreManager.class);
			List<Store> stores=storeManager.find("skipLink", processUrl);
			if(stores.size()>0){
				//已经处理过定向推广了
				if(stores.get(0).getDirectPromo()!=0){
					log.info(processUrl+"已经处理过了，不再重复处理");
					return false;
				}
			}
			//输入并搜索
			driver.findElement(By.cssSelector(".search-inp")).sendKeys(
					processUrl);// https://item.taobao.com/item.htm?id=546983040012
			List<WebElement> searchBtns = driver.findElements(By
					.cssSelector("button.search-btn"));
			WebElement searchBtn = null;
			if (searchBtns.size() > 1) {
				searchBtn = searchBtns.get(1);
			} else {
				searchBtn = searchBtns.get(0);
			}
			WindowUtil.clickElement(driver, searchBtn);
			HttpReqUtil.sleep(2000);
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.cssSelector(".search-result-wrap > .block-search-box:nth-child(1)")));
			
			int index = 1;
			
			//点击定向推广按钮
			WebElement tmp = driver
					.findElement(By
							.cssSelector(".search-result-wrap > .block-search-box:nth-child("
									+ index
									+ ") > .box-shop-info > .tags-container > a"));
			Actions actions = new Actions(driver);
			actions.moveToElement(tmp).build().perform();
			HttpReqUtil.sleep(50);
			WebElement tmp2 = driver
					.findElement(By
							.cssSelector(".search-result-wrap > .block-search-box:nth-child("
									+ index
									+ ") > .box-shop-info > .tags-container > a > span > em:nth-child(2)"));
			actions.moveToElement(tmp2).build().perform();
			tmp2.click();
			
			String js="return $(\".login-panel\").css(\"display\");";
			String display=(String) WindowUtil.execJs(driver, js);
			if("block".equals(display)){
				log.info("登录过期，需要重新登录");
				AlimamaLoginUtil.doLogin();
			}
			WebElement tmpEl = driver
					.findElement(By
							.cssSelector(".search-result-wrap > .block-search-box:nth-child("
									+ index
									+ ") > .box-content > div.content-line:nth-child(3) > span.fl > span.number"));
			double percent = Double.valueOf(tmpEl.getText().replace(
					"%", ""));
			// System.out.println(percent);

			double humanPercent = 0;
			int humanIndex = -1;
			double autoPercent = 0;
			int autoIndex = -1;

			HttpReqUtil.sleep(1000);
			// WindowUtil.taskScreenShot(driver, new
			// File("D:\\code\\img\\loged-602.png"));

			List<WebElement> trs = driver
					.findElements(By
							.cssSelector(".dialog-bd > .table-container > .table-scroll > .table > tbody > tr"));
			for (int i = 0; i < trs.size(); i++) {
				WebElement tr1 = trs.get(i);
				String tmpPercentStr = tr1
						.findElement(
								By.cssSelector("td:nth-child(2) > span:nth-child(1)"))
						.getText();
				if (!tmpPercentStr.contains(".")) {
					tmpPercentStr += ".00";
				}
				double tmpPercent = Double.valueOf(tmpPercentStr);

				String type = tr1.findElement(
						By.cssSelector("td:nth-child(3) > span"))
						.getText();
				if (Double.compare(Double.valueOf(percent),
						Double.valueOf(tmpPercent)) < 0) {
					if (type.equals("人工")) {
						if (humanPercent == 0
								|| Double.compare(humanPercent,
										tmpPercent) < 0) {
							humanPercent = tmpPercent;
							humanIndex = i;
						}
					} else {
						if (autoPercent == 0
								|| Double.compare(autoPercent,
										tmpPercent) < 0) {
							autoPercent = tmpPercent;
							autoIndex = i;
						}
					}
				}
			}
			if (autoPercent >= humanPercent) {
				humanPercent = 0;
				humanIndex = -1;
			}
			if (humanIndex >= 0) {
				By by = By
						.cssSelector(".dialog-bd > .table-container > .table-scroll > .table > tbody > tr:nth-child("
								+ (humanIndex + 1)
								+ ") > td:nth-child(5) > a:nth-child(2)");
				if (WindowUtil.checkElementExists(driver, by)) {
					WindowUtil.clickElement(driver, by);
					HttpReqUtil.sleep(100);
					// WindowUtil.taskScreenShot(driver, new
					// File("D:\\code\\img\\loged-7.png"));
					driver.findElement(By.cssSelector("#J_applyReason"))
							.sendKeys("导购网站，申请推广该商品");
					WindowUtil
							.clickElement(
									driver,
									By.cssSelector(".dialog-ft > button:nth-child(1)"));
					HttpReqUtil.sleep(1000);
					// WindowUtil.taskScreenShot(driver, new
					// File("D:\\code\\img\\loged-8.png"));
				}
			}
			if (autoIndex >= 0) {
				By by = By
						.cssSelector(".dialog-bd > .table-container > .table-scroll > .table > tbody > tr:nth-child("
								+ (autoIndex + 1)
								+ ") > td:nth-child(5) > a:nth-child(2)");
				if (WindowUtil.checkElementExists(driver, by)) {

					WindowUtil
							.clickByJsCssSelector(
									driver,
									".dialog-bd > .table-container > .table-scroll > .table > tbody > tr:nth-child("
											+ (autoIndex + 1)
											+ ") > td:nth-child(5) > a:nth-child(2)");

					HttpReqUtil.sleep(500);
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By
									.cssSelector("#J_applyReason")));
					// WindowUtil.taskScreenShot(driver, new
					// File("D:\\code\\img\\loged-7.png"));
					driver.findElement(By.cssSelector("#J_applyReason"))
							.sendKeys("导购网站，申请推广该商品");
					WindowUtil
							.clickElement(
									driver,
									By.cssSelector(".dialog-ft > button:nth-child(1)"));
					HttpReqUtil.sleep(1000);
					// WindowUtil.taskScreenShot(driver, new
					// File("D:\\code\\img\\loged-8.png"));
				}
			}
			
			int directPromo=2;
			double directPromoPercent=0;
			if(humanIndex>=0 || autoIndex>=0){
				directPromo=1;
				directPromoPercent=humanPercent>autoPercent?humanPercent:autoPercent;
			}
			for(Store s:stores){
				s.setDirectPromo(directPromo);
				s.setDirectPromoPercent(directPromoPercent);
				storeManager.normalSaveStore(s);
			}
			WindowUtil.clickByJsCssSelector(driver, ".dialog-close");
			HttpReqUtil.sleep(50);
			// WindowUtil.taskScreenShot(driver, new
			// File("D:\\code\\img\\loged-9.png"));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("处理" + processUrl + "时发生异常", e);
		}
		return false;
	}

	private class TestAction implements SeleniumAction {
		@Override
		public void execute(WebDriver driver) {
			LoginAction loginAction=new LoginAction();
			int retryLoginCount=0;
			boolean logined=false;
			while (retryLoginCount < 3 && !logined) {
				try {
					loginAction.execute(driver);
					logined=true;
				} catch (Exception e) {
					e.printStackTrace();
					log.error("阿里妈妈登录失败,准备重试", e);
					retryLoginCount++;
				}
			}
			WebDriverWait wait = new WebDriverWait(driver, 3);
			
//			WindowUtil.taskScreenShot(driver, new File("D:\\code\\img\\loged-3.png"));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#J_menu_product")));
//			WindowUtil.taskScreenShot(driver, new File("D:\\code\\img\\loged-4.png"));
			WindowUtil.clickElement(driver, By.cssSelector(".nav > li:nth-child(2) > a"));
			HttpReqUtil.sleep(1000);
			//阿里妈妈首页窗口
			String indexWH=driver.getWindowHandle();
			//点击打开新窗口
			By by0= By.cssSelector(".index-middle-wrap > index-middle:nth-child(1) > .middle-contents > .middle-block:nth-child(1) > a");
//			WindowUtil.taskScreenShot(driver, new File("D:\\code\\img\\loged-5.png"));
			try{
				wait.until(ExpectedConditions.presenceOfElementLocated(by0));
			}catch(TimeoutException e){
				log.warn("等待获取.index-middle-wrap > index-middle:nth-child(1) > .middle-contents > .middle-block:nth-child(1) > a 元素失败");
			}
			//nzjh窗口
			String nzjhWH=null;
			if(WindowUtil.checkElementExists(driver, by0)){
				WindowUtil.clickElement(driver,by0);
				HttpReqUtil.sleep(2000);
				for(String tmp:driver.getWindowHandles()){
					if(!tmp.equals(indexWH)){
						nzjhWH=tmp;
						break;
					}
				}
			}else{
				//如果加载不出元素，则手动打开新tab
				nzjhWH=WindowUtil.openNewTab(driver, "http://pub.alimama.com/promo/item/channel/index.htm?channel=nzjh");
				HttpReqUtil.sleep(1000);
			}
			
			WindowUtil.changeWindowTo(driver, nzjhWH);
			HttpReqUtil.sleep(2000);
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#J_category_filter")));
//			WindowUtil.taskScreenShot(driver, new File("D:\\code\\img\\loged-601.png"));
			String processUrl=null;
			int sleepTime=0;
			while (!shutDowned.get()) {
				processUrl=queue.poll();
				if(processUrl==null){
					if(addFinished.get()){
						break;
					}else{
						HttpReqUtil.sleep(1000);
						sleepTime+=1000;
						//如果休眠时间大于5分钟，则访问一下主动访问一次以保持session
						if(sleepTime>1000*60*5){
							driver.get("http://pub.alimama.com/promo/item/channel/index.htm?channel=nzjh");
							HttpReqUtil.sleep(2000);
							AlimamaCookieHolder.setCookies(WindowUtil.getCookies(driver));
							sleepTime=0;
						}
						continue;
					}
				}
				final String processUrl2=processUrl;
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						if(!doDirectPromo(processUrl2)){
							AlimamaLoginUtil.doLogin();
							doDirectPromo(processUrl2);	
						}
					}
				});
			}
		}

	}
	
	public static void main(String[] args) {

	}

	public void pushUrl(String url) throws InterruptedException{
		queue.put(url);
	}
	public synchronized void queryAndPushUrls(){
		StoreManager storeManager=GlobeContext.getAc().getBean(StoreManager.class);
		List<Criterion> crits=new ArrayList<Criterion>();
		crits.add(Restrictions.eq("terrace.id", 1L));
		crits.add(Restrictions.isNotNull("skipLink"));
		crits.add(Restrictions.ge("couponsEnd", new Date()));
		crits.add(Restrictions.and(Restrictions.ne("directPromo",1), Restrictions.ne("directPromo",2)));
		List<Order> orders=new ArrayList<Order>();
		orders.add(Order.desc("updateTime"));
		org.springside.modules.orm.Page<Store> page=storeManager.findPage(crits, orders, 1, 1000);
		int pageIndex=1;
		while (pageIndex <= page.getTotalPages()) {
			for(Store s:page.getResult()){
				String url=s.getSkipLink();
				if(StringUtils.isNotBlank(url) && (url.contains(".taobao.com")|| url.contains(".tmall.com"))){
					try {
						pushUrl(url);
					} catch (InterruptedException e) {
						e.printStackTrace();
						log.error("",e);
					}
				}
			}
			pageIndex++;
			if(pageIndex<=page.getTotalPages()){
				page=storeManager.findPage(crits, orders, pageIndex, 1000);
			}
		}
	}
	public void stop(boolean clearQueue) {
		if(clearQueue){
			queue.clear();
		}
		shutDowned.set(true);
		try {
			webDriverPool.shutdown();
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error("",e1);
		}
		try {
			threadPool.shutdown();
			threadPool.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.warn("",e);
		}
	}
}
*/