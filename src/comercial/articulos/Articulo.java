package comercial.articulos;

import comercial.Recurso;
import empresa.Empresa;
import excepciones.StockException;

abstract public class Articulo extends Recurso {
	private static int contadorArticulos = 0;

	private final int SKU;
	private int stock;

	public Articulo(String descripcion, double costo, int stock) {
		super(descripcion, costo);
		this.SKU = ++contadorArticulos;
		this.stock = stock;

		Empresa.getInstance().agregarArticulo(this);
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) throws StockException {
		if (0 >= stock) {
			throw new StockException("Stock debe ser mayor a 0");
		}
		this.stock = stock;
	}

	public void anadirStock(int stockAdicional) throws StockException {
		if (0 > stockAdicional) {
			throw new StockException("Stock debe ser mayor a 0");
		}
		stock += stockAdicional;
	}

	public void consumirStock(int stockAUtilizar) throws StockException {
		if (stockAUtilizar > stock) {
			throw new StockException("Stock insuficiente");
		}
		if (0 > stockAUtilizar) {
			throw new StockException("Stock a consumir no valido");
		}
		stock -= stockAUtilizar;
	}

	public int getSKU() {
		return SKU;
	}

	@Override
	public String toString() {
		return "Articulo [SKU=" + SKU + ", stock=" + stock + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + SKU;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Articulo other = (Articulo) obj;
		if (SKU != other.SKU)
			return false;
		return true;
	}

}
