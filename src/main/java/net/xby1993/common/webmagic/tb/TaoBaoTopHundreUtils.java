package net.xby1993.common.webmagic.tb;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.PageData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;
/**
 * 淘宝解析工具类
 *
 */
public class TaoBaoTopHundreUtils {
	private static Logger logger = LoggerFactory.getLogger(TaoBaoTopHundreUtils.class);
	/**
	 * 店铺基本信息JSON解析
	 * @param page
	 * @param key
	 * @param leixing
	 * @return
	 */
	public static List<PageData> parsePage(Page page,String key,String leixing){
		
		
		/**
		 * 字段意义:网店地址        网店名称          所在地            卖家ID    主营                             销量                     店铺类型 
		 *       shopUrl shopName shopAdd showNick mainAuction totalsold leixing
		 */
		String pageText = page.getRawText();
		String json = pageText.substring(pageText.indexOf('(') + 1,
				pageText.indexOf(')'));
		if (json.trim().endsWith("}") || json.trim().endsWith("]")) {
			
		} else {//json非正常结束
			
			if (page.getRequest().getUrl().contains("&s=")) {
				//分割请求url,取得当前总数
				String tmp = page.getRequest().getUrl().split("&s=")[1]
						.split("&")[0];
				int curPage = Integer.valueOf(tmp.trim()) / 20 + 1;
				json += "\"}]}},\"pager\":{\"status\":\"show\",\"data\":{\"currentPage\":"
						+ curPage + ",\"pageSize\":20}}}}";
				
			}
			logger.info("店铺基本信息JSON非正常结束,已做特殊处理");
		}
		try {
			Selectable tpage = new Json(json);
			String pageUrl = page.getRequest().getUrl();
			String url = null;
			if (tpage.jsonPath("$.mods.pager.status").get().equals("show")) {// 有分页
				if (tpage.jsonPath("$.mods.pager.data.currentPage").get()
						.equals("1")) {//第一页,不带s参数
					url = pageUrl
							+ "&s="
							+ (Integer.parseInt(tpage.jsonPath(
									"$.mods.pager.data.currentPage").get()) * Integer
									.parseInt(tpage.jsonPath(
											"$.mods.pager.data.pageSize").get()));
					logger.info("单页URL处理完毕");
				} else{//非第一页,带s参数
					url = pageUrl.substring(0, pageUrl.indexOf("&s="))
							+ "&s="
							+ (Integer.parseInt(tpage.jsonPath(
									"$.mods.pager.data.currentPage").get()) * Integer
									.parseInt(tpage.jsonPath(
											"$.mods.pager.data.pageSize").get()));
					logger.info("分页URL处理完毕");
				}
				
			}
			if (url != null) {
				page.addTargetRequest(url);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Selectable jsonShopItems = null;
		try {
			jsonShopItems = new Json(json).jsonPath("$.mods.shoplist.data.shopItems[*]");
		} catch (com.jayway.jsonpath.InvalidPathException e) {
			return null;
		}

		List<String> list = jsonShopItems.all();
		Selectable shopItems = null;
		List<PageData> dataList = new ArrayList<PageData>(list.size());
		for (int i = 0; i < list.size(); i++) {

			shopItems = new Json(list.get(i));
			String shopUrl = "https:" + shopItems.jsonPath("$.shopUrl").get();
			String shopName = shopItems.jsonPath("$.title").get();
			String shopAdd = shopItems.jsonPath("$.provcity").get();
			String showNick = shopItems.jsonPath("$.nick").get();
			String mainAuction = shopItems.jsonPath("$.mainAuction").get();
			String totalsold = shopItems.jsonPath("$.totalsold").get();
			logger.info("json解析完毕!!!");

		}
		return dataList;
	}
		
	
}
