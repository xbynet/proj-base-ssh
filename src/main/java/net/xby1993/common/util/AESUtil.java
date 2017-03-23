package net.xby1993.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/*******************************************************************************
 * AES加解密算法
 * 
 * @author arix04
 * 
 */

public class AESUtil {
	
	
	
	// 加密
    public static String encrypt(String sSrc) throws Exception {
    	
    	return AESUtil.encrypt(sSrc,"ipmacro123456789");
    }
    
    
    // 解密
    public static String decrypt(String sSrc) throws Exception
    {
    	return AESUtil.decrypt(sSrc,"ipmacro123456789");
    }
    

    // 加密
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(sKey.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(sKey
                    .getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        /*
         * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
         * 此处使用AES-128-CBC加密模式，key需要为16位。
         */
        String cKey = "ipmacro123456789";
        // 需要加密的字串
        String cSrc = "wiredMac=123456789&wirelessMac=987654321&firmId=test";
        System.out.println(cSrc);
        // 加密
        long lStart = System.currentTimeMillis();
        String enString = AESUtil.encrypt(cSrc);
        System.out.println("加密后的字串是：" + enString);

        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密耗时：" + lUseTime + "毫秒");
        // 解密
        lStart = System.currentTimeMillis();
        String DeString = AESUtil.decrypt("USU5jkwPsJtlqTmQ0A+6eOsK2UveLqbfKM+O4E3B6AyVsWMAan2LMrG3PdslbkBlfNDpFKd7UHyR\r\nI9+sBYMtDA==");
        System.out.println("解密后的字串是：" + DeString);
        
       String abc= AESUtil.encrypt("firm=12");
       System.out.println(abc);
       
    }
}