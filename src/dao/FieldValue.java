package dao;

public class FieldValue {

	private String appAttribute;
	private String targetAttribute;
	private boolean checkUniqueness;
	
	
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
	
	public FieldValue(){	}
	public FieldValue(String appAttribute,String targetAttribute,boolean checkUniqueness)
	{
		this.appAttribute = appAttribute;
		this.targetAttribute = targetAttribute;
		this.checkUniqueness = checkUniqueness;
	}
}
