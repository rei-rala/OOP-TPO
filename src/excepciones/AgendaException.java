package excepciones;


public class AgendaException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;

	public AgendaException(Throwable cause) {
		super(cause);
	}

	public AgendaException(String message) {
		super(message);
	}

	public AgendaException(String code, String message) {
		super(message);
		this.code = code;
	}

	public AgendaException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

}
