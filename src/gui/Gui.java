package gui;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import empresa.Empresa;
import comercial.*;
import comercial.articulos.*;
import excepciones.*;
import personas.*;
import gui.homes.*;
import main.DateAux;

public class Gui {
  static Gui instancia;
  public Empresa empresa = Empresa.getInstance();
  private static JFrame frame;
  private Interno usuarioLogeado;

  /**
   * Create the application.
   */
  public Gui() {
  }

  /**
   * Initialize the contents of the frame.
   */
  public void initialize() {

    frame = new JFrame();
    frame.setBounds(0, 0, 800, 600);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.add(new LogIn());
    frame.setVisible(true);
  }

  public static Gui getInstance() {
    if (instancia == null) {
      instancia = new Gui();
    }
    return instancia;
  }

  public void refrescarEmpresa() {
    empresa = Empresa.getInstance();
  }

  public Interno getUsuarioLogeado() {
    return usuarioLogeado;
  }

  public void setUsuarioLogeado(Interno usuarioLogeado) {
    this.usuarioLogeado = usuarioLogeado;
  }

  public double redondearDouble(Double d) {
    return Empresa.getInstance().redondear(d);
  }

  public int validarInt(String dato) throws ValorException {
    try {
      return Integer.parseInt(dato);
    } catch (Exception e) {
      throw new ValorException("Proporcionar valor numerico");
    }
  }

  public double validarDouble(String dato) throws ValorException {
    try {
      return redondearDouble(Double.parseDouble(dato));
    } catch (Exception e) {
      throw new ValorException("Debe ingresar un valor numerico valido");
    }

  }

  public void logout() {
    String usuarioLogeadoName = getUsuarioLogeado().getNombre();
    if (JOptionPane.showConfirmDialog(null, "Confirma cerrar sesion de " + usuarioLogeadoName) == 0) {
      setUsuarioLogeado(null);
      changePanel(new LogIn());
    }
  }

  public void removerActionListeners(JButton jb) {
    for (ActionListener al : jb.getActionListeners()) {
      jb.removeActionListener(al);
    }
  }

  public void errorHandler(Exception e) {
    Class<? extends Exception> eCls = e.getClass();
    String title = "Se produjo un error";
    String message = e.getMessage();
    int icon = JOptionPane.WARNING_MESSAGE;

    e.printStackTrace();
    if (eCls == GuiException.class) {
      title = "Informacion";
      icon = JOptionPane.INFORMATION_MESSAGE;
    } else if (eCls == CredencialException.class) {
      title = "Credenciales no validas";
      message = "Error de credenciales: " + message;
    } else if (eCls == ServicioException.class) {
      message = "Error de servicio: " + message;
    } else if (eCls == StockException.class) {
      message = "Error de stock: " + message;
    } else if (eCls == ValorException.class) {
      message = "Error de valor: " + message;
    } else if (eCls == AsignacionException.class) {
      message = "Error de asignacion: " + message;
    } else {
      message = "Error inesperado";
    }
    JOptionPane.showMessageDialog(null, message, title, icon);
  }

  public void errorHandler(String errorMessage) {
    errorHandler(new Exception(errorMessage));
  }

  public void changePanel(JPanel jp) {
    frame.getContentPane().setVisible(false);
    frame.getContentPane().removeAll();
    frame.getContentPane().add(jp);

    frame.getContentPane().setVisible(true);
  }

  public String getSimpleName(Object o) {
    return o.getClass().getSimpleName();
  }

  public String getTituloArticulo(Articulo a) {
    return getSimpleName(a) + " Stock: " + (int) a.getStock() + " Costo: " + a.getCosto();
  }

  public String getServicioTitle(Servicio s) {
    String id = "Nro: " + s.nro;
    String datoServicio;
    if (s.isFacturado()) {
      datoServicio = "FACTURADO";
    } else {
      datoServicio = s.getEstadoServicio().name();
    }

    return id + ": " + datoServicio;
  }

  public String getNro(Servicio s) {
    return "" + s.nro;
  }

  public String getFecha(Servicio s) {
    return "" + s.getFecha();
  }

  public String getTipoServicio(Servicio s) {
    return "" + s.getTipoServicio();
  }

  public String getEstadoServicio(Servicio s) {
    return "" + s.getEstadoServicio();
  }

  public String getTecnicosAsignados(Servicio s) {
    ArrayList<Tecnico> tecs = s.getTecnicos();
    String tecnicosAsignados = tecs.size() + " asignados a este servicio";

    for (int i = 0; i < tecs.size(); i++) {
      Tecnico t = tecs.get(i);
      tecnicosAsignados += "\n" + (i + 1) + ". " + t.getNombre() + "(" + t.getLegajo() + " - " + t.getSeniority()
          + ")";
    }

    return tecnicosAsignados;
  }

  public String getIncluyeAlmuerzo(Servicio s) {
    return s.isIncluyeAlmuerzo() ? "Almuerzo incluido" : "No incluye almuerzo";
  }

  public String getArticulos(Servicio s) {
    ArrayList<Costo> costos = s.getArticulos();
    String articulosUtilizados = costos.size() + " utilizados";

    for (int i = 0; i < costos.size(); i++) {
      Costo c = costos.get(i);
      articulosUtilizados += "\n" + (i + 1) + ". " + c.getArticulo().getDescripcion() + " x " + c.getCantidad()
          + " = $" + c.obtenerTotalCosto();
    }

    return articulosUtilizados;
  }

  public String getOtrosArticulos(Servicio s) {
    ArrayList<Costo> otrosCostos = s.getOtrosCostos();
    String articulosUtilizados = otrosCostos.size() + " utilizados";

    for (Costo c : otrosCostos) {
      articulosUtilizados += "\t\n" + c.getArticulo().getDescripcion() + " x " + c.getCantidad() + " = $"
          + c.obtenerTotalCosto();
    }

    return articulosUtilizados;
  }

  public String getTotalServicio(Servicio s) {
    return "TOTAL: " + s.obtenerTotalServicio();
  }

  public String getNro(Factura f) {
    return "" + f.getNro();
  }

  public String getClienteShort(Cliente c) {
    String nroCliente = "ID: " + c.getNro();
    String nombre = "Nombre: " + c.getNombre();

    return nroCliente + " " + nombre;
  }

  public String getCliente(Cliente c) {
    String clShort = getClienteShort(c);
    String direccion = "Direccion: " + c.getDireccion();
    String telefono = "Telefono: " + c.getTelefono();
    return clShort + ":\n" + direccion + "\n" + telefono;
  }

  public String getCliente(Servicio s) {
    Cliente c = s.getCliente();
    return getCliente(c);
  }

  public String getCliente(Factura f) {
    Cliente c = f.getServicio().getCliente();
    return getCliente(c);
  }

  public String limpiarStrTecnicos(ArrayList<Tecnico> alT) {
    String tecnicos = "Cantidad " + alT.size();
    for (Tecnico t : alT) {
      tecnicos += "\n\t" + t.getNombre() + " (" + t.getLegajo() + " | " + t.getSeniority() + ")";
    }
    return tecnicos;
  }

  public String limpiarStrArticulos(ArrayList<Costo> alC) {
    String encabezado = "Cantidad " + alC.size();
    double totalizado = 0;
    String costos = "";
    for (Costo c : alC) {
      costos += "\t" + c.getCantidad() + "x" + c.getArticulo().getDescripcion() + "= $" + c.obtenerTotalCosto() + '\n';
      totalizado += c.obtenerTotalCosto();
    }
    return encabezado + " ($" + totalizado + ")\n" + costos;
  }

  public String limpiarStrArticulos(Servicio s) {
    ArrayList<Costo> alA = s.getArticulos();
    return limpiarStrArticulos(alA);
  }

  public String limpiarStrOtrosArticulos(Servicio s) {
    ArrayList<Costo> alO = s.getOtrosCostos();
    return limpiarStrArticulos(alO);
  }

  public String getServicioStr(Factura f) {
    Servicio s = f.getServicio();
    String horarioDesde = "Desde: " + DateAux.obtenerHorario(s.getTurno(), s.getturnoInicio());
    String horarioHasta = "Hasta: " + DateAux.obtenerHorario(s.getTurno(), s.getturnoFin() + 1);

    String nro = "Servicio numero: " + getNro(s) + "\n";
    String fecha = "Fecha: " + getFecha(s) + "\n";
    String tipo = "Tipo: " + getTipoServicio(s) + "\n";
    String turno = "Turno " + s.getTurno() + " [" + horarioDesde + " - " + horarioHasta + "]\n";
    String tecnicos = "Tecnicos: " + limpiarStrTecnicos(s.getTecnicos()) + "\n";
    String articulos = "\nArticulos: " + limpiarStrArticulos(s.getArticulos()) + "\n";
    String otrosArts = "Extras: " + limpiarStrArticulos(s.getOtrosCostos()) + "\n";
    String tiempoTrabajado = "Tiempo: " + s.getTiempoTrabajado() + "\n";
    String costoViaje = "Viaticos: " + s.getCostoViaje() + "\n";
    String incluyeAlmuerzo = "Almuerzo tecnico/s: " + (s.isIncluyeAlmuerzo() ? "INCLUYE" : "No incluye") + "\n";
    String costoTotal = "Total servicio: " + s.obtenerTotalServicio();

    return nro + fecha + tipo + turno + tecnicos + articulos + otrosArts + tiempoTrabajado + incluyeAlmuerzo
        + costoViaje + costoTotal;
  }

  public String getSubtotal(Factura f) {
    return "$ " + empresa.redondear(f.calcularSubTotal());
  }

  public String getIVA(Factura f) {
    return "$ " + empresa.redondear(f.calcularIVA());
  }

  public String getTotal(Factura f) {
    return "$ " + empresa.redondear(f.calcularTotal());
  }

  public String getGanancia(Factura f) {
    return "$ " + empresa.redondear(f.calcularGanancias());
  }

  public String getMargen(Factura f) {
    return empresa.redondear(f.calcularMargen() * 100) + "%";
  }

  public String obtenerTituloFactura(Factura f) {
    String nro = "" + f.getNro();
    String fecha = "" + f.getFecha();
    String total = "$ " + f.calcularTotal();

    return nro + " - " + fecha + " - " + total;
  }

  private void adicionarArticulo(Articulo a, int cantidad, Servicio s) throws Exception {
    s.anadirArticulo(a, cantidad);
  }

  public void adicionarCosto(Interno i, Servicio s) {
    try {
      if (i.getClass() != Tecnico.class) {
        throw new CredencialException("Permisos insuficientes");
      }

      Articulo cable = empresa.getArticulos(Cable.class);
      Articulo conector = empresa.getArticulos(ConectorCoaxial.class);
      Articulo divisor = empresa.getArticulos(DivisorCoaxial.class);
      Articulo deco = empresa.getArticulos(DecodificadorTV.class);
      Articulo modem = empresa.getArticulos(Modem.class);

      String op1 = "1. " + cable.getDescripcion() + " (Stock " + cable.getStock() + ")\n";
      String op2 = "2. " + conector.getDescripcion() + " (Stock " + conector.getStock() + ")\n";
      String op3 = "3. " + divisor.getDescripcion() + " (Stock " + divisor.getStock() + ")\n";
      String op4 = "4. " + deco.getDescripcion() + " (Stock " + deco.getStock() + ")\n";
      String op5 = "5. " + modem.getDescripcion() + " (Stock " + modem.getStock() + ")\n";

      String mensajeOpcion = "Opciones:\n" + op1 + op2 + op3 + op4 + op5;
      String opcion = JOptionPane.showInputDialog(null, mensajeOpcion, "Nuevo articulo - servicio " + s.nro,
          JOptionPane.INFORMATION_MESSAGE);

      if (opcion == null) {
        throw new GuiException("Cancelado por usuario");
      }

      if (opcion.trim().length() == 0) {
        throw new GuiException("Cancelado por usuario");
      }

      String cantidad = JOptionPane.showInputDialog("Ingrese cantidad para el articulo (vacio para cancelar)");

      if (cantidad == null) {
        throw new GuiException("Cancelado por usuario");
      }

      if (cantidad.trim().length() == 0) {
        throw new GuiException("Cancelado por usuario");
      }

      int cantidadParseada = validarInt(cantidad);

      Articulo nuevoArticulo;

      switch (opcion) {

        case "1":
          nuevoArticulo = cable;
          break;
        case "2":
          nuevoArticulo = conector;
          break;
        case "3":
          nuevoArticulo = divisor;
          break;
        case "4":
          nuevoArticulo = deco;
          break;
        case "5":
          nuevoArticulo = modem;
          break;
        default:
          throw new CostoException("Seleccion no valida");
      }

      adicionarArticulo(nuevoArticulo, cantidadParseada, s);

      JOptionPane.showMessageDialog(null, "A�adido nuevo articulo " + getSimpleName(nuevoArticulo));

    } catch (Exception e) {
      if (e.getClass() == StockException.class || e.getClass() == GuiException.class) {
        errorHandler(e);
        return;
      }
      errorHandler("Se produjo un error:\n" + e.getMessage());
      System.out.println(e);
    }
  }

  public void adicionarOtroCosto(Interno i, Servicio s) {
    try {
      if (i.getClass() != Administrativo.class && i.getClass() != Tecnico.class) {
        throw new CredencialException("Permisos insuficientes");
      }
      String desc = JOptionPane.showInputDialog("Ingrese descripcion para nuevo producto (Vacio para cancelar)");

      if (desc == null) {
        throw new GuiException("Cancelado por usuario");
      }

      if (desc.trim().length() == 0) {
        throw new GuiException("Cancelado por usuario");
      }

      String costo = JOptionPane.showInputDialog("Ingrese costo para " + desc + "(vacio para cancelar)");

      if (costo == null) {
        throw new GuiException("Cancelado por usuario");
      }

      if (costo.trim().length() == 0) {
        throw new GuiException("Cancelado por usuario");
      }

      double costoParseado = validarDouble(costo);
      ArticuloExtra nuevoAE = new ArticuloExtra(desc, costoParseado);
      s.anadirOtroCostos(nuevoAE, 1);
      JOptionPane.showMessageDialog(null, "A�adido nuevo costo " + nuevoAE.toString());

    } catch (Exception e) {
      String msg = e.getMessage();

      if (msg.contains("Cancelado")) {
        errorHandler(e);
        return;
      }
      errorHandler("Se produjo un error:\n" + msg);
      System.out.println(e);
    }
  }

  public ArrayList<Tecnico> obtenerTecnicos() {
    return empresa.getTecnicos();
  }

  public String listarTecnicos() {
    ArrayList<Tecnico> tecs = obtenerTecnicos();
    String tecnicos = "";

    for (int i = 0; i < tecs.size(); i++) {
      Tecnico t = tecs.get(i);
      tecnicos += "\n" + (i + 1) + ") " + t.getNombre() + ", L:" + t.getLegajo() + ", Seniority"
          + t.getSeniority();
    }

    return tecnicos;
  }
}
