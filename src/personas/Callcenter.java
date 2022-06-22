package personas;

import java.util.ArrayList;
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

	// ARTICULOS
	public Articulo buscarArticulos(int SKU) {
		return Empresa.getInstance().getArticulos(SKU);
	}
	
	public Articulo buscarArticulos(Articulo a) {
		return Empresa.getInstance().getArticulos(a);
	}

	public ArrayList<Articulo> buscarArticulos() {
		return Empresa.getInstance().getArticulos();
	}

	public ArrayList<Articulo> buscarArticulosSinStock() {
		ArrayList<Articulo> articulos = buscarArticulos();
		ArrayList<Articulo> articulosSinStock = new ArrayList<Articulo>();

		for (int i = 0; i < articulos.size(); i++) {
			Articulo current = articulos.get(i);
			if (current.getStock() == 0) {
				articulosSinStock.add(current);
			}
		}

		return articulosSinStock;
	}

	public void anadirStock(Articulo a, int cantidad) throws StockException {
		a.anadirStock(cantidad);
	}

	public void editarStock(Articulo a, int cantidad) throws StockException {
		a.setStock(cantidad);
	}

	// TECNICOS
	// TODO: Merge con la agenda
	public ArrayList<Tecnico> buscarTecnicosDisponibles(Date fecha) {
		return Empresa.getInstance().getTecnicos();
	}

	// TODO: Merge con la agenda
	public ArrayList<Tecnico> buscarTecnicosDisponibles(Date fecha, int turno) {
		return Empresa.getInstance().getTecnicos();
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
}
