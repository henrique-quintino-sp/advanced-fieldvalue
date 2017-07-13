package utils;

import java.util.ArrayList;
import java.util.List;

import dao.FieldValue;
import generators.GenerateApplicatioXML;
import generators.GenerateFieldValuesXML;
import generators.GenerateSPDynamicFieldValueRuleXML;
import generators.GenerateTemplateXML;

public class TestGenerateXML {

	public static void main(String[] args) {
		List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		fieldValues.add(new FieldValue("cn", "cnTrg", "displayNameCN", true));
		fieldValues.add(new FieldValue("dn", "dnAttribute", "displayNameDN", false));
		fieldValues.add(new FieldValue("givenName", "givenNameAttribute", "displayNameGivenName", false));
		
		GenerateFieldValuesXML xml = new GenerateFieldValuesXML("Active Directory", fieldValues,  "ou=people,dc=sailpoint, dc=sp");
		xml.writeXML("C:\\Users\\ishim.manon\\Desktop");
		
		GenerateSPDynamicFieldValueRuleXML spXML = new GenerateSPDynamicFieldValueRuleXML("Active Directory");
		spXML.writeXML("C:\\Users\\ishim.manon\\Desktop");
		
		GenerateTemplateXML formXML = new GenerateTemplateXML("Active Directory", fieldValues);
		formXML.writeXML("C:\\Users\\ishim.manon\\Desktop");
		
		GenerateApplicatioXML appXML = new GenerateApplicatioXML("applicationXML.toXML()","Active Directory", fieldValues);
		appXML.writeXML("C:\\Users\\ishim.manon\\Desktop");
	}
	

}
