package gui.homes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import empresa.Empresa;
import gui.Gui;
import personas.Interno;

public class LogIn extends JPanel implements ActionListener {
	private JLabel lblNewLabel;
	private JPanel panel;
	private JLabel lblNewLabel_1;
	private final JTextField legajo;
	private JLabel lblNewLabel_2;
	private final JTextField contrasena;
	private final JButton btnLogin;

	private final Gui gui = Gui.getInstance();
	private final Empresa empresa = Empresa.getInstance();

	/**
	 * Create the panel.
	 */
	public LogIn() {
		setLayout(new BorderLayout(0, 0));

		lblNewLabel = new JLabel("Bienvenido");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNewLabel, BorderLayout.NORTH);

		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		lblNewLabel_1 = new JLabel("Numero legajo");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_1);

		legajo = new JTextField();
		legajo.setHorizontalAlignment(SwingConstants.CENTER);
		legajo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(legajo);
		legajo.setColumns(10);

		lblNewLabel_2 = new JLabel("Contrase\u00F1a");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_2);

		contrasena = new JTextField();
		contrasena.setHorizontalAlignment(SwingConstants.CENTER);
		contrasena.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(contrasena);
		contrasena.setColumns(10);

		btnLogin = new JButton("INICIAR SESION");
		btnLogin.addActionListener(this);
		add(btnLogin, BorderLayout.SOUTH);
	}

	// Create functions for login

	public String getLegajoField() {
		return legajo.getText();
	}

	public String getContrasenaField() {
		return contrasena.getText();
	}

	public JButton getLoginButton() {
		return btnLogin;
	}


	public boolean verificarFields() {
		try {
			Integer.parseInt(getLegajoField());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		try {

			if (ev.getSource() == btnLogin) {
				if (verificarFields()) {
					int legajo = Integer.parseInt(getLegajoField());
					Interno i = empresa.login(legajo, getContrasenaField());

					if (i != null) {
						gui.setUsuarioLogeado(i);
						gui.changePanel(new AdminHome());
					} else {
						throw new Exception("Legajo o contrasena no validos");
					}
				} else {
					throw new Exception("Ingrese un numero para legajo");
				}
			}
		} catch (Exception exc) {
			gui.setErrorMessage(exc);
		}

	}

}
