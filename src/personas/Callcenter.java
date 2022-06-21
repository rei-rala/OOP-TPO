package personas;

import java.util.Date;

import empresa.Empresa;
import comercial.*;
import excepciones.*;

public class Callcenter extends Interno {

	public Callcenter(String nombre, long dni, String direccion, String telefono, String contrasena) {
		super(nombre, dni, direccion, telefono, contrasena);
	}

	// ALTERNATIVO SIN DATOS
	public Callcenter(String nombre, String contrasena) {
		super(nombre, contrasena);
	}

	// SERVICIOS
	public Servicio crearNuevoServicioServicio(Cliente cliente, Date fecha, TipoServicio tipoServicio) {
		return new Servicio(cliente, fecha, tipoServicio);
	}

	public boolean asignarServicioATecnico(int nroServicio, int legajoTecnico) throws AsignacionException {
		Servicio s = Empresa.getInstance().getServicios(nroServicio);
		Tecnico t = Empresa.getInstance().getTecnicos(legajoTecnico);

		if (t == null || s == null) {
			throw new AsignacionException("No existe servicio o legajo de tecnico");
		}

		if (s.isFacturado()) {
			throw new AsignacionException("El servicio se encuentra facturado");
		}

		return s.anadirTecnico(t);
	}

	public boolean asignarServicioATecnico(Servicio serv, int legajoTecnico) throws AsignacionException {
		Servicio s = Empresa.getInstance().getServicios(serv);
		Tecnico t = Empresa.getInstance().getTecnicos(legajoTecnico);

		if (t == null || s == null) {
			throw new AsignacionException("No existe servicio o legajo de tecnico");
		}

		if (s.isFacturado()) {
			throw new AsignacionException("El servicio se encuentra facturado");
		}

		return s.anadirTecnico(t);
	}

	public boolean removerTecnicoAsignado(int nroServicio, int legajoTecnico) throws AsignacionException {
		Servicio s = Empresa.getInstance().getServicios(nroServicio);
		Tecnico t = Empresa.getInstance().getTecnicos(legajoTecnico);

		if (t == null || s == null) {
			throw new AsignacionException("No existe servicio o legajo de tecnico");
		}

		if (s.isFacturado()) {
			throw new AsignacionException("El servicio se encuentra facturado");
		}

		if (s.getTecnicos().contains(t) == false) {
			throw new AsignacionException("El tecnico no esta asignado a este servicio");
		}

		return s.anadirTecnico(t);
	}

	// ARTICULOS

	public static void anadirStock(Articulo a, int cantidad) throws StockException {
		a.anadirStock(cantidad);
	}
}
