package generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import dao.FieldValue;
import utils.UtilMethods;

public class GenerateFieldValuesXML {

	private List<FieldValue> fieldValueList	;			//ATT_NAME , TARGET_ATT_NAME, UNIQUENESS
	private Map<String,String> attributesMap;	//ATT_NAME , TARGET_ATT_NAME
	private String appName ;									//TARGET_APP_NAME
	//Types of target attribute
	private final String TYPE_REG = "REG";			//For formats like: firstname[1]+'.'+lastname 
	private final String TYPE_PRE = "PRE";			//For pre configured attributes in formats like: displayName-op3 

	public GenerateFieldValuesXML(String appName, String commaSeparatedAttributes, String uniquenesAtt){
		setAppName(appName);
		setAttributesMap(commaSeparatedAttributes);
	}
	
	public GenerateFieldValuesXML(String appName, Map<String,String> attributesMap, String uniquenesAtt){
		setAppName(appName);
		setAttributesMap(attributesMap);
	}
	
	public GenerateFieldValuesXML(String appName, List<FieldValue> fieldValueList, String uniquenesAtt){
		setAppName(appName);
		setFieldValueList(fieldValueList);
	}
	
	public String writeXML(String xmlName) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			StringBuilder content = getFile("header.txt");
			content.append(getFile("imports.txt"));
			content.append(getFile("loggers.txt"));
			
			for(FieldValue obj : fieldValueList){
				content.append(getMethodsByType(obj));
			}
			content.append(getFile("utils.txt"));
			content.append(getFile("footer.txt"));

			xmlName = xmlName+"/SPCONF_FieldValue_RulesLibrary_"+getAppName()+".xml";
			
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
		return xmlName;
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
	
	private String getMethodsByType(FieldValue field) {
		String result = new String(""); 
		//Check type of target attribute
		if(field.getTargetAttribute().startsWith(TYPE_REG)){
			result = getRegExpMethod(field);
		} else if(field.getTargetAttribute().startsWith(TYPE_PRE)){
			result = getPreConfMethod(field);
		} else{
			result = getStubMethod(field);
		} 
		
		return result.toString();
	}
	
	private String getPreConfMethod(FieldValue field) {
		String fileName = field.getTargetAttributeValue();
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("library/"+fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line= line.replaceAll("%%TARGET_APP_NAME%%", getAppName());
				line = line.replaceAll("%%ATT_NAME%%", field.getAppAttribute());
				line = line.replaceAll("%%TARGET_ATT_NAME%%", field.getTargetAttributePRE());
				result.append(line).append("\n");
			}
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	private String getRegExpMethod(FieldValue field){
		String fileName = "stubWithRegExp.txt";
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates/"+fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line= line.replaceAll("%%TARGET_APP_NAME%%", getAppName());
				line = line.replaceAll("%%ATT_NAME%%", field.getAppAttribute());
				line = line.replaceAll("%%ATT_FORMAT%%", field.getTargetAttributeValue());
				result.append(line).append("\n");
			}
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	private String getStubMethod(FieldValue field){
		String fileName = "stubWithDefaults.txt";
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates/"+fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line= line.replaceAll("%%TARGET_APP_NAME%%", getAppName());
				line = line.replaceAll("%%ATT_NAME%%", field.getAppAttribute());
				line = line.replaceAll("%%TARGET_ATT_NAME%%", field.getTargetAttribute());
				line = line.replaceAll("%%CHK_UNIQUENESS_VALUE%%", String.valueOf(field.isCheckUniqueness()));
				result.append(line).append("\n");
			}
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	private String getAppName() {
		return UtilMethods.escape(appName);
	}

	private void setAppName(String appName) {
		this.appName = appName;
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

	public List<FieldValue> getFieldValueList() {
		return fieldValueList;
	}

	public void setFieldValueList(List<FieldValue> fieldValueList) {
		this.fieldValueList = fieldValueList;
	}
}
