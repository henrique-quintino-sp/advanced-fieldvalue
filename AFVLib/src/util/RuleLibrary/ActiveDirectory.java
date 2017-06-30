package util.fieldValueFwk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.PasswordGenerator;
import sailpoint.api.PersistenceManager;
import sailpoint.api.SailPointContext;
import sailpoint.connector.Connector;
import sailpoint.object.Application;
import sailpoint.object.Custom;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.QueryOptions;
import sailpoint.object.ResourceObject;
import sailpoint.tools.CloseableIterator;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;
import sailpoint.tools.xml.XMLReferenceResolver;

public class ActiveDirectory {

	private static Log logger = LogFactory.getLog("rule.SP.FieldValue.RulesLibrary");

	private static SailPointContext context;
	
	static boolean isDebugEnabled = logger.isDebugEnabled();

	public static Object getFV_Active_Directory_accountExpires_Rule(SailPointContext context, Identity identity, String op){
        return getFV_Active_Directory_accountExpires_Rule(context, identity, op, null);
    }
    
    public static Object getFV_Active_Directory_accountExpires_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
        logger.trace("Enter get Field Value accountExpires rule");

        Object accountExpires = defaultValue;
        try {
            if (null!=identity){
                //accountExpires = identity.getAttribute("expirationDate");
                
                //if (accountExpires instanceof String) {
                //    /* Need to match the date format used in Source system such as MMM dd, yyyy when calling convertStringToDate() */
                //    accountExpires = convertStringToDate(accountExpires, "MMM dd, yyyy");
                //}
                logger.debug("Identity Expiration Date:" + accountExpires);
                if (!identity.getAttribute("employeetype").equals("Employee")) {
                   if (null==accountExpires || new Integer(0)==accountExpires) {
                       logger.debug("Identity Expiration Date is null or 0");
                       logger.trace("Calculating expirationDate…");
                       accountExpires = s(90, "yyyyMMddHHmmss");
                       /* The 18-digit Active Directory timestamps */
                       Date Win32EpochDate = (new GregorianCalendar(1601,Calendar.JANUARY,1)).getTime();
                       accountExpires = Long.toString((10000 * (accountExpires.getTime() - Win32EpochDate.getTime())));
                   }
                }
            } else {
                logger.debug("No Identity object present");
            }
        } catch(Exception e){
            logger.debug("Setting default Expiration Date - failed to get Expiration Date from Identity...");
            accountExpires = 0;
        }

        logger.trace("Exit get Field Value accountExpires rule - " + accountExpires);
        return (null!=accountExpires?accountExpires:0);
    }
    
	/**
	 * RFC2256: ISO-3166 country 2-letter code
	 * Example:
			c: FR
			c: UK
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_c_Rule(SailPointContext context, Identity identity, String op){
		return getFV_Active_Directory_c_Rule(context, identity, op);
	}
	
	/**
	 * RFC2256: ISO-3166 country 2-letter code (workCountry)
	 * Example:
			c: FR
			c: UK
	 * @param context
	 * @param identity
	 * @param op
	 * @param defaultValue
	 * @return
	 */
	public static Object getFV_Active_Directory_c_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
		logger.trace("Enter AD c rule");
		Object c = null;
        if (null!=identity){
            c = identity.getAttribute("c");
        }
		logger.trace("Exit AD c rule: " + c);
		return (null!=c?c:defaultValue);
	}

	public static Object getFV_Active_Directory_carLicense_Rule(SailPointContext context, Identity identity, String op){
		return getFV_Active_Directory_carLicense_Rule(context, identity, op, null);
	}
	
	public static Object getFV_Active_Directory_carLicense_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
		logger.trace("Enter AD carLicense rule");
		Object carLicense = null;
        if (null!=identity){
        	carLicense = identity.getAttribute("carLicense");
        }

		logger.trace("Exit AD carLicense rule: " + carLicense);
		return (null!=carLicense?carLicense:defaultValue);
	}
	
	public Object getFV_Active_Directory__co_Rule(SailPointContext context, Identity identity, String op){
        return getFV_Active_Directory__co_Rule(context, identity, op, null);
    }
    
    public Object getFV_Active_Directory__co_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
        logger.trace("Enter get Field Value Region/AD->co for app Standard rule");
        
        Object co = null;
        if (null!=identity){
            co = identity.getAttribute("region");
        }
        logger.trace("Exit get Field Value Region/AD->co for app Standard rule: " + co);
        return (null!=co?((String) co).trim():defaultValue);
    }
    
    

	public static Object getFV_Active_Directory_cn_Rule(SailPointContext context, Identity identity, String op) throws GeneralException {
		return getFV_Active_Directory_cn_Rule(context, identity, op, null);
	}
	
	/**
	 * RFC2256: common name(s) for which the entity is known by
	 * This is the X.500 commonName attribute, which contains a name of an object. If the object corresponds to a person, it is typically the person's full name.
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 * @throws GeneralException 
	 */
	public static Object getFV_Active_Directory_cn_Rule(SailPointContext context, Identity identity, String op, Object defaultValue) throws GeneralException{
		logger.trace("Enter getFV_Active_Directory_cn_Rule cn rule");

		String lastName = escapeADValues((String) getFV_Active_Directory_sn_Rule(context, identity, op));
		lastName = lastName.replaceAll("\\s+","");
		logger.trace("Last name: "+lastName); 
		String firstName = escapeADValues((String) getFV_Active_Directory_givenName_Rule(context, identity, op));
		firstName = firstName.replaceAll("\\s+","");
		logger.trace("First name: "+firstName);
		String val = firstName + "." + lastName;
		logger.trace("Value:  "+val);
		
		Application appObj = context.getObject(Application.class, "%%AD_TARGET_NAME%%");

		logger.trace("Checking for uniqueness in Application: "+"%%AD_TARGET_NAME%%");
		if (!isUniqueADName(appObj, "cn", val)) {
			int ctr = 2;
			val = firstName + "." + lastName + ctr;

			while (!isUniqueADName(appObj, "cn", val)) {
				ctr++;
				val = firstName + "." + lastName + ctr;
				logger.trace("Value " + val + " was tested");
			}
		}
		context.decache(appObj);
		logger.trace("Exit getFV_Active_Directory_cn_Rule cn rule: " + val);
		return val;
	}

	/**
	 * Calculate the CN based on the user's firstname,lastname and name
	 * CN is of the format LastName\, FirstName, Name(UserID)
	 *
	 * @param firstName
	 *            - identity firstname
	 * @param lastName
	 *            - identity lastname
	 * @param name
	 *            - IIQ login id
	 * @return
	 */
	public static Object getFV_Active_Directory_cn_Rule2(SailPointContext context, Identity identity, String op)  {
		logger.trace("Enter getFV_Active_Directory_cn_Rule2 rule");
		String cn = "";

		if (identity == null) {
			logger.warn("Invalid Identity");
			return cn;
		}

		String firstName = identity.getFirstname();
		logger.trace("First name: "+firstName);
		String lastName = identity.getLastname();
		logger.trace("Last name: "+lastName);
		String userName = identity.getName();
		logger.trace("Username: "+userName);
		cn = lastName + "\\, " + firstName + " (" + userName+")";

		logger.trace("Exit getFV_Active_Directory_cn_Rule2 cn rule: " + cn);
		return cn;
	}


	public static Object getFV_Active_Directory_cscPreferredName_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("Enter getFV_Active_Directory_cscPreferredName_Rule rule");
		String cscPreferredName = (String) identity.getAttribute("preferredName");
		String val;
		if (null!= cscPreferredName) {
			val = fixNulls(cscPreferredName);
		} else {
			val = "";
		}

		logger.trace("Exit getFV_Active_Directory_cscPreferredName_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2798: identifies a department within an organization
	 * Code for department to which a person belongs. This can also be strictly numeric (e.g., 1234) or alphanumeric (e.g., ABC/123).
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_departmentNumber_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("Enter AD departmentNumber rule");
		String departmentNumber = (String) identity.getAttribute("companyCode");
		String val = fixNulls(departmentNumber);

		logger.trace("Exit AD departmentNumber rule: " + val);
		return val;
	}

	/**
	 * RFC2256: descriptive information
	 * This attribute contains a human-readable description of the object.
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_description2_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("EntergetFV_Active_Directory_description_Rule");

		//DateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
		//Date date = new Date();
		//String val = "Not yet activated. Created on " + dateFormat.format(date);
		String val = "Created by IdentityIQ on "+new Date();
		logger.trace("Exit getFV_Active_Directory_description_Rule: " + val);
		return val;
	}
	
	public static Object getFV_Active_Directory_description_Rule(SailPointContext context, Identity identity, String op){
        return getFV_Active_Directory_description_Rule(context, identity, op, null);
    }
    
    public static Object getFV_Active_Directory_description_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
        logger.trace("Enter get Field Value AD->description for app Standard rule");
        
        Object description = null;
        if (null!=identity){
            String jobTitle = (String) identity.getAttribute("jobtitle");
            String employeeType = (String) identity.getAttribute("employeetype");
            String companyId = (String) identity.getAttribute("companyid");
            
            if(employeeType.equals("Employee")){
                description = jobTitle;
            }else{
                if(companyId.equals("598")) {
                    description = jobTitle + " Humana";
                }else {
                    description = jobTitle + " Contractor";
                }            
            }            
        }

        logger.trace("Exit get Field Value AD->description for app Standard rule: " + description);
        return (null!=description?((String) description).trim():defaultValue);
    }

	/**
	 * RFC2798: preferred name to be used when displaying entries
	 * When displaying an entry, especially within a one-line summary list, it is useful to be able to identify a name to be used. 
	 * Since other attribute types such as 'cn' are multivalued, an additional attribute type is needed. 
	 * Display name is defined for this purpose.
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_displayName_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("Enter getFV_Active_Directory_displayName_Rulee");

		String val = "";
		String firstName = identity.getFirstname();
		firstName = fixNulls(firstName);
		String lastName = identity.getLastname();
		lastName = fixNulls(lastName);
		String middleName = (String) identity.getAttribute("middleName");
		middleName = fixNulls(middleName);
		String userType = (String) identity.getAttribute("userType");
		String displayName;

		if (middleName.equals("")) {
			displayName = lastName + ", " + firstName;
		} else {
			displayName = lastName + ", " + firstName + " " + middleName;
		}

		if (userType != null && userType.equals("SC")) {
			displayName = displayName + " - NONEMP";
		}

		val = fixNulls(displayName);

		logger.trace("Exit getFV_Active_Directory_displayName_Rule: " + val);
		return val;
	}

	/**
	 * This attribute type is not used as the name of the object itself, but it is instead a base type from which attributes with DN syntax inherit.
	 * It is unlikely that values of this type itself will occur in an entry. 
	 * LDAP server implementations which do not support attribute subtyping need not recognize this attribute in requests. 
	 * Client implementations MUST NOT assume that LDAP servers are capable of performing attribute subtyping.
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 * @throws GeneralException
	 */
	public static Object getFV_Active_Directory_distinguishedName2_Rule(SailPointContext context, Identity identity, String op) throws GeneralException{
		logger.trace("Enter getFV_Active_Directory_distinguishedName2_Rule");

		String cn = (String) getFV_Active_Directory_cn_Rule(context, identity, op);
		String userOU = "%%AD_NOT_YET_ACTIVATED_OU%%";
		String baseDN = "%%AD_TOP_LEVEL_DN%%";
		String fullOU = userOU + "," + baseDN;
		String val = "CN=" + cn + "," + fullOU;

		logger.trace("Exit getFV_Active_Directory_distinguishedName2_Rule: " + val);
		return val;
	}
	
	public static Object getFV_Active_Directory_distinguishedName_Rule(SailPointContext context, Identity identity, String op) throws GeneralException{
        return getFV_Active_Directory_distinguishedName_Rule(context, identity, op, null);
    }

    public static Object getFV_Active_Directory_distinguishedName_Rule(SailPointContext context, Identity identity, String op, Object defaultValue) throws GeneralException{
        logger.trace("Enter Active_Directory APP distinguishedName rule");
        logger.trace("defaultValue:: "+defaultValue);
        
        String distinguishedName = "";    
        Custom concentraCodeToDNCustomObject = context.getObjectByName(Custom.class,"Concentra-Custom-Config-Common");
        String userBaseDNPath = (String)concentraCodeToDNCustomObject.get("userBaseDNPath");
        Map codeToDNMap;
            
        String code, accountADOU = null, attributeName;
        List attributeNames = new ArrayList(Arrays.asList(new String[] { "companyid", "departmentid", "site" }));
        int attIndx = 0;
        boolean getADOU = true;
        while (getADOU) {
            attributeName = (String) attributeNames.get(attIndx++);

            codeToDNMap = (Map) concentraCodeToDNCustomObject.get(attributeName + "CodesToADOUMapping");
            code = (String) identity.getAttribute(attributeName);
            accountADOU = (String) codeToDNMap.get(code);
            if(null!=accountADOU){
                getADOU = false;
            } else if (attIndx>=attributeNames.size()){
                accountADOU = (String) concentraCodeToDNCustomObject.get("Default");
                getADOU = false;
            }
        }
        
        distinguishedName = "CN=" + getUniqueValue(identity, "userid") + ",OU=" + accountADOU + "," + userBaseDNPath;
        
        logger.trace("Exit Active_Directory APP distinguishedName rule: " + distinguishedName);
        return (null!=distinguishedName?distinguishedName.trim():defaultValue);
    }

	/**
	 * DN is calculated based on the OU mappings defined in the custom object
	 * 
	 * @param spcontext
	 *            - context for search
	 * @param identity
	 *            - identity
	 * @return - Full DN
	 * @throws GeneralException 
	 */
	public static Object  getFV_Active_Directory_DN_Rule(SailPointContext spcontext, Identity identity, String op) throws GeneralException {

		logger.trace("Enter getFV_Active_Directory_DN_Rule");

		String CUSTOM_AD_MAPPINGS = "FD-ActiveDirectory-OU-Mappings";
		String KEY_SEARCH_ORDER = "searchOrder";
		String KEY_DEFAULT_OU = "defaultOU";
		String AND_SEPARATOR = "_";

		String dn = "";

		if (spcontext == null || identity == null) {
			logger.warn("Invalid identity or context");
		}

		String cn = (String) getFV_Active_Directory_cn_Rule(spcontext, identity, op);
		String ou = "";

		// OU should be calucalated based on the filters provided in the custom object
		// The custom object will have the OU mappings and the search order
		// First match in the order will give the right ou - Once the OU is obtained
		// exit the loop

		List searchOrderList = (List) getValueFromCustomObject(spcontext, CUSTOM_AD_MAPPINGS, KEY_SEARCH_ORDER);

		logger.debug("Search order defined in the custom object " + searchOrderList);

		// If the custom object is not valid - exit
		if (searchOrderList != null && !searchOrderList.isEmpty()) {
			for (Object searchAttributeOb : searchOrderList) {
				// Attribute from the search order
				String searchAttribute = (String) searchAttributeOb;
				// The custom object will have a key defined with the searchAttribute name defined
				// in the search order containing the possible OU combinations.
				String searchValue = "";
				// If there is no search attribute - continue to the next attribute
				if (Util.isNullOrEmpty(searchAttribute)) {
					logger.warn("SearchAttribute is null in the list, moving to the next attribute");
					continue;
				}

				// Now we have the value from the identity cube
				// This value should be present in the map that contains the OU
				Map ouMap = (Map) getValueFromCustomObject(spcontext, CUSTOM_AD_MAPPINGS, searchAttribute);
				if (ouMap == null || ouMap.isEmpty()) {
					logger.warn("ou mapping is null, moving to the next attribute");
					continue;
				}

				// Mulitple attribtues should be looked - to get the right OU
				if (searchAttribute.contains(AND_SEPARATOR)) {
					if (isDebugEnabled) {
						logger.debug("Muliple attributes are to be looked. Generating the lookup value");
					}
					int count = 0;
					String[] searchAttributes = searchAttribute.split(AND_SEPARATOR);
					for (String attributeName : searchAttributes) {
						String attributeValue = identity.getStringAttribute(attributeName);
						if (count == 0) {
							searchValue = attributeValue;
						} else {
							searchValue = searchValue + AND_SEPARATOR + attributeValue;
						}
						count++;
					}
				} else {
					if (isDebugEnabled) {
						logger.debug("Only one attribute is to be looked.");
					}
					searchValue = identity.getStringAttribute(searchAttribute);
				}

				if (isDebugEnabled) {
					logger.debug("Using the search value " + searchValue);
				}

				// see if the value is present in the ou map
				// If the value is present - break the loop
				if (Util.isNotNullOrEmpty(searchValue) && ouMap.containsKey(searchValue)) {
					ou = (String) ouMap.get(searchValue);
					break;
				}
			}
		}
		// Check if the ou is null or not
		if (Util.isNullOrEmpty(ou)) {
			if (isDebugEnabled) {
				logger.debug("No ou is defined in the mappings. Using the default ou.");
			}
			ou = (String) getValueFromCustomObject(spcontext, CUSTOM_AD_MAPPINGS, KEY_DEFAULT_OU);
		}

		// by this time , atleast a default ou should be populated, now generate the dn
		dn = "cn="+cn + "," + ou;
		if (isDebugEnabled) {
			logger.debug("DN to use " + dn);
		}
		return dn;
	}

	public static Object getFV_Active_Directory_employeeID_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("Enter AD employeeID rule");
		String employeeID = identity.getName();
		String val = fixNulls(employeeID);

		logger.trace("Exit AD employeeID rule: " + val);
		return val;
	}

	/**
	 * RFC2798: type of employment for a person
	 * Used to identify the employer to employee relationship. 
	 * Typical values used will be "Contractor", "Employee", "Intern", "Temp", "External", and "Unknown" but any value may be used.
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_employeeType_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("Enter AD employeeType rule");
		String employeeType = (String) identity.getAttribute("userType");
		String val = fixNulls(employeeType);

		logger.trace("Exit AD employeeType rule: " + val);
		return val;
	}

	/**
	 * RFC2256: first name(s) for which the entity is known by
	 * The givenName attribute is used to hold the part of a person's name which is not their surname nor middle name.
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_givenName2_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("EntergetFV_Active_Directory_givenName_Rule");

		String firstName = identity.getFirstname();
		String val = fixNulls(firstName);

		logger.trace("Exit getFV_Active_Directory_givenName_Rule: " + val);
		return val;
	}
	
	/*
     *********userPrincipalName********
     */
     public static Object getFV_Active_Directory_userPrincipalName_Rule(SailPointContext context, Identity identity, String op) throws GeneralException{
         return getFV_Active_Directory_userPrincipalName_Rule(context, identity, op, null);
     }

     public static Object getFV_Active_Directory_userPrincipalName_Rule(SailPointContext context, Identity identity, String op, Object defaultValue) throws GeneralException{
         logger.trace("Enter Active_Directory APP userPrincipalName rule");
         
         /* getUniqueValue retrieves User Id from Hist table using Employee Id */
         Custom customObject = context.getObjectByName(Custom.class,"Concentra-Custom-Config-Common");
         String userPrincipalName = getUniqueValue(identity, "userid") + customObject.get("domainName");
         logger.trace("Exit Active_Directory APP userPrincipalName rule: " + userPrincipalName);
         return userPrincipalName;
     }

     /*
     *********givenName********
     */
     public static Object getFV_Active_Directory_givenName_Rule(SailPointContext context, Identity identity, String op){
         return getFV_Active_Directory_givenName_Rule(context, identity, op, null);
     }

     public static Object getFV_Active_Directory_givenName_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
         logger.trace("Enter Active_Directory APP firstname rule");
         
         Object firstname = defaultValue;
         if (null!=identity){
            firstname = identity.getFirstname();
         }
         logger.trace("Exit Active_Directory APP firstname rule: " + firstname);
         return (null!=firstname?((String) firstname).trim():defaultValue);
     }

	public static Object getFV_Active_Directory_l_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("Enter AD l rule");
		String l = (String) identity.getAttribute("workCity");
		String val = fixNulls(l);

		logger.trace("Exit AD l rule: " + val);
		return val;
	}

	/**
	 * RFC2256: initials of some or all of names, but not the surname(s).
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_initials_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("Enter getFV_Active_Directory_initials_Rule");
		String val;
		String middleName = (String) identity.getAttribute("middleName");
		middleName = fixNulls(middleName);
		if (middleName.length() > 0) {
			val = middleName.substring(0,1);
		} else {
			val = "";
		}
		logger.trace("Exit getFV_Active_Directory_initials_Rule: " + val);
		return val;
	}


	public static Object getFV_Active_Directory_middleName_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("Enter getFV_Active_Directory_middleName_Rule");
		String middleName = (String) identity.getAttribute("middleName");
		String val = fixNulls(middleName);

		logger.trace("Exit getFV_Active_Directory_middleName_Rule: " + val);
		return val;
	}

	/**
	 * 'RFC1274: mobile telephone number
	 * The Mobile Telephone Number attribute type specifies a mobile telephone number associated with a person. 
	 * Attribute values should follow the agreed format for international telephone numbers: i.e., "+44 71 123 4567".
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_mobile_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("Enter AD mobile rule");
		String mobile = (String) identity.getAttribute("mobilePhone");
		String val = fixNulls(mobile);

		logger.trace("Exit AD mobile rule: " + val);
		return val;
	}
	
	/**
	 * RFC2459: legacy attribute for email addresses in DNs
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_mail_Rule(SailPointContext context, Identity identity, String op){
        return getFV_Active_Directory_mail_Rule(context, identity, op, null);
    }

    public static  Object getFV_Active_Directory_mail_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
        logger.trace("Enter Active_Directory APP email rule");
        
         /* getUniqueValue checks if the Employee Id already exists in the User Id Hist table, if found it gets the email attribute value
          * if none is found it generates a new one, checks it for duplicate and when a unique one is generated it updates the current Employe Id current row in the table
          */
        String email = getUniqueValue(identity, "email");

        logger.trace("Exit Active_Directory APP email rule: " + email);
        return (null!=email?email.trim():defaultValue);
    }


	/**
	 * RFC1274: DN of manager
	 * The Manager attribute type specifies the manager of an object represented by an entry.
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 * @throws GeneralException
	 */
    public static Object getFV_Active_Directory_manager_Rule(SailPointContext context, Identity identity, String op) throws GeneralException{
        return getFV_Active_Directory_manager_Rule(context, identity, op, null);
    }

    public static Object getFV_Active_Directory_manager_Rule(SailPointContext context, Identity identity, String op, Object defaultValue) throws GeneralException{
        logger.trace("Enter get Field Value manager for Active_Directory rule");
        
       Object manager = defaultValue;
       if (null!=identity){
           Identity managerIdentity = identity.getManager();
           if (null!=managerIdentity) {
               Application application = context.getObjectByName(Application.class, "Active_Directory");
               Link managerAdLink = managerIdentity.getLink(application);
               manager = (null!=managerAdLink?managerAdLink.getNativeIdentity():null);
           }
        }
        logger.trace("Exit get Field Value manager or Active_Directory rule - " + manager);
        return (null!=manager?manager:defaultValue);
    }
    
	public static Object getFV_Active_Directory_manager2_Rule(SailPointContext context, Identity identity, String op) throws GeneralException{
		logger.trace("Enter AD manager rule");

		String val = "";

		// Retrieve DN directly from the manager's AD account
		Identity managerIdentity = identity.getManager();
		if (managerIdentity == null) {
			logger.trace("No Manager found");
			return val;
		} else {
			String managerId = managerIdentity.getName();
			if (managerId==null || managerId.equals("")) {
				logger.trace("No Manager Id found");
				return val;
			} else {

				QueryOptions ops = new QueryOptions();
				ops.setIgnoreCase(true);
				ops.addFilter(Filter.ignoreCase(Filter.eq("name", managerId)));

				int num = context.countObjects(Identity.class, ops);
				Identity manager = null;

				if (num == 1) {
					manager = context.getUniqueObject(Identity.class, Filter.and(ops.getRestrictions()));
				}

				if (manager == null) {
					return val;
				}

				val = (String) getLinkVal(context, manager, "%%AD_TARGET_NAME%%", "distinguishedName");

				logger.trace("Exit AD manager rule: " + val);
				return val;
			}
		}
	}
	
	/**
     * Calculate the manager dn based on the manager information
     * Manager DN is populated on the Active directory link
     * 
     * @param spcontext
     *            - context for performing search
     * @param identity
     *            - identity to work upon
     * 
     * @param- applicationName
     *         - application name where the dn is present
     */
	public static Object getFV_Active_Directory_managerDN_Rule(SailPointContext context, Identity identity, String applicationName)  {
        String managerDN = "";
        boolean isDebugEnabled = logger.isDebugEnabled();

        if (context == null || identity == null) {
            logger.warn("context or identity to look for is not valid.");
            return managerDN;
        }

        // Get the manager present on the cube
        Identity manager = identity.getManager();
        if (manager == null) {
            logger.warn("No manager is defined.");
            return managerDN;
        }

        if (isDebugEnabled) {
            logger.debug("Manager is present for identity " + identity.getName() + ". Will search for the DN on the link.");
        }

        // Filter list
        List andFilterList = new ArrayList();
        andFilterList.add(Filter.eq("identity.name", manager.getName()));
        andFilterList.add(Filter.eq("application.name", applicationName));
        andFilterList.add(Filter.eq("displayName", manager.getName()));

        // Return attributes
        List returnAttributes = new ArrayList();
        returnAttributes.add("nativeIdentity");

        // Define Queryoptions for performing a search
        QueryOptions linkQo = new QueryOptions();
        linkQo.addFilter(Filter.and(andFilterList));
        try {
            // Search for the link
            Iterator linkIter = context.search(Link.class, linkQo, returnAttributes);
            if (linkIter.hasNext()) {
                managerDN = (String) linkIter.next();
                if (isDebugEnabled) {
                    logger.debug("Assigning manager " + managerDN + "for identity " + identity.getName());
                }
            }
        } catch (GeneralException e) {
            logger.error("Error while searching for manager dn. " + e.getMessage());
            e.printStackTrace();
        }
        return managerDN;
    }

	/**
	 * RFC2256: member of a group'
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_memberOf_Rule(SailPointContext context, Identity identity, String op){
		return getFV_Active_Directory_memberOf_Rule(context, identity, op, null);
	}

	public static Object getFV_Active_Directory_memberOf_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
		logger.trace("Enter get Field Value memberOf for app Active_Directory rule");
		
		Object memberOf = null;
		if (identity != null){
			memberOf = getAppAttribute(context, identity, "Active_Directory", "memberOf");
			logger.debug("Original memberOf: " + memberOf);
			if(memberOf == null || !(memberOf instanceof List)){
				memberOf = new ArrayList();
			}
			memberOf.add("CN=CTX-Test,OU=Citrix,OU=Groups,OU=Concentra,ou=demo,dc=seri,dc=sailpointdemo,dc=com");
		}
		logger.debug("Exit get Field Value memberOf for app Active_Directory rule: " + memberOf);
		logger.trace("Exit get Field Value memberOf for app Active_Directory rule");
		return ((memberOf!=null)?memberOf:defaultValue);
	}

	public static Object getFV_Active_Directory_memberOf2_Rule(SailPointContext context, Identity identity, String op){
		logger.trace("Enter AD memberOf rule");
		List memberOfList = new ArrayList();
		String group1="%%AD_TOP_LEVEL_DN%%";
		String group2="%%AD_TOP_LEVEL_DN%%";
		memberOfList.add(group1);
		memberOfList.add(group2);
		logger.trace("Exit AD memberOf rule: " + memberOfList);
		return memberOfList;
	}

	/**
	 * The name attribute type is the attribute supertype from which string attribute types typically used for naming may be formed. It is unlikely that values of this type itself will occur in an entry. 
	 * LDAP server implementations which do not support attribute subtyping need not recognize this attribute in requests. 
	 * Client implementations MUST NOT assume that LDAP servers are capable of performing attribute subtyping.
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_Name_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_Name_Rule rule");
		String val = identity.getName();
		logger.trace("Exit getFV_Active_Directory_Name_Rule rule: " + val);
		return val;
	}

	/**
	 * 
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_NetbootSCPBL_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_NetbootSCPBL_Rule rule");
		String val = (String) identity.getAttribute("netbootSCPBL");
		logger.trace("Exit getFV_Active_Directory_NetbootSCPBL_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_NetworkAddress_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_NetworkAddress_Rule rule");
		String val = (String) identity.getAttribute("networkAddress");
		logger.trace("Exit getFV_Active_Directory_NetworkAddress_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_NonSecurityMemberBL_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_NonSecurityMemberBL_Rule rule");
		String val = (String) identity.getAttribute("nonSecurityMemberBL");
		logger.trace("Exit getFV_Active_Directory_NonSecurityMemberBL_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_NtPwdHistory_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_NtPwdHistory_Rule rule");
		String val = (String) identity.getAttribute("ntPwdHistory");
		logger.trace("Exit getFV_Active_Directory_NtPwdHistory_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_nTSecurityDescriptor_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_nTSecurityDescriptor_Rule rule");
		String val = (String) identity.getAttribute("nTtSecurityDescriptor");
		logger.trace("Exit getFV_Active_Directory_nTSecurityDescriptor_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: organization this object belongs to
	 * This attribute contains the name of an organization (organizationName)
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_O_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_O_Rule rule");
		String val = (String) identity.getAttribute("organization");
		logger.trace("Exit getFV_Active_Directory_O_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_ObjectCategory_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ObjectCategory_Rule rule");
		String val = (String) identity.getAttribute("objectCategory");
		logger.trace("Exit getFV_Active_Directory_ObjectCategory_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: object classes of the entity
	 * The values of the objectClass attribute describe the kind of object which an entry represents. 
	 * The objectClass attribute is present in every entry, with at least two values. One of the values is either "top" or "alias".
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_ObjectClass_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter LDAP objectClass rule");
		List val = new ArrayList();
		val.add("cscPerson");
		val.add("cscSMPasswordServices");
		logger.trace("Exit LDAP objectClass rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_ObjectGUID_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ObjectGUID_Rule rule");
		String val = (String) identity.getAttribute("objectGUID");
		logger.trace("Exit getFV_Active_Directory_ObjectGUID_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_ObjectSid_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ObjectSid_Rule rule");
		String val = (String) identity.getAttribute("objectSid");
		logger.trace("Exit getFV_Active_Directory_ObjectSid_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_ObjectVersion_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ObjectVersion_Rule rule");
		String val = (String) identity.getAttribute("objectVersion");
		logger.trace("Exit getFV_Active_Directory_ObjectVersion_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_OperatorCount_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OperatorCount_Rule rule");
		String val = (String) identity.getAttribute("operatorCount");
		logger.trace("Exit getFV_Active_Directory_OperatorCount_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: organizational unit this object belongs to
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_OrganizationalUnitName_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OrganizationalUnitName_Rule rule");
		String val = (String) identity.getAttribute("organizationalUnitName");
		logger.trace("Exit getFV_Active_Directory_OrganizationalUnitName_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_OtherFacsimileTelephoneNumber_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OtherFacsimileTelephoneNumber_Rule rule");
		String val = (String) identity.getAttribute("otherFacsimileTelephoneNumber");
		logger.trace("Exit getFV_Active_Directory_OtherFacsimileTelephoneNumber_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_OtherHomePhone_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OtherHomePhone_Rule rule");
		String val = (String) identity.getAttribute("otherHomePhone");
		logger.trace("Exit getFV_Active_Directory_OtherHomePhone_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_OtherIpPhone_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OtherIpPhone_Rule rule");
		String val = (String) identity.getAttribute("otherIpPhone");
		logger.trace("Exit getFV_Active_Directory_OtherIpPhone_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_OtherLoginWorkstations_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OtherLoginWorkstations_Rule rule");
		String val = (String) identity.getAttribute("otherLoginWorkstations");
		logger.trace("Exit getFV_Active_Directory_OtherLoginWorkstations_Rule rule: " + val);
		return val;
	}

	/**
	 * The Other Mailbox attribute type specifies values for electronic mailbox types other than X.400 and rfc822.
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_OtherMailbox_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OtherMailbox_Rule rule");
		String val = (String) identity.getAttribute("otherMailbox");
		logger.trace("Exit getFV_Active_Directory_OtherMailbox_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_OtherMobile_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OtherMobile_Rule rule");
		String val = (String) identity.getAttribute("otherMobile");
		logger.trace("Exit getFV_Active_Directory_OtherMobile_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_OtherPager_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OtherPager_Rule rule");
		String val = (String) identity.getAttribute("otherPager");
		logger.trace("Exit getFV_Active_Directory_OtherPager_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_OtherTelephone_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OtherTelephone_Rule rule");
		String val = (String) identity.getAttribute("otherTelephone");
		logger.trace("Exit getFV_Active_Directory_OtherTelephone_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_OtherWellKnownObjects_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_OtherWellKnownObjects_Rule rule");
		String val = (String) identity.getAttribute("otherWellKnownObjects");
		logger.trace("Exit getFV_Active_Directory_OtherWellKnownObjects_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: organizational unit this object belongs to
	 * This attribute contains the name of an organizational unit (organizationalUnitName).
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_Ou_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_Ou_Rule rule");
		String val = (String) identity.getAttribute("organizationalUnitName");
		logger.trace("Exit getFV_Active_Directory_Ou_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC1274: pager telephone number
	 * The Pager Telephone Number attribute type specifies a pager telephone number for an object. 
	 * Attribute values should follow the agreed format for international telephone numbers: i.e., "+44 71 123 4567".
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_Pager_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_Pager_Rule rule");
		String val = (String) identity.getAttribute("pager");
		logger.trace("Exit getFV_Active_Directory_Pager_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_PartialAttributeDeletionList_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PartialAttributeDeletionList_Rule rule");
		String val = (String) identity.getAttribute("partialAttributeDeletionList");
		logger.trace("Exit getFV_Active_Directory_PartialAttributeDeletionList_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_PartialAttributeSet_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PartialAttributeSet_Rule rule");
		String val = (String) identity.getAttribute("partialAttributeSet");
		logger.trace("Exit getFV_Active_Directory_PartialAttributeSet_Rule rule: " + val);
		return val;
	}

	/**
	 * 
	 * @param context
	 * @param identity
	 * @param op
	 * @return
	 */
	public static Object getFV_Active_Directory_password_Rule(SailPointContext context, Identity identity, String op){
        return getFV_Active_Directory_password_Rule(context, identity, op, null);
    }
    
    public static Object getFV_Active_Directory_password_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
        logger.trace("Enter get Field Value AD->password for app Standard rule");
        
        Object password = null;
        if (null!=identity){
            String appName = "Active_Directory";
            try {
                PasswordGenerator pg = new PasswordGenerator(context);

                logger.debug(" generatePolicyPassword : id " + identity.getName() + " and app: " + appName);
                Application app = (Application) context.getObjectByName(Application.class, appName);
                password = pg.generatePassword(identity, app);                 
            } catch (Exception e){
                logger.error("Exception " + e.toString());
            }
        }

        logger.trace("Exit get Field Value AD->password for app Standard rule: " + password);
        return (null!=password?((String) password).trim():defaultValue);
    }

	/**
	 * Azure Active Directory Application
	 * <AllowedValuesDefinition>
            <Value>
              <List>
                <String>DisablePasswordExpiration</String>
                <String>DisableStrongPassword</String>
                <String>DisablePasswordExpiration, DisableStrongPassword</String>
              </List>
            </Value>
          </AllowedValuesDefinition>
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_PasswordPolicies_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PasswordPolicies_Rule rule");
		List values = new ArrayList();
		values.add("DisablePasswordExpiration");
		values.add("DisableStrongPassword");
		values.add("DisablePasswordExpiration, DisableStrongPassword");

		Iterator types=values.iterator();
		while (types.hasNext()) {
			logger.trace("Active_Directory_PasswordPolicies_Rule AllowedValue:: " + (String)types.next());
		}

		logger.trace("Exit getFV_Active_Directory_PasswordPolicies_Rule :" + values.size() +" allowed values.");
		return values;
	}

	/**
	 * RFC1274: personal title
	 * The Personal Title attribute type specifies a personal title for a person. 
	 * Examples of personal titles are "Ms", "Dr", "Prof" and "Rev".
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_PersonalTitle_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PersonalTitle_Rule rule");
		String val = (String) identity.getAttribute("personalTitle");
		logger.trace("Exit getFV_Active_Directory_PersonalTitle_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: Physical Delivery Office Name
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_PhysicalDeliveryOfficeName_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PhysicalDeliveryOfficeName_Rule rule");
		String val = (String) identity.getAttribute("physicalDeliveryOfficeName");
		logger.trace("Exit getFV_Active_Directory_PhysicalDeliveryOfficeName_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_PossibleInferiors_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PossibleInferiors_Rule rule");
		String val = (String) identity.getAttribute("possibleInferiors");
		logger.trace("Exit getFV_Active_Directory_PossibleInferiors_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: postal address
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_PostalAddress_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PostalAddress_Rule rule");
		String val = (String) identity.getAttribute("postalAddress");
		logger.trace("Exit getFV_Active_Directory_PostalAddress_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: postal code
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_PostalCode_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PostalCode_Rule rule");
		String val = (String) identity.getAttribute("postalCode");
		logger.trace("Exit getFV_Active_Directory_PostalCode_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: Post Office Box
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_PostOfficeBox_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PostOfficeBox_Rule rule");
		String val = (String) identity.getAttribute("postOfficeBox");
		logger.trace("Exit getFV_Active_Directory_PostOfficeBox_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: preferred delivery method'
	 * One of the following:
			any
			mhs
			physical
			telex
			teletex
			g3fax
			g4fax
			ia5
			videotex
			telephone
	 */
	public static Object getFV_Active_Directory_PreferredDeliveryMethod_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PreferredDeliveryMethod_Rule");
		String val = (String) identity.getAttribute("preferredDeliveryMethod");
		logger.trace("Exit getFV_Active_Directory_PreferredDeliveryMethod_Rule: " + val);
		return val;
	}

	/**
	 * RFC2798: preferred written or spoken language for a person
	 * Used to indicate an individual's preferred written or spoken language. 
	 * This is useful for international correspondence or human-computer interaction. 
	 * Values for this attribute type MUST conform to the definition of the Accept-Language header field defined in [RFC2068] ("Hypertext Transfer Protocol -- HTTP/1.1") with one exception: the sequence "Accept-Language" ":" should be omitted. 
	 * This is a single valued attribute type.
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_PreferredLanguaged_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PreferredLanguaged_Rule");
		String val = (String) identity.getAttribute("preferredLanguaged");
		logger.trace("Exit getFV_Active_Directory_PreferredLanguaged_Rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_PreferredOU_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PreferredOU_Rule rule");
		String val = (String) identity.getAttribute("preferredOU");
		logger.trace("Exit getFV_Active_Directory_PreferredOU_Rule rule: " + val);
		return val;
	}

	/**
	 * 
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_PrimaryGroupDN_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PrimaryGroupDN_Rule rule");
		String val = (String) identity.getAttribute("primaryGroupDN");
		logger.trace("Exit getFV_Active_Directory_PrimaryGroupDN_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_PrimaryGroupID_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PrimaryGroupID_Rule rule");
		String val = (String) identity.getAttribute("primaryGroupID");
		logger.trace("Exit getFV_Active_Directory_PrimaryGroupID_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_PrimaryInternationalISDNNumber_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PrimaryInternationalISDNNumber_Rule rule");
		String val = (String) identity.getAttribute("primaryInternationalISDNNumber");
		logger.trace("Exit getFV_Active_Directory_PrimaryInternationalISDNNumber_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_PrimaryTelexNumber_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PrimaryTelexNumber_Rule rule");
		String val = (String) identity.getAttribute("primaryTelexNumber");
		logger.trace("Exit getFV_Active_Directory_PrimaryTelexNumber_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_ProfilePath_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ProfilePath_Rule rule");
		String val = (String) identity.getAttribute("profilePath");
		logger.trace("Exit getFV_Active_Directory_ProfilePath_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_ProxiedObjectName_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ProxiedObjectName_Rule rule");
		String val = (String) identity.getAttribute("proxiedObjectName");
		logger.trace("Exit getFV_Active_Directory_ProxiedObjectName_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_ProxyAddresses_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ProxyAddresses_Rule rule");
		String val = (String) identity.getAttribute("proxyAddresses");
		logger.trace("Exit getFV_Active_Directory_ProxyAddresses_Rule rule: " + val);
		return val;
	}

	/**
	 *  type="boolean"
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_PwdLastSet_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_PwdLastSet_Rule rule");
		String val = (String) identity.getAttribute("pwdLastSet");
		logger.trace("Exit getFV_Active_Directory_PwdLastSet_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_QueryPolicyBL_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_QueryPolicyBL_Rule rule");
		String val = (String) identity.getAttribute("queryPolicyBL");
		logger.trace("Exit getFV_Active_Directory_QueryPolicyBL_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: registered postal address
	 * This attribute holds a postal address suitable for reception of telegrams or expedited documents, where it is necessary to have the recipient accept delivery.
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_RegisteredAddress_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_RegisteredAddress_Rule rule");
		String val = (String) identity.getAttribute("registeredAddress");
		logger.trace("Exit getFV_Active_Directory_RegisteredAddress_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_ReplPropertyMetaData_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ReplPropertyMetaData_Rule rule");
		String val = (String) identity.getAttribute("replPropertyMetaData");
		logger.trace("Exit getFV_Active_Directory_ReplPropertyMetaData_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_ReplUpToDateVector_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ReplUpToDateVector_Rule rule");
		String val = (String) identity.getAttribute("replUpToDateVector");
		logger.trace("Exit getFV_Active_Directory_ReplUpToDateVector_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_RepsFrom_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_RepsFrom_Rule rule");
		String val = (String) identity.getAttribute("repsFrom");
		logger.trace("Exit getFV_Active_Directory_RepsFrom_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_RepsTo_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_RepsTo_Rule rule");
		String val = (String) identity.getAttribute("repsTo");
		logger.trace("Exit getFV_Active_Directory_RepsTo_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_Revision_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_Revision_Rule rule");
		String val = (String) identity.getAttribute("revision");
		logger.trace("Exit getFV_Active_Directory_Revision_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_Rid_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_Rid_Rule rule");
		String val = (String) identity.getAttribute("rid");
		logger.trace("Exit getFV_Active_Directory_Rid_Rule rule: " + val);
		return val;
	}

	//TODO: Define which value to return
	/**
	 * 
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_sAMAccountName_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_sAMAccountName_Rule rule");
		String val = identity.getName();
		String sAMAccountName = (String) identity.getAttribute("userId");

		logger.trace("Exit getFV_Active_Directory_sAMAccountName_Rule rule: " + val);
		return val;
	}

	public static Object getFV_Active_Directory_sAMAccountType_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_sAMAccountType_Rule rule");
		String val = (String) identity.getAttribute("sAMAccountType");
		logger.trace("Exit getFV_Active_Directory_sAMAccountType_Rule rule: " + val);
		return val;
	}

	/**
	 * For section 'Skype for Business'
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_SipAddress_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SipAddress_Rule rule");
		String val = (String) identity.getAttribute("SipAddress");
		logger.trace("Exit getFV_Active_Directory_SipAddress_Rule rule: " + val);
		return val;
	}

	/**
	 * For section 'Skype for Business'
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_SipDomain_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SipDomain_Rule rule");
		String val = (identity.getAttribute("SipDomain")!=null)?(String)identity.getAttribute("SipDomain"):null;
		logger.trace("Exit getFV_Active_Directory_SipDomain_Rule rule: " + val);
		return val;
	}

	/**
	 * For section 'Skype for Business' in baptist-healthcare-system
	 * <AllowedValuesDefinition>
            <Value>
                <List>
                    <String>SamAccountName</String>
                    <String>FirstLastName</String>
                    <String>EmailAddress</String>
                </List>
            </Value>
        </AllowedValuesDefinition>
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_SipAddressType_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SipAddressType_Rule rule");

		List values = new ArrayList();
		values.add("SamAccountName");
		values.add("FirstLastName");
		values.add("EmailAddress");

		Iterator types=values.iterator();
		while (types.hasNext()) {
			logger.trace("Active_Directory_SipAddressTypen_Rule AllowedValue:: " + (String)types.next());
		}

		logger.trace("Exit getFV_Active_Directory_SipAddressTypen_Rule :" + values.size() +" allowed values.");
		return values;
	}

	public static Object getFV_Active_Directory_ScriptPath_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ScriptPath_Rule rule");
		String val = (String) identity.getAttribute("scriptPath");
		logger.trace("Exit getFV_Active_Directory_ScriptPath_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_sDRightsEffective_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_sDRightsEffective_Rule rule");
		String val = (String) identity.getAttribute("sDRightsEffective");
		logger.trace("Exit getFV_Active_Directory_sDRightsEffective_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_SecurityIdentifier_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SecurityIdentifier_Rule rule");
		String val = (String) identity.getAttribute("SecurityIdentifier");
		logger.trace("Exit getFV_Active_Directory_SecurityIdentifier_Rule rule: " + val);
		return val;
	}


	public static Object getFV_Active_Directory_SeeAlso_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SeeAlso_Rule rule");
		String val = (String) identity.getAttribute("seeAls");
		logger.trace("Exit getFV_Active_Directory_SeeAlso_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_ServerReferenceBL_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ServerReferenceBL_Rule rule");
		String val = (String) identity.getAttribute("serverReferenceBL");
		logger.trace("Exit getFV_Active_Directory_ServerReferenceBL_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_ServicePrincipalName_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ServicePrincipalName_Rule rule");
		String val = (String) identity.getAttribute("servicePrincipalName");
		logger.trace("Exit getFV_Active_Directory_ServicePrincipalName_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_ShowInAddressBook_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ShowInAddressBook_Rule rule");
		String val = (String) identity.getAttribute("showInAddressBook");
		logger.trace("Exit getFV_Active_Directory_ShowInAddressBook_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_ShowInAdvancedViewOnly_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ShowInAdvancedViewOnly_Rule rule");
		String val = (String) identity.getAttribute("showInAdvancedViewOnlyr");
		logger.trace("Exit getFV_Active_Directory_ShowInAdvancedViewOnly_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_sIDHistory_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_sIDHistory_Rule rule");
		String val = (String) identity.getAttribute("sIDHistory");
		logger.trace("Exit getFV_Active_Directory_sIDHistory_Rule rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_SiteObjectBL_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SiteObjectBL_Rule rule");
		String val = (String) identity.getAttribute("siteObjectBLr");
		logger.trace("Exit getFV_Active_Directory_SiteObjectBL_Rule rule: " + val);
		return val;
	}

	/**
	 * RFC2256: last (family) name(s) for which the entity is known by
	 * This is the X.500 surname attribute, which contains the family name of a person.
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_sn_Rule(SailPointContext context, Identity identity, String op){
        return getFV_Active_Directory_sn_Rule(context, identity, op, null);
    }

    public static Object getFV_Active_Directory_sn_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
        logger.trace("Enter Active_Directory APP lastname rule");
        
        Object lastname = defaultValue;
        if (null!=identity){
           lastname = identity.getLastname();
        }
        logger.trace("Exit Active_Directory APP lastname rule: " + lastname);
        return (null!=lastname?((String) lastname).trim():defaultValue);
    }


	/**
	 * RFC2256: state or province which this object resides in
	 * This attribute contains the full name of a state or province (stateOrProvinceName).
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_St_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_St_Rule ");
		String val = (String) identity.getAttribute("st");
		logger.trace("Exit getFV_Active_Directory_St_Rule : " + val);
		return val;
	}

	/**
	 * RFC2256: state or province which this object resides in
	 * This attribute contains the full name of a state or province (stateOrProvinceName).
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_StateOrProvinceName_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_StateOrProvinceName_Rule ");
		String val = (String) identity.getAttribute("stateOrProvinceName");
		logger.trace("Exit getFV_Active_Directory_StateOrProvinceName_Rule : " + val);
		return val;
	}

	/**
	 * RFC2256: street address of this object
	 * This attribute contains the physical address of the object to which the entry corresponds, such as an address for package delivery (streetAddress).
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_Street_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_Street_Rule ");
		String val = (String) identity.getAttribute("street");
		logger.trace("Exit getFV_Active_Directory_Street_Rule : " + val);
		return val;
	}

	/**
	 * RFC2256: street address of this object
	 * This attribute contains the physical address of the object to which the entry corresponds, such as an address for package delivery (streetAddress).
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_StreetAddress_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_StreetAddress_Rule ");
		String val = (String) identity.getAttribute("streetAddress");
		logger.trace("Exit getFV_Active_Directory_StreetAddress_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_SubRefs_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SubRefs_Rule ");
		String val = (String) identity.getAttribute("subRefs");
		logger.trace("Exit getFV_Active_Directory_SubRefs_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_SubSchemaSubEntry_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SubSchemaSubEntry_Rule ");
		String val = (String) identity.getAttribute("subSchemaSubEntry");
		logger.trace("Exit getFV_Active_Directory_SubSchemaSubEntry_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_SupplementalCredentials_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SupplementalCredentials_Rule ");
		String val = (String) identity.getAttribute("supplementalCredentials");
		logger.trace("Exit getFV_Active_Directory_SupplementalCredentials_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_SystemFlags_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SystemFlags_Rule ");
		String val = (String) identity.getAttribute("systemFlags");
		logger.trace("Exit getFV_Active_Directory_SystemFlags_Rule : " + val);
		return val;
	}

	/**
	 * RFC2256: Telephone Number
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_TelephoneNumber_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_TelephoneNumber_Rule ");
		String val = (String) identity.getAttribute("telephoneNumber");
		logger.trace("Exit getFV_Active_Directory_TelephoneNumber_Rule : " + val);
		return val;
	}

	public static Object getFV_Active_Directory_TeletexTerminalIdentifier_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_TeletexTerminalIdentifier_Rule ");
		String val = (String) identity.getAttribute("teletexTerminalIdentifier");
		logger.trace("Exit getFV_Active_Directory_TeletexTerminalIdentifier_Rule: " + val);
		return val;
	}
	public static Object getFV_Active_Directory_TelexNumber_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_TelexNumber_Rule ");
		String val = (String) identity.getAttribute("telexNumber");
		logger.trace("Exit getFV_Active_Directory_TelexNumber_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_TerminalServer_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_TerminalServer_Rule ");
		String val = (String) identity.getAttribute("terminalServer");
		logger.trace("Exit getFV_Active_Directory_TerminalServer_Rule : " + val);
		return val;
	}

	/**
	 * The Text Encoded O/R Address attribute type specifies a text encoding of an X.400 O/R address, as specified in RFC 987.
	 * The use of this attribute is deprecated as the attribute is intended for interim use only. This attribute will be the first candidate for the attribute expiry mechanisms!
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_TextEncodedORAddress_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_TextEncodedORAddress_Rule ");
		String val = (String) identity.getAttribute("textEncodedORAddress");
		logger.trace("Exit getFV_Active_Directory_TextEncodedORAddress_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_ThumbnailLogo_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ThumbnailLogo_Rule ");
		String val = (String) identity.getAttribute("thumbnailLogo");
		logger.trace("Exit getFV_Active_Directory_ThumbnailLogo_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_ThumbnailPhoto_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_ThumbnailPhoto_Rule ");
		String val = (String) identity.getAttribute("thumbnailPhoto");
		logger.trace("Exit getFV_Active_Directory_ThumbnailPhoto_Rule : " + val);
		return val;
	}

	/**
	 * RFC2256: title associated with the entity
	 * This attribute contains the title, such as "Vice President", of a person in their organizational context. 
	 * The "personalTitle" attribute would be used for a person's title independent of their job function.
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_title_Rule(SailPointContext context, Identity identity, String op){
        return getFV_Active_Directory_title_Rule(context, identity, op, null);
    }

    public static Object getFV_Active_Directory_title_Rule(SailPointContext context, Identity identity, String op, Object defaultValue){
        logger.trace("Enter Active_Directory APP title rule");
        
        Object title = defaultValue;
        if (null!=identity){
           title = identity.getAttribute("jobtitle");
        }
        logger.trace("Exit Active_Directory APP title rule: " + title);
        return (null!=title?((String) title).trim():defaultValue);
    }


	public static Object getFV_Active_Directory_TokenGroups_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_TokenGroups_Rule ");
		String val = (String) identity.getAttribute("tokenGroups");
		logger.trace("Exit getFV_Active_Directory_TokenGroups_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_TokenGroupsGlobalAndUniversal_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_TokenGroupsGlobalAndUniversal_Rule ");
		String val = (String) identity.getAttribute("tokenGroupsGlobalAndUniversal");
		logger.trace("Exit getFV_Active_Directory_TokenGroupsGlobalAndUniversal_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_TokenGroupsNoGCAcceptable_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_TokenGroupsNoGCAcceptable_Rule ");
		String val = (String) identity.getAttribute("tokenGroupsNoGCAcceptable");
		logger.trace("Exit getFV_Active_Directory_TokenGroupsNoGCAcceptable_Rule : " + val);
		return val;
	}

	/**
	 * RFC1274: user identifier
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_UID_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UID_Rule ");
		String val = (String) identity.getAttribute("uid");
		logger.trace("Exit getFV_Active_Directory_UID_Rule : " + val);
		return val;
	}


	public static Object getFV_Active_Directory_UnicodePwd_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UnicodePwd_Rule ");
		String val = (String) identity.getAttribute("unicodePwd");
		logger.trace("Exit getFV_Active_Directory_UnicodePwd_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_Url_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_Url_Rule ");
		String val = (String) identity.getAttribute("url");
		logger.trace("Exit getFV_Active_Directory_Url_Rule : " + val);
		return val;
	}

	/**
	 * Azure Active Directory Application
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_UsageLocation_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_SipAddressType_Rule rule");

		List values = new ArrayList();
		values.add("Australia;AU");
		values.add("Canada;CA");
		values.add("France;FR");
		values.add("Germany;DE");
		values.add("Greece;GR");
		values.add("Hong Kong;HK");
		values.add("Hungary;HU");
		values.add("Iceland;IS");
		values.add("India;IN");
		values.add("Israel;IL");
		values.add("Italy;IT");
		values.add("NetherLands;NL");
		values.add("New Zealand;NZ");
		values.add("Peru;PE");
		values.add("Philippines;PH");
		values.add("Poland;PL");
		values.add("Russian federation;RU");
		values.add("South Africa;ZA");
		values.add("Switzerland;CH");
		values.add("Ukrain;UA");
		values.add("United Kingdom;GB");
		values.add("United States;US");

		Iterator types=values.iterator();
		while (types.hasNext()) {
			logger.trace("Active_Directory_UsageLocation_Rule AllowedValue:: " + (String)types.next());
		}

		logger.trace("Exit getFV_Active_Directory_UsageLocation_Rule :" + values.size() +" allowed values.");
		return values;
	}

	/**
	 * Azure Active Directory Application
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_UsageLocation_Rule(SailPointContext context, Identity identity, String op, String defaultValue) {
		logger.trace("Enter getFV_Active_Directory_SipAddressType_Rule rule");

		List values = new ArrayList();
		values.add("Australia;AU");
		values.add("Canada;CA");
		values.add("France;FR");
		values.add("Germany;DE");
		values.add("Greece;GR");
		values.add("Hong Kong;HK");
		values.add("Hungary;HU");
		values.add("Iceland;IS");
		values.add("India;IN");
		values.add("Israel;IL");
		values.add("Italy;IT");
		values.add("NetherLands;NL");
		values.add("New Zealand;NZ");
		values.add("Peru;PE");
		values.add("Philippines;PH");
		values.add("Poland;PL");
		values.add("Russian federation;RU");
		values.add("South Africa;ZA");
		values.add("Switzerland;CH");
		values.add("Ukrain;UA");
		values.add("United Kingdom;GB");
		values.add("United States;US");

		Iterator types=values.iterator();
		while (types.hasNext()) {
			logger.trace("Active_Directory_UsageLocation_Rule AllowedValue:: " + (String)types.next());
		}

		logger.trace("Exit getFV_Active_Directory_UsageLocation_Rule :" + values.size() +" allowed values.");
		return values;
	}

	public static Object getFV_Active_Directory_UserAccountControl_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UserAccountControl_Rule ");
		String val = (String) identity.getAttribute("userAccountControl");
		logger.trace("Exit getFV_Active_Directory_UserAccountControl_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_UserCert_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UserCert_Rule ");
		String val = (String) identity.getAttribute("userCert");
		logger.trace("Exit getFV_Active_Directory_UserCert_Rule : " + val);
		return val;
	}

	/**
	 * RFC2256: X.509
	 * Must be transferred using ;binary with certificateExactMatch rule (per X.509)
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_UserCertificate_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UserCertificate_Rule ");
		String val = (String) identity.getAttribute("userCertificate");
		logger.trace("Exit getFV_Active_Directory_UserCertificate_Rule : " + val);
		return val;
	}

	public static Object getFV_Active_Directory_UserParameters_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UserParameters_Rule ");
		String val = (String) identity.getAttribute("userParameters");
		logger.trace("Exit getFV_Active_Directory_UserParameters_Rule : " + val);
		return val;
	}

	/**
	 * RFC2256/2307: password of user
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_UserPassword_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UserPassword_Rule ");
		String val = (String) identity.getAttribute("userPassword");
		logger.trace("Exit getFV_Active_Directory_UserPassword_Rule : " + val);
		return val;
	}

	public static Object getFV_Active_Directory_UserPrincipalName_Rule(SailPointContext context, Identity identity, String op) throws GeneralException {
		logger.trace("Enter getFV_Active_Directory_UserPrincipalName_Rule ");

		String lastName = escapeADValues((String) getFV_Active_Directory_sn_Rule(context, identity, op));
		lastName = lastName.replaceAll("\\s+","");
		String firstName = escapeADValues((String) getFV_Active_Directory_Name_Rule(context, identity, op));
		firstName = firstName.replaceAll("\\s+","");
		String preferredName = escapeADValues((String) getFV_Active_Directory_cscPreferredName_Rule(context, identity, op));
		preferredName = preferredName.replaceAll("\\s+","");

		if (preferredName!=null && !preferredName.equals("")) {
			firstName = preferredName;
		}
		//todo: compare with email address
		String val = firstName + "." + lastName + "@%%COMPANY_MAIL_ALIAS%%";

		Application appObj = context.getObject(Application.class, "%%AD_TARGET_NAME%%");


		if (!isUniqueADName(appObj, "userPrincipalName", val)) {
			int ctr = 2;
			val = firstName + "." + lastName + ctr + "@%%COMPANY_MAIL_ALIAS%%";

			while (!isUniqueADName(appObj, "userPrincipalName", val)) {
				ctr++;
				val = firstName + "." + lastName + ctr + "@%%COMPANY_MAIL_ALIAS%%";
				logger.trace("Value " + val + " was tested");
			}
		}

		logger.trace("Exit getFV_Active_Directory_UserPrincipalName_Rule : " + val);
		return val;
	}

	public static Object getFV_Active_Directory_UserSharedFolder_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UserSharedFolder_Rule ");
		String val = (String) identity.getAttribute("userSharedFolder");
		logger.trace("Exit getFV_Active_Directory_UserSharedFolder_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_UserSharedFolderOther_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UserSharedFolderOther_Rule ");
		String val = (String) identity.getAttribute("userSharedFolderOther");
		logger.trace("Exit getFV_Active_Directory_UserSharedFolderOther_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_UserSMIMECertificate_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UserSMIMECertificate_Rule ");
		String val = (String) identity.getAttribute("userSMIMECertificate");
		logger.trace("Exit getFV_Active_Directory_UserSMIMECertificate_Rule : " + val);
		return val;
	}

	/**
	 * 
	 * @param context
	 * @param identity
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static Object getFV_Active_Directory_UserType_Rule(SailPointContext context, Identity identity, String op, String defaultValue) {
		logger.trace("Enter getFV_Active_Directory_UserType_Rule rule");

		List values = new ArrayList();
		values.add("Member");
		values.add("Guest");

		Iterator types=values.iterator();
		while (types.hasNext()) {
			logger.trace("Active_Directory_UserType_Rule AllowedValue:: " + (String)types.next());
		}

		logger.trace("Exit getFV_Active_Directory_UserType_Rule :" + values.size() +" allowed values.");
		return values;
	}
	public static Object getFV_Active_Directory_UserWorkstations_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_UserWorkstations_Rule ");
		String val = (String) identity.getAttribute("userWorkstations");
		logger.trace("Exit getFV_Active_Directory_UserWorkstations_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_uSNChanged_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_uSNChanged_Rule ");
		String val = (String) identity.getAttribute("uSNChanged");
		logger.trace("Exit getFV_Active_Directory_uSNChanged_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_uSNCreated_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_uSNCreated_Rule ");
		String val = (String) identity.getAttribute("uSNCreated");
		logger.trace("Exit getFV_Active_Directory_uSNCreated_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_uSNDSALastObjRemoved_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_uSNDSALastObjRemoved_Rule ");
		String val = (String) identity.getAttribute("uSNDSALastObjRemoved");
		logger.trace("Exit getFV_Active_Directory_uSNDSALastObjRemoved_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_USNIntersite_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_USNIntersite_Rule ");
		String val = (String) identity.getAttribute("uSNIntersite");
		logger.trace("Exit getFV_Active_Directory_USNIntersite_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_uSNLastObjRem_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_uSNLastObjRem_Rule ");
		String val = (String) identity.getAttribute("uSNLastObjRem");
		logger.trace("Exit getFV_Active_Directory_uSNLastObjRem_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_uSNSource_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_uSNSource_Rule ");
		String val = (String) identity.getAttribute("uSNSource");
		logger.trace("Exit getFV_Active_Directory_uSNSource_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_WbemPath_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_WbemPath_Rule ");
		String val = (String) identity.getAttribute("wbemPath");
		logger.trace("Exit getFV_Active_Directory_WbemPath_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_WellKnownObjects_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_WellKnownObjects_Rule ");
		String val = (String) identity.getAttribute("wellKnownObjects");
		logger.trace("Exit getFV_Active_Directory_WellKnownObjects_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_WhenChanged_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_WhenChanged_Rule ");
		String val = (String) identity.getAttribute("whenChanged");
		logger.trace("Exit getFV_Active_Directory_WhenChanged_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_WhenCreated_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_WhenCreated_Rule ");
		String val = (String) identity.getAttribute("whenCreated");
		logger.trace("Exit getFV_Active_Directory_WhenCreated_Rule : " + val);
		return val;
	}
	public static Object getFV_Active_Directory_wWWHomePage_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_wWWHomePage_Rule ");
		String val = (String) identity.getAttribute("wWWHomePage");
		logger.trace("Exit getFV_Active_Directory_wWWHomePage_Rule : " + val);
		return val;
	}

	/**
	 * RFC2256: X.121 Address
	 * @param context
	 * @param identity
	 * @param str
	 * @return
	 */
	public static Object getFV_Active_Directory_X121Address_Rule(SailPointContext context, Identity identity, String op) {
		logger.trace("Enter getFV_Active_Directory_X121Address_Rule ");
		String val = (String) identity.getAttribute("x121Address");
		logger.trace("Exit getFV_Active_Directory_X121Address_Rule : " + val);
		return val;
	}


	/******************************************************************************
	UTILITIES
	 ******************************************************************************/

	public String verifyNull(Object o){
		if(o != null){
			return o.toString();
		}else{
			return "";
		}

	}

	/***********************************************************************
    Check for uniqueness of AD naming attribute against a given AD application,
    verified by using a copy of the connector.
	 ************************************************************************/

	public static Boolean isUniqueADName(Application application, String attName, String attValue) {
		XMLReferenceResolver context;
		logger.trace("Entering isUniqueADName");
		Boolean unique = true;

		// Make a copy of the AD application
		Application appCopy = (Application) application.deepCopy(context);
		String appConnName = appCopy.getConnector();

		// Get the domain DN and use this as the searchDN so that we search the entire domain
		String searchDN = appCopy.getAttributes().getMap().get("domainSettings").get(0).get("domainDN");

		List dnList = new ArrayList();
		Map setupMap = new HashMap();
		setupMap.put("iterateSearchFilter", "(" + attName +  "=" + attValue + ")");
		setupMap.put("searchDN", searchDN);

		dnList.add(0, setupMap); // This takes the map we just changed and adds it back to the list in place of the map that was there.
		appCopy.setAttribute("searchDNs", dnList); // Add the list back to the app copy.
		appCopy.setAttribute("referral", "ignore");
		appCopy.setAttribute("useHasMoreElements", true);
		appCopy.setCustomizationRule(null);

		Connector appConnector = sailpoint.connector.ConnectorFactory.getConnector(appCopy, null);
		CloseableIterator iterator = appConnector.iterateObjects("account", null, null);

		try {
			if (iterator != null && iterator.hasNext() ) {
				if(iterator.hasNext()) {
					ResourceObject user = (ResourceObject) iterator.next();
					unique = false;
				}
			}

		} finally {
			if ( iterator != null ) iterator.close();
		}
		logger.trace("Exiting isUniqueADName");
		((PersistenceManager) context).decache(appCopy);
		return unique;
	}

	public Boolean isUniqueEmail(String mailNickname, String shortName) {
		logger.trace("Entering isUniqueEmail");
		Boolean unique = null;

		Connection dbCxn = null;
		try {
			dbCxn = context.getConnection();
		} catch (Exception ex) {
			String errMsg = "Error while connecting to database";
			logger.error(errMsg);
			logger.error(ex);
			return unique;
		}

		logger.debug("Successfully connected to database");

		try {

			String emailAddress = mailNickname + "@%%COMPANY_MAIL_ALIAS%%";

			String sqlQuery = "SELECT ShortName FROM email_address WHERE EmailAddress = ?";

			PreparedStatement prStmt = dbCxn.prepareStatement(sqlQuery);
			prStmt.setString(1, emailAddress);

			ResultSet rs = prStmt.executeQuery();
			while ( (null != rs) && (rs.next()) ) {
				if (Util.nullSafeEq(rs.getString("ShortName"), shortName)) {
					unique = false;
				}
			}
			// ResultSets must be closed just like JDBC connections and cursors.
			rs.close();
			prStmt.close();

			if (unique == null) {
				unique = true;
				logger.debug("Email address "+emailAddress+" is unique");
			} else {
				logger.debug("Email address "+emailAddress+" is not unique");
				unique = false;
			}

		} catch (Exception ex) {

			logger.error(ex);

		}

		logger.trace("Exiting isUniqueEmail");
		return unique;
	}


	public Boolean isUniqueLDSync(String mailNickname,String searchOU) throws GeneralException, NamingException {
		logger.trace("Entering isUniqueLDSync");
		String emailAddress = mailNickname + "@%%COMPANY_MAIL_ALIAS%%";
		Hashtable env = new Hashtable();
		String encryptedPassword = "%%LDSYNC_PASSWORD%%";
		String decryptedPassword = context.decrypt(encryptedPassword);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "%%LDSYNC_URL%%");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "%%LDSYNC_PRIVILEGED_USER%%");
		env.put(Context.SECURITY_CREDENTIALS, decryptedPassword);
		env.put("java.naming.ldap.version","3");

		DirContext ctx = new InitialDirContext(env);
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//searchControls.setCountLimit(10);
		NamingEnumeration<SearchResult> namingEnumeration = ctx.search(searchOU, "(mail="+emailAddress+")", new Object[]{}, searchControls);

		if (namingEnumeration.hasMore()) {
			ctx.close();
			logger.trace("email address was found in LDSync");
			return false;
		} else {
			logger.trace("email address was not found in LDSync");
			ctx.close();
			return true;
		}
	}

	public static Object getLinkVal(SailPointContext context, Identity identity, String appName, String attrName) throws GeneralException{
		logger.trace("Enter getLinkVal");
		Object val = "";

		logger.trace("appName: " + appName);
		logger.trace("attrName: " + attrName);

		Link link = getSafeLink(context, identity, appName);

		if (link != null){
			val = link.getAttribute(attrName);

			if (val != null){
				if (val instanceof String){
					val = ((String) val).trim();
				}
			} else {
				val = "";
			}

		} else {
			logger.warn("No link found for appName: " + appName);
		}

		logger.trace("Exit getLinkVal: " + val);
		return val;
	}

	public static Link getSafeLink(SailPointContext context, Identity identity, String appName) throws GeneralException{
		Application app = context.getObjectByName(Application.class, appName);
		Link link = identity.getLink(app);

		if (link == null){
			link = new Link();
		}

		context.decache(app);

		return link;
	}


	public Link getLinkByNativeIdentity(SailPointContext context, String application, String nativeIdentity) throws GeneralException {
		logger.trace("Enter getLinkByNativeIdentity");

		Link val = null;

		QueryOptions qo = new QueryOptions();
		qo.addFilter(Filter.eq("application.name", application));
		qo.addFilter(Filter.like("nativeIdentity", "CN="+nativeIdentity, Filter.MatchMode.START));
		Iterator it = context.search(Link.class, qo);
		if(it.hasNext()) {
			val = (Link) it.next();
		}

		logger.trace("Exit getLinkByNativeIdentity: " + val);
		return val;
	}

	public static String escapeADValues(String strIn) {
		String val = strIn;
		logger.trace("value passed to escapeADValues=" + val);
		if (strIn.indexOf(",") > -1) {
			val = val.replace(",","\\,");
		}
		val = val.replaceFirst("\\s++$","");
		logger.trace("value returned from escapeADValues=" + val);
		return val;
	}


	public static String fixNulls(String strIn) {
		String val = strIn;
		logger.trace("value passed to fixNulls=" + val);
		if (strIn==null) {
			val = "";
		}
		val = val.replaceFirst("\\s++$","");
		logger.trace("value returned from fixNulls=" + val);
		return val;
	}
	
	/**
     * Method to get the value from the custom object for a given key
     * 
     * @param spContext
     *            - context to use for the search
     * @param customObjectName
     *            - custom object name to use
     * @param key
     *            - key to search for
     * @return - key value , returns null if no match or errors found
     */
    static Object getValueFromCustomObject(SailPointContext spContext, String customObjectName,
            String key) {

        // Log level
        boolean isDebugEnabled = logger.isDebugEnabled();

        // Flag to see if the input parameters are valid or not
        boolean isValid = true;

        // Key values to return
        Object value = null;

        // Check if the customobjectname to look for is empty or not
        if (Util.isNullOrEmpty(customObjectName)) {
            logger.error("Custom objet name to look for is not provided.");
            isValid = false;
        }

        // Check the context provided
        if (spContext == null) {
            logger.error("Sailpointcontext is not provided.");
            isValid = false;
        }

        // Check the key provided
        if (Util.isNullOrEmpty(key)) {
            logger.error("Key to look for in the custom object is not provided.");
            isValid = false;
        }

        // If all the input parameters are valid, go for a search
        if (isValid) {
            // Validate the custom object existence in the system
            QueryOptions customQo = new QueryOptions();
            customQo.addFilter(Filter.eq("name", customObjectName));
            Custom custom = null;
            try {
                // Check for the object presence
                int count = spContext.countObjects(Custom.class, customQo);
                // If the custom object is found, look for the key existence
                if (count == 1) {
                    custom = spContext.getObjectByName(Custom.class, customObjectName);
                    // If the key is present get the value
                    if (custom.containsAttribute(key)) {
                        value = custom.get(key);
                    } else {
                        logger.error(
                                "Key " + key + " is not present in " + customObjectName);
                    }
                } else {
                    logger.error(
                            "No custom object with the name " + customObjectName + " is found in the system.");
                }

            } catch (GeneralException e) {
                e.printStackTrace();
                logger.error("Error searching for key in the custom object." + e.getMessage());
            } finally {
                // Decache the objects from the cache
                if (spContext != null && custom != null) {
                    try {
                        spContext.decache(custom);
                    } catch (GeneralException e) {
                        e.printStackTrace();
                        logger.error("Error decaching the context." + e.getMessage());
                    }
                }
            }
        }
        return value;
    }
    
    public Date convertStringToDate(String dateValue, String dateFormat) throws ParseException {
        dateValue = (dateValue.indexOf(":")!=-1 ? dateValue : dateValue + " 00:00:00");
        DateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Date newDate = DateUtils.truncate(formatter.parse(dateValue), Calendar.DATE);
        return(newDate);
    }

    public Date calcFutureDate(int iDays, String dateFormat) {
        logger.trace("Enter calcFutureDate - " + iDays);
       
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.truncate(new Date(), Calendar.DATE));
        cal.add(Calendar.DATE, iDays);
        Date futureDt = cal.getTime();
        
        logger.debug("Future Date: " + (new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(futureDt)));
        logger.trace("Exit calcFutureDate - " + futureDt);
        return(futureDt);
    }


    public Link getSafeLink(Identity identity, String appName) throws GeneralException{
        Application app = context.getObjectByName(Application.class, appName);
        Link link = identity.getLink(app);

        if (link == null){
            link = new Link();
        }

        context.decache(app);
        return link;
    }
    
	
	private static Object getAppAttribute(SailPointContext context2, Identity identity, String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static String getUniqueValue(Identity identity, String string) {
		// TODO Auto-generated method stub
		return null;
	}
}

