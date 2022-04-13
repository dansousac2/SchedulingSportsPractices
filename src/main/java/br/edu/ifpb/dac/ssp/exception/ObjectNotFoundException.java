package br.edu.ifpb.dac.ssp.exception;

public class ObjectNotFoundException extends Exception {

		private static final long serialVersionUID = 1L;
		
		public ObjectNotFoundException(Integer id) {
			super("Could not find object with id " + id);
		}
		
		public ObjectNotFoundException(String name) {
			super("Could not find object with name " + name);
		}
}
