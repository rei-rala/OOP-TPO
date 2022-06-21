package personas;

public abstract class Persona {
	static int dniCounter = 0;

	protected String nombre;
	protected final long dni;
	protected String direccion;
	protected String telefono;

	public Persona(String nombre, long dni, String direccion, String telefono) {
		this.nombre = nombre;
		this.dni = dni;
		this.direccion = direccion;
		this.telefono = telefono;
	}

	// ALTERNATIVO SIN DATOS
	public Persona(String nombre) {
		this.nombre = nombre;
		this.dni = ++dniCounter;
		this.direccion = "dir" + this.dni;
		this.telefono = "tel" + this.dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getDni() {
		return dni;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return "Persona [nombre=" + nombre + ", dni=" + dni + ", direccion=" + direccion + ", telefono=" + telefono
				+ "]";
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
