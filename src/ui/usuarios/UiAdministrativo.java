package ui.usuarios;

import javax.swing.JPanel;

import personas.Administrativo;
import ui.UiUsuariosBase;
import ui.UiMethods;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class UiAdministrativo extends UiUsuariosBase {

	private Administrativo administrativo;
	private JButton btnNewButton;

	/**
	 * Create the panel.
	 */
	public UiAdministrativo(Administrativo administrativo) {
		super("Menu Administrativo");
		this.administrativo = administrativo;
		
		btnNewButton = new JButton("ADMINISTRATIVO");
		add(btnNewButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
