package consola;

import personas.Callcenter;
import personas.Cliente;
import personas.Tecnico;

import java.util.ArrayList;
import java.util.Scanner;

import comercial.TipoServicio;
import comercial.Servicio;
import comercial.articulos.*;
import main.DateAux;
import agenda.*;

public class ConsolaCallcenter extends Consola {
  private Callcenter callcenter;

  public ConsolaCallcenter(Callcenter callcenter, Scanner sc) {
    super(sc);
    this.callcenter = callcenter;
  }

  @Override
  public void iniciar() {
    pantallaCallcenter();
  }

  private void pantallaCallcenter() {
    System.out.println("\n--- Menu CALLCENTER ---");
    System.out.println("1. Crear servicios para clientes");
    System.out.println("2. Gestionar servicios pendientes por callcenter");
    System.out.println("3. Gestionar stock de articulos");
    System.out.println("0. Log out");
    System.out.print("=> ");

    int opcion = scIntParse(0, 3);

    if (opcion == 0) {
      return;
    }
    if (opcion == 1) {
      mCrearServicioCliente();
    }
    if (opcion == 2) {
      mListarServicios();
    }
    if (opcion == 3) {
      mSeleccionArticulo();
    }
    pantallaCallcenter();
  }

  private void mCrearServicioCliente() {
    Cliente seleccion = null;
    try {
      seleccion = mSeleccionCliente();
    } catch (Exception e) {
      System.out.println("Error en seleccion de cliente: " + e.getMessage());
      return;
    }

    if (seleccion == null) {
      System.out.println("No se obtuvo cliente valido.");
      return;
    }

    try {
      mSeleccionDiaCliente(seleccion);
    } catch (Exception e) {
      System.out.println("Error en asignacion de cliente a servicio: " + e.getMessage());
    }
  }

  private Cliente mSeleccionCliente() {
    System.out.println("\n--------- Listando clientes ---------");
    System.out.println("-------------- Seleccione -------------");

    ArrayList<Cliente> clientes = callcenter.getClientesSinServicios();
    for (int i = 0; i < clientes.size(); i++) {
      Cliente c = clientes.get(i);
      System.out.println((i + 1) + ") Numero " + c.getNro() + " - " + c.getNombre());
    }

    if (clientes.size() == 0) {
      System.out.println("No hay clientes!");
    }

    System.out.print("(0 para cancelar) => ");
    int opcion = scIntParse(0, clientes.size());

    if (opcion == 0) {
      return null;
    } else {
      return clientes.get(opcion - 1);
    }
  }

  private void mSeleccionDiaCliente(Cliente c) {
    System.out.println("\n--------- Creando servicio ---------");
    System.out.println("Cliente numero: " + c.getNro() + " | " + c.getNombre());
    System.out.println("---------- Seleccione DIA ---------");

    Agenda a = c.getAgenda();

    System.out.println(a.getDiasShort());
    System.out.print("(0 para cancelar) => ");

    int opcion = scIntParse(0, a.getDias().size());
    Dia diaSeleccionado = a.getDias().get(opcion - 1);

    if (opcion == 0) {
      pantallaCallcenter();
      return;
    }

    mCrearServicio(c, diaSeleccionado);
  }

  private void mCrearServicio(Cliente c, Dia dia) {
    ArrayList<FraccionTurno> fraccionesTurno = dia.obtenerFraccionesTurno();

    System.out.println("\n--------- Creando servicio ---------");
    System.out.println("Cliente numero: " + c.getNro() + " nombre" + c.getNombre());
    System.out.println("Dia seleccionado: " + dia.getNombreDiaSemana() + " " + DateAux.getDateString(dia.getFecha()));
    System.out.println("---------- Seleccione TIPO DE SERVICIO ---------");

    System.out.println("1. Servicio de instalacion (requiere al menos 1 hora)");
    System.out.println("2. Servicio de reparacion");
    System.out.println("0. Atras");

    int opcion = scIntParse(0, 2);

    if (opcion == 0) {
      mSeleccionDiaCliente(c);
      return;
    }

    TipoServicio ts = opcion == 1 ? TipoServicio.INSTALACION : TipoServicio.REPARACION;

    System.out.println("Seleccione fraccion de turno DESDE: ");
    for (int i = 0; i < fraccionesTurno.size(); i++) {
      FraccionTurno ft = fraccionesTurno.get(i);
      System.out.println((i + 1) + ") " + ft.getHorario());
    }

    System.out.print("(0 para volver atras) => ");
    int opcionDesde = scIntParse(0, fraccionesTurno.size());

    if (opcionDesde == 0) {
      mSeleccionDiaCliente(c);
      return;
    }

    ArrayList<FraccionTurno> fraccionesTurnoHasta = new ArrayList<FraccionTurno>();
    for (int i = 0; i < fraccionesTurno.size(); i++) {
      if (opcionDesde > i) {
        continue;
      }
      FraccionTurno ft = fraccionesTurno.get(i);
      fraccionesTurnoHasta.add(ft);
    }

    System.out.println("Seleccione fraccion de turno HASTA: ");

    for (int i = 0; i < fraccionesTurnoHasta.size(); i++) {
      FraccionTurno ft = fraccionesTurnoHasta.get(i);
      System.out.println((i + 1) + ") " + ft.getHorario());
    }

    System.out.print("(0 para volver atras) => ");
    int opcionHasta = scIntParse(0, fraccionesTurnoHasta.size());

    if (opcionHasta == 0) {
      mSeleccionDiaCliente(c);
      return;
    }

    FraccionTurno ftDesde = fraccionesTurno.get(opcionDesde - 1);
    FraccionTurno ftHasta = fraccionesTurnoHasta.get(opcionHasta - 1);

    System.out.println("\n---------------------------------");
    System.out.println("CREARA SERVICIO PARA CLIENTE " + c.getNro() + " (" + c.getNombre() + ")");
    System.out.println("Datos servicio:");
    System.out.println("Tipo servicio: " + ts.toString());
    System.out.println("Dia: " + dia.getNombreDiaSemana() + " " + DateAux.getDateString(dia.getFecha()));
    System.out.println("Desde: " + ftDesde.getHorario());
    System.out.println("Hasta: " + ftHasta.getHorario());
    System.out.println("---------------------------------");

    System.out.println("Confirmar?");
    System.out.print("(1 confirmar, 0 cancelar) => ");

    int opcionConfirmar = scIntParse(0, 1);

    if (opcionConfirmar == 0) {
      System.out.println("Creacion de servicio cancelado!");
      mSeleccionDiaCliente(c);
      return;
    }

    Servicio nuevoServicio = null;
    try {
      nuevoServicio = callcenter.crearServicio(dia.getFecha(), ts, ftDesde.getTurno(), ftDesde.getNro(),
          ftHasta.getNro());
    } catch (Exception e) {
      System.out.println("Error al crear servicio: " + e.getMessage());
      System.out.println("Intente nuevamente");
      mSeleccionDiaCliente(c);
      return;
    }

    if (nuevoServicio == null) {
      System.out.println("Error al crear servicio");
      mSeleccionDiaCliente(c);
      return;
    }

    try {
      callcenter.asignarServicio(nuevoServicio, c);
    } catch (Exception e) {
      System.out.println("Error al agregar servicio al cliente: " + e.getMessage());
      System.out.println("Intente nuevamente");
      mSeleccionDiaCliente(c);
      return;
    }

    System.out.println("Servicio creado numero" + nuevoServicio.getNro() + " con exito!");
    System.out.println("Asignado a cliente " + nuevoServicio.getCliente().getNombre());

    pantallaCallcenter();
  }

  private void mListarServicios() {
    ArrayList<Servicio> servs = callcenter.getServiciosPendientes();

    System.out.println("\n--------- Listando  servicios ---------");
    System.out.println("-------------- Seleccione -------------");
    for (int i = 0; i < servs.size(); i++) {
      Servicio s = servs.get(i);
      String idCliente = s.getCliente() == null ? "<SIN CLIENTE ASIGNADO>"
          : "numero " + s.getCliente().getNro() + " nombre "
              + s.getCliente().getNombre();
      System.out.println((i + 1) + ") Servicio " + s.getEstadoServicio() + " numero: " + s.getNro() + ", fecha: "
          + DateAux.getDateString(s.getFecha()) + ", horario " + DateAux.getHorarioCompleto(s) + " | "
          + " cliente: " + idCliente);
    }

    if (servs.size() == 0) {
      System.out.println("No hay servicios pendientes!");
    }

    System.out.print("(0 para volver atras) => ");
    int opcion = scIntParse(0, servs.size());

    if (opcion == 0) {
      System.out.println("Seleccion cancelada");
      pantallaCallcenter();
      return;
    }

    Servicio seleccionado = servs.get(opcion - 1);
    mSeleccionServicio(seleccionado);
  }

  private void mSeleccionServicio(Servicio s) {
    System.out.println("\n--------- Seleccionando servicio ---------");

    System.out.println(s.toStringShorter());
    System.out.println("----------- Seleccione accion -----------");
    System.out.println("1) Asignar cliente");
    System.out.println("2) Asignar tecnico");
    System.out.println("3) Enviar servicio a tecnicos");
    System.out.println("4) Cancelar servicio");
    System.out.println("0) Volver atras");

    System.out.print("Ingrese opcion => ");
    int opcion = scIntParse(0, 4);

    if (opcion == 0) {
      mListarServicios();
      return;
    }

    if (opcion == 1) {
      if (s.getCliente() != null) {
        System.out.println("Servicio ya tiene cliente asignado!");
        mSeleccionServicio(s);
        return;
      }
      mAsignacionCliente(s);
    }
    if (opcion == 2) {
      mAsignacionTecnico(s);
    }
    if (opcion == 3) {
      mEnviarServicioATecnicos(s);
    }
    if (opcion == 4) {
      mCancelarServicio(s);
    }
  }

  private void mAsignacionCliente(Servicio s) {
    Cliente c = mSeleccionCliente(s);

    if (c == null) {
      System.out.println("Seleccion cancelada");
      mSeleccionServicio(s);
      return;
    }

    System.out.println("Confirmar la asignacion del cliente nro " + c.getNro() + " al servicio numero "
        + s.getNro() + "?");

    int opcionConfirmar = scIntParse(0, 1);

    if (opcionConfirmar == 0) {
      System.out.println("Asignacion de cliente cancelada!");
      mSeleccionServicio(s);
      return;
    }
    mAsignarClienteServicio(c, s);
  }

  private Cliente mSeleccionCliente(Servicio s) {
    System.out.println("\n--------- Listando Clientes ---------");
    System.out.println("Se muestran clientes disponibles");
    System.out.println("-------------- Seleccione -------------");

    ArrayList<Cliente> clientes = callcenter.getClientesSinServicios();
    for (int i = 0; i < clientes.size(); i++) {
      Cliente c = clientes.get(i);
      System.out.println(
          (i + 1) + ") Nro" + c.getNro() + " - nombre: " + c.getNombre());
    }

    if (clientes.size() == 0) {
      System.out.println("No hay clientes disponibles para el servicio seleccionado!");
    }

    System.out.print("(0 para cancelar) => ");
    int opcion = scIntParse(0, clientes.size());

    if (opcion == 0) {
      return null;
    }
    return clientes.get(opcion - 1);
  }

  private void mAsignarClienteServicio(Cliente c, Servicio s) {
    System.out.println("\n---- Asignando  servicio a cliente ----");
    System.out.println("Servicio: " + s.getNro() + " - " + s.getFecha() + " [" + s.getHorarioServicio() + "]");
    System.out.println("Cliente: nro" + c.getNro() + ", Nombre" + c.getNombre());

    System.out.println("-------------- Seleccione -------------");

    try {
      callcenter.asignarServicio(s, c);
    } catch (Exception e) {
      System.out.println("Error al asignar cliente al servicio: " + e.getMessage());
      System.out.println("Intente nuevamente");
      mSeleccionServicio(s);
      return;
    }
    System.out.println("Cliente asignado con exito!");
    mSeleccionServicio(s);
  }

  private void mAsignacionTecnico(Servicio s) {
    Tecnico t = mSeleccionTecnico(s);

    if (t == null) {
      System.out.println("Seleccion cancelada");
      mSeleccionServicio(s);
      return;
    }

    System.out.println("Confirmar la asignacion del tecnico con legajo " + t.getLegajo() + " al servicio numero "
        + s.getNro() + "?");

    int opcionConfirmar = scIntParse(0, 1);

    if (opcionConfirmar == 0) {
      System.out.println("Asignacion de tecnico cancelada!");
      mSeleccionServicio(s);
      return;
    }
    mAsignarTecnicoAServicio(t, s);
  }

  private Tecnico mSeleccionTecnico(Servicio s) {
    System.out.println("\n--------- Listando tecnicos ---------");
    System.out.println("Se muestran tecnicos disponibles segun el servicio proporcionado");
    System.out.println("-------------- Seleccione -------------");

    ArrayList<Tecnico> tecnicos = callcenter.getTecnicosDisponibles(s);
    for (int i = 0; i < tecnicos.size(); i++) {
      Tecnico t = tecnicos.get(i);
      System.out.println(
          (i + 1) + ") Legajo " + t.getLegajo() + " - nombre: " + t.getNombre() + " <" + t.getSeniority() + ">");
    }

    if (tecnicos.size() == 0) {
      System.out.println("No hay tecnicos disponibles para el servicio seleccionado!");
    }

    System.out.print("(0 para cancelar) => ");
    int opcion = scIntParse(0, tecnicos.size());

    if (opcion == 0) {
      return null;
    }
    return tecnicos.get(opcion - 1);
  }

  private void mAsignarTecnicoAServicio(Tecnico t, Servicio s) {
    System.out.println("\n---- Asignando  servicio a tecnico ----");
    System.out.println("Servicio: " + s.getNro() + " - " + s.getFecha() + " [" + s.getHorarioServicio() + "]");
    System.out.println("Tecnico: Legajo" + t.getLegajo() + ", Nombre" + t.getNombre() + " <" + t.getSeniority() + ">");

    System.out.println("-------------- Seleccione -------------");

    try {
      callcenter.asignarServicio(s, t);
    } catch (Exception e) {
      System.out.println("Error al asignar tecnico al servicio: " + e.getMessage());
      System.out.println("Intente nuevamente");
      mSeleccionServicio(s);
      return;
    }
    System.out.println("Tecnico asignado con exito!");
    mSeleccionServicio(s);
  }

  private void mEnviarServicioATecnicos(Servicio s) {
    try {
      callcenter.preValidarLiberacion(s);
    } catch (Exception e) {
      System.out.println("No se puede liberar el servicio: " + e.getMessage());
      System.out.println("Intente nuevamente");
      mSeleccionServicio(s);
      return;
    }

    System.out.println("\n--------- Avanzando estado de servicio ---------");
    System.out.println(s.toStringShorter());
    System.out.println("-------------- Seleccione -------------");

    System.out.println("Confirma enviar este servicio a los tecnicos? La accion no es reversible");
    System.out.print("(1 confirmar, 0 cancelar) => ");

    int opcionConfirmar = scIntParse(0, 1);

    if (opcionConfirmar == 0) {
      System.out.println("Envio de servicio cancelado!");
      mSeleccionServicio(s);
      return;
    }

    try {
      callcenter.liberarServicioCallcenter(s);
      System.out.println("Servicio enviado a tecnicos!");
      mListarServicios();
    } catch (Exception e) {
      System.out.println("Error al enviar servicio a tecnicos: " + e.getMessage());
      System.out.println("Intente nuevamente");
      mSeleccionServicio(s);
      return;
    }
  }

  private void mCancelarServicio(Servicio s) {
    System.out.println("\n--------- Cancelando servicio ---------");
    System.out.println(s.toStringShorter());
    System.out.println("-------------- Seleccione -------------");

    System.out.println("Confirma cancelar este servicio? La accion no es reversible");
    System.out.print("(1 confirmar, 0 cancelar) => ");

    int opcionConfirmar = scIntParse(0, 1);

    if (opcionConfirmar == 0) {
      System.out.println("Cancelacion de servicio cancelada!");
    } else {
      try {
        callcenter.cancelarServicio(s);
        System.out.println("Servicio cancelado con exito!");
        mListarServicios();
        return;
      } catch (Exception e) {
        System.out.println("Error al cancelar servicio: " + e.getMessage());
        System.out.println("Intente nuevamente");

      }
    }
    mSeleccionServicio(s);
  }

  private void mSeleccionArticulo() {
    ArrayList<Articulo> articulos = callcenter.getArticulos();

    System.out.println("\n--- Gestionar stock de articulos ---");
    System.out.println("------------- Articulos ------------");

    for (int i = 0; i < articulos.size(); i++) {
      Articulo a = articulos.get(i);

      System.out.println((i + 1) + ") SKU=" + a.getSKU() + " - " + a.getDescripcion() + " - " + a.getStock());
    }

    System.out.print("(0 para cancelar) => ");
    int opcion = scIntParse(0, articulos.size());

    if (opcion == 0) {
      return;
    }

    mEdicionArticulo(articulos.get(opcion - 1));
  }

  private void mEdicionArticulo(Articulo articulo) {
    System.out.println("\nEDITANDO : ");
    System.out.println("SKU=" + articulo.getSKU() + " - " + articulo.getDescripcion() + " - " + articulo.getStock());
    System.out.println("Ingrese nuevo stock (Actual: " + articulo.getStock()
        + "), 0 para mantener stock anterior o -1 para volver al menu anterior: ");
    int stock = scIntParse(-1, Integer.MAX_VALUE);

    if (stock == -1) {
      System.out.println("Cancelado");
      return;
    }

    if (stock == 0) {
      System.out.println("Stock sin cambios");
      return;
    }

    System.out.println("Confirmar nuevo stock " + stock + " para " + articulo + "?");
    System.out.print("(0. No, 1. Si) =>");
    int opcion = scIntParse(0, 1);

    if (opcion == 1) {
      try {
        callcenter.setStockArticulo(articulo, stock);
      } catch (Exception e) {
        System.out.println("ERROR MODIFICANDO STOCK DE ARTICULO: " + e.getMessage());
        return;
      }
      System.out.println(
          "Stock de '" + articulo.getDescripcion() + " modificado a " + stock);
    }

    pantallaCallcenter();
  }

}
