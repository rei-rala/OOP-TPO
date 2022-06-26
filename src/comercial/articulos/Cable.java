package comercial.articulos;

import excepciones.StockException;

public class Cable extends Articulo {

	public Cable(double costo, int stock) {
		super("Tramo 1.5m CABLE", costo, stock);
	}

	@Override
	public String toString() {
		return "Cable [getStock()=" + getStock() + ", getSKU()=" + getSKU() + ", getDescripcion()=" + getDescripcion()
				+ ", getCosto()=" + getCosto() + "]";
	}

}
