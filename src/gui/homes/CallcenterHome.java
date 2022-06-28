package gui.homes;

import javax.swing.JPanel;

import gui.Gui;
import gui.parciales.PanelCreacionServicio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JButton;

public class CallcenterHome extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Gui g = Gui.getInstance();
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JButton btnLogout;
	private PanelCreacionServicio panelCreacionServicio;

	/**
	 * Create the panel.
	 */
	public CallcenterHome() {
		setBorder(null);
		setLayout(new BorderLayout(0, 0));

		menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);

		mnNewMenu = new JMenu("Callcenter");
		menuBar.add(mnNewMenu);

		btnLogout = new JButton("CERRAR SESION");
		btnLogout.addActionListener(this);
		add(btnLogout, BorderLayout.SOUTH);

		panelCreacionServicio = new PanelCreacionServicio();
		add(panelCreacionServicio, BorderLayout.CENTER);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogout) {
			g.logout();
		}

	}

}
