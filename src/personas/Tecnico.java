package personas;

import java.util.ArrayList;
import java.util.Date;

import comercial.*;
import empresa.Empresa;
import excepciones.AsignacionException;
import excepciones.ServicioException;

public class Tecnico extends Interno {
	private Seniority seniority;
	private String AGENDA;

	public Tecnico(String nombre, long dni, String direccion, String telefono, String contrasena, Seniority seniority) {
		super(nombre, dni, direccion, telefono, contrasena);
		this.seniority = seniority;
		this.AGENDA = "XXXXXXXXX";

		Empresa.getInstance().agregarTecnico(this);
	}

	// ALTERNATIVO SIN DATOS
	public Tecnico(String nombre, String contrasena, Seniority seniority) {
		super(nombre, contrasena);
		this.seniority = seniority;
		this.AGENDA = "XXXXXXXXX";

		Empresa.getInstance().agregarTecnico(this);
	}

	public Seniority getSeniority() {
		return seniority;
	}

	public void setSeniority(Seniority seniority) {
		this.seniority = seniority;
	}

	public String getAGENDA() {
		return AGENDA;
	}

	public ArrayList<Servicio> getServiciosAsignados() {
		/*
		 * Devuelve(devolvera) arraylist de servicios que estan asignados al tecnico
		 */
		// AGENDA.serviciosAsignados...

		return new ArrayList<Servicio>();
	}

	@Override
	public String toString() {
		return "Tecnico [seniority=" + seniority + ", legajo=" + legajo + ", nombre=" + nombre + ", dni=" + dni
				+ ", direccion=" + direccion + ", telefono=" + telefono + "]";
	}

	// METODOS TECNICO -> SERVICIO
	public ArrayList<Servicio> verServiciosAsignados() {
		ArrayList<Servicio> asignados = new ArrayList<Servicio>();

		for (Servicio s : Empresa.getInstance().getServicios()) {
			if (s.getTecnicos().contains(this)) {
				asignados.add(s);
			}
		}

		return asignados;
	}

	public Servicio verServiciosAsignados(int nroServicio) {
		ArrayList<Servicio> asignados = verServiciosAsignados();
		Servicio buscado = null;

		for (Servicio s : asignados) {
			if (s.nro == nroServicio) {
				buscado = s;
				break;
			}
		}

		return buscado;
	}

	public void editarTiempoServicio(Servicio s, double tiempoTrabajado) throws AsignacionException, ServicioException {
		String genExc = "No fue a�adir editar tiempo de servicio: ";

		if (s.getTecnicos().contains(this) == false) {
			throw new AsignacionException(genExc + "No estas asignado a este servicio");
		}
		if (s.isFacturado()) {
			throw new ServicioException(genExc + "El servicio ya fue facturado");
		}
		s.setTiempoTrabajado(tiempoTrabajado);
	}

	public void anadirMaterialServicio(Servicio s, int cantidad, Articulo a)
			throws AsignacionException, ServicioException {
		String genExc = "No fue posible a�adir articulo: ";

		if (s.getTecnicos().contains(this) == false) {
			throw new AsignacionException(genExc + "No estas asignado a este servicio");
		}
		if (s.isFacturado()) {
			throw new ServicioException(genExc + "El servicio ya fue facturado");
		}

		s.anadirArticulo(cantidad, a);
	}

	public ArticuloExtra crearArticuloExtra(String descripcion, double costo) {
		return new ArticuloExtra(descripcion, costo);
	}

	public void anadirOtroMaterialServicio(Servicio s, int q, ArticuloExtra ax)
			throws AsignacionException, ServicioException {
		String genExc = "No fue posible a�adir articulo extra: ";

		if (s.getTecnicos().contains(this) == false) {
			throw new AsignacionException(genExc + "No estas asignado a este servicio");
		}
		if (s.isFacturado()) {
			throw new ServicioException(genExc + "El servicio ya fue facturado");
		}
		s.anadirOtroCostos(q, ax);
	}

	public void iniciarServicio(Servicio s) throws AsignacionException, ServicioException {
		String genExc = "No fue posible finalizar servicio: ";

		if (s.getTecnicos().contains(this) == false) {
			throw new AsignacionException(genExc + "No estas asignado a este servicio");
		}
		if (s.isFacturado()) {
			throw new ServicioException(genExc + "El servicio ya fue facturado");
		}

		if (s.getEstadoServicio() == EstadoServicio.FINALIZADO) {
			throw new ServicioException(genExc + "El servicio ya fue finalizado");
		}

		s.setEstadoServicio(EstadoServicio.EN_CURSO);
	}

	public void finalizarServicio(Servicio s) throws AsignacionException, ServicioException {
		String genExc = "No fue posible finalizar servicio: ";

		if (s.getTecnicos().contains(this) == false) {
			throw new AsignacionException(genExc + "No estas asignado a este servicio");
		}
		if (s.isFacturado()) {
			throw new ServicioException(genExc + "El servicio ya fue facturado");
		}

		if (s.getEstadoServicio() == EstadoServicio.FINALIZADO) {
			throw new ServicioException(genExc + "El servicio ya fue finalizado");
		}

		s.setEstadoServicio(EstadoServicio.FINALIZADO);
	}

}
