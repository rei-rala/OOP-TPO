package agenda;

import java.util.ArrayList;
import java.util.Date;

import personas.*;

public class Agenda {
	private final ArrayList<Dia> dias;
	private final Persona propietario;
	private int cantDias = 0;

	public Agenda(Persona propietario) {
		this.dias = new ArrayList<Dia>();
		this.propietario = propietario;

		// Perdon profe, por ahora puede quedar asi?
		// Inicializa con 15 dias desde fecha de hoy
		Date currentDate = new Date();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);

		final long DAY_IN_MS = 1000 * 60 * 60 * 24;

		for (int i = 0; i < 15; i++) {
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

	public boolean verificarDisponibilidad(Date fecha, Turno turno, int nroTurno) {
		for (Dia d : dias) {
			if (d.getFecha() == fecha) {
				try {
					d.verificarPreAsignacion(turno, nroTurno, nroTurno);
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		}
		this.dias.add(new Dia(fecha));
		return true;
	}

	@Override
	public String toString() {
		return "Agenda [propietario=" + propietario.getNombre() + ", cantDias=" + cantDias + "]";
	}

}