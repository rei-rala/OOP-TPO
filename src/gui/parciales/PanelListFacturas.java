package gui.parciales;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.border.TitledBorder;

import comercial.Factura;
import comercial.Servicio;
import comercial.articulos.Costo;
import empresa.Empresa;
import gui.Gui;
import personas.Cliente;
import personas.Tecnico;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.FlowLayout;

public class PanelListFacturas extends JPanel implements ActionListener {
	private JPanel pListadoFacturas;
	private JPanel pFacturaDetalle;

	private Gui g = Gui.getInstance();
	private Empresa e = Empresa.getInstance();
	private ArrayList<Factura> fs = e.getFacturas();

	private Factura currentFactura;

	private JLabel lblNewLabel;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JTextField tfNroFactura;
	private JButton tfClienteFactura;
	private JButton tfServicioAsociado;
	private JTextField tfSubtotal;
	private JTextField tfIVA;
	private JTextField tfTotal;
	private JTextField tfMargenFactura;
	private JLabel lblNewLabel_6;
	private JTextField tfGanancia;

	/**
	 * Create the panel.
	 */
	public PanelListFacturas() {
		setBorder(new TitledBorder(null, "Facturas realizadas", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		setLayout(new GridLayout(0, 2, 0, 0));

		pListadoFacturas = new JPanel();
		add(pListadoFacturas);
		pListadoFacturas.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		pFacturaDetalle = new JPanel();
		pFacturaDetalle.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Detalle",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		add(pFacturaDetalle);
		pFacturaDetalle.setLayout(new GridLayout(0, 2, 0, 0));

		pFacturaDetalle.add(new JLabel("Nro"));

		tfNroFactura = new JTextField();
		pFacturaDetalle.add(tfNroFactura);
		tfNroFactura.setColumns(10);

		lblNewLabel = new JLabel("Cliente");
		pFacturaDetalle.add(lblNewLabel);

		tfClienteFactura = new JButton("Ver");
		tfClienteFactura.addActionListener(this);
		pFacturaDetalle.add(tfClienteFactura);

		lblNewLabel_1 = new JLabel("Servicio asociado");
		pFacturaDetalle.add(lblNewLabel_1);

		tfServicioAsociado = new JButton("Ver");
		tfServicioAsociado.addActionListener(this);
		pFacturaDetalle.add(tfServicioAsociado);

		lblNewLabel_2 = new JLabel("Subtotal");
		pFacturaDetalle.add(lblNewLabel_2);

		tfSubtotal = new JTextField();
		pFacturaDetalle.add(tfSubtotal);
		tfSubtotal.setColumns(10);

		lblNewLabel_3 = new JLabel("IVA");
		pFacturaDetalle.add(lblNewLabel_3);

		tfIVA = new JTextField();
		pFacturaDetalle.add(tfIVA);
		tfIVA.setColumns(10);

		lblNewLabel_4 = new JLabel("Total");
		pFacturaDetalle.add(lblNewLabel_4);

		tfTotal = new JTextField();
		pFacturaDetalle.add(tfTotal);
		tfTotal.setColumns(10);

		lblNewLabel_6 = new JLabel("Ganancia");
		pFacturaDetalle.add(lblNewLabel_6);

		tfGanancia = new JTextField();
		pFacturaDetalle.add(tfGanancia);
		tfGanancia.setColumns(10);

		lblNewLabel_5 = new JLabel("Margen servicio");
		pFacturaDetalle.add(lblNewLabel_5);

		tfMargenFactura = new JTextField();
		pFacturaDetalle.add(tfMargenFactura);
		tfMargenFactura.setColumns(10);

		for (Component c : pFacturaDetalle.getComponents()) {
			if (c.getClass() == JTextField.class || c.getClass() == JButton.class) {
				c.setEnabled(false);
			}
		}

		poblarFacturas();
	}

	private String obtenerTituloFactura(Factura f) {
		String nro = "" + f.getNro();
		String fecha = "" + f.getFecha();
		String total = "$ " + f.calcularTotal();

		return nro + " - " + fecha + " - " + total;
	}

	private void poblarFacturas() {
		pListadoFacturas.removeAll();

		for (Factura f : fs) {
			JButton btnFactura = new JButton(obtenerTituloFactura(f));
			btnFactura.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					currentFactura = f;
					llenarFormulario();
				}
			});
			pListadoFacturas.add(btnFactura);
		}
	}

	private String getNroStr() {
		return "" + currentFactura.getNro();
	}

	private String getClienteStr() {
		Cliente c = currentFactura.getServicio().getCliente();

		String nroCliente = "ID: " + c.getNro();
		String nombre = "Nombre: " + c.getNombre();
		String direccion = "Direccion: " + c.getDireccion();
		String telefono = "Telefono: " + c.getTelefono();

		return nroCliente + " - " + nombre + ":\n" + direccion + "\n" + telefono;
	}

	private String limpiarTecnicos(ArrayList<Tecnico> alT) {
		String tecnicos = "Cantidad " + alT.size();
		for (Tecnico t : alT) {
			tecnicos += "\n\t" + t.getNombre() + " (" + t.getLegajo() + " | " + t.getSeniority() + ")";
		}
		return tecnicos;
	}

	private String limpiarArticulos(ArrayList<Costo> alC) {
		String costos = "Cantidad " + alC.size();
		for (Costo c : alC) {
			costos += "\n\t" + c.getCantidad() + "x" + c.getArticulo().getDescripcion();
		}
		return costos;
	}

	private String getServicioStr() {
		Servicio s = currentFactura.getServicio();

		String nro = "Servcio numero: " + s.nro + "\n";
		String fecha = "Fecha: " + s.getFecha() + "\n";
		String tipo = "Tipo: " + s.getTipoServicio() + "\n";
		String tecnicos = "Tecnicos: " + limpiarTecnicos(s.getTecnicos()) + "\n";
		String articulos = "Articulos: " + limpiarArticulos(s.getArticulos()) + "\n";
		String otrosArts = "Extras: " + limpiarArticulos(s.getOtrosCostos()) + "\n";
		String tiempoTrabajado = "Tiempo: " + s.getTiempoTrabajado() + "\n";
		String costoViaje = "Viaticos: " + s.getCostoViaje() + "\n";
		String costoTotal = "Total servicio: " + s.obtenerTotalServicio();

		return nro + fecha + tipo + tecnicos + articulos + otrosArts + tiempoTrabajado + costoViaje + costoTotal;
	}

	private String getSubtotal() {
		return "$ " + e.redondear(currentFactura.calcularSubTotal());
	}

	private String getIVA() {
		return "$ " + e.redondear(currentFactura.calcularIVA());
	}

	private String getTotal() {
		return "$ " + e.redondear(currentFactura.calcularTotal());
	}

	private String getGanancia() {
		return "$ " + e.redondear(currentFactura.calcularGanancias());
	}

	private String getMargen() {
		return e.redondear(currentFactura.calcularMargen() * 100) + "%";
	}

	private void llenarFormulario() {
		if (currentFactura == null) {
			return;
		}

		tfNroFactura.setText(getNroStr());
		tfSubtotal.setText(getSubtotal());
		tfIVA.setText(getIVA());
		tfTotal.setText(getTotal());
		tfGanancia.setText(getGanancia());
		tfMargenFactura.setText(getMargen());

		tfClienteFactura.setEnabled(true);
		tfServicioAsociado.setEnabled(true);
	}

	public void mostrarMensaje(String mensaje) {
		if (currentFactura == null || mensaje.isBlank()) {
			g.setErrorMessage(new Exception("Factura incorrecta"));
			return;
		}

		JOptionPane.showMessageDialog(null, mensaje);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == tfClienteFactura) {
			mostrarMensaje(getClienteStr());
		}

		if (src == tfServicioAsociado) {
			mostrarMensaje(getServicioStr());
		}
	}

}
