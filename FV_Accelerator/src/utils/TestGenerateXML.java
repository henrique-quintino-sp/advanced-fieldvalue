package utils;

import java.util.HashMap;
import java.util.Map;

public class TestGenerateXML {

	public static void main(String[] args) {
		Map<String, String> attsMap = new HashMap<String,String>();
		attsMap.put("cn", "cnAttribute");
		attsMap.put("dn", "dnAttribute");
		attsMap.put("givenName", "givenNameAttribute");
		attsMap.put("something", "somethingAttribute");
		
		//Another way to send attributes when mapping is the same
		//String attributes = "cn,dn,givenName";
		
		GenerateFieldValuesXML xml = new GenerateFieldValuesXML("ActiveDirectory", attsMap,  "ou=people,dc=sailpoint, dc=sp");
		xml.writeXML("C:\\Users\\ishim.manon\\Desktop");
		
	}
}
