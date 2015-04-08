package edu.arizona.biosemantics.common.validation.key;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class KeyValidationException extends Exception {
	private List<String> errorMessages;
	public KeyValidationException() {
		super();
		errorMessages = new LinkedList<String>();
	}

	public KeyValidationException(String message) {
		this();
		errorMessages.add(message);
	}

	public KeyValidationException(Throwable cause) {
		super(cause);
		errorMessages = new LinkedList<String>();
	}

	public KeyValidationException(String message, Throwable cause) {
		super(message, cause);
		errorMessages = new LinkedList<String>();
	}

	public KeyValidationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		errorMessages = new LinkedList<String>();
	}
	
	public void addError(String message){
		errorMessages.add(message);
	}
	
	public List<String> getAllErrors(){
		return errorMessages;
	}
}
