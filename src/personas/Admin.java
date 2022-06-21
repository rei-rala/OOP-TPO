package personas;

import comercial.Articulo;
import comercial.TipoArticulo;
import empresa.CostoHorasTecnico;
import empresa.Empresa;
import excepciones.StockException;

public class Admin extends Interno {

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

	public void modificarCostoCombustible(double nvoCostoCombustible) {
		Empresa.getInstance().setCostoCombustible(nvoCostoCombustible);
	}

	public void modificarCostoViaje(double nvoCostoViaje) {
		Empresa.getInstance().setCostoViaje(nvoCostoViaje);
	}

	public static void modificarCostoHoraTecnico(CostoHorasTecnico nuevoCHTObject) {
		Empresa.getInstance().setCostoHoraTecnico(nuevoCHTObject);
	}

	public static void modificarCostoHoraTecnico(Seniority seniority, double nuevoCHT) {
		Empresa.getInstance().setCostoHoraTecnico(seniority, nuevoCHT);
	}

	// METODOS ARTICULOS
	public static Articulo crearArticulo(String descripcion, double costo, TipoArticulo tipoArticulo, double stock) {
		return new Articulo(descripcion, costo, tipoArticulo, stock);
	}

	public static boolean anadirArticulo(Articulo a) {
		return Empresa.getInstance().agregarArticulo(a);
	}

	public static void anadirStock(Articulo a, int cantidad) throws StockException {
		a.anadirStock(cantidad);
	}

	public static boolean quitarArticulo(int SKU) {
		return Empresa.getInstance().removerArticulo(SKU);
	}

	public static boolean quitarArticulo(Articulo a) {
		return Empresa.getInstance().removerArticulo(a);
	}
}
