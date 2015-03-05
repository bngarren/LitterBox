package application.exception;

public class LBException extends RuntimeException {

	// Constants ----------------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	// Constructors -------------------------------------------------------------------------------


	public LBException(String message) {
		super(message);
	}


	public LBException(Throwable cause) {
		super(cause);
	}


	public LBException(String message, Throwable cause) {
		super(message, cause);
	}

}
