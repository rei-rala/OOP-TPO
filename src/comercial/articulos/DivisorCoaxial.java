package comercial.articulos;

public class DivisorCoaxial extends Articulo {

	public DivisorCoaxial(double costo, int stock) {
		super("DivisorCoaxial de 1 a 2", costo, stock);
	}

	@Override
	public String toString() {
		return "DivisorCoaxial " + "[Desc=" + getDescripcion() + ", SKU=" + getSKU() + ", Stock=" + getStock()
				+ ", Costo=" + getCosto() + "]";
	}

}
