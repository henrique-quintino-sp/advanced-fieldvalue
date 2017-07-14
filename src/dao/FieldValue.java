package dao;

public class FieldValue {

	private String appAttribute;		//appAttribute will be used as name for field in ProvisioningForms also
	private String targetAttribute;	//Must include the type: REG, EXP, STB or PRE
	private String displayName;
	private boolean checkUniqueness;
	private boolean required;
	private String type;
	
	private final String TYPE_REG = "REG";			//For formats like: firstname[1]+'.'+lastname 
	private final String TYPE_PRE = "PRE";			//For pre configured attributes in formats like: displayName-op3
	
	public FieldValue(){
	}
	
	public FieldValue(String appAttribute, String targetAttribute,  boolean checkUniqueness){
		setAppAttribute(appAttribute);
		setTargetAttribute(targetAttribute);
		setDisplayName(appAttribute);
		setCheckUniqueness(checkUniqueness);
		setRequired(false);
		setType("String");
	}
	
	public FieldValue(String appAttribute, String targetAttribute, String displayName, boolean checkUniqueness){
		setAppAttribute(appAttribute);
		setTargetAttribute(targetAttribute);
		setDisplayName(displayName);
		setCheckUniqueness(checkUniqueness);
		setRequired(false);
		setType("String");
	}

	public FieldValue(String appAttribute, String targetAttribute, String displayName, boolean checkUniqueness, boolean required, String type){
		setAppAttribute(appAttribute);
		setTargetAttribute(targetAttribute);
		setDisplayName(displayName);
		setCheckUniqueness(checkUniqueness);
		setRequired(required);
		setType((type!=null&&!type.isEmpty()?type:"String"));
	}
	
	public String getAppAttribute() {
		return appAttribute;
	}
	public void setAppAttribute(String appAttribute) {
		this.appAttribute = appAttribute;
	}
	public String getTargetAttribute() {
		return targetAttribute;
	}
	//Removes First 4 characters if using REG
	public String getTargetAttributeValue() {
		return targetAttribute.substring(4);
	}
	//Removes First 4 characters if using PRE
	public String getTargetAttributePRE() {
		return targetAttribute.substring(4, targetAttribute.indexOf('/'));
	}
	public void setTargetAttribute(String targetAttribute) {
		this.targetAttribute = targetAttribute;
	}
	public void setTargetAttributeREG(String targetAttribute) {
		this.targetAttribute = TYPE_REG+":"+targetAttribute;
	}
	public void setTargetAttributePRE(String targetAttribute) {
		this.targetAttribute = TYPE_PRE+":"+targetAttribute;
	}
	public boolean isCheckUniqueness() {
		return checkUniqueness;
	}
	public void setCheckUniqueness(boolean checkUniqueness) {
		this.checkUniqueness = checkUniqueness;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
