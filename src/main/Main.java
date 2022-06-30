package main;

import empresa.*;
import personas.*;
import comercial.*;
import comercial.articulos.*;
import agenda.*;

import gui.Gui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
  static Empresa e = Empresa.getInstance();
  static ConsoleHelper ch = new ConsoleHelper();
  static Scanner sc = new Scanner(System.in);
  static boolean isRunning = true;
  static Interno i;

  public static void main(String[] args) throws Exception {

    // Al instanciar los objetos se anaden directamente a Empresa.
    // VARIABLES ARTICULOS PRUEBA
    Cable c = new Cable(30, 0);
    ConectorCoaxial cc = new ConectorCoaxial(50, 100);
    DivisorCoaxial dc = new DivisorCoaxial(50, 80);
    Modem m = new Modem(200, 120);
    DecodificadorTV dtv = new DecodificadorTV(40, 120);

    // VARIABLES PERSONAS PRUEBA
    Admin ADMIN = new Admin("Hector V.O.N.", "");
    Administrativo adm1 = new Administrativo("Administrativo UNO", "");
    Tecnico tec1 = new Tecnico("tec Senior", "", Seniority.SENIOR);
    Callcenter cc1 = new Callcenter("Callcenter UNO", "");
    Tecnico tec2 = new Tecnico("tec Junior", "passTJR", Seniority.JUNIOR);
    Cliente cl1 = new Cliente("Cliente UNO");
    Cliente cl2 = new Cliente("Cliente DOS");
    Cliente cl3 = new Cliente("Cliente TRES");

    System.out.println(e);

    try {
      // -------------------------------------------------
      // PRUEBA COMPLETA DE PROCESO
      // Por rol, en orden

      runWorkflowAdmin();
      runWorkflowCallcenter();
      runWorkflowTecnico();
      runWorkflowAdministrativo();

      System.out.println("\n//////////////////////////////////////////");
      System.out.println("/// Fin codigo prueba flujo de trabajo ///");
      System.out.println("//////////////////////////////////////////");
    }
    // Si entra en este catch, el error en el workflow es grave.
    catch (Exception e) {
      System.out.println("NOT HANDLED ERROR EN WORKFLOW TEST");
      e.printStackTrace();
    }
    // -------------------------------------------------

    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Gui.getInstance().initialize();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    while (isRunning) {
      System.out.println("\n\nBienvenido al sistema de gestión de la empresa");
      Interno i = logIn();
      pantallaPrincipal(i);
    }

    // GUI

    close();
  }

  /**
   * El administrador administra stock y valores de articulos
   * No participa en el flujo de un Servicio.
   * 
   * @throws Exception Este metodo solo arroja error si la excepcion no fue
   *                   correctamente manejada
   * 
   */
  public static void runWorkflowAdmin() throws Exception {
    // OMITIENDO LOG IN
    Admin testAdm = new Admin("", "");
    // POR ORDEN DE EJECUCION, CABLE SERIA sku=1 (inicia con stock 0 en linea 30)
    // No podrian crearse servicios de instalacion si se mantuviera en 0.
    Articulo a = testAdm.buscarArticulos(1);
    testAdm.setStockArticulo(a, 99);

    testAdm.setCostoHoraTecnico(Seniority.SENIOR, 300);
  }

  /**
   * Workflow normal de un CALLCENTER
   * 
   * 0) Se logea.
   * 1) Crear servicios siempre y cuando los parametros sean validos.
   * 2) Asignar servicios a un cliente (En tanto el cliente NO tenga servicios
   * vigentes y tenga disponible los
   * turnos del servicio).
   * 3) Asignar servicios a un tecnico (En tanto el tecnico tenga disponible los
   * turnos del servicio).
   * 4) Antes de liberar un servicio, puede cancelarlo
   * 5) Liberar servicio (Debe tener tecnico y cliente asignados), el servicio
   * pasa a manos de los tecnicos asignados.
   * 
   * @throws Exception Este metodo solo arroja error si la excepcion no fue
   *                   correctamente manejada dentro del procedimiento normal del
   *                   rol
   * 
   */
  public static void runWorkflowCallcenter() throws Exception {
    System.out.println("-----------------------------------------");
    System.out.println("------ INICIO WORKFLOW  CALLCENTER ------");
    System.out.println("-----------------------------------------");

    // Omitiendo log in
    Callcenter testCC = new Callcenter("", "");
    // Callcenter crea servicios
    Cliente clienteCC = testCC.getClientes(1);
    // tambien obtenemos un tecnico de prueba
    Tecnico tecnicoCC = testCC.getTecnicos().get(0);
    Date testDate = DateAux.getToday();

    // Creando servicio con parametros invalidos
    try {
      testCC.crearNuevoServicioServicio(testDate, TipoServicio.INSTALACION, Turno.MANANA, 0, -1);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento crear servicio parametros no validos 1  => " + e.getMessage());
    }
    try {
      testCC.crearNuevoServicioServicio(testDate, TipoServicio.INSTALACION, Turno.TARDE, 0, 13);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento crear servicio parametros no validos 2  => " + e.getMessage());
    }

    // Creando un servicio valido y asignandolo al cliente
    Servicio s1 = testCC.crearNuevoServicioServicio(testDate, TipoServicio.INSTALACION, Turno.MANANA, 0, 2);
    // probando liberar servicio sin cliente
    try {
      testCC.liberarServicioCallcenter(s1);
    } catch (Exception e) {
      System.out
          .println("***** Control funcionando: Forzado intento liberar servicio falta CLIENTE  => " + e.getMessage());
    }

    // Asignando servicio a cliente
    testCC.asignarServicio(s1, clienteCC);
    System.out.println("ASIGNADO EXITOSAMENTE CLIENTE 'clienteCC'");

    // probando liberar servicio sin tecnicos
    try {
      testCC.liberarServicioCallcenter(s1);
    } catch (Exception e) {
      System.out
          .println("***** Control funcionando: Forzado intento liberar servicio falta TECNICO  => " + e.getMessage());
    }

    // Creando servicio con sobreturnos con Servicio s1
    Servicio sobreturnado = testCC.crearNuevoServicioServicio(testDate, TipoServicio.INSTALACION, Turno.MANANA, 1, 3);
    // Creando otro servicio sin sobreturno con Servicio s1
    Servicio s2 = testCC.crearNuevoServicioServicio(testDate, TipoServicio.INSTALACION, Turno.TARDE, 0, 2);

    // probando verificacion sobreturno
    boolean estaDisponible = clienteCC.verificarDisponibilidad(sobreturnado);
    if (!estaDisponible) {
      System.out.println("***** Control funcionando: Verifica CLIENTE con turno ocupado ");
    }

    // asignando pese a sobreturno
    try {
      testCC.asignarServicio(sobreturnado, clienteCC);
    } catch (Exception e) {
      System.out
          .println("***** Control funcionando: Forzado intento asignar CLIENTE con sobreturno  => " + e.getMessage());
    }

    // asignando a cliente que ya cuenta con un turno
    try {
      testCC.asignarServicio(s2, clienteCC);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento asignar CLIENTE con turno vigente  => " + e.getMessage());
    }

    // Asignando servicio a tecnico
    testCC.asignarServicio(s1, tecnicoCC);
    System.out.println("ASIGNADO EXITOSAMENTE TECNICO 'tecnicoCC'");

    // Probando asignar el mismo tecnico al servicio
    try {
      testCC.asignarServicio(s1, tecnicoCC);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento asignar TECNICO al mismo servicio  => " + e.getMessage());
    }
    // Probando asignar sobreturno tecnico
    try {
      testCC.asignarServicio(sobreturnado, tecnicoCC);
    } catch (Exception e) {
      System.out
          .println("***** Control funcionando: Forzado intento asignar TECNICO sobreturnado  => " + e.getMessage());
    }

    // Liberando servicio
    testCC.liberarServicioCallcenter(s1);
    System.out.println("LIBERADO EXITOSAMENTE SERVICIO 's1'");

    // Probando cancelar servicio en curso
    try {
      testCC.cancelarServicio(s1);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Forzado intento cancelar servicio  => " + e.getMessage());
    }

    // Cancelando servicio
    testCC.cancelarServicio(sobreturnado);
    System.out.println("CANCELADO EXITOSAMENTE SERVICIO 'sobreturnado'");

    System.out.println("\n------- EXITO WORKFLOW CALLCENTER -------");
  }

  /**
   * Workflow normal de un TECNICO
   * 
   * 0) Se logea.
   * 1) Visualiza los servicios asignados liberados desde callcenter
   * 2) Inicializa un servicio.
   * 3) Sobre un servicio EN_CURSO puede añadir articulos, añadir otros articulos,
   * declarar si almorzo.
   * 4) Finaliza servicio
   * 5) Tras marcar finalizado, no podra editar el servicio.
   * 
   * @throws Exception Este metodo solo arroja error si la excepcion no fue
   *                   correctamente manejada dentro del procedimiento normal del
   *                   rol
   * 
   */
  public static void runWorkflowTecnico() throws Exception {
    System.out.println("\n-----------------------------------------");
    System.out.println("-------- INICIO WORKFLOW TECNICO --------");
    System.out.println("-----------------------------------------");

    // centrado en el rol tecnico
    Tecnico testTec = new Tecnico("Workflow test Tecnico", "", Seniority.JUNIOR);

    // Preasignando un servicio
    Callcenter auxCC = new Callcenter("", "");
    Servicio auxS = auxCC.crearNuevoServicioServicio(DateAux.getToday(), TipoServicio.INSTALACION, Turno.MANANA, 0, 2);
    auxCC.asignarServicio(auxS, new Cliente("Cliente workflow test"));
    auxCC.asignarServicio(auxS, testTec);
    auxCC.liberarServicioCallcenter(auxS);

    // Inicio tareas TECNICO
    ArrayList<Servicio> servsAsignados = testTec.getServiciosAsignados();
    System.out.println("\ntestTec tiene estos servicios: ");
    for (Servicio s : servsAsignados) {
      System.out.println("\tServicio nro: " + s.getNro() + ", Cliente: " + s.getCliente().getNombre());
    }
    System.out.println("Fin servicios testTec\n");

    // Selecciona un servicio: lo haria desde get(Servicio)
    Servicio servTrabajado = testTec.getServiciosAsignados().get(0);

    // intenta finalizar servicio sin iniciarlo
    try {
      testTec.finalizarServicio(servTrabajado);
    } catch (Exception e) {
      System.out
          .println("***** Control funcionando: Forzado intento finalizar servicio sin iniciarlo  => " + e.getMessage());
    }

    // intenta añadir articulo sin estar iniciado
    try {
      Articulo ejemploArticulo = testTec.getArticulos(1);
      testTec.anadirMaterialServicio(servTrabajado, 50, ejemploArticulo);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento añadir articulo a servicio sin iniciarlo  => " + e.getMessage());
    }
    // intenta modificar almuerzo de servicio sin estar iniciado
    try {
      testTec.toggleAlmuerzoServicio(servTrabajado);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento modificar almuerzo de servicio sin iniciarlo  => "
              + e.getMessage());
    }

    // inicia ejecucion servicio
    testTec.ejecutarServicio(servTrabajado);
    System.out.println("Servicio n" + servTrabajado.getNro() + ": INICIADO por -> " + testTec.getNombre());

    // intenta añadir articulo invalido
    try {
      Articulo ejemploArticulo = testTec.getArticulos(0);
      testTec.anadirMaterialServicio(servTrabajado, 50, ejemploArticulo);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento añadir articulo INVALIDO a servicio  => " + e.getMessage());
    }

    // intenta añadir articulo con stock insuficiente
    try {
      Articulo ejemploArticulo = testTec.getArticulos(1);
      testTec.anadirMaterialServicio(servTrabajado, 999999999, ejemploArticulo);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento añadir articulo STOCK INSUFICIENTE a servicio  => "
              + e.getMessage());
    }

    // anade articulo valido
    Articulo ejemploArticulo = testTec.getArticulos(1);
    testTec.anadirMaterialServicio(servTrabajado, 5, ejemploArticulo);

    // finaliza servicio
    testTec.finalizarServicio(servTrabajado);
    System.out.println("Servicio n" + servTrabajado.getNro() + ": FINALIZADO por -> " + testTec.getNombre());

    // Intenta editar servicio marcado finalizado
    try {
      testTec.toggleAlmuerzoServicio(servTrabajado);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento editar almuerzo a servicio FINALIZADO => "
              + e.getMessage());
    }

    // El tecnico tenia un solo servicio
    // tras su finalizacion, no deberia aparecer en su listado.
    ArrayList<Servicio> servsAsignados2 = testTec.getServiciosAsignados();
    System.out.println("\ntestTec tiene estos servicios (No deberia haber Servicios): ");
    for (int i = 0; i < servsAsignados2.size(); i++) {
      Servicio s = servsAsignados2.get(i);
      System.out.println("\tServicio nro: " + s.getNro() + ", Cliente: " + s.getCliente().getNombre());
    }
    System.out.println("Fin servicios testTec\n");

    System.out.println("\n-------- EXITO WORKFLOW TECNICO ---------");
  }

  /**
   * Workflow normal de un ADMINISTRATIVO
   * 
   * 0) Se logea
   * 1) Visualiza los servicios finalizados por los tecnicos
   * 2) Puede optar por anadir articulos extras (campo libre)
   * 3) Puede facturar un servicio.
   * 4) Tras facturar un servicio, puede visualizar las facturas y sus datos
   * 
   * @throws Exception Este metodo solo arroja error si la excepcion no fue
   *                   correctamente manejada dentro del procedimiento normal del
   *                   rol
   * 
   */
  public static void runWorkflowAdministrativo() throws Exception {
    System.out.println("\n-----------------------------------------");
    System.out.println("---- INICIO WORKFLOW  ADMINISTRATIVO ----");
    System.out.println("-----------------------------------------");

    // omitiendo login
    Administrativo admTest = new Administrativo("Usuario Administrativo test", "");

    // Listando Servicios finalizados
    ArrayList<Servicio> serviciosAFacturar = admTest.getServiciosAFacturar();

    System.out.println("El administrativo obtiene los servicios finalizados");
    System.out.println("\n--------- Servicios disponibles para facturar ----------");
    for (int i = 0; i < serviciosAFacturar.size(); i++) {
      Servicio s = serviciosAFacturar.get(i);
      System.out.println("\t" + (i + 1) + ") Servicio " + s.getNro() + ", Fecha: " + s.getFecha() + ", estado: "
          + s.getEstadoServicio());
    }
    System.out.println("-------------- Fin servicios finalizados ---------------");

    // Selecciona un servicio finalizado para visualizarlo o agregar articulos extra
    Servicio s = serviciosAFacturar.get(0);
    System.out.println("Servicio obtenido por Administrativo: Nro " + admTest.verServicio(s).nro);

    // Crea un articulo extra
    ArticuloExtra aeTest = admTest.crearArticuloExtra("Extra creado por el administrativo", 5000);
    System.out.println("admTest creo nuevo: " + aeTest);

    // Intenta crear un articulo con valor invalido
    try {
      admTest.crearArticuloExtra("Debo fallar por tener costo '-1'", -1);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Forzado intento crear Articulo Extra - parametros no validos => "
          + e.getMessage());
    }

    // Intenta anadir un articulo y coloca cantidad no valida
    try {
      ArticuloExtra aeErrorTest = admTest
          .crearArticuloExtra("Fallara en un paso siguiente por informar cantidad negativa", 1);
      admTest.agregarArticuloExtraServicio(aeErrorTest, -1, s);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento anadir A Extra con cantidad negativa => " + e.getMessage());
    }

    // Anadiendo exitosamente Art extra al servicio
    admTest.agregarArticuloExtraServicio(aeTest, 1, s);
    System.out.println("Extras del servicio tras modificacion del administrativo " + s.getOtrosCostos());

    // Decide facturar el servicio
    Factura f = admTest.facturarServicio(s);
    System.out.println("admTest finalizo servicio nro " + s.getNro() + " y obtuvo factura Numero:" + f.getNro());

    // Intenta anadir Art extra al servicio ya facturado
    try {
      admTest.agregarArticuloExtraServicio(aeTest, 1, s);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento anadir A Extra a un servicio ya facturado => " + e.getMessage());
    }

    // Visualiza el margen obtenido de la factura
    System.out
        .println("admTest visualiza margen de factura nro" + f.getNro() + ": " + admTest.getMargenFacturaString(f));

    System.out.println("\n----- EXITO WORKFLOW ADMINISTRATIVO -----");
  }

  public static Interno logIn() throws Exception {
    System.out.println("INICIE SESION");
    Interno i;

    do {
      System.out.print("Ingrese numero de legajo (-1 para salir) => ");
      int legajo = ch.scIntParse(sc, -1);

      if (legajo == -1) {
        close();
      }

      System.out.print("Ingrese contrasena de legajo " + legajo + " => ");
      String contrasena = sc.nextLine();

      i = e.login(legajo, contrasena);
    } while (i == null);

    System.out.println("Inicio sesion como " + i.getNombre() + " «" + i.getClass().getSimpleName() + "»");
    return i;
  }

  public static void pantallaPrincipal(Interno i) throws Exception {
    if (i.getClass() == Admin.class) {
      Admin a = (Admin) i;
      pantallaAdmin(a);
    } else if (i.getClass() == Administrativo.class) {
      Administrativo adm = (Administrativo) i;
      pantallaAdministrativo(adm);
    } else if (i.getClass() == Tecnico.class) {
      Tecnico tec = (Tecnico) i;
      pantallaTecnico(tec);
    } else if (i.getClass() == Callcenter.class) {
      Callcenter cc = (Callcenter) i;
      pantallaCallcenter(cc);
    } else {
      System.out.println("No se puede acceder a esta pantalla");
      isRunning = false;
    }
  }

  public static void pantallaAdmin(Admin a) throws Exception {
    System.out.println("\n--- Menu ADMIN ---");
    System.out.println("1. Editar costo combustible");
    System.out.println("2. Editar costo viaje");
    System.out.println("3. Editar stock y costo de articulo");
    System.out.println("4. Editar valor hora de los Tecnicos");
    System.out.println("0. Logout");

    System.out.print("Ingrese opcion => ");
    int opcion = ch.scIntParse(sc, 0);

    if (opcion == 0) {
      logIn();
      return;
    } else if (opcion == 1) {
      mCostoCombustible(a);
    } else if (opcion == 2) {
      mCostoViaje(a);
    } else if (opcion == 3) {
      mEdicionArticulos(a);
    } else if (opcion == 4) {
      mEdicionHorasTecnico(a);
    } else {
      System.out.println("Opcion invalida");
      pantallaAdmin(a);
    }
  }

  public static void mCostoCombustible(Admin a) throws Exception {
    double costoAnterior = e.getCostoCombustible();

    while (true) {
      System.out.println("\nEDITANDO COSTO COMBUSTIBLE");
      System.out.print("Ingrese nuevo costo (Actual: " + costoAnterior + ") o 0 para volver al menu anterior => ");
      double costo = ch.scDoubleParse(sc, 0);

      if (costo == 0) {
        pantallaAdmin(a);
        break;
      }

      a.modificarCostoCombustible(costo);
      System.out.print("Costo de combustible modificado a " + costo);
      break;
    }
    pantallaAdmin(a);
  }

  public static void mCostoViaje(Admin a) throws Exception {
    double costoAnterior = e.getCostoViaje();

    while (true) {
      System.out.println("\nEDITANDO COSTO DE VIAJE");
      System.out.print("Ingrese nuevo costo (Actual: " + costoAnterior + ") o 0 para volver al menu anterior => ");
      double costo = ch.scDoubleParse(sc, 0);

      if (costo == 0) {
        pantallaAdmin(a);
        break;
      }

      a.modificarCostoViaje(costo);
      System.out.println("Costo de viaje modificado a " + costo);
      break;
    }
    pantallaAdmin(a);
  }

  public static void mEdicionArticulos(Admin a) throws Exception {
    ArrayList<Articulo> articulos = a.buscarArticulos();

    System.out.println("\nEDITANDO STOCK Y COSTOS DE LOS ARTICULOS");
    System.out.println("---------------- Articulos ----------------");

    for (int i = 0; i < articulos.size(); i++) {
      Articulo articulo = articulos.get(i);
      System.out.println((i + 1) + ". " + articulo);
    }

    System.out.print("Ingrese numero de articulo a editar (0 para volver al menu anterior): => ");
    int opcion = ch.scIntParse(sc, 0, articulos.size());

    if (opcion == 0) {
      pantallaAdmin(a);
      return;
    }

    Articulo articulo = articulos.get(opcion - 1);
    mEdicionArticulo(a, articulo);
  }

  public static void mEdicionArticulo(Admin a, Articulo articulo) throws Exception {
    System.out.println("\nEDITANDO STOCK Y COSTOS DE " + articulo.getDescripcion());
    System.out.print("Ingrese nuevo stock (Actual: " + articulo.getStock()
        + "), 0 para mantener stock anterior o -1 para volver al menu anterior =>");
    int stock = ch.scIntParse(sc, 0);

    if (stock == -1) {
      pantallaAdmin(a);
      return;
    }

    System.out.println("Ingrese nuevo costo (Actual: " + articulo.getCosto()
        + "), 0 para mantener stock anterior o -1 para volver al menu anterior: ");
    double costo = ch.scDoubleParse(sc, (double) 0);

    if (costo == -1) {
      pantallaAdmin(a);
      return;
    }

    if (stock == 0) {
      stock = articulo.getStock();
    }

    if (costo == 0) {
      costo = articulo.getCosto();
    }

    System.out.println("Confirmar nuevo stock " + stock + " y costo " + costo + " para " + articulo + "?");
    System.out.print("(0. No, 1. Si) =>");
    int opcion = ch.scIntParse(sc, 0, 1);

    if (opcion == 1) {
      a.editarCifrasArticulo(articulo, stock, costo);
      System.out.println("Stock y costo modificados");
    }
    pantallaAdmin(a);
  }

  public static void mEdicionHorasTecnico(Admin a) throws Exception {
    CostoHorasTecnico cht = e.getCostoHoraTecnico();

    System.out.println("\n---EDITANDO VALOR HORA DE LOS TECNICOS---");
    System.out.println("-- Valor horas actual  segun seniority --");
    System.out.println(cht);

    System.out.println("Seleccione Seniority a editar");
    System.out.println("1. Junior");
    System.out.println("2. SemiSenior");
    System.out.println("3. Senior");
    System.out.println("0. Volver al menu anterior");
    System.out.print("=>");

    int seniority = ch.scIntParse(sc, 0, 3);

    if (seniority == 3) {
      mEdicionHorasTecnico(a, Seniority.SENIOR);
    } else if (seniority == 2) {
      mEdicionHorasTecnico(a, Seniority.SEMI_SENIOR);
    } else if (seniority == 1) {
      mEdicionHorasTecnico(a, Seniority.JUNIOR);
    }
    pantallaAdmin(a);
  }

  public static void mEdicionHorasTecnico(Admin a, Seniority s) throws Exception {
    System.out.println("\nEDITANDO HORAS DE " + s);
    System.out.print("Ingrese nuevo costo hora (Actual: " + e.getCostoHoraTecnico(s)
        + "), 0 para sin cambios, -1 para volver al menu anterior =>");
    double costoHora = ch.scDoubleParse(sc, -1);

    if (costoHora == -1) {
      return;
    }
    if (costoHora == 0) {
      costoHora = e.getCostoHoraTecnico(s);
    }

    a.setCostoHoraTecnico(s, costoHora);
    System.out.println("Costo hora de " + s + " modificado a " + costoHora);
  }

  public static void pantallaAdministrativo(Administrativo adm) throws Exception {
    System.out.println("\n--- Menu ADMINISTRATIVO ---");
    System.out.println("1. Gestionar servicios finalizados");
    System.out.println("2. Visualizar facturas");
    System.out.println("0. Log out");
    System.out.print("=> ");

    int opcion = ch.scIntParse(sc, 0, 2);

    if (opcion == 0) {
      logIn();
    } else if (opcion == 1) {
      mGestionarserviciosAFacturar(adm);
    } else if (opcion == 2) {
      mVisualizarFacturas(adm);
    }
  }

  public static void mGestionarserviciosAFacturar(Administrativo adm) throws Exception {
    ArrayList<Servicio> servicios = adm.getServiciosAFacturar();

    System.out.println("\n--- Gestionar servicios finalizados ---");
    System.out.println("---------------- Servicios ----------------");

    for (int i = 0; i < servicios.size(); i++) {
      Servicio servicio = servicios.get(i);
      System.out.println((i + 1) + ". " + servicio);
    }

    System.out.println("-------------------------------------------");

    System.out.print("Seleccione servicio visualizar (0. Volver) => ");

    int opcion = ch.scIntParse(sc, 0, servicios.size());

    if (opcion == 0) {
      pantallaAdministrativo(adm);
      return;
    }

    Servicio servicio = servicios.get(opcion - 1);
    mGestionarServicio(adm, servicio);
  }

  public static void mGestionarServicio(Administrativo adm, Servicio s) throws Exception {
    if (s.isFacturado() || s.isEnPoderTecnico()) {
      System.out.println("Servicio ya facturado o en poder de tecnico/s");
      mGestionarserviciosAFacturar(adm);
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
    ArrayList<Costo> otrosArts = s.getOtrosCostos();
    int qOtrosArts = otrosArts.size();
    ArrayList<Tecnico> tecs = s.getTecnicos();
    int qTecs = tecs.size();

    System.out.println("\n--- Gestionando servicio Nro " + nro + " ---");
    System.out.println("Fecha: " + fecha + " Cliente: " + cliente + " TipoServicio: " + ts + " EstadoServicio: "
        + es + " TiempoTrabajado: " + tiempoTrabajado + " CostoViaje: " + costoViaje + " IncluyeAlmuerzo: "
        + (incluyeAlmuerzo ? "SI" : "NO") + " Articulos utilizados: " + qArts + " Cantidad de otros costos: "
        + qOtrosArts + " Tecnicos asignados: " + qTecs);
  }

  public static void mVisualizarFacturas(Administrativo adm) {

  }

  public static void pantallaTecnico(Tecnico tec) {
    System.out.println("--- TECNICO ---");
  }

  public static void pantallaCallcenter(Callcenter cc) {
    System.out.println("--- CALLCENTER ---");
  }

  public static void mEdicionArticulo(Callcenter cc, Articulo articulo) throws Exception {
    System.out.println("\nEDITANDO STOCK Y COSTOS DE " + articulo.getDescripcion());
    System.out.println("Ingrese nuevo stock (Actual: " + articulo.getStock()
        + "), 0 para mantener stock anterior o -1 para volver al menu anterior: ");
    int stock = ch.scIntParse(sc, -1);

    if (stock == -1) {
      pantallaCallcenter(cc);
      return;
    }

    if (stock == 0) {
      stock = articulo.getStock();
    }

    System.out.println("Confirmar nuevo stock " + stock + " para " + articulo + "?");
    System.out.print("(0. No, 1. Si) =>");
    int opcion = ch.scIntParse(sc, 0, 1);

    if (opcion == 1) {
      cc.setStockArticulo(articulo, stock);
      System.out.println("Stock modificado");
    }
    pantallaCallcenter(cc);
  }

  public static void close() {
    sc.close();
    System.out.println("Saliendo del sistema...");
    System.exit(0);
  }
}
