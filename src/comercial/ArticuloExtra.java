package comercial;

public class ArticuloExtra extends Recurso {
	private String descripcion;
	private double costo;

	public ArticuloExtra(String descripcion, double costo) {
		this.descripcion = descripcion;
		this.costo = costo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

}
