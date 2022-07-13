package personas;

import empresa.Empresa;
import main.DateAux;

public abstract class Interno extends Persona {

	private static int contadorInternos = 0;
	
	protected final int legajo;
	protected String contrasena;

  protected DateAux dateAux = DateAux.getInstance();

	public Interno(String nombre, long dni, String direccion, String telefono, String contrasena) {
		super(nombre, dni, direccion, telefono);
		this.legajo = ++contadorInternos;
		this.contrasena = contrasena;

		Empresa.getInstance().agregarInterno(this);
	}

	// ALTERNATIVO SIN DATOS
	public Interno(String nombre, String contrasena) {
		super(nombre);
		this.legajo = ++contadorInternos;
		this.contrasena = contrasena;

		Empresa.getInstance().agregarInterno(this);
	}

	public int getLegajo() {
		return legajo;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	@Override
	public String toString() {
		return "Interno [legajo=" + legajo + " PASS: " + ", nombre=" + nombre + ", dni=" + dni + ", direccion=" + direccion
				+ ", telefono=" + telefono + "]";
	}

}
