package comercial.articulos;

public class DecodificadorTV extends Articulo {

	public DecodificadorTV(double costo, int stock) {
		super("Decodificador de TV", costo, stock);
	}

	@Override
	public String toString() {
		return "Cable [getStock()=" + getStock() + ", getSKU()=" + getSKU() + ", getDescripcion()=" + getDescripcion()
				+ ", getCosto()=" + getCosto() + "]";
	}

}
