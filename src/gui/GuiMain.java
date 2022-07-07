package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import excepciones.*;
import gui.usuarios.*;
import personas.*;

public class GuiMain implements GuiMethods {
	private static GuiMain instance;
	private JFrame framePrincipal;
	private Interno usuarioLogeado = null;

	private GuiMain() {
	}

	public static GuiMain getInstance() {
		if (instance == null) {
			instance = new GuiMain();
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
					guiExceptionHandler(e);
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

		if (confirm("Cerrar sesion de " + usuarioLogeado.getNombre() + "<" + usuarioLogeado.getClass().getSimpleName() + ">?")) {
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
		switchUiPanel(new GuiLogin());
	}

	private void goToHome() throws Exception {
		if (usuarioLogeado == null) {
			throw new Exception("Usuario no valido!");
		}

		JPanel jp;
		Class<? extends Interno> classI = usuarioLogeado.getClass();

		if (classI == Admin.class) {
			Admin a = (Admin) usuarioLogeado;
			jp = new GuiAdmin(a);
		} else if (classI == Administrativo.class) {
			Administrativo a = (Administrativo) usuarioLogeado;
			jp = new GuiAdministrativo(a);
		} else if (classI == Tecnico.class) {
			Tecnico t = (Tecnico) usuarioLogeado;
			jp = new GuiTecnico(t);
		} else if (classI == Callcenter.class) {
			Callcenter c = (Callcenter) usuarioLogeado;
			jp = new GuiCallcenter(c);
		} else {
			throw new CredencialException("USUARIO NO AUTORIZADO");
		}

		switchUiPanel(jp);
	}

}
