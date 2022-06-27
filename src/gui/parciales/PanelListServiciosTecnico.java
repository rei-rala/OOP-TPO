package gui.parciales;

import javax.security.auth.login.CredentialException;
import javax.swing.JButton;
import javax.swing.JPanel;

import empresa.Empresa;
import excepciones.ServicioException;
import gui.Gui;
import personas.*;
import javax.swing.border.TitledBorder;

import comercial.EstadoServicio;
import comercial.Servicio;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import java.awt.FlowLayout;

public class PanelListServiciosTecnico extends JPanel implements ActionListener {
	private Gui g = Gui.getInstance();
	private Empresa e = Empresa.getInstance();
	private Tecnico i = (Tecnico) g.getUsuarioLogeado();
	private Servicio currentServicio;

	private JPanel pListadoServicios;
	private JPanel pEdicionServicio;
	private JLabel lblNewLabel;
	private JTextField tfNroServicio;
	private JLabel lblNewLabel_1;
	private JButton btnVerCliente;
	private JLabel lblNewLabel_2;
	private JPanel panel;
	private JButton btnVerArticulos;
	private JButton btnAgregarArticulo;
	private JLabel lblNewLabel_3;
	private JPanel panel_1;
	private JButton btnVerOtrosArts;
	private JButton btnAgregarOtroArt;
	private JPanel panel_2;
	private JLabel lblNewLabel_4;
	private JLabel lblEstadoServicio;
	private JButton btnAvanzarEstado;
	private JPanel panel_3;
	private JLabel lblNewLabel_6;
	private JLabel lblAlmuerzo;
	private JButton btnToggleAlmuerzo;

	/**
	 * Create the panel.
	 */
	public PanelListServiciosTecnico() {
		setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Servicios asignados", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setLayout(new GridLayout(0, 2, 0, 0));

		pListadoServicios = new JPanel();
		add(pListadoServicios);
		pListadoServicios.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		pEdicionServicio = new JPanel();
		pEdicionServicio
				.setBorder(new TitledBorder(null, "Edicion", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		add(pEdicionServicio);
		pEdicionServicio.setLayout(new GridLayout(0, 2, 0, 0));

		lblNewLabel = new JLabel("Nro servicio");
		pEdicionServicio.add(lblNewLabel);

		tfNroServicio = new JTextField();
		tfNroServicio.setEnabled(false);
		tfNroServicio.setEditable(false);
		pEdicionServicio.add(tfNroServicio);
		tfNroServicio.setColumns(10);

		lblNewLabel_1 = new JLabel("Cliente");
		pEdicionServicio.add(lblNewLabel_1);

		btnVerCliente = new JButton("Ver");
		btnVerCliente.addActionListener(this);
		pEdicionServicio.add(btnVerCliente);

		lblNewLabel_2 = new JLabel("Articulos");
		pEdicionServicio.add(lblNewLabel_2);

		panel = new JPanel();
		pEdicionServicio.add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		btnVerArticulos = new JButton("Ver");
		btnVerArticulos.addActionListener(this);
		panel.add(btnVerArticulos);

		btnAgregarArticulo = new JButton("Agregar");
		btnAgregarArticulo.addActionListener(this);
		panel.add(btnAgregarArticulo);

		lblNewLabel_3 = new JLabel("Otros Articulos");
		pEdicionServicio.add(lblNewLabel_3);

		panel_1 = new JPanel();
		pEdicionServicio.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		btnVerOtrosArts = new JButton("Ver");
		btnVerOtrosArts.addActionListener(this);
		panel_1.add(btnVerOtrosArts);

		btnAgregarOtroArt = new JButton("Agregar");
		btnAgregarOtroArt.addActionListener(this);
		panel_1.add(btnAgregarOtroArt);

		panel_3 = new JPanel();
		pEdicionServicio.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));

		lblNewLabel_6 = new JLabel("Almuerzo");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblNewLabel_6);

		lblAlmuerzo = new JLabel("");
		lblAlmuerzo.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblAlmuerzo);

		btnToggleAlmuerzo = new JButton("Cambiar");
		btnToggleAlmuerzo.addActionListener(this);
		pEdicionServicio.add(btnToggleAlmuerzo);

		panel_2 = new JPanel();
		pEdicionServicio.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));

		lblNewLabel_4 = new JLabel("Estado servicio");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblNewLabel_4);

		lblEstadoServicio = new JLabel("");
		lblEstadoServicio.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblEstadoServicio);

		btnAvanzarEstado = new JButton("Avanzar estado");
		btnAvanzarEstado.addActionListener(this);
		pEdicionServicio.add(btnAvanzarEstado);

		for (Component c : pEdicionServicio.getComponents()) {
			if (c.getClass() == JLabel.class) {
				JLabel label = (JLabel) c;
				label.setHorizontalAlignment(SwingConstants.CENTER);
			}
		}

		poblarServicios();
	}

	private void poblarServicios() {
		pListadoServicios.setVisible(false);
		pListadoServicios.removeAll();

		for (Servicio s : i.getServiciosAsignados()) {
			if (s.getEstadoServicio() == EstadoServicio.CANCELADO
					|| s.getEstadoServicio() == EstadoServicio.FINALIZADO) {
				continue;
			}

			JButton btnServicio = new JButton("Nro: " + s.nro + " - " + "Estado: " + s.getEstadoServicio());
			btnServicio.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					currentServicio = s;
					llenarDatoServicio();
				}
			});

			pListadoServicios.add(btnServicio);
		}

		if (0 >= pListadoServicios.getComponentCount()) {
			JLabel noServAsignados = new JLabel("No tienes servicios asignados");
			noServAsignados.setHorizontalAlignment(SwingConstants.CENTER);
			pListadoServicios.add(noServAsignados);
		}

		pListadoServicios.setVisible(true);
	}

	private void llenarDatoServicio() {
		if (currentServicio == null) {
			tfNroServicio.setText("");
			lblEstadoServicio.setText("");
			lblAlmuerzo.setText("");
			return;
		}
		tfNroServicio.setText("" + currentServicio.nro);
		lblEstadoServicio.setText("" + currentServicio.getEstadoServicio());
		lblAlmuerzo.setText(currentServicio.isIncluyeAlmuerzo() ? "Incluido" : "No");
	}

	public void avanzarEstado(Tecnico t) throws Exception {
		Servicio ce = currentServicio;
		EstadoServicio es = ce.getEstadoServicio();

		if (es == EstadoServicio.PROGRAMADO) {
			if (JOptionPane.showConfirmDialog(null, "Avanzar estado a EN CURSO? No es reversible") == 0) {
				t.ejecutarServicio(ce);
			} else {
				JOptionPane.showMessageDialog(null, "Cancelado por usuario");
			}
		} else if (es == EstadoServicio.EN_CURSO) {
			if (JOptionPane.showConfirmDialog(null, "Avanzar estado a FINALIZADO? No es reversible") == 0) {
				t.finalizarServicio(ce);
				currentServicio = null;
			} else {
				JOptionPane.showMessageDialog(null, "Cancelado por usuario");
			}
		} else {
			g.errorHandler(new CredentialException("Permisos insuficientes"));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (i.getClass() != Tecnico.class) {
			g.errorHandler(new CredentialException("Permisos insuficientes"));
			return;
		}

		if (currentServicio == null) {
			g.errorHandler(new ServicioException("Primero seleccione un servicio"));
			return;
		}

		try {

			Tecnico t = (Tecnico) i;

			if (e.getSource() == btnAvanzarEstado) {
				avanzarEstado(t);
			}

			if (e.getSource() == btnToggleAlmuerzo) {
				t.toggleAlmuerzoServicio(currentServicio);
			}

			llenarDatoServicio();
			poblarServicios();

		} catch (Exception exception) {
			g.errorHandler(exception);
		}
	}
}
