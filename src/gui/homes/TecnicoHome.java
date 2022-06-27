package gui.homes;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.Gui;
import gui.parciales.PanelListServiciosTecnico;
import personas.*;

import javax.swing.JButton;

public class TecnicoHome extends JPanel implements ActionListener {
	private Gui g = Gui.getInstance();
	private Interno i = g.getUsuarioLogeado();
	private PanelListServiciosTecnico panelListServiciosTecnico;
	private JButton btnLogout;

	/**
	 * Create the panel.
	 */
	public TecnicoHome() {
		setBorder(new TitledBorder(null, "Menu de tecnico", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));

		panelListServiciosTecnico = new PanelListServiciosTecnico();
		add(panelListServiciosTecnico);

		btnLogout = new JButton("Cerrar sesion");
		btnLogout.addActionListener(this);
		add(btnLogout, BorderLayout.SOUTH);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogout) {
			g.logout();
		}

	}

}
