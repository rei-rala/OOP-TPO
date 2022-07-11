package gui.usuarios;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comercial.*;
import comercial.articulos.*;
import excepciones.*;
import main.DateAux;
import personas.*;

@SuppressWarnings("serial")
public class GuiAdministrativo extends GuiUsuarioBase {
  private final Administrativo ADMINISTRATIVO;
  private JPanel panel;
  private JPanel subSubPanel;
  private JPanel pOpcionesAdministrativo;
  private JButton btnServicios;
  private JButton btnFacturas;

  public GuiAdministrativo(Administrativo administrativo) {
    super("Bienvenido " + administrativo.getNombre() + "<ADMINISTRATIVO>");
    this.ADMINISTRATIVO = administrativo;

    panel = new JPanel();
    add(panel, BorderLayout.CENTER);
    panel.setLayout(new GridLayout(0, 3, 0, 0));

    // Estetico
    panel.add(new JPanel());

    subSubPanel = new JPanel();
    panel.add(subSubPanel);
    subSubPanel.setLayout(new GridLayout(3, 0, 0, 0));

    subSubPanel.add(new JLabel(""));

    pOpcionesAdministrativo = new JPanel();
    subSubPanel.add(pOpcionesAdministrativo);
    pOpcionesAdministrativo.setLayout(new GridLayout(0, 1, 0, 0));

    // Estetico
    pOpcionesAdministrativo.add(new JLabel(""));

    btnServicios = new JButton("Ver servicios");
    btnServicios.addActionListener(this);
    pOpcionesAdministrativo.add(btnServicios);

    // Estetico
    pOpcionesAdministrativo.add(new JLabel(""));

    btnFacturas = new JButton("Ver Facturas");
    btnFacturas.addActionListener(this);
    pOpcionesAdministrativo.add(btnFacturas);

    // Estetico
    pOpcionesAdministrativo.add(new JLabel(""));
  }

  private Servicio obtenerServicio() throws Exception {
    Servicio eleccion = null;

    ArrayList<Servicio> servicios = ADMINISTRATIVO.getServiciosAFacturar();

    if (servicios.size() == 0) {
      throw new Exception("No hay servicios para facturar");
    }

    String opciones = "Seleccione un servicio por su numero:";
    for (Servicio s : servicios) {
      opciones += "\n" + s.getNro() + ") ";
      opciones += "Visita del " + DateAux.getInstance().getNombreDiaSemana(s.getFecha());
      opciones += " " + DateAux.getInstance().getDateString(s.getFecha());
      opciones += ", horario " + s.getHorarioServicio();
    }

    int nroServicio = guiValidarInt(opciones);
    eleccion = ADMINISTRATIVO.getServicio(nroServicio);

    if (eleccion == null) {
      throw new Exception("No se encontro servicio seleccionado para facturar");
    }

    if (servicios.contains(eleccion) == false) {
      throw new Exception("El servicio seleccionado todavia no puede visualizarse");
    }

    return eleccion;
  }

  private void menuServicio(Servicio s) throws Exception {
    if (s.isFacturado()) {
      throw new ServicioException("Servicio ya facturado, no pueden realizarse cambios");
    }

    int nro = s.getNro();
    String fecha = DateAux.getInstance().getDateString(s.getFecha());
    String cliente = s.getCliente().getNombre() + "(ID: " + s.getCliente().getNro() + ")";
    String horario = s.getHorarioServicio();
    TipoServicio ts = s.getTipoServicio();
    String tiempoTrabajado = DateAux.getInstance().getHorasFormat(s.getTiempoTrabajado());
    double costoViaje = s.getCostoViaje();
    boolean incluyeAlmuerzo = s.isIncluyeAlmuerzo();
    double totalServicio = s.calcularTotalServicio();

    double costoArts = 0;
    for (Costo c : s.getArticulos()) {
      costoArts += c.obtenerTotalCosto();
    }
    double costoOtrosArts = 0;
    for (Costo c : s.getArticulosExtra()) {
      costoOtrosArts += c.obtenerTotalCosto();
    }
    ArrayList<Tecnico> tecs = s.getTecnicos();
    int qTecs = tecs.size();

    String mensaje = "Mostrando servicio nro " + nro + " del " + fecha + " [" + horario + "]";
    mensaje += "\nCliente: " + cliente + "\nTipoServicio: " + ts + "\nTiempoTrabajado: " + tiempoTrabajado
        + "hs \nCostoViaje: $" + costoViaje + "\nIncluyeAlmuerzo: " + (incluyeAlmuerzo ? "SI" : "NO")
        + "\nArticulos utilizados: $" + costoArts + "\nOtros costos: $" + costoOtrosArts + "\nTecnicos asignados: "
        + qTecs + "\nTotal servicio: $" + totalServicio;

    mensaje += "\n\nQue desea realizar?";
    mensaje += "\n\t1) Ver Tecnicos asignados";
    mensaje += "\n\t2) Ver articulos utilizados";
    mensaje += "\n\t3) Ver articulos extras utilizados";
    mensaje += "\n\t4) Agregar articulo extra";
    mensaje += "\n\t5) Facturar servicio";

    int opcion = guiValidarInt(mensaje, 1, 5);

    if (opcion == 5) {
      facturarServicio(s);
      return;
    }

    if (opcion == 1) {
      mostrarTecnicos(s);
    } else if (opcion == 2) {
      mostrarArticulos(s);
    } else if (opcion == 3) {
      mostrarArticulosExtra(s);
    } else if (opcion == 4) {
      anadirArticuloExtra(s);
    }

    // Volvera a mostrar el servicio mientras lo estemos editando y no lo facturemos
    // o cancelemos la accion
    menuServicio(s);
  }

  private void editarServicio() throws Exception {
    Servicio s = obtenerServicio();
    menuServicio(s);
  }

  private void mostrarTecnicos(Servicio s) {
    ArrayList<Tecnico> tecs = s.getTecnicos();
    String mensaje = "Tecnicos asignados:";
    for (Tecnico t : tecs) {
      mensaje += "\nLegajo " + t.getLegajo() + ": " + t.getNombre() + " - Seniority: " + t.getSeniority();
    }

    alert(mensaje);
  }

  private String concatenarArticulos(ArrayList<Costo> arrayCostos) {
    String preMensaje = "";

    for (Costo c : arrayCostos) {
      Recurso r = c.getArticulo();
      preMensaje += "\n\t-" + c.getCantidad();
      preMensaje += " x " + r.getDescripcion();
      preMensaje += " = $ " + c.obtenerTotalCosto();
    }

    return preMensaje;
  }

  private void mostrarArticulos(Servicio s) {
    ArrayList<Costo> articulos = s.getArticulos();
    String mensaje = "Articulos utilizados: " + articulos.size() + " ($" + s.calcularCostoArticulos() + ")";
    mensaje += concatenarArticulos(articulos);

    alert(mensaje);
  }

  private void mostrarArticulosExtra(Servicio s) {
    ArrayList<Costo> articulos = s.getArticulosExtra();
    String mensaje = "Articulos extra utilizados: " + articulos.size() + " ($" + s.calcularCostoArticulosExtra() + ")";
    mensaje += concatenarArticulos(articulos);

    alert(mensaje);
  }

  private void anadirArticuloExtra(Servicio s) throws Exception {
    String descripcion = input("Ingrese la descripcion del articulo extra");
    double costo = guiValidarDouble("Ingrese el costo del articulo extra");

    ArticuloExtra ae = ADMINISTRATIVO.crearArticuloExtra(descripcion, costo);
    String msgConfirm = "Anadir articulo extra " + ae + " a Servicio nro " + s.getNro() + "?";
    msgConfirm += "\nACCION NO REVERSIBLE";

    if (confirm(msgConfirm)) {
      ADMINISTRATIVO.agregarArticuloExtraServicio(ae, 1, s);
      alert("Se anadio el articulo extra " + ae + " a Servicio nro " + s.getNro());
    } else {
      alert("Cancelado por usuario");
    }
  }

  private void facturarServicio(Servicio s) throws Exception {
    String msgConfirm = "Facturar servicio nro " + s.getNro() + "?";

    if (confirm(msgConfirm)) {
      Factura f = ADMINISTRATIVO.facturarServicio(s);
      alert("Servicio " + s.getNro() + " facturado con exito, numero de factura: " + f.getNro());
    } else {
      alert("Cancelado por usuario");
    }
  }

  private Factura obtenerFactura() throws Exception {
    Factura eleccion = null;

    ArrayList<Factura> facturas = ADMINISTRATIVO.getFacturas();

    String opciones = "Seleccione una factura por su numero:";
    for (Factura f : facturas) {
      Servicio s = f.getServicio();
      opciones += "\n" + f.getNro() + ") ";
      opciones += "Facturado: " + DateAux.getInstance().getDateString(f.getFecha());
      opciones += " - Cliente: " + s.getCliente().getNombre() + "(ID: " + s.getCliente().getNro() + ")";
      opciones += " - Servicio " + s.getNro() + " del " + DateAux.getInstance().getDateString(s.getFecha());
      opciones += " - Total: $" + f.calcularTotal();
    }

    int nroFactura = guiValidarInt(opciones);
    eleccion = ADMINISTRATIVO.getFacturas(nroFactura);

    if (eleccion == null) {
      throw new Exception("No se encontro factura seleccionada para facturar");
    }

    return eleccion;
  }

  private void mostrarFactura() throws Exception {
    Factura f = obtenerFactura();

    int nro = f.getNro();
    String s = f.getServicio().toStringShorter(),
        c = f.getServicio().getCliente().getNombre() + "(ID: " + f.getServicio().getCliente().getNro() + ")",
        margen = f.calcularMargenStr();
    double subtotal = f.calcularSubTotal(), ganancia = f.calcularGanancias(), iva = f.calcularIVA(),
        total = f.calcularTotal();

    String mensaje = "Mostrando factura numero " + nro;
    mensaje += "\n\nServicio asociado: " + s + "\n";
    mensaje += "\nCliente: " + c;
    mensaje += "\nTotal Servicio: $" + subtotal;
    mensaje += "\nIVA: $" + iva;
    mensaje += "\nSubtotal Factura: $" + total;
    mensaje += "\nGanancia: $" + ganancia;
    mensaje += "\nRentabilidad de este servicio: " + margen;

    alert(mensaje);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      if (e.getSource() == btnServicios) {
        editarServicio();
      } else if (e.getSource() == btnFacturas) {
        mostrarFactura();
      }

    } catch (Exception exception) {
      guiExceptionHandler(exception);
    }
  }

}
