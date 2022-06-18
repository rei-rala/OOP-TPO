
public interface admArticulos {

	public static Articulo crearArticulo(String titulo, double stock, double costo) {
		return new Articulo(titulo, stock, costo);
	}

	public static Articulo buscar(int SKU) {
		Articulo articuloEncontrado = null;

		for (Articulo a : Empresa.articulos) {
			if (a.getSKU() == SKU) {
				articuloEncontrado = a;
				break;
			}
		}
		return articuloEncontrado;
	}

	public static void mostrar() {
		System.out.println(Empresa.articulos);
	}

	public static void mostrar(int SKU) {
		Articulo articulo = admArticulos.buscar(SKU);

		if (articulo == null) {
			System.out.println("Articulo con SKU=" + SKU + " no encontrado");
			return;
		}

		System.out.println(articulo.toString());
	}

	public static void editar(int SKU, String titulo, double stock, double costo) {
		Articulo articulo = admArticulos.buscar(SKU);

		if (articulo != null) {
			articulo.setTitulo(titulo);
			articulo.setCosto(costo);
			articulo.setStock(stock);
		}
	}
	
	public static void eliminar(int SKU) {
		Articulo articulo = admArticulos.buscar(SKU);
		if (articulo != null) {
			Empresa.articulos.remove(articulo);
		}
	}
}
