package br.com.acredita.applicationlog.config;

public final class RestControllerPath {

	///////////////////////////////////////////////////////////////
	// ROOT PATH
	///////////////////////////////////////////////////////////////
	public static final String ALL = "/**";
	public static final String ROOT_PATH = "/api";
	public static final String PUBLIC_ROOT_PATH = ROOT_PATH + "/public";
	public static final String PRIVATE_ROOT_PATH = ROOT_PATH + "/private";
	
	///////////////////////////////////////////////////////////////
	// PRIVATE PATHS
	///////////////////////////////////////////////////////////////
	public static final String HostCheck_PATH = PRIVATE_ROOT_PATH + "/hostcheck/";
	public static final String Log_PATH = PRIVATE_ROOT_PATH + "/logs/";
	
	///////////////////////////////////////////////////////////////
	// PUBLIC PATHS
	///////////////////////////////////////////////////////////////
	public static final String LOGIN_PATH = PUBLIC_ROOT_PATH + "/login";
	public static final String LOGOUT_PATH = PUBLIC_ROOT_PATH + "/logout";
}
