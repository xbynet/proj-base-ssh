/*package net.xby1993.common.webmagic.tb;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import net.xby1993.common.util.http.HttpReqUtil;
import net.xby1993.common.webmagic.downloader.SeleniumAction;
import net.xby1993.common.webmagic.downloader.WindowUtil;
import net.xby1993.common.webmagic.util.ImageRegion;
import net.xby1993.common.webmagic.util.ImageUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginAction implements SeleniumAction {
	private Logger log=LoggerFactory.getLogger(LoginAction.class);
	
	@Override
	public void execute(WebDriver driver) {
		String loginUrl="https://login.taobao.com/member/login.jhtml?style=mini&newMini2=true&from=alimama&redirectURL=http%3A%2F%2Flogin.taobao.com%2Fmember%2Ftaobaoke%2Flogin.htm%3Fis_login%3d1&full_redirect=true&disableQuickLogin=true";
		String indexUrl="http://pub.alimama.com/myunion.htm";
		String toLoginUrlStart="https://www.alimama.com/member/login.htm";
		//先判断有没有登录，没有再进行登录操作
		driver.get(indexUrl);
		HttpReqUtil.sleep(2000);
		WindowUtil.setCookies(driver, AlimamaCookieHolder.getCookies());
		driver.get(indexUrl+"?tttt="+System.currentTimeMillis());
		HttpReqUtil.sleep(4000);
		String curUrl=driver.getCurrentUrl();
		if(!curUrl.startsWith(toLoginUrlStart)){
			log.info("已经处于登录状态，无需再次登录");
			return;
		}
		driver.get(loginUrl);
		HttpReqUtil.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver, 3);
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.id("J_Quick2Static")));

		File qrcodeSrc=null;
		File qrcode=null;
		try {
			qrcodeSrc = File.createTempFile(UUID.randomUUID().toString(), ".png");
			qrcode = File.createTempFile(UUID.randomUUID().toString(), ".png");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("临时文件创建失败",e);
		}
		String mailReciever=GlobeContext.getProperty().getProperty("mail.reciever");
		getQrCodeImg(driver,qrcodeSrc,qrcode);
		MailSender.send(qrcode.getAbsolutePath(), mailReciever);
		while(WindowUtil.checkElementExists(driver,By.id("J_QRCodeLogin"))){
			String display=null;
			try{
				display=OperHelper.checkQrCode(driver);
			}catch(Exception e){
				e.printStackTrace();
			}
			//验证码过期，点击刷新
			if("block".equals(display)){
				log.info("二维码过期，重新点击中");
				WindowUtil.clickElement(driver, By.cssSelector(".J_QRCodeRefresh"));
				HttpReqUtil.sleep(1000);
				getQrCodeImg(driver,qrcodeSrc,qrcode);
				MailSender.send(qrcode.getAbsolutePath(), mailReciever);
			}
			HttpReqUtil.sleep(1000);
		}
		log.info("二维码扫描通过,登录成功");
		HttpReqUtil.sleep(2000);
		driver.get(indexUrl);
		HttpReqUtil.sleep(3000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#J_menu_product")));
		AlimamaCookieHolder.setCookies(WindowUtil.getCookies(driver));
	}
	public static void getQrCodeImg(WebDriver driver,File qrcodeSrc,File qrcode){
		WebElement qrCodeImg = driver.findElement(By.id("J_QRCodeImg"));
		Point loc = qrCodeImg.getLocation();
		Dimension d = qrCodeImg.getSize();
		if(qrcodeSrc.exists()){
			qrcodeSrc.delete();
		}
		WindowUtil.taskScreenShot(driver, qrcodeSrc);
		if(qrcode.exists()){
			qrcode.delete();
		}
		ImageUtil.crop(qrcodeSrc.getAbsolutePath(), qrcode
				.getAbsolutePath(), new ImageRegion(loc.x - 5, loc.y - 5,
				d.width + 10, d.height + 10));
	}
}
*/