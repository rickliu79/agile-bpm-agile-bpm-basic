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

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DESede;

/**
 * 加密算法。 <br/>
 * 2.SHA-256 <br/>
 * @deprecated 推荐使用 SecureUtil
 */
public class EncryptUtil {
	private static final String CODE = "UTF-8";
 

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
			byte[] bytes = StrUtil.bytes(message);
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
			
			byte[] code = cipher.doFinal(message.getBytes(CODE));
			return StrUtil.str(code,"");
		} catch (Exception e) {
			throw new BusinessError(e);
		}
	}


}
