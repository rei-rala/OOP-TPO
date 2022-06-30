package comercial.articulos;

public class Modem extends Articulo {

	public Modem(double costo, int stock) {
		super("Modem", costo, stock);
	}

	@Override
	public String toString() {
		return "Modem " + "[Desc=" + getDescripcion() + ", SKU=" + getSKU() + ", Stock=" + getStock()
				+ ", Costo=" + getCosto() + "]";
	}

}
