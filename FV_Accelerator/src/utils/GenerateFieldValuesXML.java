package utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GenerateFieldValuesXML {

	private static List<String> attributes;
	private String appName = "ActiveDirectory";

	public static void main(String[] args) {
		GenerateFieldValuesXML xml = new GenerateFieldValuesXML();
		xml.setAttributesList("cn,dn,givenName");
		System.out.println(xml.getFile("header.txt"));
		System.out.println(xml.getFile("imports.txt"));
		System.out.println(xml.getFile("loggers.txt"));
		for(String att: attributes){
			System.out.println(xml.getMethodsFile("stubWithDefaults.txt", att));
		}
		System.out.println(xml.getFile("utils.txt"));
		System.out.println(xml.getFile("footer.txt"));

	}

	private void setAttributesList(String attributesList){
		attributes =  Arrays.asList(attributesList.split("\\s*,\\s*"));
	}

	private String getFile(String fileName) {
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates/"+fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line= line.replaceAll("%%TARGET_APP_NAME%%", appName);
				if(fileName.equals("stubWithDefaults.txt") || fileName.equals("stub.txt")){
					for(String att: attributes){
						line = line.replaceAll("%%ATT_NAME%%", att);
					}
				}
				result.append(line).append("\n");
			}
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	}
	
	private String getMethodsFile(String fileName, String att) {
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates/"+fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line= line.replaceAll("%%TARGET_APP_NAME%%", appName);
				if(fileName.equals("stubWithDefaults.txt") || fileName.equals("stub.txt")){
					line = line.replaceAll("%%ATT_NAME%%", att);
				}
				result.append(line).append("\n");
			}
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	}
}
