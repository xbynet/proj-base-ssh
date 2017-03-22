//package net.xby1993.springmvc.util;
//
//import org.mindrot.jbcrypt.BCrypt;
//
//public class HashUtil {
//	public static final String MD5 = "MD5";
//	public static final String SHA_1 = "SHA-1";
//	public static final String SHA_256 = "SHA-256";
//	public static final String SHA_384 = "SHA-384";
//	public static final String SHA_512 = "SHA-512";
//	
//	public static String genSalt(){
//		String salt=BCrypt.gensalt();
//		return salt;
//	}
//	/**
//	 * @param complex 取值4-31
//	 * @return
//	 */
//	public static String genSalt(int complex){
//		String salt=BCrypt.gensalt(complex);
//		return salt;
//	}
//	public static String geneHashedPassword(String password,String salt){
//		String hashed = BCrypt.hashpw(password, salt);
//		return hashed;
//	}
//	public static boolean validatePassword(String password,String hashed){
//		if(!StringUtils.checkStrings(password,hashed)){
//			return false;
//		}
//		return BCrypt.checkpw(password.trim(), hashed.trim());
//	}
//	public static void main(String[] args) {
////		String salt="37fac46c729dccc4d59e028e4806022f";
////		System.out.println(genSalt());
////		String pass=genePasswordHash(SHA_512,"xby","123", salt);
////		System.out.println(pass);
//		
//		
//		//BCrypt
//		String hashed = BCrypt.hashpw("123", BCrypt.gensalt());
//		String hashed2 = BCrypt.hashpw("12322", BCrypt.gensalt());
//		String hashed3 = BCrypt.hashpw("12322wewq", BCrypt.gensalt(4));
//
//		if (BCrypt.checkpw("123", hashed))
//			System.out.println("It matches");
//		else
//			System.out.println("It does not match");
//		if (BCrypt.checkpw("12322wewq", hashed3))
//			System.out.println("It matches");
//		else
//			System.out.println("It does not match");
//	}
//}	
