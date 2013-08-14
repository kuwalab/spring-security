package com.example.spring.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.encoding.BaseDigestPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;

public class CustomPasswordEncoder extends BaseDigestPasswordEncoder {
	private static final Map<String, String> ALGORITHM_MAP = Collections
			.unmodifiableMap(new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
				{
					put("1", "SHA-1");
					put("2", "SHA-256");
				}
			});
	private String currentVersion;

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	@Override
	public String encodePassword(String rawPass, Object salt) {
		return encodePassword(rawPass, salt, currentVersion);
	}

	private String encodePassword(String rawPass, Object salt, String version) {
		// パスワードのencodeのバージョンによって処理を変える
		String saltedPass = mergePasswordAndSalt(rawPass, salt, false);

		MessageDigest messageDigest = getMessageDigest(version);
		byte[] digest = messageDigest.digest(Utf8.encode(saltedPass));

		return version + ":" + (new String(Hex.encode(digest)));
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		String[] splits = encPass.split(":");
		String password = encodePassword(rawPass, salt, splits[0]);

		return encPass.equals(password);
	}

	protected final MessageDigest getMessageDigest(String version)
			throws IllegalArgumentException {
		String algorithm = ALGORITHM_MAP.get(version);
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm ["
					+ algorithm + "]");
		}
	}

}
