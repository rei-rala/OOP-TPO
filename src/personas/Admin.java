package personas;

import java.util.ArrayList;

import comercial.articulos.*;
import empresa.CostoHorasTecnico;
import empresa.Empresa;
import excepciones.StockException;
import excepciones.ValorException;

public class Admin extends Interno {
	private static Empresa empresa = Empresa.getInstance();

	public Admin(String nombre, long dni, String direccion, String telefono, String contrasena) {
		super(nombre, dni, direccion, telefono, contrasena);
	}

	// ALTERNATIVO SIN DATOS
	public Admin(String nombre, String contrasena) {
		super(nombre, contrasena);
	}

	@Override
	public String toString() {
		return "Admin [legajo=" + legajo + ", contrasena=" + contrasena + " nombre=" + nombre + ", dni=" + dni
				+ ", direccion=" + direccion + ", telefono=" + telefono + "]";
	}

	public void modificarCostoCombustible(double nvoCostoCombustible) throws ValorException, Exception {
		empresa.setCostoCombustible(nvoCostoCombustible);
	}

	public void modificarCostoViaje(double nvoCostoViaje) throws ValorException, Exception {
		empresa.setCostoViaje(nvoCostoViaje);
	}

	public void modificarCostoHoraTecnico(CostoHorasTecnico nuevoCHTObject) throws ValorException {
		empresa.setCostoHoraTecnico(nuevoCHTObject);
	}

	public void modificarCostoHoraTecnico(Seniority seniority, double nuevoCHT) throws ValorException {
		empresa.setCostoHoraTecnico(seniority, nuevoCHT);
	}

	public void modificarCostoHoraTecnico(double jr, double ssr, double sr) throws ValorException {
		empresa.setCostoHoraTecnico(jr, ssr, sr);
	}

	// METODOS ARTICULOS
	public Articulo buscarArticulos(int SKU) {
		return empresa.getArticulos(SKU);
	}

	public Articulo buscarArticulos(Articulo a) {
		return empresa.getArticulos(a);
	}

	public ArrayList<Articulo> buscarArticulos() {
		return empresa.getArticulos();
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

	public static boolean quitarArticulo(Articulo a) {
		return empresa.removerArticulo(a);
	}
}
