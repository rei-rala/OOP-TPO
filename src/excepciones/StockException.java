package excepciones;

public class StockException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;

	public StockException(Throwable cause) {
		super(cause);
	}

	public StockException(String message) {
		super(message);
	}

	public StockException(String code, String message) {
		super(message);
		this.code = code;
	}

	public StockException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

}
