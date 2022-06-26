package comercial.articulos;

public class Modem extends Articulo {

	public Modem(double costo, int stock) {
		super("Modem", costo, stock);
	}

	@Override
	public String toString() {
		return "Cable [getStock()=" + getStock() + ", getSKU()=" + getSKU() + ", getDescripcion()=" + getDescripcion()
				+ ", getCosto()=" + getCosto() + "]";
	}

}
