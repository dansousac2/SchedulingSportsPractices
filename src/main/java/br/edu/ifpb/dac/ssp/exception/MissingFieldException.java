package br.edu.ifpb.dac.ssp.exception;

public class MissingFieldException extends Exception {

	private static final long serialVersionUID = 1L;
	
	// Exceção de exemplo, podemos modificar depois para ficar mais genérico
	
	public MissingFieldException(String fieldName) {
		super("Could not complete action, the field " + fieldName + " is missing!");
	}
}
