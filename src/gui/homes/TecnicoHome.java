package gui.homes;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import excepciones.CredencialException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.Gui;
import gui.parciales.PanelListServiciosTecnico;
import personas.*;

import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

public class TecnicoHome extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Gui g = Gui.getInstance();
	private Interno i = g.getUsuarioLogeado();
	private PanelListServiciosTecnico panelListServiciosTecnico;
	private JButton btnLogout;

	/**
	 * Create the panel.
	 */
	public TecnicoHome() {
		setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"MENU TECNICO", TitledBorder.CENTER, TitledBorder.TOP, null, Color.GRAY));
		setLayout(new BorderLayout(0, 0));

		panelListServiciosTecnico = new PanelListServiciosTecnico();
		add(panelListServiciosTecnico);

		btnLogout = new JButton("Cerrar sesion");
		btnLogout.addActionListener(this);
		add(btnLogout, BorderLayout.SOUTH);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (i.getClass() != Tecnico.class) {
				throw new CredencialException("Permisos insuficientes");
			}

			if (e.getSource() == btnLogout) {
				g.logout();
			}

		} catch (Exception exception) {
			g.errorHandler(exception);
		}

	}

}
