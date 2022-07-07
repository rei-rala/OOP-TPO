package ui.usuarios;


import personas.Administrativo;
import ui.UiUsuariosBase;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

public class UiAdministrativo extends UiUsuariosBase {
	private final Administrativo ADMINISTRATIVO;
	private JButton btnNewButton;

	/**
	 * Create the panel.
	 */
	public UiAdministrativo(Administrativo administrativo) {
		super("Menu Administrativo");
		this.ADMINISTRATIVO = administrativo;
		
		btnNewButton = new JButton("ADMINISTRATIVO");
		add(btnNewButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
    // TODO: ACTIONS
		System.out.println(ADMINISTRATIVO);
		
	}

}
