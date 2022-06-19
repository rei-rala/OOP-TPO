package personas;

public class Tecnico extends Interno {
	private Seniority seniority;
	private final String AGENDA;

	public Tecnico(String nombre, long dni, String direccion, String telefono, String contrasena, Seniority seniority) {
		super(nombre, dni, direccion, telefono, contrasena);
		this.seniority = seniority;
		this.AGENDA = "XXXXXXXXX";
	}

	public Seniority getSeniority() {
		return seniority;
	}

	public void setSeniority(Seniority seniority) {
		this.seniority = seniority;
	}

	public String getAGENDA() {
		return AGENDA;
	}

	@Override
	public String toString() {
		return "Tecnico [seniority=" + seniority + ", legajo=" + legajo + ", nombre=" + nombre + ", dni=" + dni
				+ ", direccion=" + direccion + ", telefono=" + telefono + "]";
	}

}
