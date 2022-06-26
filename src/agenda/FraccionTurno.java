package agenda;

import comercial.*;

public class FraccionTurno {
	private Servicio servicioAsignado;
	private boolean estaOcupado;
	private final Dia dia;
	private final Turno turno;
	private final int nro;

	public FraccionTurno(Dia dia, Turno turno, int nro) {
		this.dia = dia;
		this.turno = turno;
		this.nro = nro;
	}

	public void asignarServicio(Servicio s) {
		servicioAsignado = s;
		estaOcupado = true;
	}

	public boolean getEstaOcupado() {
		return estaOcupado;
	}

	public Servicio getServicioAsignado() {
		return servicioAsignado;
	}

	public Servicio AsignarServicio(Servicio s) {
		if (!estaOcupado) {
			servicioAsignado = s;
			estaOcupado = true;

			return servicioAsignado;
		}
		return null;
	}

	public void marcarOcupado() {
		estaOcupado = true;
	}

	public Dia getDia() {
		return dia;
	}

	public int getNro() {
		return nro;
	}

	@Override
	public String toString() {
		return "FraccionTurno [servicioAsignado=" + servicioAsignado + ", estaOcupado=" + estaOcupado + ", diaSemana="
				+ dia.getDiaSemana() + ", turno=" + turno + ", nro=" + nro + "]";
	}

}
