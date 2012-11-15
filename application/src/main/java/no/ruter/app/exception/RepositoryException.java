package no.ruter.app.exception;

/**
 * Custom exception for failing repository calls
 * 
 * @author Kristian
 *
 */
public class RepositoryException extends Exception {
	
	public RepositoryException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;

}
