package comercial.articulos;

public class ConectorCoaxial extends Articulo {

	public ConectorCoaxial(double costo, int stock) {
		super("Divisor Coaxial de 1 a 2", costo, stock);
	}

	@Override
	public String toString() {
		return "ConectorCoaxial " + "[Desc=" + getDescripcion() + ", SKU=" + getSKU() + ", Stock=" + getStock() + ", Costo="
				+ getCosto() + "]";
	}
}
