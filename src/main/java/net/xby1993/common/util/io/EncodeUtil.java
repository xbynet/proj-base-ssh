/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package net.xby1993.common.util.io;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.io.BaseEncoding;

/**
 * hex/base64 编解码工具集，依赖Guava, 取消了对Commmon Codec的依赖
 */
public class EncodeUtil {

	/**
	 * Hex编码, 将byte[]编码为String，默认为ABCDEF为大写字母.
	 */
	public static String encodeHex(byte[] input) {
		return BaseEncoding.base16().encode(input);
	}

	/**
	 * Hex解码, 将String解码为byte[].
	 * 
	 * 字符串有异常时抛出IllegalArgumentException.
	 */
	public static byte[] decodeHex(CharSequence input) {
		return BaseEncoding.base16().decode(input);
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return BaseEncoding.base64().encode(input);
	}

	/**
	 * Base64解码.
	 * 
	 * 如果字符不合法，抛出IllegalArgumentException
	 */
	public static byte[] decodeBase64(CharSequence input) {
		return BaseEncoding.base64().decode(input);
	}

	/**
	 * Base64编码, URL安全.(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
	 */
	public static String encodeBase64UrlSafe(byte[] input) {
		return BaseEncoding.base64Url().encode(input);
	}

	/**
	 * Base64解码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
	 * 
	 * 如果字符不合法，抛出IllegalArgumentException
	 */
	public static byte[] decodeBase64UrlSafe(CharSequence input) {
		return BaseEncoding.base64Url().decode(input);
	}
	
	/**
	 * URL 编码, Encode默认为UTF-8.
	 * 
	 * 转义后的URL可作为URL中的参数
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, "UTF-8");
		} catch (UnsupportedEncodingException ignored) {
			return null;
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8. 转义后的URL可作为URL中的参数
	 */
	public static String urlDecode(String part) {
		try {
			return URLDecoder.decode(part, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * Xml转码，将字符串转码为符合XML1.1格式的字符串.
	 * 
	 * 比如 "bread" & "butter" 转化为 &quot;bread&quot; &amp; &quot;butter&quot;
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml11(xml);
	}

	/**
	 * Xml转码，XML格式的字符串解码为普通字符串.
	 * 
	 * 比如 &quot;bread&quot; &amp; &quot;butter&quot; 转化为"bread" & "butter"
	 */
	public static String unescapeXml(String xml) {
		return StringEscapeUtils.unescapeXml(xml);
	}

	/**
	 * Html转码，将字符串转码为符合HTML4格式的字符串.
	 * 
	 * 比如 "bread" & "butter" 转化为 &quot;bread&quot; &amp; &quot;butter&quot;
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html解码，将HTML4格式的字符串转码解码为普通字符串.
	 * 
	 * 比如 &quot;bread&quot; &amp; &quot;butter&quot;转化为"bread" & "butter"
	 */
	public static String unescapeHtml(String html) {
		return StringEscapeUtils.unescapeHtml4(html);
	}
}
