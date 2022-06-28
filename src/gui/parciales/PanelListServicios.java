package gui.parciales;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import comercial.*;
import empresa.Empresa;
import excepciones.*;
import gui.Gui;
import personas.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import java.awt.Component;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

public class PanelListServicios extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel pSeleccionServicio;
	private Gui g = Gui.getInstance();
	private Empresa e = g.empresa;
	private Interno i = g.getUsuarioLogeado();
	private ArrayList<Servicio> s = e.getServicios();

	private Servicio currentEdicionServicio = null;

	private JPanel pEdicionServicio;
	private JTextField tfNro;
	private JTextField tfCliente;
	private JTextField tfFecha;
	private JTextField tfTipoServicio;
	private JButton btnTecnicos;
	private JTextField tfEstadoServicio;
	private JTextField tfTiempoTrabajado;
	private JButton btnArts;
	private JButton btnFacturar;
	private JLabel lblTotalServicio;
	private JButton btnOtrosArts;
	private JPanel panel;
	private JButton btnNuevoOtroArt;

	/**
	 * Create the panel.
	 */
	public PanelListServicios() {
		setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Servicios Finalizados", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setLayout(new GridLayout(0, 2, 0, 0));

		pSeleccionServicio = new JPanel();

		add(pSeleccionServicio);

		pEdicionServicio = new JPanel();
		pEdicionServicio.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Detalle",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		add(pEdicionServicio);
		pEdicionServicio.setLayout(new GridLayout(0, 2, 0, 0));

		pEdicionServicio.add(new JLabel("Numero"));

		tfNro = new JTextField();
		tfNro.setEditable(false);
		pEdicionServicio.add(tfNro);
		tfNro.setColumns(10);

		pEdicionServicio.add(new JLabel("Fecha"));

		tfFecha = new JTextField();
		tfFecha.setEditable(false);
		pEdicionServicio.add(tfFecha);
		tfFecha.setColumns(10);

		pEdicionServicio.add(new JLabel("Cliente"));

		tfCliente = new JTextField();
		tfCliente.setEditable(false);
		pEdicionServicio.add(tfCliente);
		tfCliente.setColumns(10);

		JLabel label = new JLabel("Tipo");
		pEdicionServicio.add(label);

		tfTipoServicio = new JTextField();
		tfTipoServicio.setEditable(false);
		pEdicionServicio.add(tfTipoServicio);
		tfTipoServicio.setColumns(10);

		pEdicionServicio.add(new JLabel("Estado"));

		tfEstadoServicio = new JTextField();
		tfEstadoServicio.setEditable(false);
		pEdicionServicio.add(tfEstadoServicio);

		pEdicionServicio.add(new JLabel("Tecnicos asignados"));

		btnTecnicos = new JButton("Ver");
		btnTecnicos.addActionListener(this);
		pEdicionServicio.add(btnTecnicos);

		pEdicionServicio.add(new JLabel("Tiempo trabajado"));

		tfTiempoTrabajado = new JTextField();
		tfTiempoTrabajado.setEditable(false);
		pEdicionServicio.add(tfTiempoTrabajado);
		tfTiempoTrabajado.setColumns(10);

		pEdicionServicio.add(new JLabel("Articulos"));
		btnArts = new JButton("Ver");
		btnArts.addActionListener(this);
		pEdicionServicio.add(btnArts);

		pEdicionServicio.add(new JLabel("Otros articulos"));

		panel = new JPanel();
		pEdicionServicio.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		btnOtrosArts = new JButton("Ver");
		btnOtrosArts.addActionListener(this);
		panel.add(btnOtrosArts);

		btnNuevoOtroArt = new JButton("A\u00F1adir");
		btnNuevoOtroArt.addActionListener(this);
		panel.add(btnNuevoOtroArt);

		lblTotalServicio = new JLabel("Total $ -");
		lblTotalServicio.setFont(new Font("Tahoma", Font.BOLD, 10));
		pEdicionServicio.add(lblTotalServicio);

		btnFacturar = new JButton("Seleccione servicio");
		pEdicionServicio.add(btnFacturar);

		for (Component c : pEdicionServicio.getComponents()) {
			if (c.getClass() == JLabel.class) {
				JLabel jlC = (JLabel) c;
				jlC.setHorizontalAlignment(SwingConstants.CENTER);
			}
		}

		poblarServicios();
	}

	private void poblarServicios() {
		pSeleccionServicio.setVisible(false);
		pSeleccionServicio.removeAll();

		for (Servicio currentServ : s) {

			if (currentServ.getEstadoServicio() != EstadoServicio.FINALIZADO) {
				continue;
			}
			if (currentServ.isFacturado()) {
				continue;
			}

			JButton btnServicio = new JButton(g.getServicioTitle(currentServ));
			btnServicio.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setCurrentEdicionServicio(currentServ);
					rellenarDatos();
					obtenerAccion();
				}
			});

			pSeleccionServicio.add(btnServicio);
		}
		pSeleccionServicio.setVisible(true);
	}

	private void obtenerAccion() {

		g.removerActionListeners(btnFacturar);
		btnFacturar.setEnabled(false);

		if (currentEdicionServicio == null) {
			btnFacturar.setText("Seleccione servicio");

		} else if (currentEdicionServicio.isFacturado()) {
			btnFacturar.setText("Servicio ya facturado");
		} else {
			btnFacturar.setText("FACTURAR");
			btnFacturar.setEnabled(true);
			btnFacturar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					facturarServicio(currentEdicionServicio);
				}
			});
		}

	}

	private void setCurrentEdicionServicio(Servicio currentEdicionServicio) {
		this.currentEdicionServicio = currentEdicionServicio;
	}

	private void rellenarDatos() {
		Servicio s = currentEdicionServicio;
		obtenerAccion();

		if (s == null) {
			tfNro.setText("");
			tfCliente.setText("");
			tfFecha.setText("");
			tfTipoServicio.setText("");
			tfTiempoTrabajado.setText("");
			tfEstadoServicio.setText("");
			lblTotalServicio.setText("");
			return;
		}
		tfNro.setText("" + s.nro);
		tfCliente.setText(g.getCliente(s.getCliente()));
		tfFecha.setText(g.getFecha(s));
		tfTipoServicio.setText(g.getTipoServicio(s));
		tfTiempoTrabajado.setText("" + s.getTiempoTrabajado());
		tfEstadoServicio.setText(g.getEstadoServicio(s));
		lblTotalServicio.setText(g.getTotalServicio(s));
	}

	private Factura invocarFacturacion(Servicio s) throws Exception {
		Class<? extends Interno> cls = i.getClass();

		if (cls == Administrativo.class) {
			Administrativo administrativo = (Administrativo) i;
			return administrativo.facturarServicio(s);
		} else {
			throw new CredencialException("No se pudo facturar: Permisos insuficientes");
		}
	}

	private void facturarServicio(Servicio s) {
		try {
			if (JOptionPane.showConfirmDialog(null, "Facturar el servicio nro " + s.nro) == 0) {
				Factura f = invocarFacturacion(s);
				JOptionPane.showMessageDialog(null, "Servicio " + s.nro + " facturado. Nro factura " + f.getNro());
				System.out.println(f);

				currentEdicionServicio = null;
				poblarServicios();
				obtenerAccion();
			}
		} catch (Exception e) {
			g.errorHandler(e);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (i.getClass() != Administrativo.class) {
				throw new CredencialException("Permisos insuficientes");
			}

			Administrativo adm = (Administrativo) i;

			Object src = e.getSource();
			Servicio ces = currentEdicionServicio;

			if (ces == null) {
				g.errorHandler(new ServicioException("Debe seleccionar un servicio"));
				return;
			}

			if (src == btnTecnicos) {
				JOptionPane.showMessageDialog(null, g.getTecnicosAsignados(ces));
			}

			if (src == btnArts) {
				JOptionPane.showMessageDialog(null, g.getArticulos(ces));
			}

			if (src == btnOtrosArts) {
				JOptionPane.showMessageDialog(null, g.getOtrosArticulos(ces));
			}

			if (src == btnNuevoOtroArt) {
				g.adicionarOtroCosto(adm, ces);
				rellenarDatos();
			}
		} catch (Exception exception) {
			g.setErrorMessage(exception);
		}
	}
}
