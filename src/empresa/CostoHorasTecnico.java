package empresa;

import excepciones.ValorException;
import personas.Seniority;

public class CostoHorasTecnico {

	// VALORES DE EJEMPLO
	private double junior = 50;
	private double semiSenior = 80;
	private double senior = 110;

	CostoHorasTecnico() {
	}

	@Override
	public String toString() {
		return "CostoHorasTecnico [junior=" + junior + ", semiSenior=" + semiSenior + ", senior=" + senior + "]";
	}

	public CostoHorasTecnico(double junior, double semiSenior, double senior) throws ValorException {

		if (0 >= junior || 0 >= semiSenior || 0 >= senior) {
			throw new ValorException("Nuevos CHT deben ser mayores a 0");
		}

		this.junior = junior;
		this.semiSenior = semiSenior;
		this.senior = senior;
	}

	public void editarCompleto(double nuevoJr, double nuevoSsr, double nuevoSr) throws ValorException {
		setJunior(nuevoJr);
		setSemiSenior(semiSenior);
		setSenior(nuevoSr);
	}

	public double getJunior() {
		return junior;
	}

	public void setJunior(double junior) throws ValorException {
		if (0 > junior) {
			throw new ValorException("Nuevo valor debe ser mayor a 0");
		}
		this.junior = junior;
	}

	public double getSemiSenior() {
		return semiSenior;
	}

	public void setSemiSenior(double semiSenior) throws ValorException {
		if (0 > semiSenior) {
			throw new ValorException("Nuevo valor debe ser mayor a 0");
		}
		this.semiSenior = semiSenior;
	}

	public double getSenior() {
		return senior;
	}

	public void setSenior(double senior) throws ValorException {
		if (0 > senior) {
			throw new ValorException("Nuevo valor debe ser mayor a 0");
		}
		this.senior = senior;
	}

	public double obtenerCHT(Seniority seniority) {
		double costeObtenido = -1;

		switch (seniority) {
		case JUNIOR:
			costeObtenido = getJunior();
			break;
		case SEMI_SENIOR:
			costeObtenido = getSemiSenior();
			break;
		case SENIOR:
			costeObtenido = getSenior();
			break;
		default:
			break;
		}

		return costeObtenido;
	}

	public void editarCHT(Seniority seniority, double nuevoCHT) throws ValorException {

		switch (seniority) {
		case JUNIOR:
			setJunior(nuevoCHT);
			break;
		case SEMI_SENIOR:
			setSemiSenior(nuevoCHT);
			break;
		case SENIOR:
			setSenior(nuevoCHT);
			break;
		default:
			break;
		}

	}
}
