package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import empresa.Empresa;
import personas.*;

public class UiLogin extends JPanel implements ActionListener, UiMethods {
	private static final long serialVersionUID = 1L;

	private static UiApplicationWindow ui = UiApplicationWindow.getInstance();
	private final Empresa empresa = Empresa.getInstance();

	private JLabel lblNewLabel;
	private JPanel panel;
	private JLabel lblNewLabel_1;
	private JTextField legajo;
	private JLabel lblNewLabel_2;
	private JTextField contrasena;
	private JButton btnLogin;

	public UiLogin() {
		setLayout(new BorderLayout(0, 0));

		lblNewLabel = new JLabel("Bienvenido");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNewLabel, BorderLayout.NORTH);

		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 7, 0, 0));

		// ESTETICO
		for (int i = 0; i < 16; i++) {
			panel.add(new JLabel(""));
		}

		lblNewLabel_1 = new JLabel("Numero legajo");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_1);

		// ESTETICO
		panel.add(new JLabel(""));

		legajo = new JTextField();
		legajo.setHorizontalAlignment(SwingConstants.CENTER);
		legajo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(legajo);
		legajo.setColumns(10);

		// ESTETICO
		for (int i = 0; i < 4; i++) {
			panel.add(new JLabel(""));
		}

		lblNewLabel_2 = new JLabel("Contrasena");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_2);

		// ESTETICO
		panel.add(new JLabel(""));

		contrasena = new JTextField();
		contrasena.setHorizontalAlignment(SwingConstants.CENTER);
		contrasena.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(contrasena);
		contrasena.setColumns(10);

		// ESTETICO
		for (int i = 0; i < 5; i++) {
			panel.add(new JLabel(""));
		}

		btnLogin = new JButton("LOG IN");
		btnLogin.addActionListener(this);
		panel.add(btnLogin);

		// ESTETICO
		for (int i = 0; i < 16; i++) {
			panel.add(new JLabel(""));
		}
	}

	public void login() throws Exception {
		int legajoIngresado = validarInt(legajo.getText());
		Interno i = empresa.login(legajoIngresado, contrasena.getText());
		ui.logUserIn(i);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		try {
			if (ev.getSource() == btnLogin) {
				login();
			}
		} catch (Exception exc) {
			uiExceptionHandler(exc);
		}

	}

}
