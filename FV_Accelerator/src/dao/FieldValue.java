package dao;

public class FieldValue {

	private String appAttribute;		//appAttribute will be used as name for field in ProvisioningForms also
	private String targetAttribute;
	private String displayName;
	private boolean checkUniqueness;
	private boolean required;
	private String type;
	
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
		setType(type);
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
	public void setTargetAttribute(String targetAttribute) {
		this.targetAttribute = targetAttribute;
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
