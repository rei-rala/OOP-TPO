package agenda;

import java.util.ArrayList;
import java.util.Date;

import comercial.Servicio;
import excepciones.*;
import main.DateAux;
import personas.*;

@SuppressWarnings("deprecation")
public class Agenda {
  private final ArrayList<Dia> dias;
  private final Persona propietario;
  private int cantDias = 0;
  private final int cantDiasGeneradosXDefecto = 30;

  public Agenda(Persona propietario) {
    this.dias = new ArrayList<Dia>();
    this.propietario = propietario;
    
    
    // Creacion inicial de objetos Dia
    Date currentDate = DateAux.getToday();

    final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    for (int i = 0; i < cantDiasGeneradosXDefecto; i++) {
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

  public void asignarServicio(Servicio s) throws Exception {
    Dia aAsignar = obtenerDiaAgenda(s.getFecha());
    Turno turno = s.getTurno();
    int desde = s.getturnoInicio();
    int hasta = s.getturnoFin();

    if (aAsignar.verificarDisponibilidad(turno, desde, hasta)) {
      aAsignar.asignarServicioDia(s);
    } else {
      throw new AsignacionException("No se puede asignar el servicio");
    }
  }

  public boolean verificarDisponibilidad(Date fecha, Turno turno, int desde, int hasta) {
    try {
      Dia d = obtenerDiaAgenda(fecha);
      if (d == null) {
        throw new AgendaException("Dia no valido");
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

  @Override
  public String toString() {
    return "Agenda [propietario=" + propietario.getNombre() + ", cantDias=" + cantDias + "]";
  }

}