package gui.homes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import empresa.*;
import gui.Gui;
import gui.parciales.PanelEdicionArticulos;
import personas.Admin;
import personas.Seniority;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class AdminHome extends JPanel implements ActionListener {
	private final Gui gui = Gui.getInstance();
	private final Admin adm = (Admin) gui.getUsuarioLogeado();
	private final Empresa empresa = Empresa.getInstance();

	private JLabel lblNewLabel;
	private JPanel panel;
	private JPanel pCC;
	private JPanel pCV;
	private JPanel pCHT;
	private JButton btnLogout;
	private JButton btnNuevoCC;
	private JButton btnNuevoCV;
	private JButton btnNuevoCHT;
	private JTextField tfNuevoCC;
	private JTextField tfNuevoCV;
	private JTextField tfCHTJr;
	private JTextField tfCHTSsr;
	private JTextField tfCHTSr;
	private JLabel lblNewLabel_4;
	private JLabel lblJr;
	private JLabel lblSsr;
	private JLabel lblSr;
	private JLabel labelCC;
	private JLabel labelCV;
	private PanelEdicionArticulos panelEdicionArticulos;
	private JLabel tituloPanel;

	public AdminHome() {
		setBorder(new TitledBorder(null, "MENU ADMIN", TitledBorder.CENTER, TitledBorder.TOP, null, Color.GRAY));
		setLayout(new BorderLayout(0, 0));
		String username = gui.getUsuarioLogeado().getClass().getSimpleName();

		lblNewLabel = new JLabel("Bienvenido «%s»".formatted(username));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		add(lblNewLabel, BorderLayout.NORTH);

		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		tituloPanel = new JLabel("Configuraciones de admin");
		tituloPanel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tituloPanel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(tituloPanel);

		pCC = new JPanel();
		panel.add(pCC);
		pCC.setLayout(new BoxLayout(pCC, BoxLayout.X_AXIS));

		labelCC = new JLabel("(Actual: " + empresa.getCostoCombustible() + ") Nuevo costo combustible");
		pCC.add(labelCC);

		tfNuevoCC = new JTextField("" + empresa.getCostoCombustible());
		tfNuevoCC.setColumns(10);
		pCC.add(tfNuevoCC);

		btnNuevoCC = new JButton("Actualizar");
		btnNuevoCC.addActionListener(this);
		pCC.add(btnNuevoCC);

		pCV = new JPanel();
		panel.add(pCV);
		pCV.setLayout(new BoxLayout(pCV, BoxLayout.X_AXIS));

		labelCV = new JLabel("(Actual: " + empresa.getCostoViaje() + ") Nuevo costo viaje");
		pCV.add(labelCV);

		tfNuevoCV = new JTextField("" + empresa.getCostoViaje());
		pCV.add(tfNuevoCV);
		tfNuevoCV.setColumns(10);

		btnNuevoCV = new JButton("Actualizar");
		btnNuevoCV.addActionListener(this);
		pCV.add(btnNuevoCV);

		pCHT = new JPanel();
		panel.add(pCHT);
		pCHT.setLayout(new GridLayout(0, 3, 0, 0));

		pCHT.add(new JSeparator());

		lblNewLabel_4 = new JLabel("Costo Horas Tecnico");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		pCHT.add(lblNewLabel_4);

		pCHT.add(new JSeparator());

		lblJr = new JLabel("Junior");
		lblJr.setHorizontalAlignment(SwingConstants.CENTER);
		pCHT.add(lblJr);

		lblSsr = new JLabel("Semi Senior");
		lblSsr.setHorizontalAlignment(SwingConstants.CENTER);
		pCHT.add(lblSsr);

		lblSr = new JLabel("Senior");
		lblSr.setHorizontalAlignment(SwingConstants.CENTER);
		pCHT.add(lblSr);

		tfCHTJr = new JTextField("" + empresa.obtenerCostoHoraTecnico(Seniority.JUNIOR));
		tfCHTJr.setHorizontalAlignment(SwingConstants.CENTER);
		pCHT.add(tfCHTJr);
		tfCHTJr.setColumns(10);

		tfCHTSsr = new JTextField("" + empresa.obtenerCostoHoraTecnico(Seniority.SEMI_SENIOR));
		tfCHTSsr.setHorizontalAlignment(SwingConstants.CENTER);
		pCHT.add(tfCHTSsr);
		tfCHTSsr.setColumns(10);

		tfCHTSr = new JTextField("" + empresa.obtenerCostoHoraTecnico(Seniority.SENIOR));
		tfCHTSr.setHorizontalAlignment(SwingConstants.CENTER);
		pCHT.add(tfCHTSr);
		tfCHTSr.setColumns(10);

		pCHT.add(new JSeparator());

		btnNuevoCHT = new JButton("Actualizar CHTs");
		btnNuevoCHT.addActionListener(this);
		pCHT.add(btnNuevoCHT);
		
		panelEdicionArticulos = new PanelEdicionArticulos();
		panel.add(panelEdicionArticulos);

		btnLogout = new JButton("Cerrar sesion");
		btnLogout.addActionListener(this);
		add(btnLogout, BorderLayout.SOUTH);
	}

	private void actualizarCC() {
		int confirm = JOptionPane.showConfirmDialog(null, "Confirmar nuevo costo Costo Combustible");
		try {
			if (confirm == 0) {
				double nuevoCC = gui.validarDouble(tfNuevoCC.getText());
				adm.modificarCostoCombustible(nuevoCC);
				labelCC.setText("(Actual: " + empresa.getCostoCombustible() + ") Nuevo costo combustible");
			}
		} catch (Exception e) {
			gui.setErrorMessage(e);
		}
	}

	private void actualizarCV() {
		int confirm = JOptionPane.showConfirmDialog(null, "Confirmar nuevo costo Costo Viaje");
		try {
			if (confirm == 0) {
				double nuevoCV = gui.validarDouble(tfNuevoCV.getText());
				adm.modificarCostoViaje(nuevoCV);
				labelCV.setText("(Actual: " + empresa.getCostoViaje() + ") Nuevo costo viaje");
			}
		} catch (Exception e) {
			gui.setErrorMessage(e);
		}
	}

	private void actualizarCHT() {
		int confirm = JOptionPane.showConfirmDialog(null, "Confirmar nuevos costo horas tecnico");
		try {
			if (confirm == 0) {
				double nuevoCHT_jr = gui.validarDouble(tfCHTJr.getText());
				double nuevoCHT_ssr = gui.validarDouble(tfCHTSsr.getText());
				double nuevoCHT_sr = gui.validarDouble(tfCHTSr.getText());
				adm.modificarCostoHoraTecnico(nuevoCHT_jr, nuevoCHT_ssr, nuevoCHT_sr);
				JOptionPane.showMessageDialog(null, "Valores actualizados con exito");
			}
		} catch (Exception e) {
			gui.setErrorMessage(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogout) {
			gui.logout();
		}

		if (e.getSource() == btnNuevoCC) {
			actualizarCC();
		}

		if (e.getSource() == btnNuevoCV) {
			actualizarCV();
		}

		if (e.getSource() == btnNuevoCHT) {
			actualizarCHT();
		}
	}
}
