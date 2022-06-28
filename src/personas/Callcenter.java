package personas;

import java.util.ArrayList;
import java.util.Date;

import agenda.*;
import empresa.Empresa;
import comercial.*;
import comercial.articulos.*;
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

	public void anadirStockArticulo(Articulo a, int cantidad) throws Exception {
		a.anadirStock(cantidad);
	}

	public void setStockArticulo(Articulo a, int stock) throws Exception {
		a.setStock(stock);
	}

	public void setCostoArticulo(Articulo a, double costo) throws Exception {
		a.setCosto(costo);
	}

	// TECNICOS
	// TODO: Merge con la agenda
	public ArrayList<Tecnico> buscarTecnicosDisponibles(Date fecha) {
		return Empresa.getInstance().getTecnicos();
	}

	// TODO: Merge con la agenda
	public ArrayList<Tecnico> buscarTecnicosDisponibles(Date fecha, Turno turno, int nroTurno) {
		ArrayList<Tecnico> tecnicosDisponibles = new ArrayList<Tecnico>();

		for (Tecnico t : Empresa.getInstance().getTecnicos()) {
			try {
				t.getAgenda().verificarDisponibilidad(fecha, turno, nroTurno);
				tecnicosDisponibles.add(t);
			} catch (Exception e) {
			}
		}
		return tecnicosDisponibles;
	}

	// SERVICIOS
	public void asignarServicioACliente(Cliente c, Servicio s, Date fecha, Turno t, int desde, int hasta)
			throws Exception {

		if (c == null || s == null) {
			throw new AsignacionException("No existe servicio o legajo de tecnico");
		}

		if (s.isFacturado()) {
			throw new AsignacionException("El servicio se encuentra facturado");
		}

		c.asignarServicio(s, fecha, t, desde, hasta);
	}

	public void asignarServicioACliente(Cliente c, Servicio s, Date fecha, Turno t, int nroTurno) throws Exception {
		asignarServicioACliente(c, s, fecha, t, nroTurno, nroTurno);
	}

	public Servicio crearNuevoServicioServicio(Cliente cliente, Date fecha, TipoServicio ts, Turno t, int desde,
			int hasta) throws Exception {

		if (Empresa.getInstance().verificarArticulosSuficientes(ts) == false) {
			throw new StockException("Faltan articulos necesarios para crear nuevo servicio");
		}
		double duracionServInicial = DateAux.calcularHoras(desde, hasta);
		if (0 > duracionServInicial) {
			throw new ServicioException("Duracion de servicio no valida");
		}
		cliente.verificarDisponibilidad(fecha, t, desde, hasta);
		Servicio s = new Servicio(cliente, fecha, ts, duracionServInicial);
		asignarServicioACliente(cliente, s, fecha, t, desde, hasta);
		return s;
	}

	public void asignarServicioATecnico(Servicio s, Tecnico t) throws Exception {

		if (t == null || s == null) {
			throw new AsignacionException("No existe servicio o legajo de tecnico");
		}

		if (s.getTecnicos().contains(t)) {
			throw new AsignacionException("El tecnico ya se encuentra asignado a este servicio");
		}

		if (s.isFacturado()) {
			throw new AsignacionException("El servicio se encuentra facturado");
		}

		s.anadirTecnico(t);
	}

	public void asignarServicioATecnico(int nroServicio, int legajoTecnico) throws Exception {
		Servicio s = Empresa.getInstance().getServicios(nroServicio);
		Tecnico t = Empresa.getInstance().getTecnicos(legajoTecnico);

		asignarServicioATecnico(s, t);
	}

	public void asignarServicioATecnico(Servicio serv, int legajoTecnico) throws Exception {
		Servicio s = Empresa.getInstance().getServicios(serv);
		Tecnico t = Empresa.getInstance().getTecnicos(legajoTecnico);

		asignarServicioATecnico(s, t);
	}

}
