package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GenerateSPDynamicFieldValueRuleXML {

	private String appName ;									//TARGET_APP_NAME
	
	public GenerateSPDynamicFieldValueRuleXML(String appName){
		setAppName(appName );
	}
	
	
	public void writeXML(String xmlName){
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			StringBuilder content = getFile("SPDynamicFieldValueRule.xml");
			xmlName = xmlName+"\\SP_Dynamic_FieldValue_Rule_"+getAppName()+".xml";
			
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


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}
}
