package br.com.acredita.score.config;

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
	public static final String Score_PATH = PRIVATE_ROOT_PATH + "/scores/";

	public static final String Perfil_PATH = PRIVATE_ROOT_PATH + "/perfis/";
	public static final String Usuario_PATH = PRIVATE_ROOT_PATH + "/usuarios/";

	
	public static final String BACKUP_PATH = PRIVATE_ROOT_PATH + "/backup/";
	public static final String RESTORE_PATH = PRIVATE_ROOT_PATH + "/restore/";

	
	///////////////////////////////////////////////////////////////
	// PUBLIC PATHS
	///////////////////////////////////////////////////////////////
	public static final String LOGIN_PATH = PUBLIC_ROOT_PATH + "/login";
	public static final String LOGOUT_PATH = PUBLIC_ROOT_PATH + "/logout";
}
