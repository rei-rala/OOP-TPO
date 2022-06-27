package excepciones;

public class CredencialException extends ValorException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;

	public CredencialException(Throwable cause) {
		super(cause);
	}

	public CredencialException(String message) {
		super(message);
	}

	public CredencialException(String code, String message) {
		super(message);
		this.code = code;
	}
}
