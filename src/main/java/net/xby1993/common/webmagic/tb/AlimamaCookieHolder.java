package net.xby1993.common.webmagic.tb;

import java.util.Set;

import org.openqa.selenium.Cookie;


public class AlimamaCookieHolder {
	private static Set<Cookie> cookies;

	public static Set<Cookie> getCookies() {
		return cookies;
	}

	public static synchronized void setCookies(Set<Cookie> cookies) {
		AlimamaCookieHolder.cookies = cookies;
	}
}
