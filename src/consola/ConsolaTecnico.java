package consola;

import comercial.*;
import comercial.articulos.*;
import personas.*;

import java.util.Scanner;
import java.util.ArrayList;


public class ConsolaTecnico extends Consola {
  private Tecnico tecnico;

  public ConsolaTecnico(Tecnico tecnico, Scanner sc) {
    super(sc);
    this.tecnico = tecnico;
  }

  public void iniciar() {
    pantallaTecnico();
  }

  public void pantallaTecnico() {
    System.out.println("\n--- Menu TECNICO ---");
    System.out.println("1. Gestionar servicios asignados");
    System.out.println("0. Log out");
    System.out.print("=> ");

    int opcion = scIntParse(0, 1);

    if (opcion == 0) {
      return;
    } else {
      mListarServicios();
    }
    pantallaTecnico();
  }

  public void mListarServicios() {
    ArrayList<Servicio> servs = tecnico.getServiciosPendientes();

    System.out.println("\n----------- Visualizando Servicios -----------");

    for (int i = 0; i < servs.size(); i++) {
      Servicio s = servs.get(i);
      System.out.print("\t" + (i + 1) + ") ");
      System.out.println(s.toStringShorter());
    }

    if (0 >= servs.size()) {
      System.out.println("\t<NO HAY SERVICIOS PENDIENTES>");
    }

    System.out.println("-------------------------------------------");

    System.out.print("Seleccione servicio para mas opciones\n(0. Volver) => ");

    int opcion = scIntParse(0, servs.size());

    if (opcion != 0) {
      Servicio elegido = servs.get(opcion - 1);
      mVerServicio(elegido);
    }
  }

  public void mVerServicio(Servicio s) {
    String nroServicio = "\nNumero servicio " + s.getNro();
    String fechaCreacion = "\nCreado " + dateAux.getDateString(s.getFechaCreacion());
    String diaServicio = "\nFecha Servicio " + dateAux.getDateString(s.getFecha());
    String horarioServicio = "\nHorario " + dateAux.getHorarioCompleto(s);
    String cliente = "\nCliente" + s.getCliente().toStringShort();
    String articulos = "\nArticulos " + s.getArticulos().toString();
    String articulosExtra = "\nArticulos " + s.getArticulosExtra().toString();
    String almuerzo = "\nAlmuerzo " + (s.isIncluyeAlmuerzo() ? "INCLUIDO" : "no incluido");
    String estado = "\nEstado servicio " + s.getEstadoServicio();

    System.out.println("\n--- Mostrando servicio Nro " + s.getNro() + " ---");
    System.out.println(nroServicio + fechaCreacion + diaServicio + horarioServicio + cliente + articulos
        + articulosExtra + almuerzo + estado);

    EstadoServicio es = s.getEstadoServicio();

    System.out.println("\nQue desea realizar?");
    System.out.println("1) Anadir articulo utilizado");
    System.out.println("2) Anadir articulo extra");
    System.out.println("3) Declarar almuerzo");
    System.out.println("4) " + (es == EstadoServicio.PROGRAMADO ? "Comenzar servicio" : "Finalizar servicio"));
    System.out.println("0) Volver a menu principal");
    System.out.print("=>");

    int opc = scIntParse(0, 4);

    if (opc == 0) {
      return;
    }

    if (opc == 4) {
      mAvanzarEstadoServicio(s);

      if (s.getEstadoServicio() == EstadoServicio.EN_CURSO) {
        mVerServicio(s);
      }
      return;
    }

    if (es != EstadoServicio.EN_CURSO) {
      System.out.println("Para acceder a estas opciones primero debe comenzar el servicio (opcion 4)");
      mVerServicio(s);
      return;
    }

    if (opc == 1) {
      mAnadirArticulo(s);
    }

    if (opc == 3) {
      mToggleAlmuerzo(s);
    }

    mVerServicio(s);
  }

  public void mAvanzarEstadoServicio(Servicio s) {
    System.out.println("\nConfirmar avanzar el estado a "
        + (s.getEstadoServicio() == EstadoServicio.PROGRAMADO ? EstadoServicio.EN_CURSO
            : EstadoServicio.FINALIZADO)
        + "?");
    System.out.print("(1 para confirmar, 0 para cancelar) => ");
    int confirmaAvance = scIntParse(0, 1);

    if (confirmaAvance == 1) {
      try {
        tecnico.avanzarServicio(s);
        System.out.println("El estado de servicio cambio a: " + s.getEstadoServicio());
      } catch (Exception e) {
        System.out.println("ERROR AVANZANDO ESTADO SERVICIO: " + e.getMessage());
      }
    }
  }

  public void mToggleAlmuerzo(Servicio s) {
    try {
      tecnico.toggleAlmuerzoServicio(s);
    } catch (Exception e) {
      System.out.println("ERROR CAMBIANDO ALMUERZO SERVICIO: " + e.getMessage());
      return;
    }
    System.out.println("Has indicado " + (s.isIncluyeAlmuerzo() ? "" : "NO ") + "incluir almuerzo en el servicio");
  }

  public void mAnadirArticulo(Servicio s) {
    System.out.println("\nAnadir articulo a ingresar para el servicio numero " + s.getNro());
    ArrayList<Articulo> arts = tecnico.getArticulos();

    for (int i = 0; i < arts.size(); i++) {
      Articulo a = arts.get(i);
      System.out.println("" + (i + 1) + ") " + a.getDescripcion() + ", stock: " + a.getStock());
    }
    System.out.print("Seleccione su opcion (0 para cancelar) => ");
    int inputEleccionArticulo = scIntParse(0, arts.size());
    if (inputEleccionArticulo == 0) {
      System.out.println("Cancelo agregar articulo");
      return;
    }
    Articulo articuloSeleccionado = arts.get(inputEleccionArticulo - 1);

    System.out.print("Ingrese el cantidad utilizada => ");
    int cantidadEleccion = scIntParse(0, articuloSeleccionado.getStock());

    if (cantidadEleccion == 0) {
      System.out.println("Cancelo agregar articulo");
      return;
    }

    System.out.println("Confirmar agregar el articulo al servicio " + s.getNro() + "? ("
        + articuloSeleccionado.getDescripcion() + ")");
    System.out.print("(1 Confirmar, 0 cancelar) => ");

    int confirma = scIntParse(0, 1);

    if (confirma == 1) {
      try {
        tecnico.anadirArticuloServicio(s, cantidadEleccion, articuloSeleccionado);
        System.out.println(
            "Anadido " + cantidadEleccion + " unidades de " + articuloSeleccionado.getDescripcion());
      } catch (Exception e) {
        System.out.println("ERROR ANADIENDO ARTICULO A SERVICIO: " + e.getMessage());
        return;
      }
    }
  }

  public void mAnadirArticuloExtra(Servicio s) {
    System.out.println("\nAnadir articulo extra para el servicio numero " + s.getNro());
    System.out.print("Ingrese la descripcion para el articulo => ");
    String descripcion = sc.nextLine();

    System.out.print("Ingrese el costo para el articulo => ");
    double costo = scDoubleParse(0.1);

    ArticuloExtra nuevoArticuloExtra = tecnico.crearArticuloExtra(descripcion, costo);

    System.out.println("Confirmar agregar el articulo extra creado? (" + nuevoArticuloExtra + ")");
    System.out.print("(1 Confirmar, 0 cancelar)");

    int confirma = scIntParse(0, 1);

    if (confirma == 1) {
      try {
        tecnico.anadirArticuloExtraServicio(s, 1, nuevoArticuloExtra);
      } catch (Exception e) {
        System.out.println("ERROR ANADIENDO ARTICULO EXTRA A SERVICIO: " + e.getMessage());
        return;
      }
    }
  }
}
