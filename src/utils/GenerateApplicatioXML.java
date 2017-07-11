package utils;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GenerateApplicatioXML {

	public GenerateApplicatioXML(String applicationXML){
		Document currentAppXML = readApplicationXMLasString(applicationXML);
		
	}
	
	private Document readApplicationXMLasString(String applicationXML){
		ClassLoader classLoader = getClass().getClassLoader();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(applicationXML);
			//optional, but recommended
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			//If there is a ProvisioningForms tag, it will be replaced for the new form - Needs GenerateTemplateXML.java 
			NodeList nList = doc.getElementsByTagName("ProvisioningForms");
			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
}
