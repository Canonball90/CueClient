package cn.origin.cube.core.security.keyauth;


import cn.origin.cube.core.security.keyauth.api.KeyAuth;

/**
 * @author SprayD
 */
public class KeyAuthApp {
	private static String url = "https://keyauth.win/api/1.1/";
	
	private static String ownerid = "eN68K5qktw"; // You can find out the owner id in the profile settings keyauth.com
	private static String appname = "cueclient"; // Application name
	private static String version = "1.0"; // Application version

	public static KeyAuth keyAuth = new KeyAuth(appname, ownerid, version, url);
}
