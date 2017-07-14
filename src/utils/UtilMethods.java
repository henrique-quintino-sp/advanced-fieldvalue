package utils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class UtilMethods {

	public static String escape(String appName2) {
		String escaped = appName2.replaceAll("\\s", "_");
		return escaped;
	}
	
	public String getTestFile(String fileName) {
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("testFiles/"+fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();

	}
	
}
