package exceptions;

public class NoTargetAttTypeRecognizedException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String message = "Target attribute type not recognized. \n Allowed values are: REG, EXP, PRE and STB.";

	public NoTargetAttTypeRecognizedException() {
		super(message); 
	}
	
	public NoTargetAttTypeRecognizedException(String appAttribute) {
		super(message + " For attribute: "+appAttribute); 
	}
}
