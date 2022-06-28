package comercial.articulos;

public class Cable extends Articulo {

	public Cable(double costo, int stock) {
		super("Tramo 1.5m CABLE", costo, stock);
	}

	@Override
	public String toString() {
		return "Cable " + "[Desc=" + getDescripcion() + ", SKU=" + getSKU() + " Stock" + getStock() + ", Costo="
				+ getCosto() + "]";
	}

}
