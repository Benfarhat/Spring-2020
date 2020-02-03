package ca.benfarhat.restapi.exception;

public class SalleException extends Exception{

	private static final long serialVersionUID = 1L;

	public SalleException() {
		super();
	}

	public SalleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SalleException(String message, Throwable cause) {
		super(message, cause);
	}

	public SalleException(String message) {
		super(message);
	}

	public SalleException(Throwable cause) {
		super(cause);
	}
	

}
