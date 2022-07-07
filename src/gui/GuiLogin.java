package gui;

import java.awt.BorderLayout;
import java.awt.Component;
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

public class GuiLogin extends JPanel implements ActionListener, GuiMethods {
	private static final long serialVersionUID = 1L;

	private static GuiMain ui = GuiMain.getInstance();
	private final Empresa empresa = Empresa.getInstance();

	private JLabel lblNewLabel;
	private JPanel panel;
	private JLabel lblNewLabel_1;
	private JTextField legajo;
	private JLabel lblNewLabel_2;
	private JTextField contrasena;
	private JButton btnLogin;

	public GuiLogin() {
		setLayout(new BorderLayout(0, 0));

		lblNewLabel = new JLabel("Bienvenido");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNewLabel, BorderLayout.NORTH);

		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 7, 0, 0));

		// ESTETICO
		for (int i = 0; i < 8; i++) {
			panel.add(new JLabel(""));
		}
		
		panel.add(new JLabel("[Perfil: legajo]"));
		panel.add(new JLabel("Admin: 1"));
		panel.add(new JLabel("Administrativo: 2"));
		panel.add(new JLabel("Tecnico: 3"));
		panel.add(new JLabel("Callcenter: 4"));
		
		// ESTETICO
		for (int i = 0; i < 3; i++) {
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
		
		for (Component c: panel.getComponents()) {
			if (c.getClass() == JLabel.class) {
				JLabel jl = (JLabel) c;
				jl.setHorizontalAlignment(SwingConstants.CENTER);
			}
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
