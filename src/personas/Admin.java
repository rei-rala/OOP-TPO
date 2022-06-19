package personas;

import empresa.CostoHorasTecnico;
import empresa.Empresa;

public class Admin extends Interno {

	public Admin(String nombre, long dni, String direccion, String telefono, String contrasena) {
		super(nombre, dni, direccion, telefono, contrasena);

	}

	public void modificarCostoCombustible(double nvoCostoCombustible) {
		Empresa.getInstance().setCostoCombustible(nvoCostoCombustible);
	}

	public void modificarCostoViaje(double nvoCostoViaje) {
		Empresa.getInstance().setCostoViaje(nvoCostoViaje);
	}

	public static void modificarCostoHoraTecnico(CostoHorasTecnico nuevoCHTObject) {
		Empresa.getInstance().setCostoHoraTecnico(nuevoCHTObject);
	}

	public static void modificarCostoHoraTecnico(Seniority seniority, double nuevoCHT) {
		Empresa.getInstance().setCostoHoraTecnico(seniority, nuevoCHT);
	}

	@Override
	public String toString() {
		return "Admin [legajo=" + legajo + ", nombre=" + nombre + ", dni=" + dni + ", direccion=" + direccion
				+ ", telefono=" + telefono + "]";
	}

}
