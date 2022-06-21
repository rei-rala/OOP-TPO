package comercial;

public class Costo {
	@Override
	public String toString() {
		return "Costo [cantidad=" + cantidad + ", articulo=" + articulo + "]";
	}

	private int cantidad;
	private Recurso articulo;

	public Costo(int cantidad, Recurso articulo) {
		this.cantidad = cantidad;
		this.articulo = articulo;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Recurso getArticulo() {
		return articulo;
	}

	public void setArticulo(Recurso articulo) {
		this.articulo = articulo;
	}

}
