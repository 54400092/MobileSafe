package com.hcy.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public class MD5Utils {

	/**
	 * MD5加密
	 * 
	 * @param password
	 * @return
	 */
	public static String encode(String password) {

		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");
			byte[] digest = instance.digest(password.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				int i = b & 0xff; // 获取字节的低八位有效值
				String hexString = Integer.toHexString(i); // 将整数转为16进制

				if (hexString.length() < 2) {
					hexString = "0" + hexString; // 如果是1为位,补0
				}

				sb.append(hexString);
			}
			Log.d("测试1", "MD5:" + sb.toString());
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
}
