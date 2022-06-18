
public class Administrativo extends Interno implements admArticulos {
	public String nombre;
	public int legajo;

	Administrativo(String nombre) {
		super(nombre);
		this.nombre = super.nombre;
		this.legajo = super.legajo;
	}
	

	@Override
	public String toString() {
		return "Administrativo [nombre=" + nombre + ", legajo=" + legajo + "]";
	}


	public void mostrarArticulos() {
		admArticulos.mostrar();
	}

	public void mostrarArticulos(int SKU) {
		admArticulos.mostrar(SKU);
	}
}
