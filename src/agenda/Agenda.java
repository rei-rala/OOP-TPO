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
  private final int cantDiasGeneradosXDefecto = 15;

  public Agenda(Persona propietario) {
    this.dias = new ArrayList<Dia>();
    this.propietario = propietario;

    inicializarDias();
  }

  private void inicializarDias() {
    Date currentDate = DateAux.getInstance().getToday();
    int i = 0;
    while (i < cantDiasGeneradosXDefecto) {
      currentDate = DateAux.getInstance().getNextDay(currentDate);
      // si es domingo lo omitimos
      if (currentDate.getDay() == 0) {
        continue;
      }
      obtenerDiaAgenda(currentDate);
      i++;
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
    Date f = DateAux.getInstance().getStartDay(fecha);

    Dia encontrado = null;

    for (Dia d : dias) {

      if (DateAux.getInstance().getDateString(d.getFecha()).equalsIgnoreCase(DateAux.getInstance().getDateString(f))) {
        encontrado = d;
        break;
      }
    }

    if (encontrado == null && f.getDay() != 0) {
      Dia nuevoDia = new Dia(f);

      dias.add(nuevoDia);
      cantDias = dias.size();

      return nuevoDia;
    }

    return encontrado;
  }

  public void asignarServicio(Servicio s) throws Exception {
    Dia aAsignar = obtenerDiaAgenda(s.getFecha());

    if (aAsignar == null) {
      throw new AgendaException("No se pudo asignar el servicio");
    }
    
    if (this.propietario.getClass() == Tecnico.class) {
      aAsignar.verificarDisponibilidadPostServicio(s);
    }

    aAsignar.verificarDisponibilidad(s);
    
    aAsignar.asignarServicio(s);
  }

  public void verificarDisponibilidad(Servicio s) throws Exception {
    Date fecha = DateAux.getInstance().getStartDay(s.getFecha());

    if (fecha.before(DateAux.getInstance().getToday())) {
      s.forceCancelar();
      throw new AgendaException("La fecha debe ser posterior");
    }
    Dia d = obtenerDiaAgenda(fecha);
    if (d == null) {
      throw new AgendaException("Dia no valido");
    }

    if (this.propietario.getClass() == Tecnico.class) {
      d.verificarDisponibilidadPostServicio(s);
    }

    d.verificarDisponibilidad(s);
  }

  @Override
  public String toString() {
    return "Agenda [propietario=" + propietario.getNombre() + ", cantDias=" + cantDias + "]";
  }

  public String getDiasShort() {
    String formateado = "";

    for (int i = 0; i < dias.size(); i++) {
      Dia d = dias.get(i);
      formateado += (i + 1) + ") Fecha " + DateAux.getInstance().getDateString(d.getFecha()) + " - " + d.getNombreDiaSemana()
          + (d.verificarDiaOcupado() ? " *SERVICIO PENDIENTE*" : "") + "\n";
    }

    return formateado;
  }

  public ArrayList<Dia> obtenerDias() {
    ArrayList<Dia> diasDispo = new ArrayList<Dia>();

    for (Dia d : dias) {
      if (d.getFecha().after(new Date())) {
        diasDispo.add(d);
      }
    }

    return diasDispo;
  }

  public String getDiasFormatted() {
    String formateado = "";

    for (Dia d : dias) {
      formateado += d.toStringFormatted() + "\n";
    }

    return formateado;
  }

  public String toStringFormatted() {
    String formateado = "";

    for (Dia d : dias) {
      formateado += d.toStringFormatted() + "\n";
    }

    return formateado;
  }
}