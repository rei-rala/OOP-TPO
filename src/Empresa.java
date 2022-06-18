import java.util.ArrayList;

public class Empresa {
	static ArrayList<Articulo> articulos = new ArrayList<Articulo>();
	static ArrayList<Interno> internos = new ArrayList<Interno>();
	static double costoCombustible;
	static double costoViaje;
	static CostoHorasTecnico costoHorasTecnico = new CostoHorasTecnico();

	public Empresa() {
	}

	public ArrayList<Articulo> obtenerArticulos() {
		return Empresa.articulos;
	}

	public ArrayList<Interno> obtenerInternos() {
		return Empresa.internos;
	}

	public CostoHorasTecnico obtenerCostoHoraTecnico() {
		return Empresa.costoHorasTecnico;
	}

	public double obtenerCostoHoraTecnico(Seniority seniority) {
		double costeObtenido = 0;

		if (seniority == Seniority.JUNIOR) {
			costeObtenido = costoHorasTecnico.getJunior();
		} else if (seniority == Seniority.SEMI_SENIOR) {
			costeObtenido = costoHorasTecnico.getSemiSenior();
		} else if (seniority == Seniority.SENIOR) {
			costeObtenido = costoHorasTecnico.getSenior();
		}

		return costeObtenido;
	}
}
