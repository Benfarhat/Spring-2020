package ca.benfarhat.restapi.exception;

public class ProfesseurException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public ProfesseurException() {
		super();
	}

	public ProfesseurException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProfesseurException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProfesseurException(String message) {
		super(message);
	}

	public ProfesseurException(Throwable cause) {
		super(cause);
	}

}
