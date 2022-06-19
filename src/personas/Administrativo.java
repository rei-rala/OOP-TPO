package personas;

public class Administrativo extends Interno {

	public Administrativo(String nombre, long dni, String direccion, String telefono, String contrasena) {
		super(nombre, dni, direccion, telefono, contrasena);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Administrativo [legajo=" + legajo + ", nombre=" + nombre + ", dni=" + dni + ", direccion=" + direccion
				+ ", telefono=" + telefono + "]";
	}

}
