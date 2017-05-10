package net.xby1993.common.webmagic.tb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.xby1993.common.webmagic.downloader.ChromeWebDriverPool;
import net.xby1993.common.webmagic.downloader.SeleniumAction;
import net.xby1993.common.webmagic.downloader.SeleniumDownloader;
import net.xby1993.common.webmagic.downloader.WebDriverPool;
import net.xby1993.common.webmagic.downloader.WindowUtil;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 店铺销售信息
 *
 * @author taojw
 */
@Scope("prototype")
@Component
public class TaoBaoShopInfoProcessor implements PageProcessor {
    private static final Logger log = LoggerFactory
            .getLogger(TaoBaoShopInfoProcessor.class);


    private Site site = Site
            .me()
            .setCharset("UTF-8")
            .setCycleRetryTimes(3)
            .setSleepTime(3 * 1000)
            .addHeader("Connection", "keep-alive")
            .addHeader("Cache-Control", "max-age=0")
            .addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");

    private AtomicBoolean isPageAdd = new AtomicBoolean(false);
    private static AtomicBoolean running = new AtomicBoolean(false);
    private WebDriverPool pool=new ChromeWebDriverPool();
    @Override
    public Site getSite() {
        return this.site;
    }

    @Override
    public void process(Page page) {
        if (islistPage(page)) {
            List<String> urls = page.getHtml()
                    .$("dl.item a.J_TGoldData", "href").all();
            List<String> targetUrls = new ArrayList<String>();
            for (String url : urls) {
                targetUrls.add(url.trim());
            }
            page.addTargetRequests(targetUrls);
            if (isPageAdd.compareAndSet(false, true)) {
                // 分页处理
                String pageinfo = page.getHtml()
                        .$(".pagination .page-info", "text").get();
                int pageCount = Integer.valueOf(pageinfo.split("/")[1]);
                String cururl = page.getUrl().get();
                //只抓前5页
                if(pageCount>5){
                    pageCount=5;
                }
                for (int i = 1; i < pageCount; i++) {
                    String tmp = cururl + "&pageNo=" + (i + 1);
                    page.addTargetRequest(tmp);
                }
            }
            return;
        }

        // 商品页面
        String curUrl = page.getUrl().get();
        boolean isTaoBao=curUrl.startsWith("https://item.taobao.com");
        boolean isTmall=curUrl.startsWith("https://detail.tmall.com");
        
        String tmpspm = curUrl.split("\\?")[1].split("&")[0];
        // spm码
        String spm = tmpspm.split("=")[1];
        // 网店地址
        String shopUrl="";
     // 商品名称
        String name="";
     // 价格
        double price =0; 
     // 30天交易总数
        int sellCount=0;
     // 交易总价
        double allPrice=0;
        if(isTaoBao){
        	shopUrl= page.getHtml()
                    .xpath("//div[@class='tb-shop-name']/dl/dd/strong/a/@href")
                    .get();
        	shopUrl = shopUrl.split("\\?")[0];
        	
        	name = page.getHtml().xpath("//*[@id='J_Title']/h3/text()")
                    .get();
        	try{
                price=Double.valueOf(page.getHtml()
                        .$("#J_PromoPriceNum", "text").get().split("-")[0].trim());
                }catch(Exception e){
                	
                	price=Double.valueOf(page.getHtml()
                            .$("#J_StrPrice .tb-rmb-num", "text").get().split("-")[0].trim());
                }
        	sellCount = Integer.valueOf(page.getHtml()
                    .$("#J_SellCounter", "text").get());
        	allPrice = Double.valueOf(price) * Double.valueOf(sellCount);
        }else if(isTmall){
        	shopUrl= page.getHtml()
                    .xpath("//*[@id='side-shop-info']/div/h3/div/a/@href")
                    .get();
        	shopUrl = shopUrl.split("\\?")[0];
        	
        	name = page.getHtml().$(".tb-detail-hd h1","text")
                    .get().trim();
        
            price=Double.valueOf(page.getHtml()
                        .$(".tm-price", "text").get().split("-")[0].trim());
                
        	sellCount = Integer.valueOf(page.getHtml()
                    .$(".tm-count", "text").get().trim());
        	allPrice = Double.valueOf(price) * Double.valueOf(sellCount);
        }

        // 采集日期
        // Timestamp recordDate=new Timestamp(new Date().getTime());
        String recordDate = DateUtil.formatDate(new Date(), "yyyy-MM-dd");

        log.debug(shopUrl + ":" + spm + ":" + name + ":" + price + ":"
                + sellCount + ":" + allPrice + ":" + recordDate);



    }

    private boolean islistPage(Page page) {
        String tmp = page.getHtml().$("#J_PromoPrice").get();
        if (StringUtils.isBlank(tmp)) {
            return true;
        }
        return false;
    }

    public void start() {
        if (running.compareAndSet(false, true)) {
            try {
   
                List<String> urls = new ArrayList<>();
                String[] urlStrs=new String[]{};
//                int size=50;
                int size=urls.size();
                if(urls.size()<size){
                	urlStrs=new String[urls.size()];
                	urlStrs=urls.toArray(urlStrs);
                	
                }else{
                	urlStrs=new String[size];
                	for(int i=0;i<size;i++){
                		urlStrs[i]=urls.get(i).trim();
                	}
                }
                log.info("准备抓取,需要抓取的店铺数为{}", urls.size());
               // String[] urlStrs = new String[urls.size()];
              //  "https://zhuzhuwo.taobao.com" urls.toArray(urlStrs)
                Spider spider = Spider.create(this)
                        .setDownloader(new SeleniumDownloader(5000, pool, new TestAction()))
//                        .addUrl(urlStrs);
                        .addUrl("https://zhuzhuwo.taobao.com");

                spider.thread(1).run();
                log.info("淘宝店铺销售信息数据正常抓取完毕");
            } finally {
            	log.info("淘宝店铺销售信息数据抓取完毕,准备关闭webdriverpool");
            	pool.shutdown();
            	log.info("webdriverpool关闭完毕");
                running.set(false);
            }
        }
    }

    public static void main(String[] args) {
        new TaoBaoShopInfoProcessor().start();
    }

    private class TestAction implements SeleniumAction {

        @Override
        public void execute(WebDriver driver) {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            // 商品页，避免加载过多无用图片信息。
            if (driver.getCurrentUrl().startsWith("https://item.taobao.com/")|| driver.getCurrentUrl().startsWith("https://detail.tmall.com")) {
              //  wait.until(ExpectedConditions.presenceOfElementLocated(By
              //          .cssSelector("#J_PromoPriceNum")));
                return;
            }
            // 店铺首页，点击所有分类
            if ((!(driver.getCurrentUrl().startsWith("https://item.taobao.com")))
                    && !(driver.getCurrentUrl().startsWith("https://detail.tmall.com")) && (!driver.getCurrentUrl().contains("search"))) {
                WebElement allcate = driver.findElement(By
                        .cssSelector(".all-cats-trigger a"));
                Actions action = new Actions(driver);
                action.click(allcate).perform();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String url = driver.getCurrentUrl();
            // 列表页，加载所有
            WindowUtil.loadAll(driver);
            url = driver.getCurrentUrl();
            try {
                Thread.sleep(3000);
//                WindowUtil.taskScreenShot(driver, new File("d:\\data\\tb\\" + UUID.randomUUID().toString() + ".png"));
                // wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".pagination .page-info")));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
