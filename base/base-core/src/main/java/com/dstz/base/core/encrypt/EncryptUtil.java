package com.dstz.base.core.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import com.dstz.base.api.exception.BusinessError;
import com.dstz.base.core.util.StringUtil;

/**
 * 加密算法。 <br/>
 * 1.MD5 <br/>
 * 2.SHA-256 <br/>
 * 3.对称加解密算法。
 */
public class EncryptUtil {
	private static final String CODE = "UTF-8";

	/**
	 * 使用MD5加密
	 *
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encryptMd5(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(str.getBytes());
			return new String(Base64.encodeBase64(digest));
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessError(e);
		}

	}

	/**
	 * 输出明文按sha-256加密后的密文
	 *
	 * @param inputStr
	 *            明文
	 * @return
	 */
	public static synchronized String encryptSha256(String inputStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte digest[] = md.digest(inputStr.getBytes(CODE));
			return new String(Base64.encodeBase64(digest));
		} catch (Exception e) {
			throw new BusinessError(e);
		}
	}

	public static String byte2hex(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < b.length; n++) {
			String str = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (str.length() == 1) {
				sb = sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * 密钥
	 */
	private static final String KEY = "@#$%^6a7";

	/**
	 * 对称解密算法
	 *
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String message) {
		try {
			byte[] bytes = StringUtil.stringToBytes(message);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(KEY.getBytes(CODE));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(KEY.getBytes(CODE));

			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

			byte[] retByte = cipher.doFinal(bytes);
			return new String(retByte, CODE);
		} catch (Exception e) {
			throw new BusinessError(e);
		}
	}

	/**
	 * 对称加密算法
	 *
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String message) {
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

			DESKeySpec desKeySpec = new DESKeySpec(KEY.getBytes(CODE));

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(KEY.getBytes(CODE));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

			return bytesToString(cipher.doFinal(message.getBytes(CODE)));
		} catch (Exception e) {
			throw new BusinessError(e);
		}
	}

	/**
	 * Byte数组转String
	 *
	 * @param b
	 * @return
	 */
	private static String bytesToString(byte b[]) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}

		return hexString.toString();
	}

}
