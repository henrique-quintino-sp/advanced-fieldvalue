package utils;

import java.util.ArrayList;
import java.util.List;

import dao.FieldValue;

public class TestGenerateXML {

	public static void main(String[] args) {
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
		
		GenerateApplicatioXML appXML = new GenerateApplicatioXML("applicationXML.toXML()","ActiveDirectory");
		appXML.writeXML("C:\\Users\\ishim.manon\\Desktop");
	}
	

}
