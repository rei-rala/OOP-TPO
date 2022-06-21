package personas;

import java.util.ArrayList;

import empresa.Empresa;

public abstract class Interno extends Persona {

	public static ArrayList<Interno> internosBackup = new ArrayList<Interno>();
	public static int conteoInternos = 0;
	public final int legajo;

	protected String contrasena;

	public Interno(String nombre, long dni, String direccion, String telefono, String contrasena) {
		super(nombre, dni, direccion, telefono);
		this.legajo = ++conteoInternos;
		this.contrasena = contrasena;

		Empresa.getInstance().agregarInterno(this);
	}

	// ALTERNATIVO SIN DATOS
	public Interno(String nombre, String contrasena) {
		super(nombre);
		this.legajo = ++conteoInternos;
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
