package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import excepciones.CredencialException;
import gui.homes.AdminHome;
import gui.homes.AdministrativoHome;
import gui.homes.CallcenterHome;
import gui.homes.TecnicoHome;
import personas.Admin;
import personas.Administrativo;
import personas.Callcenter;
import personas.Interno;
import personas.Tecnico;
import ui.usuarios.UiAdmin;
import ui.usuarios.UiTecnico;

public class UiApplicationWindow implements UiMethods {
	private static UiApplicationWindow instance;
	private JFrame framePrincipal;
	private Interno usuarioLogeado = null;

	private UiApplicationWindow() {
	}

	public static UiApplicationWindow getInstance() {
		if (instance == null) {
			instance = new UiApplicationWindow();
		}

		return instance;
	}

	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					instance.framePrincipal.setVisible(true);
				} catch (Exception e) {
					uiExceptionHandler(e);
				}
			}
		});
	}

	private void initialize() {
		framePrincipal = new JFrame();
		framePrincipal.setBounds(100, 100, 800, 600);
		framePrincipal.setLocationRelativeTo(null);
		framePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		goToLogin();
	}

	public void logUserIn(Interno i) throws Exception {
		if (i == null) {
			throw new CredencialException("Legajo o contrasena no validos");
		}
		usuarioLogeado = i;
		goToHome();
	}

	public void logUserOut() {
		if (usuarioLogeado == null) {
			alert("No hay usuarios logeados");
			return;
		}

		if (confirm("Cerrar sesion de " + usuarioLogeado.getNombre() + "<" + usuarioLogeado.getClass() + ">?")) {
			usuarioLogeado = null;
			goToLogin();
		}
	}

	private void switchUiPanel(JPanel newJPanel) {
		framePrincipal.setVisible(false);
		framePrincipal.getContentPane().removeAll();
		framePrincipal.getContentPane().add(newJPanel);
		framePrincipal.setVisible(true);
	}

	private void goToLogin() {
		switchUiPanel(new UiLogin());
	}

	private void goToHome() throws Exception {
		if (usuarioLogeado == null) {
			throw new Exception("Usuario no valido!");
		}

		JPanel jp;
		Class<? extends Interno> classI = usuarioLogeado.getClass();

		if (classI == Admin.class) {
			Admin a = (Admin) usuarioLogeado;
			jp = new UiAdmin(a);
		} else if (classI == Administrativo.class) {
			jp = new AdministrativoHome();
		} else if (classI == Tecnico.class) {
			Tecnico t = (Tecnico) usuarioLogeado;
			jp = new UiTecnico(t);
		} else if (classI == Callcenter.class) {
			jp = new CallcenterHome();
		} else {
			throw new CredencialException("USUARIO NO AUTORIZADO");
		}

		switchUiPanel(jp);
	}

}
