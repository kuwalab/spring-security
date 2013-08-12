package com.example.spring.security.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * http://d.hatena.ne.jp/liquidfunc/20100930/1285775071
 * を改造。ユーザーIDをsaltとして利用し、10000回のストレッチング
 * いわゆるハッシュ関数を使った暗号化クラス。
 */
public class Encryption {

	/* メッセージダイジェストアルゴリズム */
	public MessageDigest md = null;

	/*
	 * テストコード
	 */
	public static void main(String[] args) {
		final String algorithmName = "SHA-256";
		final String[] passwords = new String[] { "user1", "user2", "admin" };
		final int count = 10000;

		for (String password : passwords) {
			Encryption e = new Encryption(algorithmName);
			byte[] bytes = e
					.toHashValue(password + "{" + password + "}", count);
			String result = e.toEncryptedString(bytes);
			System.out.println(password + "=" + result);
		}
	}

	/*
	 * 引数でメッセージダイジェストアルゴリズムを指定する。 MD2, MD5, SHA, SHA-256, SHA-384, SHA-512が利用可能。
	 */
	public Encryption(String algorithmName) {
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