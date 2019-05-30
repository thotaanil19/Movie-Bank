package com.springboot.moviebank.constants;

public class SecurityConstants {

	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String SIGN_UP_URL = "/user/sign-up";
	public static final String LOGIN_URL = "/user/login";

	public static final String AUTH = "auth";
	public static final String AUTHORIZATION = "Authorization";

}