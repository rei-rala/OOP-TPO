package excepciones;

public class CostoException extends ValorException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;

	public CostoException(Throwable cause) {
		super(cause);
	}

	public CostoException(String message) {
		super(message);
	}

	public CostoException(String code, String message) {
		super(message);
		this.code = code;
	}
}
