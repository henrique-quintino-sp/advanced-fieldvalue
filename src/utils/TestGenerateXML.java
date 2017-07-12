package utils;

import java.util.ArrayList;
import java.util.List;

import dao.FieldValue;

public class TestGenerateXML {

	public static void main(String[] args) {
//		Map<String, String> attsMap = new HashMap<String,String>();
//		attsMap.put("cn", "cnAttribute");
//		attsMap.put("dn", "dnAttribute");
//		attsMap.put("givenName", "givenNameAttribute");
//		attsMap.put("something", "somethingAttribute");
		
		//Another way to send attributes when mapping is the same
		//String attributes = "cn,dn,givenName";
		
		List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		fieldValues.add(new FieldValue("cn", "cnTrg", "displayNameCN", true));
		fieldValues.add(new FieldValue("dn", "dnAttribute", "displayNameDN", false));
		fieldValues.add(new FieldValue("givenName", "givenNameAttribute", "displayNameGivenName", false));
		
		GenerateFieldValuesXML xml = new GenerateFieldValuesXML("ActiveDirectory", fieldValues,  "ou=people,dc=sailpoint, dc=sp");
		xml.writeXML("C:\\Users\\ishim.manon\\Desktop");
		
		
		GenerateSPDynamicFieldValueRuleXML spXML = new GenerateSPDynamicFieldValueRuleXML("ActiveDirectory");
		spXML.writeXML("C:\\Users\\ishim.manon\\Desktop");
		
		GenerateTemplateXML formXML = new GenerateTemplateXML("ActiveDirectory", fieldValues);
		formXML.writeXML("C:\\Users\\ishim.manon\\Desktop");
	}
}
