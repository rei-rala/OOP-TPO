package empresa;

import java.util.ArrayList;

import personas.*;
import comercial.*;
import comercial.articulos.*;
import excepciones.*;

public class Empresa {
  private static Empresa empresa;

  private ArrayList<Articulo> articulos = new ArrayList<Articulo>();
  private ArrayList<Interno> internos = new ArrayList<Interno>();
  private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
  private ArrayList<Tecnico> tecnicos = new ArrayList<Tecnico>();
  private ArrayList<Servicio> servicios = new ArrayList<Servicio>();
  private ArrayList<Factura> facturas = new ArrayList<Factura>();
  private double costoCombustible = 200;
  private double costoViaje = 150;
  private CostoHorasTecnico costoHorasTecnico = new CostoHorasTecnico();

  private final double RENTABILIDAD = 0.3;
  private final double IVA = 0.21;

  public Empresa() {
  }
  
  public static Empresa getInstance() {
    if (empresa == null) {
      empresa = new Empresa();
    }
    return empresa;
  }

  public double getRENTABILIDAD() {
    return RENTABILIDAD;
  }

  public double getIVA() {
    return IVA;
  }

  // Metodos EMPRESA
  public Interno login(int legajo, String contrasena) {
    Interno i = getInternos(legajo);

    if (i != null && (i.getContrasena() == contrasena || i.getContrasena().equals(contrasena))) {
      return i;
    }

    return null;
  }

  private String iterarToString(ArrayList<? extends Object> arrayListObj) {
    String objStr = "";

    int index = 0;

    while (index < arrayListObj.size()) {
      objStr += "\n\t" + (index + 1) + ") " + arrayListObj.get(index);
      index++;
    }

    return objStr;
  }

  @Override
  public String toString() {
    String articulosStr = iterarToString(articulos);
    String clientesStr = iterarToString(clientes);
    String internosStr = iterarToString(internos);
    String tecnicosStr = iterarToString(tecnicos);
    String servicioStr = iterarToString(servicios);
    String facturasStr = iterarToString(facturas);

    return "Empresa [getCostoCombustible()=" + getCostoCombustible() + "\ngetCostoViaje()=" + getCostoViaje()
        + "\ngetCostoHoraTecnico()=" + getCostoHoraTecnico() + "\ngetArticulos()=" + articulosStr
        + "\ngetTecnicos()=" + tecnicosStr + "\ngetClientes()=" + clientesStr + "\ngetInternos()=" + internosStr
        + "\ngetServicios()=" + servicioStr + "\ngetFacturas()=" + facturasStr + "]";
  }

  public double redondear(double valor) {
    return (double) (Math.round(valor * 100.0) / 100.0);
  }

  public double getCostoCombustible() {
    return costoCombustible;
  }

  public void setCostoCombustible(double nuevoCostoCombustible) throws Exception {
    double nuevoCC = redondear(nuevoCostoCombustible);

    if (0 >= nuevoCC) {
      throw new ValorException("Nuevo costo combustible debe ser mayor a 0");
    }

    costoCombustible = nuevoCC;
  }

  public double getCostoViaje() {
    return costoViaje;
  }

  public void setCostoViaje(double nuevoCostoViaje) throws Exception {
    double nuevoCV = redondear(nuevoCostoViaje);

    if (0 >= nuevoCV) {
      throw new ValorException("Nuevo costo viaje  debe ser mayor a 0");
    }

    costoViaje = nuevoCV;
  }

  public CostoHorasTecnico getCostoHoraTecnico() {
    return costoHorasTecnico;
  }

  public double getCostoHoraTecnico(Seniority seniority) {
    return costoHorasTecnico.getCHT(seniority);
  }

  public void setCostoHoraTecnico(Seniority seniority, double nuevoCHT) throws ValorException {
    costoHorasTecnico.setCHT(seniority, nuevoCHT);
  }

  public void setCostoHoraTecnico(double jr, double ssr, double sr) throws ValorException {
    costoHorasTecnico.setJunior(jr);
    costoHorasTecnico.setSemiSenior(ssr);
    costoHorasTecnico.setSenior(sr);
  }

  public void setCostoHoraTecnico(CostoHorasTecnico nuevoCHTObject) {
    costoHorasTecnico = nuevoCHTObject;
  }
  // FIN metodos EMPRESA

  // Metodos: ARTICULOS

  // PREVIO CREACION SERVICIO
  public boolean verificarArticulosSuficientes(TipoServicio ts) {

    if (ts == TipoServicio.INSTALACION) {
      Articulo cable = Empresa.getInstance().getArticulos(Cable.class);
      Articulo decoTV = Empresa.getInstance().getArticulos(DecodificadorTV.class);
      Articulo modem = Empresa.getInstance().getArticulos(Modem.class);
      Articulo divCoax = Empresa.getInstance().getArticulos(DivisorCoaxial.class);
      Articulo conCoax = Empresa.getInstance().getArticulos(ConectorCoaxial.class);

      return (cable.getStock() > 2 && decoTV.getStock() > 0 && modem.getStock() > 0 && divCoax.getStock() > 0
          && conCoax.getStock() > 5);
    }

    return true;
  }

  public ArrayList<Articulo> getArticulos() {
    return articulos;
  }

  public Articulo getArticulos(int sku) {
    Articulo articuloEncontrado = null;

    for (Articulo a : articulos) {
      if (a.getSKU() == sku) {
        articuloEncontrado = a;
        break;
      }
    }
    return articuloEncontrado;
  }

  public Articulo getArticulos(Articulo articulo) {
    Articulo articuloEncontrado = null;
    boolean empresaContieneArt = articulos.contains(articulo);

    if (empresaContieneArt) {
      articuloEncontrado = articulo;
    }

    return articuloEncontrado;
  }

  public Articulo getArticulos(Class<? extends Articulo> claseArticulo) {
    Articulo articuloEncontrado = null;

    for (Articulo a : articulos) {
      if (a.getClass() == claseArticulo) {
        return a;
      }
    }

    return articuloEncontrado;
  }

  public ArrayList<Articulo> getArticulosLowStock() {
    ArrayList<Articulo> lowStock = new ArrayList<Articulo>();

    for (Articulo a : articulos) {
      if (5 > a.getStock()) {
        lowStock.add(a);
      }
    }

    return lowStock;
  }

  public boolean agregarArticulo(Articulo a) {
    if (a == null || articulos.contains(a)) {
      return false;
    }

    return articulos.add(a);
  }
  // FIN Metodos: ARTICULOS

  // Metodos: SERVICIOS
  public ArrayList<Servicio> getServicios() {
    return servicios;
  }

  public Servicio getServicios(int nroServicio) {
    Servicio servicioEncontrado = null;

    for (Servicio s : servicios) {
      if (s.getNro() == nroServicio) {
        servicioEncontrado = s;
        break;
      }
    }
    return servicioEncontrado;
  }

  public Servicio getServicios(Servicio s) {
    Servicio servicioEncontrado = null;

    if (servicios.contains(s)) {
      servicioEncontrado = s;
    }

    return servicioEncontrado;
  }

  public boolean agregarServicio(Servicio s) {
    if (s == null || servicios.contains(s)) {
      return false;
    }
    return servicios.add(s);
  }

  // FIN Metodos: SERVICIOS

  // Metodos: FACTURAS
  public Factura getFacturas(int nroFactura) {
    Factura facturaEncontrada = null;

    for (Factura f : facturas) {
      if (f.getNro() == nroFactura) {
        facturaEncontrada = f;
        break;
      }
    }
    return facturaEncontrada;
  }

  public Factura getFacturas(Factura f) {
    Factura facturaEncontrada = null;

    if (facturas.contains(f)) {
      facturaEncontrada = f;
    }

    return facturaEncontrada;
  }

  public ArrayList<Factura> getFacturas() {
    return facturas;
  }

  public boolean agregarFactura(Factura f) {
    if (f == null || facturas.contains(f)) {
      return false;
    }
    return facturas.add(f);
  }

  // FIN Metodos: FACTURAS

  // Metodos: INTERNOS
  public ArrayList<Interno> getInternos() {
    return internos;
  }

  public Interno getInternos(Interno i) {
    if (internos.contains(i)) {
      return i;
    }
    return null;
  }

  public Interno getInternos(int nroLegajo) {
    for (Interno i : internos) {
      if (i.getLegajo() == nroLegajo) {
        return i;
      }
    }
    return null;
  }

  public boolean agregarInterno(Interno i) {
    if (i == null || internos.contains(i)) {
      return false;
    }
    return internos.add(i);
  }

  // Fin metodos INTERNOS

  // Metodos: TECNICOS
  public ArrayList<Tecnico> getTecnicos() {
    return tecnicos;
  }

  public Tecnico getTecnicos(int legajo) {
    Tecnico internoEncontrado = null;

    for (Tecnico t : tecnicos) {
      if (t.getLegajo() == legajo) {
        internoEncontrado = t;
        break;
      }
    }
    return internoEncontrado;
  }

  public Tecnico getTecnicos(Tecnico t) {
    Tecnico internoEncontrado = null;

    for (Tecnico tecIterado : tecnicos) {
      if (t.getLegajo() == tecIterado.getLegajo()) {
        internoEncontrado = t;
        break;
      }
    }
    return internoEncontrado;
  }

  public boolean agregarTecnico(Tecnico t) {
    if (t == null || tecnicos.contains(t)) {
      return false;
    }
    return tecnicos.add(t);
  }

  // FIN Metodos: TECNICOS

  // Metodos: CLIENTES
  public boolean agregarCliente(Cliente c) {
    if (c == null || clientes.contains(c)) {
      return false;
    }
    return clientes.add(c);
  }

  public Cliente getClientes(int nroCliente) {
    Cliente clienteEncontrado = null;

    for (Cliente c : clientes) {
      if (c.getNro() == nroCliente) {
        clienteEncontrado = c;
        break;
      }
    }
    return clienteEncontrado;
  }

  public Cliente getClientes(Cliente c) {
    Cliente clienteEncontrado = null;

    for (Cliente clienteIterado : clientes) {
      if (c.getNro() == clienteIterado.getNro()) {
        clienteEncontrado = c;
        break;
      }
    }
    return clienteEncontrado;
  }

  public ArrayList<Cliente> getClientes() {
    return clientes;
  }

  // FIN Metodos: CLIENTES

}
