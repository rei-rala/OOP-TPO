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
  private static int contadorServicios = 0;

  private final int nro;
  private Cliente cliente;
  private final Date fecha;
  private final Date fechaCreacion;

  private TipoServicio tipoServicio;
  private EstadoServicio estadoServicio;
  private ArrayList<Tecnico> tecnicosAsignados = new ArrayList<Tecnico>();
  private ArrayList<Costo> articulosUtilizados = new ArrayList<Costo>();
  private ArrayList<Costo> articulosExtraUtilizados = new ArrayList<Costo>();
  private final double costoViaje = Empresa.getInstance().getCostoViaje();

  private Turno turno;
  private int turnoInicio = -1;
  private int turnoFin = -1;

  private double tiempoTrabajado;
  private boolean almuerzo;
  private boolean enPoderTecnico;
  private boolean facturado;

  private DateAux dateAux = DateAux.getInstance();

  public Servicio(Date fecha, TipoServicio tipoServicio, Turno turno, int turnoInicio, int turnoFin) throws Exception {
    this.nro = ++contadorServicios;
    this.fechaCreacion = dateAux.getNow();
    this.fecha = dateAux.getStartDay(fecha);
    this.tipoServicio = tipoServicio;
    this.turno = turno;
    this.turnoInicio = turnoInicio;
    this.turnoFin = turnoFin;
    this.tiempoTrabajado = dateAux.calcularHoras(turnoInicio, turnoFin);
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

  public String getHorarioServicio() {
    return dateAux.getHorarioCompleto(turno, turnoInicio, turnoFin);
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

  public void forceCancelar() {
    estadoServicio = EstadoServicio.CANCELADO;
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
    String clienteStr = getCliente() == null
        ? "<SIN CLIENTE ASIGNADO>"
        : getCliente().getNombre() + " <ID: " + getCliente().getNro() + ">";
    int cantTecnicos = getTecnicos().size(),
        cantArticulos = getArticulos().size(),
        cantArticulosExtra = getArticulosExtra().size();

    return "Numero: " + nro
        + "\nCliente: " + clienteStr
        + "\nEstado " + estadoServicio
        + "\nFecha: " + dateAux.getDateString(fecha)
        + "\nTipo: " + tipoServicio
        + "\nTecnicos asignados: " + (cantTecnicos > 0 ? cantTecnicos : "<SIN TECNICOS ASIGNADOS>")
        + "\nArticulos utilizados: " + (cantArticulos > 0 ? cantArticulos : "<SIN ARTICULOS UTILIZADOS>")
        + "\nArticulos extra utilizados: "
        + (cantArticulosExtra > 0 ? cantArticulosExtra : "<SIN ARTICULOS EXTRA UTILIZADOS>");
  }

  public String toStringShort() {
    String clienteStr = getCliente() == null ? "<SIN CLIENTE ASIGNADO>"
        : getCliente().getNombre() + "(ID: "
            + getCliente().getNro() + ")";

    double totalArts = 0, totalOtrosArts = 0;
    String tArts = "Articulos " + articulosUtilizados.size(),
        tOtrosArts = "Articulos extra " + articulosExtraUtilizados.size();

    for (Costo cArts : articulosUtilizados) {
      totalArts += cArts.obtenerTotalCosto();
    }

    for (Costo cArtsExtra : articulosExtraUtilizados) {
      totalOtrosArts += cArtsExtra.obtenerTotalCosto();
    }

    return "\nNumero: " + nro
        + "\nFecha: " + dateAux.getDateString(fecha)
        + "\nCliente: " + clienteStr
        + "\nTiempo trabajado: " + tiempoTrabajado
        + "\nTipo servicio: " + tipoServicio
        + "\nTecnicos asignados: " + tecnicosAsignados.size()
        + "\n" + tArts + " ($ " + (totalArts > 0 ? totalArts : "-") + ")"
        + "\n" + tOtrosArts + " ($ " + (totalOtrosArts > 0 ? totalOtrosArts : "-") + ")"
        + "\nCosto Viaje: " + costoViaje
        + " ]";
  }

  @Override
  public String toString() {
    return "Servicio [ "
        + "nro: " + nro
        + ", Cliente: " + cliente
        + ", Fecha: " + dateAux.getDateString(fecha)
        + ", Tiempo trabajado: " + tiempoTrabajado
        + ", Tipo servicio: " + tipoServicio
        + ", Estado servicio: " + estadoServicio
        + ", Tecnicos asignados: " + tecnicosAsignados
        + ", Articulos utilizados: " + articulosUtilizados
        + ", Articulos extra utilizados: " + articulosExtraUtilizados
        + ", Costo viaje: " + costoViaje
        + ", Facturado: " + facturado
        + " ]";
  }
}
