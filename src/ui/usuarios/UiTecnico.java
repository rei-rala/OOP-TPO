package ui.usuarios;

import javax.swing.JPanel;

import comercial.*;
import excepciones.*;
import main.DateAux;
import personas.Tecnico;
import ui.UiUsuariosBase;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;

public class UiTecnico extends UiUsuariosBase {
	private final Tecnico TECNICO;
	private JPanel panel;
	private JPanel subPanel;
	private JPanel subSubPanel;
	private JButton btnSeleccionServicio;

	/**
	 * Create the panel.
	 */
	public UiTecnico(Tecnico tecnico) {
		super("Menu Administrativo");
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

		btnSeleccionServicio = new JButton("Servicios");
		btnSeleccionServicio.addActionListener(this);
		subSubPanel.add(btnSeleccionServicio);
	}

	public void seleccionarServicio() throws Exception {
		ArrayList<Servicio> servs = TECNICO.getServiciosPendientes();
		String opciones = "Selecciona un servicio:";

		for (int i = 0; i < servs.size(); i++) {
			Servicio s = servs.get(i);
			opciones += ("\n\t" + (i + 1) + ") " + s.toStringShorter());
		}

		if (0 >= servs.size()) {
			throw new AsignacionException("NO POSEES SERVICIOS PENDIENTES!");
		}

		int opcion = guiValidarInt(opciones, 1, servs.size());

		Servicio elegido = servs.get(opcion - 1);
		if (elegido != null) {
			// ejecucion de edicion de servicio
			// editarServicio(elegido);
			return;
		}
		throw new GuiException("Seleccion incorrecta");
	}

	public void mVerServicio(Servicio s) throws Exception {
		String nroServicio = "\nNumero servicio " + s.getNro();
		String fechaCreacion = "\nCreado " + DateAux.getDateString(s.getFechaCreacion());
		String diaServicio = "\nFecha Servicio " + DateAux.getDateString(s.getFecha());
		String horarioServicio = "\nHorario " + DateAux.getHorarioCompleto(s);
		String cliente = "\nCliente" + s.getCliente().toStringShort();
		String articulos = "\nArticulos " + s.getArticulos().toString();
		String articulosExtra = "\nArticulos " + s.getArticulosExtra().toString();
		String almuerzo = "\nAlmuerzo " + (s.isIncluyeAlmuerzo() ? "INCLUIDO" : "no incluido");
		String estado = "\nEstado servicio " + s.getEstadoServicio();

		String opciones = nroServicio + fechaCreacion + diaServicio + horarioServicio + cliente + articulos
				+ articulosExtra + almuerzo + estado;

		EstadoServicio es = s.getEstadoServicio();

		opciones += "\n\tQue desea realizar?";
		opciones += "\n\t\t1) Anadir articulo utilizado";
		opciones += "\n\t\t2) Anadir articulo extra";
		opciones += "\n\t\t3) Declarar almuerzo";
		opciones += "\n\t\t4) " + (es == EstadoServicio.PROGRAMADO ? "Comenzar servicio" : "Finalizar servicio");

		int opc = guiValidarInt(opciones, 1, 4);

		if (opc == 0) {
			// pantallaTecnico();
			return;
		}

		if (opc == 4) {
			// mAvanzarEstadoServicio(s);

			if (s.getEstadoServicio() == EstadoServicio.EN_CURSO) {
				mVerServicio(s);
			}
			return;
		}

		if (es != EstadoServicio.EN_CURSO) {
			System.out.println("Para acceder a estas opciones primero debe comenzar el servicio (opcion 4)");
			mVerServicio(s);
			return;
		}

		if (opc == 1) {
			// mAnadirArticulo(s);
		}

		if (opc == 3) {
			// mToggleAlmuerzo(s);
		}

		mVerServicio(s);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {

			if (e.getSource() == btnSeleccionServicio) {
				seleccionarServicio();
			}
		} catch (Exception exception) {
			uiExceptionHandler(exception);
		}

	}

}
