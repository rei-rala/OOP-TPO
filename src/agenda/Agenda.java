package agenda;

import java.util.ArrayList;
import java.util.Date;

import comercial.Servicio;
import excepciones.AsignacionException;
import main.DateAux;
import personas.*;

@SuppressWarnings("deprecation")
public class Agenda {
	private final ArrayList<Dia> dias;
	private final Persona propietario;
	private int cantDias = 0;

	public Agenda(Persona propietario) {
		this.dias = new ArrayList<Dia>();
		this.propietario = propietario;
		Date currentDate = DateAux.getToday();

		final long DAY_IN_MS = 1000 * 60 * 60 * 24;

		for (int i = 0; i < 30; i++) {
			// si es domingo lo omitimos
			if (currentDate.getDay() == 0) {
				continue;
			}
			dias.add(new Dia(currentDate));
			currentDate = new Date(currentDate.getTime() + DAY_IN_MS);
		}

		this.cantDias = dias.size();
	}

	public int getCantDias() {
		return cantDias;
	}

	public ArrayList<Dia> getDias() {
		return dias;
	}

	public Persona getPropietario() {
		return propietario;
	}

	public Dia obtenerDiaAgenda(Date fecha) {
		Date f = DateAux.getStartDay(fecha);
		Dia encontrado = null;

		for (Dia d : dias) {
			if (d.getFecha() == fecha) {
				encontrado = d;
				break;
			}
		}

		if (encontrado == null && f.getDay() != 0) {
			Dia nuevoDia = new Dia(f);
			dias.add(nuevoDia);
			return nuevoDia;
		}

		return encontrado;
	}

	public void asignarServicio(Servicio s, Date fecha, Turno t, int desde, int hasta) throws Exception {
		Dia aAsignar = obtenerDiaAgenda(fecha);

		for (int i = desde; i <= hasta; i++) {
			FraccionTurno ft = aAsignar.obtenerFraccionesTurno(i, t);
			if (ft == null) {
				throw new AsignacionException("Verifique datos de turno");
			}

			if (ft.getEstaOcupado()) {
				throw new AsignacionException("El turno se encuentra ocupado");
			}
		}

		for (int i = desde; i <= hasta; i++) {
			FraccionTurno ft = aAsignar.obtenerFraccionesTurno(i, t);
			ft.asignarServicio(s);
		}
	}

	public boolean verificarDisponibilidad(Date fecha, Turno turno, int desde, int hasta) {
		try {
			Dia d = obtenerDiaAgenda(fecha);
			if (d == null) {
				throw new Exception("Dia no valido");
			}
			return d.verificarDisponibilidad(turno, desde, hasta);
		} catch (Exception e) {
			return false;
		}
	}

	public FraccionTurno obtenerTurnoDisponible() {
		FraccionTurno disponible = null;

		for (Dia d : dias) {
			FraccionTurno turnoDiaDisponible = d.obtenerSiguienteFraccionDisponible();

			if (turnoDiaDisponible == null) {
				continue;
			}
			disponible = turnoDiaDisponible;
		}

		return disponible;
	}

	public ArrayList<FraccionTurno> obtenerTodosTurnosDisponible(Turno t) {
		ArrayList<FraccionTurno> disponibles = new ArrayList<FraccionTurno>();

		for (Dia d : dias) {
			for (FraccionTurno ft : d.obtenerTodosTurnoDisponibles(t)) {
				disponibles.add(ft);
			}
		}

		return disponibles;
	}

	public ArrayList<FraccionTurno> obtenerTodosTurnosDisponible() {
		ArrayList<FraccionTurno> disponibles = new ArrayList<FraccionTurno>();

		for (Dia d : dias) {
			for (FraccionTurno ft : d.obtenerTodosTurnoDisponibles()) {
				disponibles.add(ft);
			}
		}

		return disponibles;
	}

	public boolean verificarServicioVigente() {
		Date today = DateAux.getToday();
		boolean ocupado = false;

		for (Dia d : getDias()) {
			if (today.after(d.getFecha())) {
				continue;
			}

			if (d.verificarDiaOcupado()) {
				ocupado = true;
				break;
			}
		}
		return ocupado;
	}

	@Override
	public String toString() {
		return "Agenda [propietario=" + propietario.getNombre() + ", cantDias=" + cantDias + "]";
	}

}