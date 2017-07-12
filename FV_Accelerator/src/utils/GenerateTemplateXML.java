package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dao.FieldValue;

public class GenerateTemplateXML {

	private String appName ;									//TARGET_APP_NAME
	private List<FieldValue> fieldValueList;
	
	public GenerateTemplateXML(String appName , List<FieldValue> fieldValueList){
		setAppName(appName);
		setFieldValueList(fieldValueList);
	}

	public void processFormXML(String xmlName){
		ClassLoader classLoader = getClass().getClassLoader();
		File fXmlFile = new File(classLoader.getResource("templates/form.xml").getFile());
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		Document doc = null;
		
		try {
			docBuilder = docFactory.newDocumentBuilder();
			// root elements
			doc = docBuilder.newDocument();
			Document temp = docBuilder.parse(fXmlFile);
			//optional, but recommended
			temp.getDocumentElement().normalize();

			System.out.println("Root element :" + temp.getDocumentElement().getNodeName());

			NodeList formList = temp.getElementsByTagName("Form");
			Node formNode = doc.importNode(formList.item(0), true);
			Node newFormNode = doc.createElement("Form");
			Element formEl = (Element) formNode;
			formEl.setAttribute("name", getAppName()+" Create");
			newFormNode.appendChild(formEl);
			doc.appendChild(newFormNode);
			
			NodeList sectionList =  temp.getElementsByTagName("Section");
			Node sectionNode = doc.importNode(sectionList.item(0), true);
			newFormNode.appendChild(sectionNode);
			NodeList fieldList = temp.getElementsByTagName("Field");

			System.out.println("----------------------------");

			for (FieldValue field : getFieldValueList()) {

				Node fieldNode = doc.importNode(fieldList.item(0), true);

				if (fieldNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) fieldNode;
					eElement.setAttribute("displayName", field.getDisplayName());
					eElement.setAttribute("name", field.getAppAttribute());
					System.out.println("\nCurrent Element :" + eElement.getAttribute("name"));

					NodeList referenceList = eElement.getElementsByTagName("Reference");
					Node referenceNode = referenceList.item(0);
					Element referenceEl = (Element) referenceNode;
					referenceEl.setAttribute("name", "SP Dynamic Field Value Rule "+getAppName());
					Node importedField = doc.importNode(eElement, true);
					sectionNode.appendChild(importedField);
					
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeXML(doc, xmlName);
	}
	
	private void writeXML(Document doc, String xmlName){
		// write the content into xml file
		try{
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(xmlName+getAppName()+" Create Form.xml"));

				// Output to console for testing
				StreamResult result2 = new StreamResult(System.out);

				transformer.transform(source, result);
		} catch(Exception e){
			
		}
		
	}

	public String getAppName() {
		return appName;
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
