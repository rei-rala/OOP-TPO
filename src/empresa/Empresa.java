package empresa;

import java.util.ArrayList;

import comercial.Articulo;
import personas.*;

public class Empresa {
	private static Empresa empresa;

	static ArrayList<Articulo> articulos = new ArrayList<Articulo>();
	static ArrayList<Interno> internos = new ArrayList<Interno>();
	static ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	static ArrayList<Tecnico> tecnicos = new ArrayList<Tecnico>();
	static double costoCombustible;
	static double costoViaje;
	static CostoHorasTecnico costoHorasTecnico = new CostoHorasTecnico();

	private Empresa() {
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
			if (interno.getContrasena() == contrasena) {
				loggeado = interno;
				break;
			}
		}

		return loggeado;
	}

	public double getCostoCombustible() {
		return costoCombustible;
	}

	public void setCostoCombustible(double nuevoCostoCombustible) {
		if (nuevoCostoCombustible > 0) {
			costoCombustible = nuevoCostoCombustible;
		}
	}

	public double getCostoViaje() {
		return costoViaje;
	}

	public void setCostoViaje(double nuevoCostoViaje) {
		if (nuevoCostoViaje > 0) {
			costoViaje = nuevoCostoViaje;
		}
	}

	public CostoHorasTecnico obtenerCostoHoraTecnico() {
		return costoHorasTecnico;
	}

	public double obtenerCostoHoraTecnico(Seniority seniority) {
		return costoHorasTecnico.obtenerCHT(seniority);
	}

	public void setCostoHoraTecnico(Seniority seniority, double nuevoCHT) {
		costoHorasTecnico.editarCHT(seniority, nuevoCHT);
	}

	public void setCostoHoraTecnico(CostoHorasTecnico nuevoCHTObject) {
		costoHorasTecnico = nuevoCHTObject;
	}
	// FIN metodos EMPRESA

	// Metodos: ARTICULOS
	public void agregarArticulo(Articulo a) {
		if (a != null) {
			articulos.add(a);
		}
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

	public Articulo getArticulos(Articulo articulo) {
		Articulo articuloEncontrado = null;
		boolean empresaContieneArt = articulos.contains(articulo);

		if (empresaContieneArt) {
			articuloEncontrado = articulo;
		}

		return articuloEncontrado;
	}

	public void agregarArtAEmpresa(Articulo articuloAAgregar) {
		boolean empresaContieneArt = articulos.contains(articuloAAgregar);

		if (articuloAAgregar != null) {
			return;
		}

		if (empresaContieneArt) {
			return;
		}

		articulos.add(articuloAAgregar);
	}

	public void editarArtEmpresa(Articulo articuloAEditar, String descripcion, double stock, double costo) {
		Articulo articulo = getArticulos(articuloAEditar);

		if (articulo != null) {
			articulo.setDescripcion(descripcion);
			articulo.setCosto(costo);
			articulo.setStock(stock);
		}
	}

	public void editarArtEmpresa(int SKU, String descripcion, double stock, double costo) {
		Articulo articulo = getArticulos(SKU);

		if (articulo != null) {
			articulo.setDescripcion(descripcion);
			articulo.setCosto(costo);
			articulo.setStock(stock);
		}
	}

	public void removerArticulo(int sku) {
		Articulo porBorrar = getArticulos(sku);

		if (porBorrar != null) {
			articulos.remove(sku);
		}
	}

	public void removerArticulo(Articulo articuloABorrar) {
		if (articuloABorrar != null) {
			articulos.remove(articuloABorrar);
		}
	}
	// FIN Metodos: ARTICULOS

	// Metodos: INTERNOS
	public void agregarInterno(Interno i) {
		if (i != null) {
			internos.add(i);
		}
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
	// FIN Metodos: INTERNOS

	// Metodos: TECNICOS
	public void agregarTecnico(Tecnico t) {
		if (t != null) {
			internos.add(t);
		}
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

	public ArrayList<Tecnico> getTecnicos() {
		return tecnicos;
	}

	public void removerTecnico(int legajo) {
		for (Tecnico t : tecnicos) {
			if (t.getLegajo() == legajo) {
				tecnicos.remove(t);
				internos.remove(t);
				break;
			}
		}
	}

	public void removerTecnico(Tecnico t) {
		tecnicos.remove(t);
		internos.remove(t);
	}
	// FIN Metodos: TECNICOS

	// Metodos: CLIENTES
	public void agregarCliente(Cliente c) {
		if (c != null) {
			clientes.add(c);
		}
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

	public void removerCliente(int nroCliente) {
		for (Cliente c : clientes) {
			if (c.getNro() == nroCliente) {
				clientes.remove(nroCliente);
				break;
			}
		}
	}

	public void removerCliente(Cliente c) {
		clientes.remove(c);
	}
	// FIN Metodos: CLIENTES

}
