package com.hcy.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public class MD5Utils {

	/**
	 * MD5����
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
				int i = b & 0xff; // ��ȡ�ֽڵĵͰ�λ��Чֵ
				String hexString = Integer.toHexString(i); // ������תΪ16����

				if (hexString.length() < 2) {
					hexString = "0" + hexString; // �����1Ϊλ,��0
				}

				sb.append(hexString);
			}
			Log.d("����1", "MD5:" + sb.toString());
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
}
