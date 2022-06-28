package agenda;

import java.util.Date;

import empresa.Empresa;

public class DateAux {

	public static Date getNow() {
		return new Date();
	}

	public static Date getToday() {
		return getStartDay(getNow());
	}

	public static Date getStartDay(Date d) {
		Date f = d;
		f.setHours(0);
		f.setMinutes(0);
		f.setSeconds(0);

		return f;
	}

	public static Date getNextDay(Date d) {
		long inputMilliseconds = getStartDay(d).getTime();
		return new Date(inputMilliseconds + (1000 * 60 * 60 * 24));
	}

	public static double calcularHoras(int turnoDesde, int turnoHasta) {
		return (turnoHasta - turnoDesde) / 2;
	}
}
