package personas;

import java.util.ArrayList;

import agenda.*;
import comercial.*;
import comercial.articulos.*;
import empresa.Empresa;
import excepciones.*;

public class Tecnico extends Interno {
  private Seniority seniority;
  private Agenda agenda;

  public Tecnico(String nombre, long dni, String direccion, String telefono, String contrasena, Seniority seniority) {
    super(nombre, dni, direccion, telefono, contrasena);
    this.seniority = seniority;
    this.agenda = new Agenda(this);

    Empresa.getInstance().agregarTecnico(this);
  }

  // ALTERNATIVO SIN DATOS
  public Tecnico(String nombre, String contrasena, Seniority seniority) {
    super(nombre, contrasena);
    this.seniority = seniority;
    this.agenda = new Agenda(this);

    Empresa.getInstance().agregarTecnico(this);
  }

  public Seniority getSeniority() {
    return seniority;
  }

  public void setSeniority(Seniority seniority) {
    this.seniority = seniority;
  }

  public Agenda getAgenda() {
    return agenda;
  }

  public boolean verificarDisponibilidad(Servicio s) throws Exception {
    return agenda.verificarDisponibilidad(s.getFecha(), s.getTurno(), s.getTurnoInicio(), s.getTurnoFin());
  }

  public ArrayList<Servicio> getServiciosPendientes() {
    ArrayList<Servicio> asignados = new ArrayList<Servicio>();

    for (Servicio s : Empresa.getInstance().getServicios()) {
      if (s.getTecnicos().contains(this) == false) {
        continue;
      }
      if (s.isFacturado()) {
        continue;
      }
      if (s.isEnPoderTecnico() == false) {
        continue;
      }

      if (s.getEstadoServicio() == EstadoServicio.PROGRAMADO || s.getEstadoServicio() == EstadoServicio.EN_CURSO) {
        asignados.add(s);
      }

    }

    return asignados;
  }

  @Override
  public String toString() {
    return "Tecnico [seniority=" + seniority + ", legajo=" + legajo + ", nombre=" + nombre + ", dni=" + dni
        + ", direccion=" + direccion + ", telefono=" + telefono + "]";
  }

  // METODOS TECNICO -> SERVICIO
  public ArrayList<Servicio> verServiciosAsignados() {
    ArrayList<Servicio> asignados = new ArrayList<Servicio>();

    for (Servicio s : Empresa.getInstance().getServicios()) {
      if (s.getTecnicos().contains(this)) {
        asignados.add(s);
      }
    }

    return asignados;
  }

  public Servicio verServiciosAsignados(int nroServicio) {
    ArrayList<Servicio> asignados = verServiciosAsignados();
    Servicio buscado = null;

    for (Servicio s : asignados) {
      if (s.getNro() == nroServicio) {
        buscado = s;
        break;
      }
    }

    return buscado;
  }

  public void asignarServicio(Servicio s) throws Exception {
    if (s.getTecnicos().contains(this)) {
      throw new AsignacionException("El servicio ya esta asignado a este tecnico");
    }
    agenda.asignarServicio(s);
    s.asignarTecnico(this);
  }

  public void preValidarEdicionServicio(Servicio s) throws Exception {
    if (s.getTecnicos().contains(this) == false) {
      throw new AsignacionException("No estas asignado a este servicio");
    }
    if (s.isFacturado()) {
      throw new ServicioException("El servicio ya fue facturado");
    }
    if (s.isEnPoderTecnico() == false) {
      throw new ServicioException("El servicio no fue liberado");
    }
    if (s.getEstadoServicio() != EstadoServicio.EN_CURSO && s.getEstadoServicio() != EstadoServicio.PROGRAMADO) {
      throw new ServicioException("El servicio debe estar PROGRAMADO o EN_CURSO");
    }
  }

  public Articulo getArticulos(int sku) {
    return Empresa.getInstance().getArticulos(sku);
  }

  public Articulo getArticulos(Articulo a) {
    return Empresa.getInstance().getArticulos(a);
  }

  public ArrayList<Articulo> getArticulos() {
    return Empresa.getInstance().getArticulos();
  }

  public void anadirArticuloServicio(Servicio s, int cantidad, Articulo a) throws Exception {
    String genExc = "No fue posible anadir articulo: ";

    try {
      preValidarEdicionServicio(s);
      if (s.getEstadoServicio() == EstadoServicio.PROGRAMADO) {
        throw new ServicioException("Primero debe iniciar la ejecucion del servicio");
      }
    } catch (Exception e) {
      if (e instanceof ServicioException || e instanceof AsignacionException) {
        throw new ServicioException(genExc + e.getMessage());
      }
      throw new Exception(genExc + "NOT HANDLED ERROR");
    }

    s.anadirArticulo(a, cantidad);
  }

  public ArticuloExtra crearArticuloExtra(String descripcion, double costo) {
    return new ArticuloExtra(descripcion, costo);
  }

  public void anadirArticuloExtraServicio(Servicio s, int q, ArticuloExtra ax)
      throws Exception {
    String genExc = "No fue posible a�adir articulo extra: ";

    try {
      preValidarEdicionServicio(s);
      if (s.getEstadoServicio() == EstadoServicio.PROGRAMADO) {
        throw new ServicioException("Primero debe iniciar la ejecucion del servicio");
      }
    } catch (Exception e) {
      if (e instanceof ServicioException || e instanceof AsignacionException) {
        throw new ServicioException(genExc + e.getMessage());
      }
      throw new Exception(genExc + "NOT HANDLED ERROR");
    }

    s.anadirOtroCostos(ax, q);
  }

  public void toggleAlmuerzoServicio(Servicio s) throws Exception {
    String genExc = "No fue posible modificar almuerzo: ";

    if (s.getTecnicos().contains(this) == false) {
      throw new AsignacionException(genExc + "No estas asignado a este servicio");
    }
    if (s.isFacturado()) {
      throw new ServicioException(genExc + "El servicio ya fue facturado");
    }

    if (s.getEstadoServicio() != EstadoServicio.EN_CURSO) {
      throw new ServicioException(genExc + "El servicio no esta en curso");
    }

    s.toggleIncluyeAlmuerzo();
  }

  public void ejecutarServicio(Servicio s) throws Exception {
    String genExc = "No fue posible finalizar servicio: ";

    if (s.getTecnicos().contains(this) == false) {
      throw new AsignacionException(genExc + "No estas asignado a este servicio");
    }
    if (s.isFacturado()) {
      throw new ServicioException(genExc + "El servicio ya fue facturado");
    }

    if (s.getEstadoServicio() == EstadoServicio.FINALIZADO) {
      throw new ServicioException(genExc + "El servicio ya fue finalizado");
    }

    if (s.getEstadoServicio() == EstadoServicio.CANCELADO) {
      throw new ServicioException(genExc + "El servicio fue cancelado");
    }

    if (s.getEstadoServicio() == EstadoServicio.EN_CURSO) {
      throw new ServicioException(genExc + "El servicio ya se encuentra en curso");
    }
    try {
      preValidarEdicionServicio(s);
    } catch (Exception e) {
      throw new ServicioException("No fue posible ejecutar servicio: " + e.getMessage());
    }

    s.setEstadoServicio(EstadoServicio.EN_CURSO);
  }

  public void finalizarServicio(Servicio s) throws Exception {
    try {
      preValidarEdicionServicio(s);
      if (s.getEstadoServicio() == EstadoServicio.PROGRAMADO) {
        throw new ServicioException("Primero debe inicializar el servicio");
      }
    } catch (Exception e) {
      throw new ServicioException("No fue posible finalizar servicio: " + e.getMessage());
    }

    s.setEstadoServicio(EstadoServicio.FINALIZADO);
  }

  public void avanzarServicio(Servicio s) throws Exception {
    if (s.getEstadoServicio() == EstadoServicio.EN_CURSO) {
      finalizarServicio(s);
    }
    if (s.getEstadoServicio() == EstadoServicio.PROGRAMADO) {
      ejecutarServicio(s);
    }
  }

}
