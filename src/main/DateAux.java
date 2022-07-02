package main;

import agenda.Turno;
import comercial.Servicio;

import java.util.Date;

@SuppressWarnings("deprecation")
public class DateAux {

  public static final long DAY_IN_MS = 1000 * 60 * 60 * 24;

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
    return (turnoHasta - turnoDesde + 1) / 2 + 0.5;
  }

  public static String getMinutos(int nroTurno) {
    return nroTurno % 2 == 0 ? "00" : "30";
  }

  public static String getHoraManana(int nroTurno) {
    int h = (int) 8 + nroTurno / 2;
    return 10 > h ? "0" + h : "" + h;
  }

  public static String getHoraTarde(int nroTurno) {
    return "" + ((int) 14 + nroTurno / 2);
  }

  public static String getHorarioUnico(Turno turno, int nroTurno) {
    return (turno == Turno.MANANA ? getHoraManana(nroTurno) : getHoraTarde(nroTurno)) + ":" + getMinutos(nroTurno);
  }

  public static String getHorarioCompleto(Turno turno, int inicioTurno, int finalTurno) {
    int finalTno = finalTurno + 1;
    String inicioTurnoStr = (turno == Turno.MANANA ? getHoraManana(inicioTurno) : getHoraTarde(inicioTurno)) + ":"
        + getMinutos(inicioTurno);
    String finalTurnoStr = (turno == Turno.MANANA ? getHoraManana(finalTno) : getHoraTarde(finalTno)) + ":"
        + getMinutos(finalTno);
    return inicioTurnoStr + "-" + finalTurnoStr;
  }

  public static String getHorarioCompleto(Servicio s) {
    return getHorarioCompleto(s.getTurno(), s.getTurnoInicio(), s.getTurnoFin());
  }

  public static String getDateString(Date d) {
    int dia = d.getDate();
    int mes = d.getMonth()+1;
    int anio = 1900 + d.getYear();

    return "" + dia + "/" + mes + "/" + anio;
  }
}
