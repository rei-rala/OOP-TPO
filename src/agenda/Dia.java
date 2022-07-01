package agenda;

import java.util.ArrayList;
import java.util.Date;

import comercial.Servicio;
import excepciones.AgendaException;
import excepciones.AsignacionException;
import main.DateAux;

@SuppressWarnings("deprecation")
public class Dia {
  private final Date fecha;
  private final String diaSemana;
  private final ArrayList<FraccionTurno> turnos;

  public Dia(Date fecha) {
    this.fecha = DateAux.getStartDay(fecha);
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

  public String getDiaSemana() {
    return diaSemana;
  }

  public FraccionTurno obtenerFraccionesTurno(int nroTurno, Turno t) {
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
    ArrayList<FraccionTurno> seleccion = new ArrayList<FraccionTurno>();

    for (FraccionTurno ft : turnos) {
      if (ft.getTurno() == t) {
        seleccion.add(ft);
      }
    }
    return seleccion;
  }

  private FraccionTurno obtenerTurnoDisponible(ArrayList<FraccionTurno> alFT) {
    FraccionTurno disponible = null;
    for (FraccionTurno ft : alFT) {
      if (ft.getEstaOcupado()) {
        continue;
      }
      disponible = ft;
      break;
    }
    return disponible;
  }

  private ArrayList<FraccionTurno> obtenerTodosTurnosDisponibles(ArrayList<FraccionTurno> alFT) {
    ArrayList<FraccionTurno> disponibles = new ArrayList<FraccionTurno>();
    for (FraccionTurno ft : alFT) {
      if (ft.getEstaOcupado()) {
        continue;
      }
      disponibles.add(ft);
    }
    return disponibles;
  }

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

  public boolean validarTurnos(Turno t, int desde, int hasta) {
    if (desde > hasta || 0 > desde || 0 > hasta) {
      return false;
    }

    if (t == Turno.MANANA) {
      if (desde > turnos.size() || hasta > turnos.size()) {
        return false;
      }
    } else {
      if (desde > turnos.size() || hasta > turnos.size()) {
        return false;
      }
    }

    return true;
  }

  public FraccionTurno obtenerTurnoDisponible(Turno t) {
    ArrayList<FraccionTurno> turnosDia = obtenerFraccionesTurno(t);
    return obtenerTurnoDisponible(turnosDia);
  }

  public FraccionTurno obtenerTurnoDisponible() {
    ArrayList<FraccionTurno> turnosTurno = obtenerFraccionesTurno();

    return obtenerTurnoDisponible(turnosTurno);
  }

  public ArrayList<FraccionTurno> obtenerTodosTurnoDisponibles(Turno t) {
    ArrayList<FraccionTurno> turnosDia = obtenerFraccionesTurno(t);
    return obtenerTodosTurnosDisponibles(turnosDia);
  }

  public ArrayList<FraccionTurno> obtenerTodosTurnoDisponibles() {
    ArrayList<FraccionTurno> turnosTurno = obtenerFraccionesTurno();

    return obtenerTodosTurnosDisponibles(turnosTurno);
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

  public boolean estanTurnosOcupados(Turno t, int desde, int hasta) throws Exception {
    boolean ocupado = false;

    for (int i = desde; i < hasta; i++) {
      FraccionTurno ft = obtenerFraccionesTurno(i, t);

      if (ft == null) {
        throw new AsignacionException("Parametros de turno no validos");
      }
      if (ft.getEstaOcupado()) {
        ocupado = true;
      }
    }

    return ocupado;
  }

  public ArrayList<FraccionTurno> obtenerTurnos(Turno t, int desde, int hasta) throws Exception {
    ArrayList<FraccionTurno> turnos = new ArrayList<FraccionTurno>();
    int expectedSize = hasta - desde;

    for (int i = desde; i < hasta; i++) {
      FraccionTurno ft = obtenerFraccionesTurno(i, t);
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

  public void asignarServicioDia(Servicio s) throws Exception {
    ArrayList<FraccionTurno> ftAsignar = obtenerTurnos(s.getTurno(), s.getTurnoInicio(), s.getturnoFin());

    for (FraccionTurno ft: ftAsignar) {
      if (ft.getEstaOcupado()) {
        throw new AsignacionException("El turno se encuentra ocupado");
      }
    }

    for (FraccionTurno ft : ftAsignar) {
      ft.asignarServicio(s);
    }
  }

  public boolean verificarDisponibilidad(Turno turno, int desde, int hasta) throws Exception {
    if (validarTurnos(turno, desde, hasta) == false) {
      throw new AgendaException("Turno/s no valido/s");
    }
    if (estanTurnosOcupados(turno, desde, hasta)) {
      throw new AgendaException("Turno/s ya ocupado/s");
    }
    return true;
  }

  public boolean verificarDisponibilidad(Servicio s) throws Exception {
    Turno t = s.getTurno();
    int desde = s.getTurnoInicio();
    int hasta = s.getturnoFin();

    if (validarTurnos(t, desde, hasta) == false) {
      throw new AgendaException("Turno no validos");
    }
    if (estanTurnosOcupados(t, desde, hasta)) {
      throw new AgendaException("Turno/s ya ocupado/s");
    }
    return true;
  }

  public void asignarFraccionesTurnos(Servicio s) throws Exception {
    try {
      asignarServicioDia(s);
    } catch (AgendaException e) {
      throw e;
    } catch (Exception e) {
      throw new AgendaException("Error NOT HANDLED:\n" + e);
    }
  }

  public FraccionTurno obtenerSiguienteFraccionDisponible() {
    Date today = DateAux.getToday();

    for (FraccionTurno fr : obtenerFraccionesTurno()) {
      Date fechaFT = fr.getDia().getFecha();
      System.out.println("fechaFT: " + fechaFT);
      System.out.println("today: " + today);
      if (today.toString() != fechaFT.toString() && today.after(fechaFT) ) {
        continue;
      }
      if (fr.getEstaOcupado()) {
        continue;
      }

      return fr;
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

  public void verFraccionesTurno() {
    for (FraccionTurno ft : obtenerFraccionesTurno()) {
      System.out
          .println("" + ft.getNro() + ". " + ft.getHorario() + "(" + fecha.getDay() + ") | " + ft.getTurno());
    }
  }

  @Override
  public String toString() {
    return "Dia [diaSemana=" + diaSemana + ", turnoManana=" + turnos + "]";
  }

}
