package generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import dao.FieldValue;
import utils.UtilMethods;

public class GenerateTemplateXML {

	private String appName ;									//TARGET_APP_NAME
	private List<FieldValue> fieldValueList;

	public GenerateTemplateXML(String appName , List<FieldValue> fieldValueList){
		setAppName(appName);
		setFieldValueList(fieldValueList);
	}
	
	public String writeXML(String xmlName){
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			StringBuilder content = getFile("formHeader.xml");
			
			for(FieldValue obj : fieldValueList){
				content.append(getFormFile("form.xml",obj));
			}
			content.append(getFile("formFooter.xml"));

			xmlName = xmlName+"\\Template_"+getAppName()+"_Create.xml";
			
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

	private  String getFormFile(String fileName, FieldValue obj){
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates/"+fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line= line.replaceAll("%%TARGET_APP_NAME%%", getAppName());
				line = line.replaceAll("%%ATT_NAME%%", obj.getAppAttribute());
				line = line.replaceAll("%%ATT_DISPLAYNAME%%", obj.getDisplayName());
				result.append(line).append("\n");
			}
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
		}

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
			return UtilMethods.escape(appName);
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public List<FieldValue> getFieldValueList() {
			return fieldValueList;
		}

		public void setFieldValueList(List<FieldValue> fieldValueList) {
			this.fieldValueList = fieldValueList;
		}
	}
