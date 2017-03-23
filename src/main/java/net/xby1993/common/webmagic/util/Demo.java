package net.xby1993.common.webmagic.util;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author taojw
 *
 */
public class Demo {
	/**
	 * 浏览器最大化 前进，后退， 刷新
	 * @param driver
	 * @throws Exception
	 */
	public static void testBrowser(WebDriver driver) throws Exception
    {
        driver.get("https://www.baidu.com");
        Thread.sleep(5000);
        // 浏览器最大化
        driver.manage().window().maximize();
        
        driver.navigate().to("https://www.google.com");
        // 刷新浏览器
        driver.navigate().refresh();
        // 浏览器后退
        driver.navigate().back();
        // 浏览器前进
        driver.navigate().forward();
        // 浏览器退出
        driver.quit();
    }
	/**截图操作
	 * @param driver
	 * @throws Exception
	 */
	public static void testScreenShot(WebDriver driver) throws Exception
    {
        driver.get("http://www.baidu.com");
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File("c:\\1.png"));
    }
	/**
	 * 模拟鼠标操作
	 * @param driver
	 */
	public static void rightClickMouse(WebDriver driver)
    {
        driver.get("http://www.baidu.com");
        Actions action = new Actions(driver);
        
        action.contextClick(driver.findElement(By.id("kw"))).perform();    
    }
	/**
	 * 隐式等待
	 * @param driver
	 */
	public static void testWait(WebDriver driver){
        driver.get("file:///C:/demo/set_timeout.html");    
        //总共等待10秒， 如果10秒后，元素还不存在，就会抛出异常  org.openqa.selenium.NoSuchElementException
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        WebElement element = driver.findElement(By.cssSelector(".red_box"));      
        ((JavascriptExecutor)driver).executeScript("arguments[0].style.border = \"5px solid yellow\"",element);  
	}
	/**
	 * 显式等待
	 * 显式等待 使用ExpectedConditions类中自带方法， 可以进行显试等待的判断。 
	 * 显式等待可以自定义等待的条件，用于更加复杂的页面等待条件
	 * 页面元素是否在页面上可用和可被单击
elementToBeClickable(By locator)
页面元素处于被选中状态
elementToBeSelected(WebElement element)
页面元素在页面中存在
presenceOfElementLocated(By locator)
在页面元素中是否包含特定的文本
textToBePresentInElement(By locator)
页面元素值
textToBePresentInElementValue(By locator, java.lang.String text)
标题 (title)
titleContains(java.lang.String title)


只有满足显式等待的条件满足，测试代码才会继续向后执行后续的测试逻辑
如果超过设定的最大显式等待时间阈值， 这测试程序会抛出异常。 
	 * @param driver
	 */
	public static void testWait2(WebDriver driver)
    {
        driver.get("E:\\test\\set_timeout.html");    
        
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".red_box")));
        WebElement element = driver.findElement(By.cssSelector(".red_box"));      
        ((JavascriptExecutor)driver).executeScript("arguments[0].style.border = \"5px solid yellow\"",element);  
    }
	/**
	 * 元素定位
使用WebDriver对象的findElement函数定义一个Web页面元素
使用findElements函数可以定位页面的多个元素
定位的页面元素需要使用WebElement对象来存储，以便后续使用
常用的定位页面元素方法如下，   按推荐排序
id 定位
driver.findElement(By.id(“id的值”))；
name定位
driver.findElement(By.name(“name的值”))；
链接的全部文字定位
driver.findElement(By.linkText(“链接的全部文字”))；
链接的部分文字定位
driver.findElement(By.partialLinkText(“链接的部分文字”))；
css 方式定位
driver.findElement(By.cssSelector(“css表达式”))；
xpath 方式定位
driver.findElement(By.xpath(“xpath表达式”))；
Class 名称定位
driver.findElement(By.className(“class属性”))；
TagName 标签名称定位
driver.findElement(By.tagName(“标签名称”))；
Jquery方式
Js.executeScript(“return jQuery.find(“jquery表达式”)”)

如何定位
在使用selenium webdriver进行元素定位时,通常使用findElement或findElements方法结合By类返回元素句柄来定位元素
findElement() 方法返回一个元素, 如果没有找到,会抛出一个异常 NoElementFindException()
findElements()方法返回多个元素, 如果没有找到,会返回空数组, 不会抛出异常

如何选择 定位方法
策略是， 选择简单，稳定的定位方法。
1. 当页面元素有id属性的时候， 尽量使用id来定位。  没有的话，再选择其他定位方法
2. cssSelector  执行速度快， 推荐使用
3. 定位超链接的时候，可以考虑linkText或partialLinkText：　但是要注意的是 ，  文本经常发生改变，　所以不推荐用
3. xpath 功能最强悍。 当时执行速度慢，因为需要查找整个DOM,  所以尽量少用。  实在没有办法的时候，才使用xpath 
	 * @param driver
	 */
	public static void testLocation1(WebDriver driver){
        driver.get("http://www.baidu.com");
         
        WebElement searchBox = driver.findElement(By.id("kw"));
        searchBox.sendKeys("android");
        WebElement searchButton = driver.findElement(By.id("su"));
        searchButton.submit();
        
        driver.close();
	}
	/**
	 * 常见web UI 元素操作 及API使用
	 * @param driver
	 */
	public static void testCommonOper1(WebDriver driver){
		/**
		 * 链接(link)
		 * <div>
        <p>链接 link</p>
        <a href="www.cnblogs.com/tankxiao">小坦克</a>
    </div>
		 */
		// 找到链接元素
        WebElement link1 = driver.findElement(By.linkText("小坦克"));
        WebElement link11 = driver.findElement(By.partialLinkText("坦克"));
        
        // 点击链接
        link1.click();
        
        /**
         * 输入框 textbox
         * <div>
        <p>输入框 testbox</p>
        <input type="text" id="usernameid" value="username" />
    </div>
         */
     // 找到元素
        WebElement element = driver.findElement(By.id("usernameid"));
        
        // 在输入框中输入内容
        element.sendKeys("test111111");
        
        // 清空输入框
        element.clear();
        
        // 获取输入框的内容
        element.getAttribute("value");
        
        /**
         * 按钮(Button)
         * <div>
        <p>按钮 button</p>
        <input type="button" value="添加" id="proAddItem_0" />
    </div> 
         */
      //找到按钮元素
        String xpath="//input[@value='添加']";
        WebElement addButton = driver.findElement(By.xpath(xpath));

        // 点击按钮
        addButton.click();

        // 判断按钮是否enable
        addButton.isEnabled();
        
        /**
         * 下拉选择框(Select)
         * <div>
        <p>下拉选择框框 Select</p>
        <select id="proAddItem_kind" name="kind">
            <option value="1">电脑硬件</option>
            <option value="2">房产</option>
            <option value="18">种类AA</option>
            <option value="19">种类BB</option>
            <option value="20">种类BB</option>
            <option value="21">种类CC</option>
        </select>
    </div>
         */
        // 找到元素
        Select select = new Select(driver.findElement(By.id("proAddItem_kind")));

        // 选择对应的选择项， index 从0开始的
        select.selectByIndex(2);
        select.selectByValue("18");
        select.selectByVisibleText("种类AA");

        // 获取所有的选项
        List<WebElement> options = select.getOptions();
        for (WebElement webElement : options) {
            System.out.println(webElement.getText());    
        }
        
        /**
         * 单选按钮(Radio Button),多选框的操作和单选框一模一样的
         * <div>
        <p>单选项  Radio Button</p>
        <input type="radio" value="Apple" name="fruit>" />Apple
        <input type="radio" value="Pear" name="fruit>" />Pear
        <input type="radio" value="Banana" name="fruit>" />Banana
        <input type="radio" value="Orange" name="fruit>" />Orange
    </div>
         */
     // 找到单选框元素
        String xpath2="//input[@type='radio'][@value='Apple']";
        WebElement apple = driver.findElement(By.xpath(xpath));

        //选择某个单选框
        apple.click();

        //判断某个单选框是否已经被选择
        boolean isAppleSelect = apple.isSelected();

        // 获取元素属性
        apple.getAttribute("value");
	}
}
