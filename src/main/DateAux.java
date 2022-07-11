package main;

import agenda.Turno;
import comercial.Servicio;

import java.util.Date;

@SuppressWarnings("deprecation")
public class DateAux {
  private static DateAux instancia;

  private DateAux() {
  }

  public static DateAux getInstance() {
    if (instancia == null) {
      instancia = new DateAux();
    }
    return instancia;
  }

  public String getNombreDiaSemana(Date fecha) {
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

  public Date getStartDay(Date d) {
    Date f = d;
    f.setHours(0);
    f.setMinutes(0);
    f.setSeconds(0);

    return f;
  }

  public Date getStartDay() {
    return getStartDay(new Date());
  }

  public Date getNow() {
    return new Date();
  }

  public Date getToday() {
    return getStartDay(getNow());
  }

  public Date getNextDay(Date d) {
    Date f = new Date(d.getTime());
    int newDate = f.getDate() + 1;
    f.setDate(newDate);

    return getStartDay(f);
  }

  public Date getNextDay() {
    return getNextDay(new Date());
  }

  public double calcularHoras(int turnoDesde, int turnoHasta) {
    return (turnoHasta - turnoDesde + 1) / 2 + 0.5;
  }

  public String getMinutos(int nroTurno) {
    return nroTurno % 2 == 0 ? "00" : "30";
  }

  public String getHorasFormat(double tiempoTrabajado) {
    double correccion = tiempoTrabajado - 0.5;
    return "" + (int) correccion + ":" + (correccion % 1 == 0 ? "00" : "30");

  }

  public String getHoraManana(int nroTurno) {
    int h = (int) 8 + nroTurno / 2;
    return 10 > h ? "0" + h : "" + h;
  }

  public String getHoraTarde(int nroTurno) {
    return "" + ((int) 14 + nroTurno / 2);
  }

  public String getHorarioUnico(Turno turno, int nroTurno) {
    return (turno == Turno.MANANA ? getHoraManana(nroTurno) : getHoraTarde(nroTurno)) + ":" + getMinutos(nroTurno);
  }

  public String getHorarioCompleto(Turno turno, int inicioTurno, int finalTurno) {
    int finalTno = finalTurno + 1;
    String inicioTurnoStr = (turno == Turno.MANANA ? getHoraManana(inicioTurno) : getHoraTarde(inicioTurno)) + ":"
        + getMinutos(inicioTurno);
    String finalTurnoStr = (turno == Turno.MANANA ? getHoraManana(finalTno) : getHoraTarde(finalTno)) + ":"
        + getMinutos(finalTno);
    return inicioTurnoStr + "-" + finalTurnoStr;
  }

  public String getHorarioCompleto(Servicio s) {
    return getHorarioCompleto(s.getTurno(), s.getTurnoInicio(), s.getTurnoFin());
  }

  public String getDateString(Date d) {
    int dia = d.getDate();
    int mes = d.getMonth() + 1;
    int anio = 1900 + d.getYear();

    return "" + dia + "/" + mes + "/" + anio;
  }
}
