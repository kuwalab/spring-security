package com.example.spring.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 * http://d.hatena.ne.jp/liquidfunc/20100930/1285775071
 * を改造。ユーザーIDをsaltとして利用する。
 * version 1がSHA-1、version 2がSHA-256
 * いわゆるハッシュ関数を使った暗号化クラス。
 */
public class CustomEncryption {

	/* メッセージダイジェストアルゴリズム */
	public MessageDigest md = null;

	private static final Map<String, String> ALGORITHM_MAP = Collections
			.unmodifiableMap(new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
				{
					put("1", "SHA-1");
					put("2", "SHA-256");
				}
			});

	/*
	 * テストコード
	 */
	public static void main(String[] args) {
		final String[] passwords = new String[] { "user1", "user2", "admin" };
		final String[] versions = new String[] { "2", "1", "2" };

		for (int i = 0; i < passwords.length; i++) {
			CustomEncryption e = new CustomEncryption(
					ALGORITHM_MAP.get(versions[i]));
			byte[] bytes = e.toHashValue(passwords[i] + "{" + passwords[i]
					+ "}", 1);
			String result = e.toEncryptedString(bytes);
			System.out.println(passwords[i] + "=" + versions[i] + ":" + result);
		}
	}

	/*
	 * 引数でメッセージダイジェストアルゴリズムを指定する。 MD2, MD5, SHA, SHA-256, SHA-384, SHA-512が利用可能。
	 */
	public CustomEncryption(String algorithmName) {
		try {
			md = MessageDigest.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/*
	 * メッセージダイジェストアルゴリズムを使い、文字列をハッシュ値へ変換する。
	 */
	public byte[] toHashValue(String password, int count) {
		byte[] digest = password.getBytes();
		for (int i = 0; i < count; i++) {
			digest = md.digest(digest);
		}
		return digest;
	}

	/*
	 * バイト配列を16進数の文字列に変換し、連結して返す。
	 */
	public String toEncryptedString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			String hex = String.format("%02x", b);
			sb.append(hex);
		}
		return sb.toString();
	}

}