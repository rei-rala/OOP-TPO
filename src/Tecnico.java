
public class Tecnico extends Interno {
	public String nombre;
	public int legajo;
	public Seniority seniority;

	Tecnico(String nombre, Seniority seniority) {
		super(nombre);
		this.nombre = super.nombre;
		this.legajo = super.legajo;
		this.seniority = seniority;
	}

	@Override
	public String toString() {
		return "Tecnico [nombre=" + nombre + ", legajo=" + legajo + ", seniority=" + seniority + "]";
	}
}
