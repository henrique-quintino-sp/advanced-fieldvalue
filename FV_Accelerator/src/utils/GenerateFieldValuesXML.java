package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class GenerateFieldValuesXML {

	private Map<String,String> attributesMap;	//ATT_NAME , TARGET_ATT_NAME
	private String appName ;									//TARGET_APP_NAME
	private String uniquenessAtt;								//LDAP_SEARCH_DN

	public GenerateFieldValuesXML(String appName, String commaSeparatedAttributes, String uniquenesAtt){
		setAppName(appName);
		setAttributesMap(commaSeparatedAttributes);
		setUniquenesAtt(uniquenesAtt);
	}
	
	public GenerateFieldValuesXML(String appName, Map<String,String> attributesMap, String uniquenesAtt){
		setAppName(appName);
		setAttributesMap(attributesMap);
		setUniquenesAtt(uniquenesAtt);
	}
	
	public void printXML(){
		System.out.println(getFile("header.txt"));
		System.out.println(getFile("imports.txt"));
		System.out.println(getFile("loggers.txt"));
		
		Iterator<Entry<String, String>> it = attributesMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String,String> pair = (Map.Entry<String,String>) it.next();
			System.out.println(getMethodsFile("stubWithDefaults.txt", pair.getKey(), pair.getValue()));
			it.remove();
		}
		System.out.println(getFile("utils.txt"));
		System.out.println(getFile("footer.txt"));
	}
	
	public void writeXML(String xmlName){
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			StringBuilder content = getFile("header.txt");
			content.append(getFile("imports.txt"));
			content.append(getFile("loggers.txt"));
			
			Iterator<Entry<String, String>> it = attributesMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,String> pair = (Map.Entry<String,String>) it.next();
				content.append(getMethodsFile("stubWithDefaults.txt", pair.getKey(), pair.getValue()));
				it.remove();
			}
			content.append(getFile("utils.txt"));
			content.append(getFile("footer.txt"));

			xmlName = xmlName+"\\SPCONF_FieldValue_RulesLibrary_"+getAppName()+".xml";
			
			fw = new FileWriter(xmlName);
			bw = new BufferedWriter(fw);
			bw.write(content.toString());

			System.out.println("Done!");
			System.out.println("Wrote to "+xmlName);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Use for any template except for stub templates
	 * @param fileName
	 * @return
	 */
	private StringBuilder getFile(String fileName) {
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates/"+fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line= line.replaceAll("%%TARGET_APP_NAME%%", getAppName());
				result.append(line).append("\n");
			}
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}
	
	private String getMethodsFile(String fileName, String att, String targetAtt) {
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates/"+fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line= line.replaceAll("%%TARGET_APP_NAME%%", getAppName());
				if(fileName.equals("stubWithDefaults.txt") || fileName.equals("stub.txt")){
					line = line.replaceAll("%%ATT_NAME%%", att);
					line = line.replaceAll("%%TARGET_ATT_NAME%%", targetAtt);
					if(!getUniquenessAtt().isEmpty()){
						line = line.replaceAll("%%LDAP_SEARCH_DN%%", getUniquenessAtt());
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
	
	private String getAppName() {
		return appName;
	}

	private void setAppName(String appName) {
		this.appName = appName;
	}

	private String getUniquenessAtt() {
		return uniquenessAtt;
	}

	private void setUniquenesAtt(String uniquenessAtt) {
		this.uniquenessAtt = null==uniquenessAtt?"":uniquenessAtt;
	}

	private void setAttributesMap(String attributesList){
		List<String> attributes =  Arrays.asList(attributesList.split("\\s*,\\s*"));
		attributesMap = new HashMap<String,String>();
		for(String att : attributes){
			attributesMap.put(att, att);
		}
	}
	public Map<String, String> getAttributesMap() {
		return attributesMap;
	}

	public void setAttributesMap(Map<String, String> attributesMap) {
		this.attributesMap = attributesMap;
	}
}
