package comercial;

import java.util.ArrayList;
import java.util.Objects;

public class Articulo extends Recurso {
	public static ArrayList<Articulo> articulosBackup = new ArrayList<Articulo>();
	public static int conteoArticulos = 0;

	private final int SKU;
	private double stock;

	public Articulo(String descripcion, double costo) {
		this.SKU = Articulo.conteoArticulos++;
		this.stock = 0;
	}

	public Articulo(String descripcion, double costo, double stock) {
		this.SKU = Articulo.conteoArticulos++;
		this.stock = stock > 0 ? stock : 0;
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
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
		result = prime * result + Objects.hash(SKU);
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
		return SKU == other.SKU;
	}

}
