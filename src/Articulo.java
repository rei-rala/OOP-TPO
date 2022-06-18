import java.util.ArrayList;

public class Articulo {
	public static ArrayList<Articulo> articulosBackup = new ArrayList<Articulo>();
	public static int conteoArticulos = 0;

	private int SKU;
	private String titulo;
	private double stock;
	private double costo;

	public Articulo(String titulo, double stock, double costo) {
		this.SKU = Articulo.conteoArticulos++;
		this.titulo = titulo;
		this.stock = stock;
		this.costo = costo;
		
		Articulo.articulosBackup.add(this);
		Empresa.articulos.add(this);
	}

	public int getSKU() {
		return SKU;
	}

	public String getDescripcion() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	@Override
	public String toString() {
		return "-Articulo\n\tSKU=" + SKU + "\n\ttitulo=" + titulo + "\n\tstock=" + stock + "\n\tcosto=" + costo + "\n";
	}

	public ArrayList<Articulo> listadoArticulos() {
		return Empresa.articulos;
	}

}
