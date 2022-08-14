package br.edu.ifpb.dac.ssp.presentation.exception;

public class MissingFieldException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public MissingFieldException(String fieldName) {
		super("Não foi possível concluir a ação, o campo " + fieldName + " está faltando!");
	}
	
	public MissingFieldException(String fieldName, String typeOfRequest) {
		super("Não foi possível usar " + typeOfRequest + ", o campo " + fieldName + " está faltando!");
	}
}
