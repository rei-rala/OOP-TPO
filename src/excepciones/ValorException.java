package excepciones;

public class ValorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;

	public ValorException(Throwable cause) {
		super(cause);
	}

	public ValorException(String message) {
		super(message);
	}

	public ValorException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ValorException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

}
