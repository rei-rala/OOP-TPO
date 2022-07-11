package gui.usuarios;

import javax.swing.JPanel;

import comercial.*;
import comercial.articulos.Articulo;
import comercial.articulos.ArticuloExtra;
import comercial.articulos.Costo;
import excepciones.*;
import main.DateAux;
import personas.Tecnico;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GuiTecnico extends GuiUsuarioBase {
	private final Tecnico TECNICO;
	private JPanel panel;
	private JPanel subPanel;
	private JPanel subSubPanel;
	private JButton btnSeleccionServicio;
	private JPanel panel_1;

	public GuiTecnico(Tecnico tecnico) {
		super("Bienvenido " + tecnico.getNombre() + "<TECNICO>");
		this.TECNICO = tecnico;

		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 3, 0, 0));

		subPanel = new JPanel();
		panel.add(subPanel);

		subSubPanel = new JPanel();
		panel.add(subSubPanel);
		subSubPanel.setLayout(new GridLayout(3, 0, 0, 0));

		subSubPanel.add(new JLabel(""));

		panel_1 = new JPanel();
		subSubPanel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		// Estetico
		panel_1.add(new JLabel(""));

		btnSeleccionServicio = new JButton("Servicios");
		panel_1.add(btnSeleccionServicio);

		// Estetico
		panel_1.add(new JLabel(""));
		btnSeleccionServicio.addActionListener(this);
	}

	private Servicio seleccionarServicioAsignado() throws Exception {
		ArrayList<Servicio> servs = TECNICO.getServiciosPendientes();
		String opciones = "Selecciona un servicio por su ID:";

		if (0 >= servs.size()) {
			throw new AsignacionException("NO POSEES SERVICIOS PENDIENTES!");
		}

		for (Servicio s : servs) {
			String fStr = DateAux.getDateString(s.getFecha());
			String horario = s.getHorarioServicio();
			opciones += "\n" + s.getNro() + ") Fecha: " + fStr + " - " + horario;
		}

		int opcion = guiValidarInt(opciones);

		Servicio elegido = TECNICO.getServiciosPendientes(opcion);

		if (elegido == null) {
			throw new AsignacionException("Valor no valido");
		}

		return elegido;
	}

	private void mostrarServicio() throws Exception {
		Servicio s = seleccionarServicioAsignado();
		editarServicio(s);
	}

	private void editarServicio(Servicio s) throws Exception {
		String nroServicio = "\nNumero servicio " + s.getNro();
		String fechaCreacion = "\nCreado " + DateAux.getDateString(s.getFechaCreacion());
		String diaServicio = "\nFecha Servicio " + DateAux.getDateString(s.getFecha());
		String horarioServicio = "\nHorario " + DateAux.getHorarioCompleto(s);
		String cliente = "\nCliente" + s.getCliente().toStringShort();

		String articulos = "\nArticulos :";
		for (Costo c : s.getArticulos()) {
			String descr = c.getArticulo().getDescripcion();
			double costo = c.getArticulo().getCosto(), total = c.obtenerTotalCosto();
			articulos += "\n\t-" + descr + ": " + c.getCantidad() + "x" + costo + " = $" + total;
		}

		String articulosExtra = "\nArticulos Extra:";
		for (Costo c : s.getArticulosExtra()) {
			String descr = c.getArticulo().getDescripcion();
			double costo = c.getArticulo().getCosto(), total = c.obtenerTotalCosto();
			articulosExtra += "\n\t-" + descr + ": " + c.getCantidad() + "x" + costo + " = $" + total;
		}

		String almuerzo = "\nAlmuerzo " + (s.isIncluyeAlmuerzo() ? "INCLUIDO" : "no incluido");
		String estado = "\nEstado servicio " + s.getEstadoServicio();

		String opciones = nroServicio + fechaCreacion + diaServicio + horarioServicio + cliente + articulos
				+ articulosExtra + almuerzo + estado;

		EstadoServicio es = s.getEstadoServicio();

		opciones += "\n\nQue desea realizar?";
		opciones += "\n\t\t1) Anadir articulo utilizado";
		opciones += "\n\t\t2) Anadir articulo extra";
		opciones += "\n\t\t3) Declarar almuerzo";
		opciones += "\n\t\t4) " + (es == EstadoServicio.PROGRAMADO ? "Comenzar" : "Finalizar") + " servicio";

		int opc = guiValidarInt(opciones, 1, 4);

		if (opc == 4) {
			avanzarEstadoServicio(s);
			if (s.getEstadoServicio() == EstadoServicio.EN_CURSO) {
				editarServicio(s);
			}
			return;
		}

		if (es != EstadoServicio.EN_CURSO) {
			alert("Para acceder a estas opciones primero debe comenzar el servicio (opcion 4)");
      editarServicio(s);
      return;
		}

		if (opc == 1) {
			anadirArticulo(s);
		}

		if (opc == 2) {
			anadirArticuloExtra(s);
		}

		if (opc == 3) {
			toggleAlmuerzo(s);
		}

		editarServicio(s);
	}

	private Articulo seleccionarArticulo() throws Exception {
		Articulo seleccionado = null;

		ArrayList<Articulo> articulos = TECNICO.getArticulos();

		String opciones = "Selecciona un articulo por su SKU:";
		for (Articulo a : articulos) {
			opciones += "\n" + a.getSKU() + ") " + a.getDescripcion();
		}

		int opcion = guiValidarInt(opciones);
		seleccionado = TECNICO.getArticulos(opcion);

		if (seleccionado == null) {
			throw new ValorException("Opcion no valida");
		}

		return seleccionado;
	}

	private void anadirArticulo(Servicio s) throws Exception {
		Articulo a = seleccionarArticulo();
		String opciones = "Ingrese la cantidad de " + a.getDescripcion() + " a anadir <Stock:" + a.getStock() + ">";
		int cantidad = guiValidarInt(opciones);

		String msgConfirm = "Anadir " + cantidad + " " + a.getDescripcion() + " a Servicio nro " + s.getNro() + "?";
		if (confirm(msgConfirm)) {
			TECNICO.anadirArticuloServicio(s, cantidad, a);
      alert("Se ha anadido el articulo");
		} else {
			alert("Cancelado por usuario");
		}
	}

	private void anadirArticuloExtra(Servicio s) throws Exception {
		String descripcion = input("Ingrese la descripcion del articulo extra");
		double costo = guiValidarDouble("Ingrese el costo del articulo extra");

		ArticuloExtra ae = TECNICO.crearArticuloExtra(descripcion, costo);
		String msgConfirm = "Anadir articulo extra " + ae + " a Servicio nro " + s.getNro() + "?";
		msgConfirm += "\nACCION NO REVERSIBLE";

		if (confirm(msgConfirm)) {
			TECNICO.anadirArticuloExtraServicio(s, 1, ae);
      alert("Se ha anadido el articulo extra");
		} else {
			alert("Cancelado por usuario");
		}
	}

	private void toggleAlmuerzo(Servicio s) throws Exception {
		String accion = s.isIncluyeAlmuerzo() ? "QUITAR" : "AGREGAR";
		String msgConfirm = "Esta seguro que desea " + accion + " almuerzo?";
		if (confirm(msgConfirm)) {
			TECNICO.toggleAlmuerzoServicio(s);
      alert("El estado del almuerzo cambio a " + s.isIncluyeAlmuerzo());
		} else {
			alert("No se edito almuerzo del servicio");
		}
	}

	private void avanzarEstadoServicio(Servicio s) throws Exception {
		String accion = s.getEstadoServicio() == EstadoServicio.PROGRAMADO ? "COMENZAR" : "FINALIZAR";
		String msgConfirm = "Esta seguro que desea " + accion + " el servicio?";

		if (confirm(msgConfirm)) {
			TECNICO.avanzarServicio(s);
		} else {
			alert("No se edito estado del servicio");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {

			if (e.getSource() == btnSeleccionServicio) {
				mostrarServicio();
			}
		} catch (Exception exception) {
			guiExceptionHandler(exception);
		}

	}

}
