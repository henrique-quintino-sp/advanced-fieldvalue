/**
 * 
 */
package util.fieldValueFwk;

import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;

/**
 * @author ishim.manon
 *
 */
public class ActiveDirectoryTest {
	
	private static SailPointContext context;
	private static Identity identity;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = null;
		identity = createIdentity("Alec","I.","Rios",false);
		identity.setManager(createIdentity("Nissim","X.","Garrisons",true));
	}
	
	private static Identity createIdentity(String firstName, String middleName, String lastName, boolean isManager){
		Identity identity = new Identity();
		identity.setDisplayName(firstName + " "+middleName +" "+lastName);
		identity.setDescription("Ceated by jUnit on "+new Date());
		identity.setEmail(firstName+"."+lastName+"@sailpoint.sp");
		identity.setFirstname(firstName);
		Random random = new Random(System.currentTimeMillis());
		identity.setId(String.valueOf(random.nextInt()));
		identity.setInactive(false);
		identity.setLastLogin(new Date());
		identity.setLastname(lastName);
		identity.setManagerStatus(isManager);
		identity.setName(firstName+"."+lastName);
		identity.setPassword("xyzzy");
		identity.setUid(firstName+"."+lastName);
		
		Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.truncate(new Date(), Calendar.DATE));
        cal.add(Calendar.DATE, 364);
        Date futureDate = cal.getTime();
		identity.setPasswordExpiration(futureDate);
		
		return identity;
	}
	
	

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFVActive_Directory_accountExpires_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFVActive_Directory_accountExpires_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFVActive_Directory_accountExpires_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFVActive_Directory_accountExpires_Rule2() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_c_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_c_Rule() {
		String c = (String) ActiveDirectory.getFV_Active_Directory_c_Rule(context, identity, null);
		 Assert.assertEquals(c, null);
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_c_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_c_RuleDefaultVal() {
		String defaultValue = "MX";
		String c = (String) ActiveDirectory.getFV_Active_Directory_c_Rule(context, identity, null, defaultValue);
		 Assert.assertEquals(c, defaultValue);
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_carLicense_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_carLicense_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_carLicense_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_carLicense_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Standard_co_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Standard_co_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Standard_co_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Standard_co_Rule2() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_cn_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_cn_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_cn_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_cn_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_cn_Rule2(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_cn_Rule2() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_cscPreferredName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_cscPreferredName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_departmentNumber_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_departmentNumber_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_description2_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_description2_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_description_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_description_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_description_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_description_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_displayName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_displayName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_distinguishedName2_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_distinguishedName2_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_distinguishedName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_distinguishedName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_distinguishedName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_distinguishedName_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_DN_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_DN_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_employeeID_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_employeeID_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_employeeType_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_employeeType_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_givenName2_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_givenName2_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_userPrincipalName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_userPrincipalName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_userPrincipalName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_userPrincipalName_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_givenName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_givenName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_givenName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_givenName_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_l_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_l_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_initials_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_initials_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_middleName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_middleName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_mobile_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_mobile_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_mail_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_mail_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_mail_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_mail_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_manager_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_manager_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_manager_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_manager_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_manager2_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_manager2_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_managerDN_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_managerDN_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_memberOf_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_memberOf_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_memberOf_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_memberOf_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_memberOf2_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_memberOf2_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_Name_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_Name_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_NetbootSCPBL_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_NetbootSCPBL_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_NetworkAddress_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_NetworkAddress_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_NonSecurityMemberBL_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_NonSecurityMemberBL_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_NtPwdHistory_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_NtPwdHistory_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_nTSecurityDescriptor_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_nTSecurityDescriptor_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_O_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_O_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ObjectCategory_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ObjectCategory_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ObjectClass_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ObjectClass_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ObjectGUID_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ObjectGUID_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ObjectSid_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ObjectSid_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ObjectVersion_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ObjectVersion_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OperatorCount_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OperatorCount_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OrganizationalUnitName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OrganizationalUnitName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OtherFacsimileTelephoneNumber_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OtherFacsimileTelephoneNumber_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OtherHomePhone_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OtherHomePhone_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OtherIpPhone_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OtherIpPhone_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OtherLoginWorkstations_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OtherLoginWorkstations_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OtherMailbox_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OtherMailbox_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OtherMobile_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OtherMobile_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OtherPager_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OtherPager_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OtherTelephone_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OtherTelephone_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_OtherWellKnownObjects_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_OtherWellKnownObjects_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_Ou_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_Ou_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_Pager_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_Pager_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PartialAttributeDeletionList_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PartialAttributeDeletionList_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PartialAttributeSet_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PartialAttributeSet_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_password_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_password_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_password_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_password_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PasswordPolicies_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PasswordPolicies_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PersonalTitle_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PersonalTitle_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PhysicalDeliveryOfficeName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PhysicalDeliveryOfficeName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PossibleInferiors_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PossibleInferiors_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PostalAddress_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PostalAddress_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PostalCode_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PostalCode_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PostOfficeBox_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PostOfficeBox_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PreferredDeliveryMethod_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PreferredDeliveryMethod_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PreferredLanguaged_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PreferredLanguaged_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PreferredOU_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PreferredOU_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PrimaryGroupDN_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PrimaryGroupDN_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PrimaryGroupID_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PrimaryGroupID_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PrimaryInternationalISDNNumber_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PrimaryInternationalISDNNumber_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PrimaryTelexNumber_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PrimaryTelexNumber_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ProfilePath_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ProfilePath_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ProxiedObjectName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ProxiedObjectName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ProxyAddresses_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ProxyAddresses_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_PwdLastSet_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_PwdLastSet_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_QueryPolicyBL_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_QueryPolicyBL_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_RegisteredAddress_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_RegisteredAddress_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ReplPropertyMetaData_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ReplPropertyMetaData_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ReplUpToDateVector_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ReplUpToDateVector_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_RepsFrom_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_RepsFrom_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_RepsTo_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_RepsTo_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_Revision_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_Revision_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_Rid_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_Rid_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_sAMAccountName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_sAMAccountName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_sAMAccountType_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_sAMAccountType_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_SipAddress_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_SipAddress_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_SipDomain_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_SipDomain_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_SipAddressType_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_SipAddressType_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ScriptPath_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ScriptPath_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_sDRightsEffective_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_sDRightsEffective_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_SecurityIdentifier_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_SecurityIdentifier_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_SeeAlso_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_SeeAlso_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ServerReferenceBL_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ServerReferenceBL_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ServicePrincipalName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ServicePrincipalName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ShowInAddressBook_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ShowInAddressBook_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ShowInAdvancedViewOnly_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ShowInAdvancedViewOnly_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_sIDHistory_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_sIDHistory_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_SiteObjectBL_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_SiteObjectBL_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_sn_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_sn_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_sn_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_sn_RuleDefaultVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_St_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_St_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_StateOrProvinceName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_StateOrProvinceName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_Street_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_Street_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_StreetAddress_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_StreetAddress_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_SubRefs_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_SubRefs_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_SubSchemaSubEntry_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_SubSchemaSubEntry_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_SupplementalCredentials_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_SupplementalCredentials_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_SystemFlags_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_SystemFlags_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_TelephoneNumber_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_TelephoneNumber_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_TeletexTerminalIdentifier_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_TeletexTerminalIdentifier_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_TelexNumber_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_TelexNumber_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_TerminalServer_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_TerminalServer_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_TextEncodedORAddress_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_TextEncodedORAddress_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ThumbnailLogo_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ThumbnailLogo_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_ThumbnailPhoto_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_ThumbnailPhoto_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_title_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_title_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_title_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testGetFV_Active_Directory_title_Rule2() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_TokenGroups_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_TokenGroups_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_TokenGroupsGlobalAndUniversal_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_TokenGroupsGlobalAndUniversal_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_TokenGroupsNoGCAcceptable_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_TokenGroupsNoGCAcceptable_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UID_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UID_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UnicodePwd_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UnicodePwd_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_Url_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_Url_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UsageLocation_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UsageLocation_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UsageLocation_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UsageLocation_RuleString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserAccountControl_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserAccountControl_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserCert_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserCert_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserCertificate_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserCertificate_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserParameters_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserParameters_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserPassword_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserPassword_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserPrincipalName_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserPrincipalName_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserSharedFolder_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserSharedFolder_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserSharedFolderOther_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserSharedFolderOther_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserSMIMECertificate_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserSMIMECertificate_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserType_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserType_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_UserWorkstations_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_UserWorkstations_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_uSNChanged_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_uSNChanged_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_uSNCreated_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_uSNCreated_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_uSNDSALastObjRemoved_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_uSNDSALastObjRemoved_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_USNIntersite_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_USNIntersite_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_uSNLastObjRem_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_uSNLastObjRem_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_uSNSource_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_uSNSource_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_WbemPath_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_WbemPath_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_WellKnownObjects_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_WellKnownObjects_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_WhenChanged_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_WhenChanged_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_WhenCreated_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_WhenCreated_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_wWWHomePage_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_wWWHomePage_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getFV_Active_Directory_X121Address_Rule(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetFV_Active_Directory_X121Address_Rule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#verifyNull(java.lang.Object)}.
	 */
	@Test
	public void testVerifyNull() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#isUniqueADName(sailpoint.object.Application, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsUniqueADName() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#isUniqueEmail(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsUniqueEmail() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#isUniqueLDSync(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsUniqueLDSync() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getLinkVal(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetLinkVal() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getSafeLink(sailpoint.api.SailPointContext, sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetSafeLink() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getLinkByNativeIdentity(sailpoint.api.SailPointContext, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetLinkByNativeIdentity() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#escapeADValues(java.lang.String)}.
	 */
	@Test
	public void testEscapeADValues() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#fixNulls(java.lang.String)}.
	 */
	@Test
	public void testFixNulls() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getValueFromCustomObject(sailpoint.api.SailPointContext, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetValueFromCustomObject() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#convertStringToDate(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testConvertStringToDate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#calcFutureDate(int, java.lang.String)}.
	 */
	@Test
	public void testCalcFutureDate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.fieldValueFwk.ActiveDirectory#getSafeLink(sailpoint.object.Identity, java.lang.String)}.
	 */
	@Test
	public void testGetSafeLinkIdentityString() {
		fail("Not yet implemented");
	}

}
