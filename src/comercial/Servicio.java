package comercial;

import java.util.ArrayList;
import java.util.Date;

import agenda.Turno;
import empresa.Empresa;
import comercial.articulos.*;
import personas.*;
import excepciones.*;
import main.DateAux;

public class Servicio {
  static int contadorServicios = 0;

  private final int nro;
  private Cliente cliente;
  private final Date fecha;
  private final Date fechaCreacion;

  private double tiempoTrabajado;
  private TipoServicio tipoServicio;
  private EstadoServicio estadoServicio;
  private ArrayList<Tecnico> tecnicosAsignados = new ArrayList<Tecnico>();
  private ArrayList<Costo> articulosUtilizados = new ArrayList<Costo>();
  private ArrayList<Costo> articulosExtraUtilizados = new ArrayList<Costo>();
  private final double costoViaje = Empresa.getInstance().getCostoViaje();
  private boolean almuerzo;
  private boolean enPoderTecnico;
  private boolean facturado;

  private Turno turno;
  private int turnoInicio = -1;
  private int turnoFin = -1;

  public Servicio(Date fecha, TipoServicio tipoServicio, Turno turno, int turnoInicio, int turnoFin) throws Exception {
    this.nro = ++contadorServicios;
    this.fechaCreacion = DateAux.getNow();
    this.fecha = fecha;
    this.tipoServicio = tipoServicio;
    this.turno = turno;
    this.turnoInicio = turnoInicio;
    this.turnoFin = turnoFin;
    this.tiempoTrabajado = DateAux.calcularHoras(turnoInicio, turnoFin);
    this.estadoServicio = EstadoServicio.PROGRAMADO;
    this.almuerzo = false;
    this.enPoderTecnico = false;
    this.facturado = false;

    preAnadirArticulos();
    Empresa.getInstance().agregarServicio(this);
  }

  private void preAnadirArticulos() throws Exception {
    if (tipoServicio == TipoServicio.INSTALACION) {
      Articulo cable = Empresa.getInstance().getArticulos(Cable.class);
      Articulo decoTV = Empresa.getInstance().getArticulos(DecodificadorTV.class);
      Articulo modem = Empresa.getInstance().getArticulos(Modem.class);
      Articulo divCoax = Empresa.getInstance().getArticulos(DivisorCoaxial.class);
      Articulo conCoax = Empresa.getInstance().getArticulos(ConectorCoaxial.class);

      anadirArticulo(cable, 3);
      anadirArticulo(decoTV, 1);
      anadirArticulo(modem, 1);
      anadirArticulo(divCoax, 1);
      anadirArticulo(conCoax, 6);
    }
  }

  public int getNro() {
    return nro;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public ArrayList<Tecnico> getTecnicos() {
    return tecnicosAsignados;
  }

  public Date getFecha() {
    return fecha;
  }

  public Date getFechaCreacion() {
    return fechaCreacion;
  }

  public TipoServicio getTipoServicio() {
    return tipoServicio;
  }

  public void setTipoServicio(TipoServicio tipoServicio) throws ServicioException {
    if (facturado || enPoderTecnico) {
      throw new ServicioException("No se puede cambiar el tipo de servicio una vez facturado o en poder del tecnico");
    }
    this.tipoServicio = tipoServicio;
  }

  public double getTiempoTrabajado() {
    return tiempoTrabajado;
  }

  public EstadoServicio getEstadoServicio() {
    return estadoServicio;
  }

  public void setEstadoServicio(EstadoServicio estadoServicio) {
    this.estadoServicio = estadoServicio;
  }

  public ArrayList<Costo> getArticulos() {
    return articulosUtilizados;
  }

  public ArrayList<Costo> getArticulosExtra() {
    return articulosExtraUtilizados;
  }

  public double getCostoViaje() {
    return costoViaje;
  }

  public boolean isFacturado() {
    return facturado;
  }

  public boolean isEnPoderTecnico() {
    return enPoderTecnico;
  }

  public void otorgarPoderTecnico() {
    enPoderTecnico = true;
  }

  public boolean isIncluyeAlmuerzo() {
    return almuerzo;
  }

  public void setIncluyeAlmuerzo(boolean almuerzo) {
    this.almuerzo = almuerzo;
  }

  public void toggleIncluyeAlmuerzo() {
    this.almuerzo = !almuerzo;
  }

  public Turno getTurno() {
    return turno;
  }

  public void setTurno(Turno turno) {
    this.turno = turno;
  }

  public void setCliente(Cliente c) {
    this.cliente = c;
  }

  public int getTurnoInicio() {
    return turnoInicio;
  }

  public void setTurnoInicio(int turnoInicio) {
    this.turnoInicio = turnoInicio;
  }

  public int getTurnoFin() {
    return turnoFin;
  }

  public void setturnoFin(int turnoFin) {
    this.turnoFin = turnoFin;
  }

  // EDICION DE SERVICIO
  public void asignarCliente(Cliente c) throws Exception {
    if (facturado || enPoderTecnico) {
      throw new ServicioException(
          "No se puede cambiar añadir tecnico una vez facturado o estando el servicio en poder del tecnico");
    }
    if (c == null) {
      throw new ServicioException("Cliente no valido");
    }

    if (this.cliente == c) {
      throw new AsignacionException("Cliente ya asignado a este servicio");
    }

    this.cliente = c;
  }

  public void asignarTecnico(Tecnico t) throws Exception {
    if (facturado || enPoderTecnico) {
      throw new ServicioException(
          "No se puede cambiar añadir tecnico una vez facturado o estando el servicio en poder del tecnico");
    }
    if (t == null) {
      throw new AsignacionException("Tecnico no valido");
    }
    if (tecnicosAsignados.contains(t)) {
      throw new AsignacionException("Tecnico ya asignado a este servicio");
    }

    if (tecnicosAsignados.add(t) == false) {
      throw new AsignacionException("Error desconocido al asignar tecnico");
    }
  }

  public boolean anadirArticulo(Articulo art, int cantidad) throws Exception {
    if (isFacturado()) {
      throw new ServicioException("Servicio ya facturado");
    }

    if (art == null) {
      throw new ServicioException("El articulo ingresado no es valido");
    }

    art.consumirStock(cantidad);

    Costo nuevoCosto = new Costo(cantidad, art);
    return articulosUtilizados.add(nuevoCosto);
  }

  public boolean anadirOtroCostos(ArticuloExtra extraArt, int cantidad) throws ServicioException {
    if (isFacturado()) {
      throw new ServicioException("Servicio ya facturado");
    }

    Costo nuevoOtroCosto = new Costo(cantidad, extraArt);
    return articulosExtraUtilizados.add(nuevoOtroCosto);
  }

  public double obtenerValorHoraServicio() {
    double aux = 0;

    for (Tecnico t : getTecnicos()) {
      aux += tiempoTrabajado * Empresa.getInstance().getCostoHoraTecnico(t.getSeniority());
    }
    return aux;
  }

  public double calcularCostoArticulos() {
    double stArtsUtilizados = 0;
    for (Costo ca : articulosUtilizados) {
      stArtsUtilizados += ca.obtenerTotalCosto();
    }

    return stArtsUtilizados;
  }

  public double calcularCostoArticulosExtra() {
    double stOtrosArtsUtilizados = 0;

    for (Costo co : articulosExtraUtilizados) {
      stOtrosArtsUtilizados += co.obtenerTotalCosto();
    }

    return stOtrosArtsUtilizados;
  }

  public double calcularTotalServicio() {
    double tiempoTrabajado = getTiempoTrabajado();
    double costoArticulos = calcularCostoArticulos();
    double costoArticulosExtra = calcularCostoArticulosExtra();
    double stHorasTecnico = obtenerValorHoraServicio() * tiempoTrabajado;

    return stHorasTecnico + costoArticulos + costoArticulosExtra + costoViaje;
  }

  public void cancelarServicio() throws Exception {
    if (facturado) {
      throw new ServicioException("Servicio ya facturado");
    }
    if (enPoderTecnico) {
      throw new ServicioException("Servicio se encuentra en poder de lo/s tecnico/s");
    }
    if (estadoServicio != EstadoServicio.PROGRAMADO) {
      throw new ServicioException("El servicio ya se encuentra en curso");
    }

    estadoServicio = EstadoServicio.CANCELADO;
  }

  public boolean facturarServicio() throws ServicioException {
    if (isFacturado()) {
      throw new ServicioException("Servicio ya facturado");
    }

    boolean minHoras = tiempoTrabajado >= 0.5;
    boolean minTecnicos = tecnicosAsignados.size() > 0;

    if (minHoras && minTecnicos) {
      this.facturado = true;
    } else {
      throw new ServicioException("Verificar datos del servicio antes de facturar");
    }

    return this.facturado;
  }

  public String toStringShorter() {
    return "Servicio " + estadoServicio + " [nro=" + nro + " fecha=" + DateAux.getDateString(fecha) + ", tipoServicio="
        + tipoServicio
        + ", tecnicosAsignados=" + tecnicosAsignados.size()
        + ", articulosUtilizados=" + articulosUtilizados.size()
        + ", articulosExtraUtilizados=" + articulosExtraUtilizados.size() + "]";
  }

  public String toStringShort() {
    return "Servicio [nro=" + nro + " fecha=" + DateAux.getDateString(fecha) + ", tiempoTrabajado="
        + tiempoTrabajado + ", tipoServicio=" + tipoServicio + ", tecnicosAsignados=" + tecnicosAsignados.size()
        + ", articulosUtilizados=" + articulosUtilizados
        + ", articulosExtraUtilizados=" + articulosExtraUtilizados + ", costoViaje=" + costoViaje;
  }

  @Override
  public String toString() {
    return "Servicio [nro=" + nro + ", cliente=" + cliente + ", fecha=" + DateAux.getDateString(fecha)
        + ", tiempoTrabajado="
        + tiempoTrabajado + ", tipoServicio=" + tipoServicio + ", estadoServicio=" + estadoServicio
        + ", tecnicosAsignados=" + tecnicosAsignados + ", articulosUtilizados=" + articulosUtilizados
        + ", articulosExtraUtilizados=" + articulosExtraUtilizados + ", costoViaje=" + costoViaje + ", facturado="
        + facturado + "]";
  }
}
