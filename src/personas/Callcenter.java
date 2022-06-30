package personas;

import java.util.ArrayList;
import java.util.Date;

import agenda.*;
import empresa.Empresa;
import comercial.*;
import comercial.articulos.*;
import excepciones.*;
import main.DateAux;

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

	public Cliente getCliente(int numeroCliente) {
		return Empresa.getInstance().getClientes(numeroCliente);
	}

	public Tecnico getTecnicos(int numeroLegajo) {
		return Empresa.getInstance().getTecnicos(numeroLegajo);
	}

	public void anadirStockArticulo(Articulo a, int cantidad) throws Exception {
		a.anadirStock(cantidad);
	}

	public void setStockArticulo(Articulo a, int stock) throws Exception {
		a.setStock(stock);
	}

	// TECNICOS
	// TODO: Merge con la agenda
	public ArrayList<Tecnico> buscarTecnicosDisponibles(Date fecha) {
		return Empresa.getInstance().getTecnicos();
	}

	// TODO: Merge con la agenda
	public ArrayList<Tecnico> buscarTecnicosDisponibles(Date fecha, Turno turno, int desde, int hasta) {
		ArrayList<Tecnico> tecnicosDisponibles = new ArrayList<Tecnico>();

		for (Tecnico t : Empresa.getInstance().getTecnicos()) {
			try {
				t.getAgenda().verificarDisponibilidad(fecha, turno, desde, hasta);
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

	public boolean verificarDisponibilidadCliente(Cliente c, Date fecha, Turno t, int desde, int hasta) throws Exception {
		return c.verificarDisponibilidad(fecha, t, desde, hasta);
	}

	public Servicio crearNuevoServicioServicio(Date fecha, TipoServicio ts, Turno t, int desde,
			int hasta) throws Exception {
		double duracionServInicial = DateAux.calcularHoras(desde, hasta);

		if (fecha == null || ts == null || t == null) {
			throw new ServicioException("Verificar datos ingresados");
		}

		if (fecha.before(new Date())) {
			throw new Exception("La fecha no debe ser anterior a la actual");
		}

		if (0 >= fecha.getDay() || fecha.getDay() > 6) {
			throw new Exception("Dia no valido");
		} else if (desde >= 12 || hasta >= 12) {
			throw new Exception("El numero de turno es incorrecto");
		} else if (desde > hasta) {
			throw new Exception("La hora de inicio no puede ser mayor a la hora de fin");
		} else if (fecha.getDay() == 6 && t == Turno.TARDE) {
			throw new Exception("No se puede asignar un servicio a sabado a la tarde");
		}

		if (Empresa.getInstance().verificarArticulosSuficientes(ts) == false) {
			throw new StockException("Faltan articulos necesarios para crear nuevo servicio");
		}

		if (ts == TipoServicio.INSTALACION && 1 > duracionServInicial) {
			throw new ServicioException("Una reparacion debe durar al menos 1 hora");
		} else if (0 > duracionServInicial) {
			throw new ServicioException("Duracion de servicio no valida");
		}

		return new Servicio(fecha, ts, t, desde, hasta);
	}

	public boolean validarAgenda(Tecnico t, Date fecha, Turno turno, int desde, int hasta) throws Exception {
		return t.getAgenda().verificarDisponibilidad(fecha, turno, desde, hasta);
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
