package gui.homes;

import javax.swing.JPanel;

import gui.Gui;
import gui.parciales.PanelListFacturas;
import gui.parciales.PanelListServicios;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

public class AdministrativoHome extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JMenuBar mbAdministrativo;
	private JMenu mnNewMenu;
	private JMenuItem mitemServ;
	private JMenuItem mitemFact;
	private Gui g = Gui.getInstance();

	private JPanel pContenedor;
	private PanelListServicios panelListServicios;
	private JButton btnLogout;

	/**
	 * Create the panel.
	 */
	public AdministrativoHome() {
		setLayout(new BorderLayout(0, 0));

		mbAdministrativo = new JMenuBar();
		mbAdministrativo.setToolTipText("");
		add(mbAdministrativo, BorderLayout.NORTH);

		mnNewMenu = new JMenu("Administrativo");
		mbAdministrativo.add(mnNewMenu);

		mitemServ = new JMenuItem("Servicios");
		mitemServ.addActionListener(this);
		mnNewMenu.add(mitemServ);

		mitemFact = new JMenuItem("Facturas");
		mitemFact.addActionListener(this);
		mnNewMenu.add(mitemFact);

		pContenedor = new JPanel();
		pContenedor.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "MENU ADMINISTRATIVO", TitledBorder.CENTER, TitledBorder.TOP, null, Color.GRAY));
		add(pContenedor, BorderLayout.CENTER);
		pContenedor.setLayout(new BorderLayout(0, 0));

		panelListServicios = new PanelListServicios();
		pContenedor.add(panelListServicios, BorderLayout.CENTER);

		btnLogout = new JButton("Cerrar sesion");
		btnLogout.addActionListener(this);
		add(btnLogout, BorderLayout.SOUTH);
	}

	public void switchMenuAdministrativo(JPanel jp) {
		if (0 >= pContenedor.getComponentCount()) {
			return;
		}
		if (jp.getClass() == pContenedor.getComponent(0).getClass()) {
			return;
		}
		pContenedor.setVisible(false);
		pContenedor.removeAll();
		pContenedor.add(jp, BorderLayout.CENTER);
		pContenedor.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		try {
			if (src == mitemServ) {
				switchMenuAdministrativo(new PanelListServicios());
			}
			if (src == mitemFact) {
				switchMenuAdministrativo(new PanelListFacturas());
			}
			if (src == btnLogout) {
				g.logout();
			}
		} catch (Exception exception) {
			g.errorHandler(exception);
		}
	}
}
