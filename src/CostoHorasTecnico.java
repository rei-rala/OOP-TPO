
public class CostoHorasTecnico {
	double junior = 50;
	double semiSenior = 80;
	double senior = 110;

	CostoHorasTecnico() {
	}

	CostoHorasTecnico(double junior, double semiSenior, double senior) {
		this.junior = junior;
		this.semiSenior = semiSenior;
		this.senior = senior;
	}

	public void editarCompleto(double nuevoJr, double nuevoSsr, double nuevoSr) {
		this.junior = nuevoJr;
		this.semiSenior = nuevoSsr;
		this.senior = nuevoSr;
	}

	public double getJunior() {
		return junior;
	}

	public void setJunior(double junior) {
		this.junior = junior;
	}

	public double getSemiSenior() {
		return semiSenior;
	}

	public void setSemiSenior(double semiSenior) {
		this.semiSenior = semiSenior;
	}

	public double getSenior() {
		return senior;
	}

	public void setSenior(double senior) {
		this.senior = senior;
	}

	
}
