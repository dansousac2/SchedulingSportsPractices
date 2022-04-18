package br.edu.ifpb.dac.ssp.exception;

public class MissingFieldException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public MissingFieldException(String fieldName) {
		super("Could not complete action, the field " + fieldName + " is missing!");
	}
	
	public MissingFieldException(String fieldName, String typeOfRequest) {
		super("Could not " + typeOfRequest + ", the field " + fieldName + " is missing!");
	}
}
