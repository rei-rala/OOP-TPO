package gui;

import java.awt.EventQueue;

import javax.swing.*;

import empresa.Empresa;
import excepciones.ValorException;
import personas.Interno;
import gui.homes.*;

public class Gui {
	static Gui instancia;
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

	public void setErrorMessage(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage());
		System.out.println(e);
	}

	public double redondearDouble(Double d) {
		return Empresa.getInstance().redondear(d);
	}

	public double validarDouble(String dato) throws ValorException {
		try {
			return redondearDouble(Double.parseDouble(dato));
		} catch (Exception e) {
			throw new ValorException("Debe ingresar un valor numerico valido");
		}

	}

	public void changePanel(JPanel jp) {
		frame.getContentPane().setVisible(false);
		frame.getContentPane().removeAll();
		frame.getContentPane().add(jp);

		frame.getContentPane().setVisible(true);
	}

}
