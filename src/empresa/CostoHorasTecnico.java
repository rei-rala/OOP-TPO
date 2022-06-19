package empresa;

import personas.Seniority;

public class CostoHorasTecnico {

	// VALORES DE EJEMPLO
	private double junior = 50;
	private double semiSenior = 80;
	private double senior = 110;

	CostoHorasTecnico() {
	}

	CostoHorasTecnico(double junior, double semiSenior, double senior) {
		this.junior = junior > 0 ? junior : 0;
		this.semiSenior = semiSenior > 0 ? semiSenior : 0;
		this.senior = senior > 0 ? senior : 0;
	}

	public void editarCompleto(double nuevoJr, double nuevoSsr, double nuevoSr) {
		this.junior = junior > 0 ? junior : 0;
		this.semiSenior = semiSenior > 0 ? semiSenior : 0;
		this.senior = senior > 0 ? senior : 0;
	}

	public double getJunior() {
		return junior;
	}

	public void setJunior(double junior) {
		if (junior > 0) {
			this.junior = junior;
		}
	}

	public double getSemiSenior() {
		return semiSenior;
	}

	public void setSemiSenior(double semiSenior) {
		if (semiSenior > 0) {
			this.semiSenior = semiSenior;
		}
	}

	public double getSenior() {
		return senior;
	}

	public void setSenior(double senior) {
		if (senior > 0) {
			this.senior = senior;
		}
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

	public void editarCHT(Seniority seniority, double nuevoCHT) {

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
