package gui.usuarios;


import personas.Administrativo;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class GuiAdministrativo extends GuiUsuarioBase {
	private final Administrativo ADMINISTRATIVO;
	private JButton btnNewButton;

	/**
	 * Create the panel.
	 */
	public GuiAdministrativo(Administrativo administrativo) {
		super("Bienvenido " + administrativo.getNombre() + "<ADMINISTRATIVO>");
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
