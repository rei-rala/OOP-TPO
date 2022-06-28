package excepciones;

public class GuiException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;

	public GuiException(Throwable cause) {
		super(cause);
	}

	public GuiException(String message) {
		super(message);
	}

	public GuiException(String code, String message) {
		super(message);
		this.code = code;
	}

	public GuiException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

}
