package comercial;

import java.util.Objects;

import excepciones.CostoException;

public abstract class Recurso {

	private String descripcion;
	private double costo;

	public Recurso(String descripcion, double costo) {
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

	public void setCosto(double costo) throws CostoException {
		if (0 >= costo) {
			throw new CostoException("Nuevo costo debe ser mayor a 0");
		}
		this.costo = costo > 0 ? costo : 0;
	}

	@Override
	public String toString() {
		return "Recurso [descripcion=" + descripcion + ", costo=" + costo + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(costo, descripcion);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recurso other = (Recurso) obj;
		return Double.doubleToLongBits(costo) == Double.doubleToLongBits(other.costo)
				&& Objects.equals(descripcion, other.descripcion);
	}

}
