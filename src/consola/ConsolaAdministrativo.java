package consola;

import personas.*;
import comercial.*;
import comercial.articulos.*;

import main.DateAux;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;

public class ConsolaAdministrativo extends Consola {
  private final Administrativo administrativo;

  public ConsolaAdministrativo(Administrativo administrativo, Scanner sc) {
    super(sc);
    this.administrativo = administrativo;
  }

  public void iniciar() {
    pantallaAdministrativo();
  }

  private void pantallaAdministrativo() {
    System.out.println("\n--- Menu ADMINISTRATIVO ---");
    System.out.println("1. Gestionar servicios finalizados");
    System.out.println("2. Visualizar facturas");
    System.out.println("0. Log out");
    System.out.print("=> ");

    int opcion = scIntParse( 0, 2);

    if (opcion == 0) {
      return;
    } else if (opcion == 1) {
      mGestionarServiciosAFacturar();
    } else if (opcion == 2) {
      mVisualizarFacturas();
    }

    pantallaAdministrativo();
  }

  private void mGestionarServiciosAFacturar() {
    ArrayList<Servicio> servicios = administrativo.getServiciosAFacturar();

    System.out.println("\n--- Gestionar servicios finalizados ---");
    System.out.println("---------------- Servicios ----------------");

    for (int i = 0; i < servicios.size(); i++) {
      Servicio s = servicios.get(i);
      int inicioT = s.getTurnoInicio(), finT = s.getTurnoFin();

      System.out
          .println("\t" + (i + 1) + ") Servicio nro " + s.getNro() + " [fecha servicio=" + s.getFecha() + ", Horario ["
              + DateAux.getHorarioCompleto(s.getTurno(), inicioT, finT) + ", cliente="
              + s.getCliente().getNombre() + "]");
    }

    if (0 >= servicios.size()) {
      System.out.println("\t<NO HAY SERVICIOS PENDIENTES DE FACTURAR>");
    }

    System.out.println("-------------------------------------------");

    System.out.print("Seleccione servicio visualizar (0. Volver) => ");
    int opcion = scIntParse( 0, servicios.size());

    if (opcion == 0) {
      return;
    }

    Servicio servicio = servicios.get(opcion - 1);
    mGestionarServicio(servicio);
  }

  private void mGestionarServicio(Servicio s) {
    if (s.isFacturado()) {
      System.out.println("Servicio ya facturado.");
      return;
    }

    int nro = s.getNro();
    Date fecha = s.getFecha();
    Cliente cliente = s.getCliente();
    TipoServicio ts = s.getTipoServicio();
    EstadoServicio es = s.getEstadoServicio();
    double tiempoTrabajado = s.getTiempoTrabajado();
    double costoViaje = s.getCostoViaje();
    boolean incluyeAlmuerzo = s.isIncluyeAlmuerzo();

    ArrayList<Costo> arts = s.getArticulos();
    int qArts = arts.size();
    ArrayList<Costo> otrosArts = s.getArticulosExtra();
    int qOtrosArts = otrosArts.size();
    ArrayList<Tecnico> tecs = s.getTecnicos();
    int qTecs = tecs.size();

    System.out.println("\n--- Gestionando servicio Nro " + nro + " ---");
    System.out.println("Fecha: " + fecha + "\nCliente: " + cliente + "\nTipoServicio: " + ts + "\nEstadoServicio: "
        + es + "\nTiempoTrabajado: " + tiempoTrabajado + "\nCostoViaje: " + costoViaje + "\nIncluyeAlmuerzo: "
        + (incluyeAlmuerzo ? "SI" : "NO") + "\nArticulos utilizados: " + qArts + "\nCantidad de otros costos: "
        + qOtrosArts + "\nTecnicos asignados: " + qTecs + "\nTotal servicio: $" + s.calcularTotalServicio());

    System.out.println("\nQue desea realizar?");
    System.out.println("1) Ver Tecnicos asignados");
    System.out.println("2) Ver articulos utilizados");
    System.out.println("3) Ver articulos extras utilizados");
    System.out.println("4) Agregar articulo extra");
    System.out.println("5) Facturar servicio");
    System.out.println("0) Volver a menu principal");
    System.out.print("=>");

    int opc = scIntParse( 0, 5);

    if (opc == 0) {
      return;
    } else if (opc == 1) {
      mMostrarTecnicos(s);
    } else if (opc == 2) {
      mMostrarArticulos(s);
    } else if (opc == 3) {
      mMostrarArticulosExtra(s);
    } else if (opc == 4) {
      mAnadirArticuloExtra(s);
    } else if (opc == 5) {
      mFacturarServicio(s);
      return;
    }
    mGestionarServicio(s);
  }

  private void mMostrarTecnicos(Servicio s) {
    ArrayList<Tecnico> tecnicosServicio = administrativo.getTecnicos(s);
    System.out.println("MOSTRANDO " + tecnicosServicio.size() + " TECNICOS DE SERVICIO Nro " + s.getNro());
    for (Tecnico t : tecnicosServicio) {
      System.out.println(
          "\tTecnico legajo: " + t.getLegajo() + " - Nombre: " + t.getNombre() + " - Seniority: " + t.getSeniority());
    }
  }

  private void mMostrarArticulos(Servicio s) {
    ArrayList<Costo> articulos = administrativo.getArticulos(s);
    System.out.println("MOSTRANDO " + articulos.size() + " ARTICULOS DE SERVICIO Nro " + s.getNro() + " total costo: $"
        + s.calcularCostoArticulos());
    for (Costo c : articulos) {
      System.out.println("\t" + c);
    }

    if (0 >= articulos.size()) {
      System.out.println("<NO HAY ARTICULOS>");
    }
  }

  private void mMostrarArticulosExtra(Servicio s) {
    ArrayList<Costo> articulos = administrativo.getArticulosExtra(s);
    System.out
        .println("MOSTRANDO " + articulos.size() + " ARTICULOS *Extra* DE SERVICIO Nro" + s.getNro() + " total costo: $"
            + s.calcularCostoArticulosExtra());
    for (Costo c : articulos) {
      System.out.println("\t" + c);
    }

    if (0 >= articulos.size()) {
      System.out.println("<NO HAY ARTICULOS>");
    }
  }

  private void mAnadirArticuloExtra(Servicio s) {
    System.out.println("Anadir articulo extra para el servicio numero " + s.getNro());
    System.out.print("Ingrese la descripcion para el articulo => ");
    String descripcion = sc.nextLine();

    System.out.print("Ingrese el costo para el articulo => ");
    double costo = scDoubleParse( 0.1);

    ArticuloExtra nuevoArticuloExtra = null;

    try {
      nuevoArticuloExtra = administrativo.crearArticuloExtra(descripcion, costo);
    } catch (Exception e) {
      System.out.println("ERROR CREANDO ARTICULO EXTRA: " + e.getMessage());
      return;
    }

    System.out.println("Confirmar agregar el articulo extra creado? (" + nuevoArticuloExtra + ")");
    System.out.print("(1 Confirmar, 0 cancelar)");

    int confirma = scIntParse( 0, 1);

    if (confirma == 1) {
      try {
        administrativo.agregarArticuloExtraServicio(nuevoArticuloExtra, 1, s);
      } catch (Exception e) {
        System.out.println("ERROR AGREGANDO ARTICULO EXTRA: " + e.getMessage());
      }
    }
  }

  private void mFacturarServicio(Servicio s) {
    System.out.println("CONFIRMA FACTURAR EL SERVICIO Nro " + s.getNro() + "?");
    System.out.println("La accion no es reversible");
    System.out.print("(1 Confirmar, 0 cancelar)");

    int confirmaFacturar = scIntParse( 0, 1);
    if (confirmaFacturar == 1) {
      Factura f = null;
      try {
        f = administrativo.facturarServicio(s);
      } catch (Exception e) {
        System.out.println("ERROR FACTURANDO SERVICIO: " + e.getMessage());
        return;
      }
      System.out
          .println(administrativo.getNombre() + " FACTURO el servicio nro " + s.getNro() + " => Factura numero: "
              + f.getNro());
      mGestionarServiciosAFacturar();
    } else {
      System.out.println("CANCELO LA FACTURACION");
    }
  }

  private void mVisualizarFacturas() {
    ArrayList<Factura> facturas = administrativo.getFacturas();

    System.out.println("\n----------- Visualizar Facturas -----------");
    System.out.println("----------------- Facturas ----------------");

    for (int i = 0; i < facturas.size(); i++) {
      Factura f = facturas.get(i);

      System.out.print("\t" + (i + 1) + ") ");
      System.out
          .println("Factura nro " + f.getNro() + " [facturado:" + DateAux.getDateString(f.getFecha()) + ", cliente: "
              + f.getServicio().getCliente().getNombre() + ", Servicio nro " + f.getServicio().getNro() + " del "
              + DateAux.getDateString(f.getServicio().getFecha()) + "]");
    }

    if (0 >= facturas.size()) {
      System.out.println("\t<NO HAY SERVICIOS FACTURADOS>");
    }

    System.out.println("-------------------------------------------");

    System.out.print("Seleccione factura para visualizar (0. Volver) => ");

    int opcion = scIntParse( 0, facturas.size());

    if (opcion != 0) {
      Factura f = facturas.get(opcion - 1);
      mostrarFactura(f);
      return;
    }
  }

  private void mostrarFactura(Factura f) {
    int nro = f.getNro();
    String s = f.getServicio().toStringShort();
    String c = f.getServicio().getCliente().toStringShort();
    double subtotal = f.calcularSubTotal(), ganancia = f.calcularGanancias(), iva = f.calcularIVA(),
        total = f.calcularTotal();
    String margen = f.calcularMargenStr();

    System.out.println("\n----------- Mostrando Factura " + nro + " -----------");
    System.out.println("\tServicio: " + s);
    System.out.println("\tCliente: " + c);
    System.out.println("\tSubtotal: $" + subtotal);
    System.out.println("\tTotal: $" + total);
    System.out.println("\tIVA: $" + iva);
    System.out.println("\tGanancia: $" + ganancia);
    System.out.println("\tRentabilidad de este servicio: " + margen);
    System.out.println("-------------------------------------------");
  }

}
