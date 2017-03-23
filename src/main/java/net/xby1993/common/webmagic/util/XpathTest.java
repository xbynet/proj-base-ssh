package net.xby1993.common.webmagic.util;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import us.codecraft.xsoup.Xsoup;


public class XpathTest {
	public static void main(String[] args) {
		//testSelect();
	}
	@Test
    public static void testSelect() {
        String html = "<html><p></p><p>2016</p><p></p><p></p></html>";
        Document document = Jsoup.parse(html);
        String result = Xsoup.compile("//p[last()-2]").evaluate(document).get();
        Assert.assertEquals("2016", result);
    }
}
