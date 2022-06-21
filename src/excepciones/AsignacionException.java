package excepciones;

public class AsignacionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;

	public AsignacionException(Throwable cause) {
		super(cause);
	}

	public AsignacionException(String message) {
		super(message);
	}

	public AsignacionException(String code, String message) {
		super(message);
		this.code = code;
	}

	public AsignacionException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

}
