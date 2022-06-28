package agenda;

import comercial.*;
import excepciones.AsignacionException;

public class FraccionTurno {
	private Servicio servicioAsignado;
	private boolean estaOcupado;
	private final Dia dia;
	private final Turno turno;
	private final int nro;
	private String horario;

	public FraccionTurno(Dia dia, Turno turno, int nro) {
		this.dia = dia;
		this.turno = turno;
		this.nro = nro;
		this.horario = obtenerHorario();
	}

	private String getMinutos() {
		return nro % 2 == 0 ? "00" : "30";
	}

	private String getHoraManana() {
		int h = (int) 8 + nro / 2;
		return 10 > h ? "0" + h : "" + h;
	}

	private String getHoraTarde() {
		return "" + ((int) 14 + nro / 2);
	}

	private String obtenerHorario() {
		return (turno == Turno.MANANA ? getHoraManana() : getHoraTarde()) + ":" + getMinutos();
	}

	public String getHorario() {
		return horario;
	}

	public void asignarServicio(Servicio s) throws AsignacionException {
		if (estaOcupado) {
			throw new AsignacionException("La fraccion del turno ya cuenta con asignacion");
		}
		servicioAsignado = s;
		marcarOcupado();
	}

	public boolean getEstaOcupado() {
		return estaOcupado;
	}

	public Servicio getServicioAsignado() {
		return servicioAsignado;
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
	
	public Turno getTurno() {
		return turno;
	}

	@Override
	public String toString() {
		return "FraccionTurno [servicioAsignado=" + servicioAsignado + ", estaOcupado=" + estaOcupado + ", diaSemana="
				+ dia.getDiaSemana() + ", turno=" + turno + ", horario=" + horario + ", nro=" + nro + "]";
	}

}
