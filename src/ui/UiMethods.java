package ui;

import javax.swing.JOptionPane;

import empresa.Empresa;
import excepciones.*;

public interface UiMethods {
	default boolean confirm(String message) {

		return JOptionPane.showConfirmDialog(null, message) == JOptionPane.OK_OPTION;
	}

	default boolean confirm(String title, String message) {
		return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.DEFAULT_OPTION) == JOptionPane.OK_OPTION;
	}

	default void alert(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
	}

	default void alert(String message) {
		JOptionPane.showMessageDialog(null, message, "Alerta", JOptionPane.WARNING_MESSAGE);
	}

	default String input(String message) {
		return JOptionPane.showInputDialog(null, message);
	}

	default String input(String message, String title) {
		return JOptionPane.showInputDialog(null, message, title);
	}

	default void uiExceptionHandler(String error) {
		uiExceptionHandler(new Exception("ERROR INESPERADO: " + error));
	}

	default void uiExceptionHandler(Exception e) {
		Class<? extends Exception> eCls = e.getClass();
		String title = "Se produjo un error";
		String message = e.getMessage();
		int icon = JOptionPane.WARNING_MESSAGE;

		if (eCls == GuiException.class) {
			title = "Informacion";
			icon = JOptionPane.INFORMATION_MESSAGE;
		} else if (eCls == CredencialException.class) {
			title = "Credenciales no validas";
			message = "Error de credenciales: " + message;
		} else if (eCls == ServicioException.class) {
			message = "Error de servicio: " + message;
		} else if (eCls == StockException.class) {
			message = "Error de stock: " + message;
		} else if (eCls == ValorException.class) {
			message = "Error de valor: " + message;
		} else if (eCls == AsignacionException.class) {
			message = "Error de asignacion: " + message;
		} else {
			title = "Error inesperado";
		}
		JOptionPane.showMessageDialog(null, message, title, icon);

		if (eCls != GuiException.class) {
			e.printStackTrace();
		} else {
			System.out.println(e.getLocalizedMessage());
		}
	}

	default double redondearDouble(Double d) {
		return Empresa.getInstance().redondear(d);
	}

	default int validarInt(String input) throws ValorException, GuiException {
		try {
			if (input == null || input.isBlank()) {
				throw new GuiException("Cancelado por usuario");
			}

			return Integer.parseInt(input);
		} catch (GuiException e) {
			throw e;
		} catch (Exception e) {
			throw new ValorException("Debe ingresar valor numerico");
		}
	}

	default double validarDouble(String input) throws ValorException, GuiException {
		try {
			System.out.println(input);
			if (input == null || input.isBlank()) {
				throw new GuiException("Cancelado por usuario");
			}

			return Double.parseDouble(input);
		} catch (GuiException e) {
			throw e;
		} catch (Exception e) {
			throw new ValorException("Debe ingresar valor numerico double");
		}
	}

	default int guiValidarInt(String message) throws GuiException {
		try {
			String dato = input(message);
			return validarInt(dato);
		} catch (GuiException e) {
			throw e;
		} catch (Exception e) {
			uiExceptionHandler(new ValorException("Proporcionar valor numerico"));
		}

		return guiValidarInt(message);
	}

	default int guiValidarInt(String message, int min) throws GuiException {
		try {
			int parsedInt = guiValidarInt(message);
			if (parsedInt >= min) {
				return parsedInt;
			}
			throw new ValorException("Proporcionar valor numerico mayor a " + min);
		} catch (GuiException e) {
			throw e;
		} catch (Exception e) {
			uiExceptionHandler(e);
		}

		return guiValidarInt(message, min);
	}

	default int guiValidarInt(String message, int min, int max) throws GuiException {
		try {
			int parsedInt = guiValidarInt(message);
			if (parsedInt >= min && max >= parsedInt) {
				return parsedInt;
			}
			throw new ValorException("Proporcionar valor numerico entre " + min + " y " + max);
		} catch (GuiException e) {
			throw e;
		} catch (Exception e) {
			uiExceptionHandler(e);
		}

		return guiValidarInt(message, min, max);
	}

	default double guiValidarDouble(String message) throws GuiException {
		try {
			String input = input(message);
			return validarDouble(input);
		} catch (GuiException e) {
			throw e;
		} catch (Exception e) {
			uiExceptionHandler(new ValorException("Proporcionar valor numerico double"));
		}

		return guiValidarDouble(message);
	}

	default double guiValidarDouble(String message, int min) throws ValorException, GuiException {
		try {
			double parsedDouble = guiValidarDouble(message);

			if (parsedDouble >= min) {
				return parsedDouble;
			}
			throw new ValorException("Proporcionar valor numerico double mayor o igual a " + min);
		} catch (GuiException e) {
			throw e;
		} catch (Exception e) {
			uiExceptionHandler(e);
		}

		return guiValidarDouble(message, min);
	}

	default double guiValidarDouble(String message, int min, int max) throws ValorException, GuiException {
		try {
			double parsedDouble = guiValidarDouble(message);

			if (parsedDouble >= min && max >= parsedDouble) {
				return parsedDouble;
			}
			throw new ValorException("Proporcionar valor numerico double entre " + min + " y " + max);
		} catch (GuiException e) {
			throw e;
		} catch (Exception e) {
			uiExceptionHandler(e);
		}

		return guiValidarDouble(message, min, max);
	}

}
