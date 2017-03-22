package net.xby1993.common.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordHelper {
//	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	private static final String algorithmName = "md5";
	private static final int hashIterations = 1;

	public static String encryptPassword(String password) {
		if(StringUtils.isBlank(password)){
			return null;
		}
		String newPassword = new SimpleHash(algorithmName, password,
				null,
				hashIterations).toHex();
		return newPassword;
	}
	public static void main(String[] args) {
		System.out.println(new SimpleHash(algorithmName, "admin",
				null,
				1).toHex());
	}
}
