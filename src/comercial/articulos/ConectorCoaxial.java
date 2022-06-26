package comercial.articulos;

public class ConectorCoaxial extends Articulo {

	public ConectorCoaxial(double costo, int stock) {
		super("Divisor Coaxial de 1 a 2", costo, stock);
	}

	@Override
	public String toString() {
		return "Cable [getStock()=" + getStock() + ", getSKU()=" + getSKU() + ", getDescripcion()=" + getDescripcion()
				+ ", getCosto()=" + getCosto() + "]";
	}

}
