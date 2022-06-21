package comercial;

import java.util.ArrayList;

import empresa.Empresa;
import excepciones.StockException;

public class Articulo extends Recurso {
	public static ArrayList<Articulo> articulosBackup = new ArrayList<Articulo>();
	public static int conteoArticulos = 0;

	private final int SKU;
	private final TipoArticulo tipoArticulo;
	private double stock;

	public Articulo(String descripcion, double costo, TipoArticulo tipoArticulo, double stock) {
		super(descripcion, costo);
		this.SKU = ++conteoArticulos;
		this.tipoArticulo = tipoArticulo;
		this.stock = stock;

		Empresa.getInstance().agregarArticulo(this);
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock > 0 ? stock : 0;
	}

	public void anadirStock(double stockAdicional) throws StockException {
		if (0 > stockAdicional) {
			throw new StockException("Stock debe ser mayor a 0");
		}
		stock += stockAdicional;
	}

	public void consumirStock(double stockAUtilizar) throws StockException {
		if (stockAUtilizar > stock) {
			throw new StockException("Stock insuficiente");
		}
		if (0 > stockAUtilizar) {
			throw new StockException("Stock a consumir invalido");
		}
		stock -= stockAUtilizar;
	}

	public int getSKU() {
		return SKU;
	}

	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	@Override
	public String toString() {
		return "Articulo [SKU=" + SKU + ", tipoArticulo=" + tipoArticulo + ", stock=" + stock + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + SKU;
		result = prime * result + ((tipoArticulo == null) ? 0 : tipoArticulo.hashCode());
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
		if (tipoArticulo != other.tipoArticulo)
			return false;
		return true;
	}

}
