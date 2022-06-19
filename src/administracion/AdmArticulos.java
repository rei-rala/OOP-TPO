package administracion;

import comercial.Articulo;

public interface AdmArticulos {

	public static Articulo crearArticulo(String titulo, double stock, double costo) {
		return new Articulo(titulo, stock, costo);
	}

	public static void anadirArtEmpresa(Articulo art) {
	}

	public static void editarArtEmpresa(int SKU, String titulo, double stock, double costo) {
	}

	public static void editarArtEmpresa(Articulo art, String Titulo, double stock, double costo) {
	}

	public static void quitarArtEmpresa(int SKU) {
	}

	public static void quitarArtEmpresa(Articulo art) {
	}

}
