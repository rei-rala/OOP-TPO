package agenda;

import java.util.ArrayList;
import java.util.Date;

import comercial.Servicio;
import excepciones.AgendaException;
import excepciones.AsignacionException;
import excepciones.ServicioException;
import main.DateAux;

@SuppressWarnings("deprecation")
public class Dia {
  private final Date fecha;
  private final String fechaString;
  private final String diaSemana;
  private final ArrayList<FraccionTurno> turnos;

  public Dia(Date fecha) {
    this.fecha = DateAux.getInstance().getStartDay(fecha);
    this.fechaString = DateAux.getInstance().getDateString(this.fecha);
    this.diaSemana = getNombreDiaSemana();
    this.turnos = new ArrayList<FraccionTurno>();

    inicializarTurnos();
  }

  private void inicializarTurnos() {
    if (0 >= fecha.getDay() || fecha.getDay() > 6) {
      return;
    }

    for (int i = 0; i < 12; i++) {
      FraccionTurno nvoManana = new FraccionTurno(this, Turno.MANANA, i);
      this.turnos.add(nvoManana);
    }

    if (fecha.getDay() == 6) {
      return;
    }
    for (int i = 0; i < 12; i++) {
      FraccionTurno nvoTarde = new FraccionTurno(this, Turno.TARDE, i);
      this.turnos.add(nvoTarde);
    }
  }

  public Date getFecha() {
    return fecha;
  }

  public String getFechaString() {
    return fechaString;
  }

  public String getDiaSemana() {
    return diaSemana;
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

  public FraccionTurno obtenerFraccionTurno(int nroTurno, Turno t) {
    FraccionTurno encontrado = null;

    for (FraccionTurno fr : obtenerFraccionesTurno(t)) {
      if (fr.getNro() == nroTurno) {
        encontrado = fr;
        break;
      }
    }
    return encontrado;
  }

  public ArrayList<FraccionTurno> obtenerFraccionesTurno() {
    return turnos;
  }

  public ArrayList<FraccionTurno> obtenerFraccionesTurno(Turno t) {
    String turnoString = t.toString();
    ArrayList<FraccionTurno> seleccion = new ArrayList<FraccionTurno>();

    for (FraccionTurno ft : turnos) {
      String ftTurnoString = ft.getTurno().toString();
      if (turnoString.contains(ftTurnoString)) {
        seleccion.add(ft);
      }
    }
    return seleccion;
  }

  /**
   * Valida los valores de turno
   * 
   * @param t     turno a validar, no null
   * @param desde numero de turno de inicio
   * @param hasta numero de turno de fin
   * @throws ServicioException si los valores de turno no son validos
   */
  public void validarTurnos(Turno t, int desde, int hasta) throws ServicioException {

    if (t == null) {
      throw new ServicioException("Turno no puede ser null");
    }

    String tno = t.toString();

    if (desde > hasta) {
      throw new ServicioException("El final del turno debe ser posterior a su inicio");
    }

    if (0 > desde || 0 > hasta) {
      throw new ServicioException("El nro de turno es invalido");
    }

    if (tno.contains(Turno.MANANA.toString())) {
      if (desde > turnos.size() || hasta > turnos.size()) {
        throw new ServicioException("El nro de turno no corresponde al turno manana");
      }
    } else {
      if (desde > turnos.size() || hasta > turnos.size()) {
        throw new ServicioException("El nro de turno no corresponde al turno tarde");
      }
    }
  }

  private boolean estanTurnosOcupados(Turno t, int inicio, int fin) throws Exception {
    boolean ocupado = false;

    for (int i = inicio; i <= fin; i++) {
      FraccionTurno ft = obtenerFraccionTurno(i, t);

      if (ft == null) {
        throw new AsignacionException("Parametros de turno no validos");
      }
      if (ft.getEstaOcupado()) {
        ocupado = true;
        break;
      }
    }

    return ocupado;
  }

  /**
   * Verifica si se tiene al menos un servicio en el dia
   */
  public boolean verificarDiaOcupado() {
    boolean diaOcupado = false;

    for (FraccionTurno ft : turnos) {
      if (ft.getEstaOcupado()) {
        diaOcupado = true;
        break;
      }
    }

    return diaOcupado;
  }

  public ArrayList<FraccionTurno> obtenerTurnos(Turno t, int desde, int hasta) throws Exception {
    ArrayList<FraccionTurno> turnos = new ArrayList<FraccionTurno>();
    int expectedSize = hasta - desde + 1;

    for (int i = desde; i <= hasta; i++) {
      FraccionTurno ft = obtenerFraccionTurno(i, t);
      if (ft == null) {
        throw new AsignacionException("Parametros de turno no validos");
      }
      if (ft.getEstaOcupado()) {
        throw new AsignacionException("Turno ocupado");
      }
      turnos.add(ft);
    }

    if (turnos.size() != expectedSize) {
      throw new AsignacionException("Parametros de turno no validos");
    }

    return turnos;
  }

  public void verificarDisponibilidadPostServicio(Servicio s) throws Exception {
    Turno turnoServicio = s.getTurno();
    int turnoInicio = s.getTurnoInicio();
    int turnoAnteriorInicio;

    if (turnoInicio == 0) {
      return;
    }

    // Si no es 0 el inicio del nuevo turno, siempre vamos a verificar la
    // disponibilidad del turno anterior al servicio
    turnoAnteriorInicio = turnoInicio - 1;

    FraccionTurno ft = obtenerFraccionTurno(turnoAnteriorInicio, turnoServicio);

    if (ft == null) {
      throw new AsignacionException("Parametros de turno no validos");
    }

    if (ft.getEstaOcupado()) {
      throw new AsignacionException("El tecnico requiere un turno adicional tras finalizar un servicio");
    }
  }

  public void verificarDisponibilidad(Servicio s) throws Exception {
    Turno t = s.getTurno();
    int desde = s.getTurnoInicio();
    int hasta = s.getTurnoFin();

    validarTurnos(t, desde, hasta);

    if (estanTurnosOcupados(t, desde, hasta)) {
      throw new AgendaException("Turno/s ya ocupado/s");
    }
  }

  public void asignarServicio(Servicio s) throws Exception {
    try {
      ArrayList<FraccionTurno> ftAsignar = obtenerTurnos(s.getTurno(), s.getTurnoInicio(), s.getTurnoFin());

      for (FraccionTurno ft : ftAsignar) {
        if (ft.getEstaOcupado()) {
          throw new AsignacionException("El turno se encuentra ocupado");
        }
      }

      for (FraccionTurno ft : ftAsignar) {
        ft.asignarServicio(s);
      }
    } catch (AsignacionException e) {
      throw e;
    } catch (Exception e) {
      throw new AgendaException("Error NOT HANDLED:\n" + e);
    }
  }

  @Override
  public String toString() {
    return "Dia [diaSemana=" + diaSemana + ", turnoManana=" + turnos + "]";
  }

  public String toStringFormatted() {
    String formateado = "";

    for (int i = 0; i < turnos.size(); i++) {
      FraccionTurno ft = turnos.get(i);
      formateado += (i + 1) + ") " + getDiaSemana() + " " + ft.getHorario() + " | "
          + (ft.getEstaOcupado() ? "Asignado servicio Nro " + ft.getServicioAsignado().getNro() : "LIBRE") + "\n";
    }

    return formateado;
  }

}
