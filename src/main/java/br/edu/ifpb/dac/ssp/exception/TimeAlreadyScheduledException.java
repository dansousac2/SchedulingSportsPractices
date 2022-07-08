package br.edu.ifpb.dac.ssp.exception;

public class TimeAlreadyScheduledException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TimeAlreadyScheduledException() {
		super("Já existe uma prática agendada para esse horário!");
	}

}
