package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.*;

import empresa.Empresa;
import excepciones.*;
import personas.Interno;
import gui.homes.*;

public class Gui {
	static Gui instancia;
	public Empresa empresa = Empresa.getInstance();
	private static JFrame frame;
	private Interno usuarioLogeado;

	/**
	 * Create the application.
	 */
	public Gui() {
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {

		frame = new JFrame();
		frame.setBounds(0, 0, 800, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new LogIn());
		frame.setVisible(true);
	}

	public static Gui getInstance() {
		if (instancia == null) {
			instancia = new Gui();
		}
		return instancia;
	}

	public Interno getUsuarioLogeado() {
		return usuarioLogeado;
	}

	public void setUsuarioLogeado(Interno usuarioLogeado) {
		this.usuarioLogeado = usuarioLogeado;
	}

	public void logout() {
		String usuarioLogeadoName = getUsuarioLogeado().getNombre();
		if (JOptionPane.showConfirmDialog(null, "Confirma cerrar sesion de " + usuarioLogeadoName) == 0) {
			setUsuarioLogeado(null);
			changePanel(new LogIn());
		}
	}

	public void setErrorMessage(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage());
		System.out.println(e);
	}

	public double redondearDouble(Double d) {
		return Empresa.getInstance().redondear(d);
	}

	public int validarInt(String dato) throws ValorException {
		try {
			return Integer.parseInt(dato);
		} catch (Exception e) {
			throw new ValorException("Proporcionar valor numerico");
		}
	}

	public double validarDouble(String dato) throws ValorException {
		try {
			return redondearDouble(Double.parseDouble(dato));
		} catch (Exception e) {
			throw new ValorException("Debe ingresar un valor numerico valido");
		}

	}

	public void removerActionListeners(JButton jb) {
		for (ActionListener al : jb.getActionListeners()) {
			jb.removeActionListener(al);
		}
	}

	public void errorHandler(Exception e) {
		Class<? extends Exception> exceptionClass = e.getClass();
		String message = e.getMessage();

		if (exceptionClass == ServicioException.class) {
			message = "Error de servicio: " + message;
		} else if (exceptionClass == StockException.class) {
			message = "Error de stock: " + message;
		} else if (exceptionClass == ValorException.class) {
			message = "Error de valor: " + message;
		} else {
			message = "Error inesperado";
			System.out.println(e);
		}
		JOptionPane.showMessageDialog(null, message);
	}

	public void changePanel(JPanel jp) {
		frame.getContentPane().setVisible(false);
		frame.getContentPane().removeAll();
		frame.getContentPane().add(jp);

		frame.getContentPane().setVisible(true);
	}

}
