package comercial.articulos;

public class DivisorCoaxial extends Articulo {

	public DivisorCoaxial(double costo, int stock) {
		super("Cable", costo, stock);
	}

	@Override
	public String toString() {
		return "Cable [getStock()=" + getStock() + ", getSKU()=" + getSKU() + ", getDescripcion()=" + getDescripcion()
				+ ", getCosto()=" + getCosto() + "]";
	}

}
