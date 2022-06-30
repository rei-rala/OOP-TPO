package empresa;

import java.util.ArrayList;

import personas.*;
import comercial.*;
import comercial.articulos.*;
import excepciones.*;

public class Empresa {
	private static Empresa empresa;

	static ArrayList<Articulo> articulos = new ArrayList<Articulo>();
	static ArrayList<Interno> internos = new ArrayList<Interno>();
	static ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	static ArrayList<Tecnico> tecnicos = new ArrayList<Tecnico>();
	static ArrayList<Servicio> servicios = new ArrayList<Servicio>();
	static ArrayList<Factura> facturas = new ArrayList<Factura>();
	static double costoCombustible = 200;
	static double costoViaje = 150;
	static CostoHorasTecnico costoHorasTecnico = new CostoHorasTecnico();

	private final double RENTABILIDAD = 0.3;
	private final double IVA = 0.21;

	public double getRENTABILIDAD() {
		return RENTABILIDAD;
	}

	public double getIVA() {
		return IVA;
	}

	public Empresa() {
	}

	public static Empresa getInstance() {
		if (empresa == null) {
			empresa = new Empresa();
		}
		return empresa;
	}

	// Metodos EMPRESA
	public Interno login(int legajo, String contrasena) {
		Interno loggeado = null;

		for (Interno interno : internos) {
			if (interno.getLegajo() != legajo) {
				continue;
			}

			if (interno.getContrasena() == contrasena || interno.getContrasena().equals(contrasena)) {
				loggeado = interno;
				break;
			}
		}

		return loggeado;
	}

	@Override
	public String toString() {
		return "Empresa [getCostoCombustible()=" + getCostoCombustible() + ", getCostoViaje()=" + getCostoViaje()
				+ ", getCostoHoraTecnico()=" + getCostoHoraTecnico() + ", getArticulos()=" + getArticulos()
				+ ", getInternos()=" + getInternos() + ", getTecnicos()=" + getTecnicos() + ", getClientes()="
				+ getClientes() + "]";
	}

	public double redondear(double valor) {
		return (double) (Math.round(valor * 100.0) / 100.0);
	}

	public double getCostoCombustible() {
		return costoCombustible;
	}

	public void setCostoCombustible(double nuevoCostoCombustible) throws Exception {
		double nuevoCC = redondear(nuevoCostoCombustible);

		if (0 >= nuevoCC) {
			throw new ValorException("Nuevo costo combustible debe ser mayor a 0");
		}

		costoCombustible = nuevoCC;
	}

	public double getCostoViaje() {
		return costoViaje;
	}

	public void setCostoViaje(double nuevoCostoViaje) throws Exception {
		double nuevoCV = redondear(nuevoCostoViaje);

		if (0 >= nuevoCV) {
			throw new ValorException("Nuevo costo viaje  debe ser mayor a 0");
		}

		costoViaje = nuevoCV;
	}

	public CostoHorasTecnico getCostoHoraTecnico() {
		return costoHorasTecnico;
	}

	public double getCostoHoraTecnico(Seniority seniority) {
		return costoHorasTecnico.getCHT(seniority);
	}

	public void setCostoHoraTecnico(Seniority seniority, double nuevoCHT) throws ValorException {
		costoHorasTecnico.setCHT(seniority, nuevoCHT);
	}

	public void setCostoHoraTecnico(double jr, double ssr, double sr) throws ValorException {
		costoHorasTecnico.setJunior(jr);
		costoHorasTecnico.setSemiSenior(ssr);
		costoHorasTecnico.setSenior(sr);
	}

	public void setCostoHoraTecnico(CostoHorasTecnico nuevoCHTObject) {
		costoHorasTecnico = nuevoCHTObject;
	}
	// FIN metodos EMPRESA

	// Metodos: ARTICULOS

	// PREVIO CREACION SERVICIO
	public boolean verificarArticulosSuficientes(TipoServicio ts) {

		if (ts == TipoServicio.INSTALACION) {
			Articulo cable = Empresa.getInstance().getArticulo(Cable.class);
			Articulo decoTV = Empresa.getInstance().getArticulo(DecodificadorTV.class);
			Articulo modem = Empresa.getInstance().getArticulo(Modem.class);
			Articulo divCoax = Empresa.getInstance().getArticulo(DivisorCoaxial.class);
			Articulo conCoax = Empresa.getInstance().getArticulo(ConectorCoaxial.class);

			return (cable.getStock() > 2 && decoTV.getStock() > 0 && modem.getStock() > 0 && divCoax.getStock() > 0
					&& conCoax.getStock() > 5);
		}

		return true;
	}

	public ArrayList<Articulo> getArticulos() {
		return articulos;
	}

	public Articulo getArticulos(int sku) {
		Articulo articuloEncontrado = null;

		for (Articulo a : articulos) {
			if (a.getSKU() == sku) {
				articuloEncontrado = a;
				break;
			}
		}
		return articuloEncontrado;
	}

	public Articulo getArticulos(Class<Articulo> classArticulo) {
		Articulo articuloEncontrado = null;

		for (Articulo a : articulos) {
			if (a.getClass() == classArticulo) {
				articuloEncontrado = a;
				break;
			}
		}
		return articuloEncontrado;
	}

	public Articulo getArticulos(Articulo articulo) {
		Articulo articuloEncontrado = null;
		boolean empresaContieneArt = articulos.contains(articulo);

		if (empresaContieneArt) {
			articuloEncontrado = articulo;
		}

		return articuloEncontrado;
	}

	public Articulo getArticulo(Class<? extends Articulo> claseArticulo) {
		Articulo articuloEncontrado = null;

		for (Articulo a : articulos) {
			if (a.getClass() == claseArticulo) {
				return a;
			}
		}

		return articuloEncontrado;
	}

	public boolean agregarArticulo(Articulo a) {
		if (a == null || articulos.contains(a)) {
			return false;
		}

		return articulos.add(a);
	}

	public void editarArticulo(Articulo articuloAEditar, String descripcion, int stock, double costo) throws Exception {
		Articulo articulo = getArticulos(articuloAEditar);

		if (articulo != null) {
			articulo.setDescripcion(descripcion);
			articulo.setCosto(costo);
			articulo.setStock(stock);
		}
	}

	public void editarArticulo(int SKU, String descripcion, int stock, double costo) throws Exception {
		Articulo articulo = getArticulos(SKU);

		if (articulo != null) {
			articulo.setDescripcion(descripcion);
			articulo.setCosto(costo);
			articulo.setStock(stock);
		}
	}

	public boolean removerArticulo(int sku) {
		Articulo porBorrar = getArticulos(sku);
		return articulos.remove(porBorrar);
	}

	public boolean removerArticulo(Articulo articuloABorrar) {
		return articulos.remove(articuloABorrar);

	}
	// FIN Metodos: ARTICULOS

	// Metodos: SERVICIOS
	public ArrayList<Servicio> getServicios() {
		return servicios;
	}

	public Servicio getServicios(int nroServicio) {
		Servicio servicioEncontrado = null;

		for (Servicio s : servicios) {
			if (s.nro == nroServicio) {
				servicioEncontrado = s;
				break;
			}
		}
		return servicioEncontrado;
	}

	public Servicio getServicios(Servicio s) {
		Servicio servicioEncontrado = null;

		if (servicios.contains(s)) {
			servicioEncontrado = s;
		}

		return servicioEncontrado;
	}

	public boolean agregarServicio(Servicio s) {
		if (s == null || servicios.contains(s)) {
			return false;
		}
		return servicios.add(s);
	}

	public boolean removerServicio(Servicio s) {
		return servicios.remove(s);
	}

	public boolean removerServicio(int nroServicio) {
		Servicio i = getServicios(nroServicio);
		return servicios.remove(i);
	}
	// FIN Metodos: SERVICIOS

	// Metodos: FACTURAS
	public Factura getFacturas(int nroFactura) {
		Factura facturaEncontrada = null;

		for (Factura f : facturas) {
			if (f.getNro() == nroFactura) {
				facturaEncontrada = f;
				break;
			}
		}
		return facturaEncontrada;
	}

	public Factura getFacturas(Factura f) {
		Factura facturaEncontrada = null;

		if (facturas.contains(f)) {
			facturaEncontrada = f;
		}

		return facturaEncontrada;
	}

	public ArrayList<Factura> getFacturas() {
		return facturas;
	}

	public boolean agregarFactura(Factura f) {
		if (f == null || facturas.contains(f)) {
			return false;
		}
		return facturas.add(f);
	}

	public boolean removerFactura(Factura f) {
		return facturas.remove(f);
	}

	public boolean removerFactura(int nroFactura) {
		Factura i = getFacturas(nroFactura);
		return facturas.remove(i);
	}
	// FIN Metodos: FACTURAS

	// Metodos: INTERNOS
	public boolean agregarInterno(Interno i) {
		if (i == null || internos.contains(i)) {
			return false;
		}
		return internos.add(i);
	}

	public Interno getInternos(int legajo) {
		Interno internoEncontrado = null;

		for (Interno i : internos) {
			if (i.getLegajo() == legajo) {
				internoEncontrado = i;
				break;
			}
		}
		return internoEncontrado;
	}

	public ArrayList<Interno> getInternos() {
		return internos;
	}

	public boolean removerInterno(Interno i) {
		return internos.remove(i);
	}

	public boolean removerInterno(int legajo) {
		Interno i = getInternos(legajo);

		if (i.getClass() == Tecnico.class) {
			Tecnico it = (Tecnico) i;
			return removerTecnico(it);
		}
		return removerInterno(i);
	}
	// FIN Metodos: INTERNOS

	// Metodos: TECNICOS

	public ArrayList<Tecnico> getTecnicos() {
		return tecnicos;
	}

	public boolean removerTecnico(Tecnico t) {
		return tecnicos.remove(t) && internos.remove(t);
	}

	public Tecnico getTecnicos(int legajo) {
		Tecnico internoEncontrado = null;

		for (Tecnico t : tecnicos) {
			if (t.getLegajo() == legajo) {
				internoEncontrado = t;
				break;
			}
		}
		return internoEncontrado;
	}

	public boolean agregarTecnico(Tecnico t) {
		if (t == null || tecnicos.contains(t)) {
			return false;
		}
		return tecnicos.add(t);
	}

	public boolean removerTecnico(int legajo) {
		Tecnico t = getTecnicos(legajo);
		return removerTecnico(t);
	}

	// FIN Metodos: TECNICOS

	// Metodos: CLIENTES
	public boolean agregarCliente(Cliente c) {
		if (c == null || clientes.contains(c)) {
			return false;
		}
		return clientes.add(c);
	}

	public Cliente getClientes(int nroCliente) {
		Cliente clienteEncontrado = null;

		for (Cliente c : clientes) {
			if (c.getNro() == nroCliente) {
				clienteEncontrado = c;
				break;
			}
		}
		return clienteEncontrado;
	}

	public ArrayList<Cliente> getClientes() {
		return clientes;
	}

	public boolean removerCliente(Cliente c) {
		return clientes.remove(c);
	}

	public boolean removerCliente(int nroCliente) {
		Cliente c = getClientes(nroCliente);
		return removerCliente(c);
	}
	// FIN Metodos: CLIENTES

}
