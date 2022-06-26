package agenda;

import java.util.ArrayList;
import java.util.Date;

import comercial.Servicio;
import excepciones.AgendaException;

public class Dia {
	private final Date fecha;
	private final String diaSemana;
	private final ArrayList<FraccionTurno> turnoManana;
	private final ArrayList<FraccionTurno> turnoTarde;

	public Dia(Date fecha) {
		this.fecha = fecha;
		this.diaSemana = getNombreDiaSemana();
		this.turnoManana = new ArrayList<FraccionTurno>();
		this.turnoTarde = new ArrayList<FraccionTurno>();

		poblarEspaciosTurnos();
	}

	private void poblarEspaciosTurnos() {
		int nroDia = fecha.getDay();

		for (int i = 0; i < 12; i++) {
			turnoManana.add(new FraccionTurno(this, Turno.MANANA, i));

			if (nroDia > 0 && 6 > nroDia) {
				turnoTarde.add(new FraccionTurno(this, Turno.TARDE, i));
			}
		}
	}

	public Date getFecha() {
		return fecha;
	}

	public String getDiaSemana() {
		return diaSemana;
	}

	public ArrayList<FraccionTurno> obtenerFraccionesTurno() {
		ArrayList<FraccionTurno> diaCompletoTurnos = turnoManana;

		turnoManana.addAll(turnoTarde);

		return diaCompletoTurnos;
	}

	public ArrayList<FraccionTurno> verTurnoManana() {
		return turnoManana;
	}

	public ArrayList<FraccionTurno> verTurnoTarde() {
		return turnoTarde;
	}

	public ArrayList<FraccionTurno> obtenerFraccionesTurno(Turno t) {
		if (t == Turno.MANANA) {
			return verTurnoManana();
		} else {
			return verTurnoTarde();
		}
	}

	public String getNombreDiaSemana() {
		int nroDia = fecha.getDay();

		switch (nroDia) {
		case 0:
			return "DOMINGO";
		case 1:
			return "LUNES";
		case 2:
			return "MARTES";
		case 3:
			return "MIERCOLES";
		case 4:
			return "JUEVES";
		case 5:
			return "VIERNES";
		case 6:
			return "SABADO";
		default:
			return "error";
		}
	}

	private boolean validarSeleccionTurnos(Turno t, int desde, int hasta) {
		if (desde > hasta || 0 > desde || 0 > hasta) {
			return false;
		}

		if (t == Turno.MANANA) {
			if (desde > turnoManana.size() || hasta > turnoManana.size()) {
				return false;
			}
		} else {
			if (desde > turnoManana.size() || hasta > turnoManana.size()) {
				return false;
			}
		}

		return true;
	}

	private boolean estanTurnosOcupados(Turno t, int desde, int hasta) {
		boolean ocupado = false;

		if (t == Turno.MANANA) {
			for (int i = desde; i < hasta; i++) {
				FraccionTurno currentFR = turnoManana.get(i);
				if (currentFR.getEstaOcupado()) {
					ocupado = true;
					break;
				}
			}
		} else {
			for (int i = desde; i < hasta; i++) {
				FraccionTurno currentFR = turnoTarde.get(i);
				if (currentFR.getEstaOcupado()) {
					ocupado = true;
					break;
				}
			}
		}

		return ocupado;
	}

	private void asignarServicioTurnos(Turno t, int desde, int hasta, Servicio s) {
		if (t == Turno.MANANA) {
			for (int i = desde; i < hasta; i++) {
				FraccionTurno currentFR = turnoManana.get(i);
				currentFR.asignarServicio(s);
			}
		} else {
			for (int i = desde; i < hasta; i++) {
				FraccionTurno currentFR = turnoTarde.get(i);
				currentFR.asignarServicio(s);
			}
		}
	}

	public void verificarPreAsignacion(Turno turno, int desde, int hasta) throws AgendaException {
		if (validarSeleccionTurnos(turno, desde, hasta) == false) {
			throw new AgendaException("Verifique valores de turno ingresados");
		}
		if (estanTurnosOcupados(turno, desde, hasta)) {
			throw new AgendaException("Turno/s ya ocupado/s");
		}
	}

	public void asignarFraccionesTurnos(Turno turno, int desde, int hasta, Servicio s) throws AgendaException {
		try {
			verificarPreAsignacion(turno, desde, hasta);
			asignarServicioTurnos(turno, desde, hasta, s);
		} catch (AgendaException e) {
			throw e;
		} catch (Exception e) {
			throw new AgendaException("Error NOT HANDLED:\n" + e);
		}
	}

	public FraccionTurno obtenerSiguienteFraccionDisponible() {
		for (FraccionTurno fr : obtenerFraccionesTurno()) {
			if (fr.getEstaOcupado() == false) {
				return fr;
			}
		}
		return null;
	}

	public FraccionTurno obtenerSiguienteFraccionDisponible(Turno t) {
		ArrayList<FraccionTurno> turnoSeleccionado = obtenerFraccionesTurno(t);

		for (FraccionTurno fr : turnoSeleccionado) {
			if (fr.getEstaOcupado() == false) {
				return fr;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Dia [diaSemana=" + diaSemana + ", turnoManana=" + turnoManana + ", turnoTarde=" + turnoTarde + "]";
	}

}
