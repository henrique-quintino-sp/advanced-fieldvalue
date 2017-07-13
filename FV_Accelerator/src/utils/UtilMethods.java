package utils;

public class UtilMethods {

	public static String escape(String appName2) {
		String escaped = appName2.replaceAll("\\s", "_");
		return escaped;
	}
}
