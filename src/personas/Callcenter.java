package personas;

import java.util.ArrayList;
import java.util.Date;

import agenda.*;
import empresa.Empresa;
import comercial.*;
import comercial.articulos.*;
import excepciones.*;
import main.DateAux;

public class Callcenter extends Interno {

  public Callcenter(String nombre, long dni, String direccion, String telefono, String contrasena) {
    super(nombre, dni, direccion, telefono, contrasena);
  }

  // ALTERNATIVO SIN DATOS
  public Callcenter(String nombre, String contrasena) {
    super(nombre, contrasena);
  }

  @Override
  public String toString() {
    return "Callcenter [legajo=" + legajo + ", nombre=" + nombre + ", dni=" + dni + ", direccion=" + direccion
        + ", telefono=" + telefono + "]";
  }

  // ARTICULOS
  public ArrayList<Articulo> getArticulos() {
    return Empresa.getInstance().getArticulos();
  }

  public void setStockArticulo(Articulo a, int stock) throws Exception {
    a.setStock(stock);
  }

  public Cliente getClientes(int numeroCliente) {
    return Empresa.getInstance().getClientes(numeroCliente);
  }

  public Cliente getClientes(Cliente c) {
    return Empresa.getInstance().getClientes(c);
  }

  public ArrayList<Cliente> getClientesSinServicios() {
    ArrayList<Cliente> clientes = Empresa.getInstance().getClientesSinServiciosPendientes();

    return clientes;
  }

  public ArrayList<Tecnico> getTecnicos() {
    return Empresa.getInstance().getTecnicos();
  }

  // TECNICOS
  public ArrayList<Tecnico> buscarTecnicosDisponibles(Servicio s) {
    ArrayList<Tecnico> tecnicosDisponibles = new ArrayList<Tecnico>();

    for (Tecnico t : getTecnicos()) {
      try {
        if (t.verificarDisponibilidad(s)) {
          tecnicosDisponibles.add(t);
        }
      } catch (Exception e) {
      }
    }
    return tecnicosDisponibles;
  }

  // Auxiliares para servicio
  private void preValidarCliente(Cliente c) throws Exception {
    if (c == null) {
      throw new AsignacionException("No existe cliente");
    }
  }

  private void preValidarTecnico(Tecnico t) throws Exception {
    if (t == null) {
      throw new AsignacionException("No existe tecnico");
    }
  }

  private void preValidarEdicionServicio(Servicio s) throws Exception {
    if (s == null) {
      throw new AsignacionException("No existe servicio");
    }
    if (s.isFacturado()) {
      throw new AsignacionException("El servicio se encuentra facturado");
    }
  }

  public void preValidarLiberacion(Servicio s) throws Exception {
    preValidarEdicionServicio(s);

    if (s.getCliente() == null) {
      throw new AsignacionException("Primero debe asignarse cliente");
    }

    preValidarCliente(s.getCliente());

    if (s.getTecnicos().isEmpty()) {
      throw new AsignacionException("El servicio no tiene tecnicos asignados");
    }

    for (Tecnico t : s.getTecnicos()) {
      preValidarTecnico(t);
    }
  }

  // SERVICIOS
  private ArrayList<Servicio> getServicios() {
    return Empresa.getInstance().getServicios();
  }

  public ArrayList<Servicio> getServiciosPendientes() {
    ArrayList<Servicio> serviciosPendientes = new ArrayList<Servicio>();

    for (Servicio s : getServicios()) {
      if (s.isFacturado()) {
        continue;
      }
      if (s.getEstadoServicio() == EstadoServicio.CANCELADO || s.getEstadoServicio() == EstadoServicio.EN_CURSO) {
        continue;
      }
      if (s.isEnPoderTecnico()) {
        continue;
      }
      serviciosPendientes.add(s);
    }
    return serviciosPendientes;
  }

  public boolean verificarDisponibilidadCliente(Cliente c, Servicio s) throws Exception {
    preValidarCliente(c);
    return c.verificarDisponibilidad(s);
  }

  public boolean verificarDisponibilidadTecnico(Tecnico t, Servicio s)
      throws Exception {
    preValidarTecnico(t);

    return t.verificarDisponibilidad(s);
  }

  public Servicio crearNuevoServicioServicio(Date fechaServicio, TipoServicio ts, Turno t, int desde,
      int hasta) throws Exception {
    double duracionServInicial = DateAux.calcularHoras(desde, hasta);
    Date today = DateAux.getToday();

    if (fechaServicio == null || ts == null || t == null) {
      throw new ServicioException("Verificar datos ingresados");
    }
    if (fechaServicio.before(today) && fechaServicio.compareTo(today) > 0) {

      throw new ServicioException("La fecha no debe ser anterior a la actual");
    }
    if (0 >= fechaServicio.getDay() || fechaServicio.getDay() > 6) {
      throw new Exception("Dia no valido");
    }
    if (desde >= 12 || hasta >= 12) {
      throw new Exception("El numero de turno es incorrecto");
    }
    if (desde > hasta) {
      throw new Exception("La hora de inicio no puede ser mayor a la hora de fin");
    }
    if (fechaServicio.getDay() == 6 && t == Turno.TARDE) {
      throw new Exception("No se puede asignar un servicio a sabado a la tarde");
    }

    if (Empresa.getInstance().verificarArticulosSuficientes(ts) == false) {
      throw new StockException("Faltan articulos necesarios para crear nuevo servicio");
    }

    if (ts == TipoServicio.INSTALACION && 1 > duracionServInicial) {
      throw new ServicioException("Una reparacion debe durar al menos 1 hora");
    } else if (0 > duracionServInicial) {
      throw new ServicioException("Duracion de servicio no valida");
    }

    return new Servicio(fechaServicio, ts, t, desde, hasta);
  }

  public void asignarServicio(Servicio s, Cliente c)
      throws Exception {
    preValidarCliente(c);
    preValidarEdicionServicio(s);

    c.asignarServicio(s);
  }

  public void asignarServicio(Servicio s, Tecnico t) throws Exception {
    preValidarTecnico(t);
    preValidarEdicionServicio(s);

    t.asignarServicio(s);
  }

  public void cancelarServicio(Servicio s) throws Exception {
    preValidarEdicionServicio(s);
    s.cancelarServicio();
  }

  public void liberarServicioCallcenter(Servicio s) throws Exception {
    preValidarLiberacion(s);

    if (s.getCliente() == null) {
      throw new ServicioException("El servicio debe estar asignado a un cliente.");
    }
    if (s.isEnPoderTecnico()) {
      throw new ServicioException("El servicio se encuentra en poder de lo/s tecnico/s.");
    }
    if (0 >= s.getTecnicos().size()) {
      throw new ServicioException("El servicio debe contar con al menos 1 (un) tecnico");
    }
    s.otorgarPoderTecnico();
  }
}
