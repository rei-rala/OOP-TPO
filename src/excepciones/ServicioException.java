package excepciones;

public class ServicioException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;

	public ServicioException(Throwable cause) {
		super(cause);
	}

	public ServicioException(String message) {
		super(message);
	}

	public ServicioException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ServicioException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

}
