package com.ichat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.Md5Crypt;

@Slf4j
public class MD5Utils {

	/**
	 * @Description: 对字符串进行md5加密 
	 */
	public static String getMD5Str(String strValue) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		String newstr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
		return newstr;
	}

	public static void main(String[] args) {
		try {
			String md5 = getMD5Str("ichat");
			log.info("MD5Utils main md5={}", md5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
