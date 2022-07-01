package personas;

import java.util.ArrayList;
import java.util.Date;

import agenda.*;
import comercial.EstadoServicio;
import comercial.Servicio;
import empresa.Empresa;
import excepciones.*;
import main.DateAux;

public class Cliente extends Persona {
  static int contadorClientes = 0;

  private final int nro;
  private final Agenda agenda;

  public Cliente(String nombre, long dni, String direccion, String telefono) {
    super(nombre, dni, direccion, telefono);
    this.nro = ++contadorClientes;
    this.agenda = new Agenda(this);

    Empresa.getInstance().agregarCliente(this);
  }

  public Cliente(String nombre) {
    super(nombre);
    this.nro = ++contadorClientes;
    this.agenda = new Agenda(this);

    Empresa.getInstance().agregarCliente(this);
  }

  public int getNro() {
    return nro;
  }

  public Agenda getAgenda() {
    return agenda;
  }

  public boolean verificarServicioVigente() {
    Date today = DateAux.getToday();
    Agenda agendaCliente = this.getAgenda();
    boolean tieneServicioVigente = false;

    for (Dia d : agendaCliente.getDias()) {
      Date fechaDia = d.getFecha();
      if (today.after(fechaDia)) {
        continue;
      }

      if (d.verificarDiaOcupado()) {
        for (FraccionTurno ft : d.obtenerFraccionesTurno()) {
          Servicio servDelTurno = ft.getServicioAsignado();

          if (servDelTurno == null) {
            continue;
          }

          if (servDelTurno.isFacturado()) {
            continue;
          }

          if (servDelTurno.getEstadoServicio() != EstadoServicio.FINALIZADO) {
            tieneServicioVigente = true;
            break;
          }
        }
      }

    }
    return tieneServicioVigente;
  }

  public boolean verificarDisponibilidad(Servicio s) throws Exception {
    if (verificarServicioVigente()) {
      throw new AgendaException("El cliente ya tiene un servicio vigente");
    }

    return agenda.verificarDisponibilidad(s.getFecha(), s.getTurno(), s.getTurnoInicio(), s.getTurnoFin());
  }

  public void asignarServicio(Servicio s) throws Exception {
    if (s.getCliente() == this) {
      throw new AsignacionException("El servicio ya se encuentra asignado a este cliente");
    }

    if (!verificarDisponibilidad(s)) {
      throw new AgendaException("El cliente ya tiene un servicio vigente");
    }

    agenda.asignarServicio(s);
    s.setCliente(this);
  }

  public ArrayList<FraccionTurno> verTurnosDisponibles(Turno t) {
    return agenda.obtenerTodosTurnosDisponible(t);
  }

  public ArrayList<FraccionTurno> verTurnosDisponibles() {
    return agenda.obtenerTodosTurnosDisponible();
  }

  public String toStringShort() {
    return "Cliente [nro=" + nro + ", nombre=" + nombre + ", dni=" + dni + ", direccion="
        + direccion + "]";
  }

  @Override
  public String toString() {
    return "Cliente [nro=" + nro + ", agenda=" + agenda + ", nombre=" + nombre + ", dni=" + dni + ", direccion="
        + direccion + ", telefono=" + telefono + "]";
  }

}
