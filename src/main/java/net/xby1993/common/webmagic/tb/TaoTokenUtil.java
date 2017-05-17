package net.xby1993.common.webmagic.tb;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.protocol.HttpClientContext;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopping.shop.selenium.WindowUtil;
import com.shopping.shop.utility.Constants;
import com.shopping.shop.utility.HttpReqUtil;

public class TaoTokenUtil {
	private static final Logger log=LoggerFactory.getLogger(TaoTokenUtil.class);
	
	public static String getTaoToken(String goodId){
		String raw=getContent(goodId);
		if(raw.contains("nologin")){
			//{"data":null,"info":{"message":"nologin","ok":false},"ok":false,"invalidKey":null}
			log.error("登录过期需要重新登录");
			AlimamaLoginUtil.doLogin();
			raw=getContent(goodId);
		}
		JSONObject json=JSONObject.fromObject(raw);
		JSONObject data=json.getJSONObject("data");
		String finalToken=null;
		String taoToken=data.getString("taoToken");
		finalToken=taoToken;
		if(data.containsKey("couponLinkTaoToken")){
			String couponLinkTaoToken=data.getString("couponLinkTaoToken");			
			if(StringUtils.isNotBlank(couponLinkTaoToken)){
				finalToken=couponLinkTaoToken;
			}
		}
		return finalToken;
	}
	private static String getContent(String goodId){
		//String url="http://pub.alimama.com/common/code/getAuctionCode.json?auctionid=550171213845&adzoneid=14538565&siteid=4310284&scenes=1";
		String url="http://pub.alimama.com/common/code/getAuctionCode.json?auctionid="+goodId+"&adzoneid="+Constants.ALIMAMA_ADZONID+"&siteid="+Constants.ALIMAMA_SITEID+"&scenes=1";
		final Set<Cookie> cookies=AlimamaCookieHolder.getCookies();
		String httpCookie=WindowUtil.getHttpCookieString(cookies);
		
		Map<String,Object> headers=new HashMap<String,Object>();
		headers.put("Referer","http://pub.alimama.com/promo/item/channel/index.htm");
		headers.put("X-Requested-With","XMLHttpRequest");
		headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
		headers.put("Cookie",httpCookie);
		HttpClientContext httpContext=new HttpClientContext();
		String raw=HttpReqUtil.getRawContent(url, "GET", null, headers,httpContext);
		return raw;
	}
}
