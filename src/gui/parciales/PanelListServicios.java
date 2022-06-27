package gui.parciales;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import comercial.*;
import comercial.articulos.*;
import empresa.Empresa;
import excepciones.*;
import gui.Gui;
import personas.*;

import java.awt.GridLayout;
import java.awt.HeadlessException;
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

			JButton btnServicio = new JButton(getServicioTitle(currentServ));
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

	private String getServicioTitle(Servicio s) {
		String id = "Nro: " + s.nro;
		String datoServicio;
		if (s.isFacturado()) {
			datoServicio = "FACTURADO";
		} else {
			datoServicio = s.getEstadoServicio().name();
		}

		return id + ": " + datoServicio;
	}

	private String getFecha(Servicio s) {
		return "" + s.getFecha();
	}

	private String getCliente(Cliente c) {
		return "" + c.getNro() + " - " + c.getNombre();
	}

	private String getTipoServicio(Servicio s) {
		return "" + s.getTipoServicio();
	}

	private String getEstadoServicio(Servicio s) {
		return "" + s.getEstadoServicio();
	}

	private String getTecnicosAsignados(Servicio s) {
		ArrayList<Tecnico> tecs = s.getTecnicos();
		String tecnicosAsignados = tecs.size() + " asignados a este servicio";

		for (int i = 0; i < tecs.size(); i++) {
			Tecnico t = tecs.get(i);
			tecnicosAsignados += "\n" + (i + 1) + ". " + t.getNombre() + "(" + t.getLegajo() + " - " + t.getSeniority()
					+ ")";
		}

		return tecnicosAsignados;
	}

	private String getArticulos(Servicio s) {
		ArrayList<Costo> costos = s.getArticulos();
		String articulosUtilizados = costos.size() + " utilizados";

		for (int i = 0; i < costos.size(); i++) {
			Costo c = costos.get(i);
			articulosUtilizados += "\n" + (i + 1) + ". " + c.getArticulo().getDescripcion() + " x " + c.getCantidad()
					+ " = $" + c.obtenerTotalCosto();
		}

		return articulosUtilizados;
	}

	private String getOtrosArticulos(Servicio s) {
		ArrayList<Costo> otrosCostos = s.getOtrosCostos();
		String articulosUtilizados = otrosCostos.size() + " utilizados";

		for (Costo c : otrosCostos) {
			articulosUtilizados += "\t\n" + c.getArticulo().getDescripcion() + " x " + c.getCantidad() + " = $"
					+ c.obtenerTotalCosto();
		}

		return articulosUtilizados;
	}

	private String getTotalServicio(Servicio s) {
		return "TOTAL: " + s.obtenerTotalServicio();
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
		tfCliente.setText(getCliente(s.getCliente()));
		tfFecha.setText(getFecha(s));
		tfTipoServicio.setText(getTipoServicio(s));
		tfTiempoTrabajado.setText("" + s.getTiempoTrabajado());
		tfEstadoServicio.setText(getEstadoServicio(s));
		lblTotalServicio.setText(getTotalServicio(s));
	}

	private void adicionarOtroCosto(Servicio s) {
		try {
			String desc = JOptionPane.showInputDialog("Ingrese descripcion para nuevo producto (Vacio para cancelar)");

			if (desc == null) {
				throw new Exception("Cancelado por usuario");
			}

			if (desc.trim().length() == 0) {
				throw new Exception("Cancelado por usuario");
			}

			String costo = JOptionPane.showInputDialog("Ingrese costo para " + desc + "(vacio para cancelar)");

			if (costo == null) {
				throw new Exception("Cancelado por usuario");
			}

			if (costo.trim().length() == 0) {
				throw new Exception("Cancelado por usuario");
			}

			double costoParseado = g.validarDouble(costo);
			ArticuloExtra nuevoAE = new ArticuloExtra(desc, costoParseado);
			s.anadirOtroCostos(nuevoAE, 1);
			JOptionPane.showMessageDialog(null, "Añadido nuevo costo " + nuevoAE.toString());
			rellenarDatos();

		} catch (Exception e) {
			String msg = e.getMessage();
			if (msg.contains("Cancelado")) {
				JOptionPane.showMessageDialog(null, msg);
				return;
			}
			JOptionPane.showMessageDialog(null, "Se produjo un error:\n" + msg);
			System.out.println(e);
		}
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
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		Servicio ces = currentEdicionServicio;

		if (ces == null) {
			JOptionPane.showMessageDialog(null, "No hay servicio seleccionado");
			return;
		}
		if (src == btnTecnicos) {
			JOptionPane.showMessageDialog(null, getTecnicosAsignados(ces));
		}

		if (src == btnArts) {
			JOptionPane.showMessageDialog(null, getArticulos(ces));
		}

		if (src == btnOtrosArts) {
			JOptionPane.showMessageDialog(null, getOtrosArticulos(ces));
		}

		if (src == btnNuevoOtroArt) {
			adicionarOtroCosto(ces);
		}
	}
}
