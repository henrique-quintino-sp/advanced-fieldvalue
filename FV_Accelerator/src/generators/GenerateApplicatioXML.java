package generators;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import dao.FieldValue;
import utils.UtilMethods;

public class GenerateApplicatioXML {

	private String appName ;						//TARGET_APP_NAME
	private Document docXML;					//Applicatio XML String converted to DOM
	private List<FieldValue> fieldValues;
	
	public GenerateApplicatioXML(String applicationXML, String appName, List<FieldValue> fieldValues){
		Document currentAppXML = readApplicationXMLasString(applicationXML);
		setDocXML(currentAppXML);
		setAppName(appName);
		setFieldValues(fieldValues);
	}
	
	public GenerateApplicatioXML(){
	}
	
	public String writeXML(String applicationXML, String appName, String fileLocation, List<FieldValue> fieldValues){
		Document currentAppXML = readApplicationXMLasString(applicationXML);
		setDocXML(currentAppXML);
		setAppName(appName);
		setFieldValues(fieldValues);
		return writeXML(fileLocation);
	}
	
	public String writeXML(String fileLocation){
		String xmlName = fileLocation+"\\"+getAppName()+".xml";
		try {
			Document doc = getDocXML();

			// Get the root element
			Element root=doc.getDocumentElement();
			
			NodeList provForms = doc.getElementsByTagName("ProvisioningForms");
			//Check if there are any Provisioning Forms declared in the Application XML, if so it will be replaced for the new one
			if(null!=provForms){
				Node provNode = provForms.item(0);
				root.removeChild(provNode);
			}
			//Create a new Form to add it to the new ProvisioningForms node
			Element newProvForm = generateFormNode(doc, getFieldValues());
			root.appendChild(newProvForm);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			//Add Doctype and dtd
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			DOMImplementation domImpl = doc.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("doctype",
			    "sailpoint.dtd",
			    "sailpoint.dtd");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(xmlName));
			transformer.transform(source, result);
			
			System.out.println("Done!");
			System.out.println("Wrote to "+xmlName);

		}catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} 
		return xmlName;
	}
	
	/**
	 * <Form name="account" objectType="account" type="Create">
			<Field displayName="con_prov_policy_ldap_user_DN" helpKey="help_con_prov_policy_ldap_user_DN" name="dn"	required="true" type="string" />
		</Form>
	 * @param doc
	 * @return
	 */
	private Element generateFormNode(Document doc, List<FieldValue> fieldValues){
		Element provForms = doc.createElement("ProvisioningForms");
		
		//<Form name="Ldap Create" objectType="account" type="Create">
		Element form = doc.createElement("Form");
		form.setAttribute("name", "account");
		form.setAttribute("objectType", "account");
		form.setAttribute("type", "Create");
		provForms.appendChild(form);
		
		for(FieldValue field: fieldValues){
			Element fieldEl = doc.createElement("Field");
			fieldEl.setAttribute("displayName", field.getDisplayName());
			fieldEl.setAttribute("helpKey", field.getDisplayName());
			fieldEl.setAttribute("name", field.getAppAttribute());
			fieldEl.setAttribute("required", String.valueOf(field.isRequired()));
			fieldEl.setAttribute("type", field.getType());
			form.appendChild(fieldEl);
		}
		
		return provForms;
	}
	
	private String getTestFile(String fileName) {
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
	
	private Document readApplicationXMLasString(String applicationXML){
		Document doc = null;
		
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(applicationXML));
			doc = db.parse(is);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return doc;
	}

	public String getAppName() {
		return UtilMethods.escape(appName);
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Document getDocXML() {
		return docXML;
	}

	public void setDocXML(Document docXML) {
		this.docXML = docXML;
	}

	public List<FieldValue> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(List<FieldValue> fieldValues) {
		this.fieldValues = fieldValues;
	}
}
