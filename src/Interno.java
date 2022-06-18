import java.util.ArrayList;

public class Interno extends Usuario {
	
	public static ArrayList<Interno> internosBackup = new ArrayList<Interno>();
	public static int conteoInternos = 0;
	public int legajo;
	public String nombre;
	
	public Interno(String nombre) {
		super(nombre);
		this.nombre = super.nombre;
		this.legajo = conteoInternos++;
		
		Interno.internosBackup.add(this);
		Empresa.internos.add(this);
	}

	public int getLegajo() {
		return legajo;
	}

	public void setLegajo(int legajo) {
		this.legajo = legajo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
