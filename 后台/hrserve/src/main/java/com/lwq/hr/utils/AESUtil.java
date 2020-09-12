/**
 * 
 */
package com.lwq.hr.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * AES 加密解密算法
 * @author YZ 
 * 2016年5月30日 上午11:02:44
 */
public class AESUtil {

	private static Logger log = Logger.getLogger(AESUtil.class);
	/**
	 * 工具类，私有化构造器
	 */
	private AESUtil() {
		
	}

	/**
	 * 加密算法
	 * @param sSrc 待加密字符
	 * @param keyStr 加密密钥
	 * @return 加密后字符
	 * @throws Exception
	 */
	public static String Encrypt(String sSrc, String keyStr) throws Exception {
		byte[] raw = null;
		if ("".equals(keyStr) || keyStr == null || keyStr.length() < 16) {
			String S_Key = "0102030405060708";
			raw = S_Key.getBytes();
			keyStr = S_Key;
		} else {
			raw = keyStr.getBytes();
		}
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		IvParameterSpec iv = new IvParameterSpec(keyStr.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
		// 此处使用BAES64做转码功能，同时能起到2次加密的作用。
		return Base64.encodeBase64String(encrypted);
	}

	/**
	 * 解密算法
	 * @param sSrc 已加密字符
	 * @param keyStr 解密密钥
	 * @return 解密字符
	 * @throws Exception
	 */
	public static String Decrypt(String sSrc, String keyStr){
		try {
			byte[] raw = null;
			if ("".equals(keyStr) || keyStr == null || keyStr.length() < 16) {
				String S_Key = "0102030405060708";
				raw = S_Key.getBytes();
				keyStr = S_Key;
			} else {
				raw = keyStr.getBytes();
			}
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(keyStr.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = Base64.decodeBase64(sSrc);// 先用bAES64解密
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original);
			return originalString;
		} catch (Exception ex) {
			log.error("解密出错 " + ex.getMessage(), ex);
			return null;
		}
	}
}
