package br.edu.ifpb.dac.ssp.exception;

public class ObjectNotFoundException extends Exception {

		private static final long serialVersionUID = 1L;
		
		public ObjectNotFoundException(String object, String fieldName, Object field) {
			super("Could not find " + object + " with " + fieldName+ " " + field);
		}
}
