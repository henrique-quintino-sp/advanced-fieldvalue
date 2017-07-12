package dao;

public class FieldValue {

	private String appAttribute;
	private String targetAttribute;
	private String displayName;
	private boolean checkUniqueness;

	public FieldValue(){

	}

	public FieldValue(String appAttribute, String targetAttribute,  boolean checkUniqueness){
		setAppAttribute(appAttribute);
		setTargetAttribute(targetAttribute);
		setDisplayName(appAttribute);
		setCheckUniqueness(checkUniqueness);
	}

	public FieldValue(String appAttribute, String targetAttribute, String displayName, boolean checkUniqueness){
		setAppAttribute(appAttribute);
		setTargetAttribute(targetAttribute);
		setDisplayName(displayName);
		setCheckUniqueness(checkUniqueness);
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


}
