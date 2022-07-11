package main;

import empresa.*;
import gui.*;
import personas.*;
import comercial.*;
import comercial.articulos.*;
import agenda.*;
import consola.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

@SuppressWarnings("deprecation")
public class Main {
  static Empresa e = Empresa.getInstance();
  static Scanner scanner = new Scanner(System.in);
  static boolean isRunning = true;
  static Interno i;
  private static GuiMain gui = GuiMain.getInstance();

  public static void main(String[] args) throws Exception {

    instanciarObjectosInicialesEmpresa();
    instanciarObjectosAdicionalesEmpresa();
    testWorkflowInicial();

    String ejecucion = "G";

    while (("GUI".contains(ejecucion) || "CONSOLA".contains(ejecucion)) == false) {
      System.out.print("Iniciando programa, ingrese (G)ui o (C)onsola:\n\t=> ");
      ejecucion = scanner.nextLine();
      ejecucion = ejecucion.toUpperCase();
    }

    if ("GUI".contains(ejecucion)) {
      System.out.println("Iniciando GUI");
      gui.run();
      consolaInicio();
    } else {
      System.out.println("Iniciando Consola");
      consolaInicio();
    }

    close();
  }

  /**
   * Instancia objetos para la empresa EL codigo implementado incorpora cada
   * instancia automaticamente a la empresa
   */
  public static void instanciarObjectosInicialesEmpresa() {
    System.out.println("\n\n---------------------------------------------------------");
    System.out.println("------------ INSTANCIANDO OBJETOS INICIALES -------------");
    System.out.println("---------------------------------------------------------");
    // VARIABLES ARTICULOS PRUEBA

    // Si estos objetos no se instancian de esta manera, lo mas probable es que
    // fallen las pruebas del workflow.
    new Cable(50, 9999);
    new ConectorCoaxial(30, 9999);
    new DivisorCoaxial(30, 9999);
    new Modem(250, 9999);
    new DecodificadorTV(180, 9999);

    // VARIABLES PERSONAS PRUEBA
    new Admin("Hector V.O.N.", "");
    new Administrativo("Administrativo UNO", "");
    new Tecnico("tec Senior", "", Seniority.SENIOR);
    new Callcenter("Callcenter UNO", "");
    new Tecnico("tec Junior", "passTJR", Seniority.JUNIOR);

    System.out.println("\n----- EMPRESA tras instanciar los objetos iniciales -----");
    System.out.println(e);
    System.out.println("---------------------------------------------------------");
    System.out.println("------------------ FIN PREVIEW EMPRESA ------------------");
    System.out.println("---------------------------------------------------------\n\n");
  }

  /**
   * Instancia objetos para la empresa EL codigo implementado incorpora cada
   * instancia automaticamente a la empresa
   */
  public static void instanciarObjectosAdicionalesEmpresa() throws Exception {
    System.out.println("\n\n---------------------------------------------------------");
    System.out.println("----------- INSTANCIANDO OBJETOS Adicionales ------------");
    System.out.println("---------------------------------------------------------");

    // De acuerdo a la funcion instanciarObjectosInicialesEmpresa,
    // los legajos corresponderian a los siguientes roles de acuerdo al orden en el
    // que se instancian.
    // 4: Callcenter | 3: Tecnico Senior | 2: Administrativo | 1: ADMIN
    Callcenter cc = new Callcenter("Callcenter ADICIONAL", "");
    Tecnico t = new Tecnico("Tecnico ADICIONAL", "", Seniority.JUNIOR);
    Administrativo a = new Administrativo("Administrativo ADICIONAL", "");

    // HARDCODED:
    // A. 3 servicios creados sin ninguna asignacion.
    // B. 3 servicios creados sin asignacion de tecnico.
    // C. 1 servicio programado
    // D. 1 servicio en curso
    // E. 2 servicios finalizados (pre factura).
    // F. 1 servicio ya facturado.
    // TOTAL: 11 servicios asignados al mismo tecnico (pobre tipo).

    // Variables de control la creacion de los servicios
    int etapaB = 2, etapaC = 5, etapaD = 6, etapaE = 8, etapaF = 10;
    // Fecha base, iremos avanzando la fecha en cada iteracion
    Date fecha = DateAux.getNextDay(DateAux.getToday());

    for (int i = 0; i < 12; i++) {
      while (fecha.getDay() == 0 || fecha.getDay() >= 5) {
        fecha = DateAux.getNextDay(fecha);
        System.out.println("NUEVA FECHA ==> " + fecha + DateAux.getNombreDiaSemana(fecha));
      }

      // Propiedades de los servicios creados
      TipoServicio tipoServAlternar = i % 2 == 0 ? TipoServicio.INSTALACION : TipoServicio.REPARACION;
      fecha = i % 2 == 0 ? fecha : DateAux.getNextDay(fecha);
      Turno turnoAlternar = i % 2 == 0 ? Turno.MANANA : Turno.TARDE;
      int turnoInicio = 0, turnoFin = 5;

      // Punto A: Creacion servicio
      Servicio servicioDinamico = new Servicio(fecha, tipoServAlternar, turnoAlternar, turnoInicio, turnoFin);

      if (etapaB > i) {
        continue;
      }

      // Punto B: Asignacion de un cliente
      Cliente clienteDinamico = new Cliente("");
      clienteDinamico.setNombre("Cliente Dinamico " + (i + 1) + "[Asignacion Cliente]");
      cc.asignarServicio(servicioDinamico, clienteDinamico);

      if (etapaC > i) {
        continue;
      }
      // Punto C: Asignacion Tecnico
      clienteDinamico.setNombre("Cliente Dinamico " + (i + 1) + "[Asignacion Tecnico]");
      cc.asignarServicio(servicioDinamico, t);

      if (etapaD > i) {
        continue;
      }
      // Punto D: Liberacion desde Callcenter
      clienteDinamico.setNombre("Cliente Dinamico " + (i + 1) + "[Servicio liberado desde CC]");
      cc.liberarServicioCallcenter(servicioDinamico);

      if (etapaE > i) {
        continue;
      }

      // Punto E: Tecnico finaliza servicio
      clienteDinamico.setNombre("Cliente Dinamico " + (i + 1) + "[Servicio finalizado tecnico]");
      t.ejecutarServicio(servicioDinamico);
      t.finalizarServicio(servicioDinamico);

      if (etapaF > i) {
        continue;
      }

      // Punto F: Administrativo factura servicio
      clienteDinamico.setNombre("Cliente Dinamico " + (i + 1) + "[Servicio facturado]");
      a.facturarServicio(servicioDinamico);
    }

    System.out.println("\n----- EMPRESA tras instanciar los objetos adicionales -----");
    System.out.println(e);
    System.out.println("---------------------------------------------------------");
    System.out.println("------------- FIN PREVIEW ADICIONAL EMPRESA! -------------");
    System.out.println("---------------------------------------------------------\n\n");
  }

  /**
   * Prueba todos los workflow en un solo metodo Incluye cada rol
   * 
   * @throws Exception
   */
  public static void testWorkflowInicial() {

    try {
      // PRUEBA COMPLETA DE PROCESO
      // Por rol, en orden

      runWorkflowAdmin();
      runWorkflowCallcenter();
      runWorkflowTecnico();
      runWorkflowAdministrativo();

      System.out.println("\n///////////////////////////////////////////");
      System.out.println("/// Fin codigo prueba workflow EXITOSO! ///");
      System.out.println("///////////////////////////////////////////");
    }
    // Si entra en este catch, el error en el workflow es grave.
    catch (Exception e) {
      System.out.println("NOT HANDLED ERROR EN WORKFLOW TEST");
      e.printStackTrace();
    }
  }

  /**
   * El administrador administra stock y valores de articulos No participa en el
   * flujo de un Servicio.
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
    Articulo a = testAdm.getArticulos(1);
    testAdm.setStockArticulo(a, 99);

    testAdm.setCostoHoraTecnico(Seniority.SENIOR, 300);
  }

  /**
   * Workflow normal de un CALLCENTER
   * 
   * 0) Se logea. 1) Crear servicios siempre y cuando los parametros sean validos.
   * 2) Asignar servicios a un cliente (En tanto el cliente NO tenga servicios
   * vigentes y tenga disponible los turnos del servicio). 3) Asignar servicios a
   * un tecnico (En tanto el tecnico tenga disponible los turnos del servicio). 4)
   * Antes de liberar un servicio, puede cancelarlo 5) Liberar servicio (Debe
   * tener tecnico y cliente asignados), el servicio pasa a manos de los tecnicos
   * asignados.
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
    Cliente clienteCC = new Cliente("Cliente [Prueba Workflow Callcenter]");
    // tambien obtenemos un tecnico de prueba
    Tecnico tecnicoCC = new Tecnico("Tecnico [Prueba Workflow Callcenter]", "", Seniority.SENIOR);
    Date testDate = DateAux.getStartDay();

    if (testDate.getDay() == 0) {
      testDate = DateAux.getNextDay(testDate);
    }

    Agenda agC = clienteCC.getAgenda();
    FraccionTurno cFtDesde = agC.getDias().get(0).obtenerFraccionTurno(0, Turno.MANANA);
    FraccionTurno cFtDesdeNoV = agC.getDias().get(0).obtenerFraccionTurno(-1, Turno.MANANA);
    FraccionTurno cFtHasta = agC.getDias().get(0).obtenerFraccionTurno(5, Turno.MANANA);
    FraccionTurno cFtHastaNoV = agC.getDias().get(0).obtenerFraccionTurno(5, Turno.TARDE);
    // Creando servicio con parametros invalidos
    try {
      testCC.crearServicio(testDate, TipoServicio.INSTALACION, cFtDesde, cFtDesdeNoV);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Forzado intento crear servicio parametros no validos 1  => "
          + e.getMessage());
    }
    try {
      testCC.crearServicio(testDate, TipoServicio.INSTALACION, cFtDesde, cFtHastaNoV);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Forzado intento crear servicio parametros no validos 2  => "
          + e.getMessage());
    }

    // Creando un servicio valido y asignandolo al cliente
    Servicio s1 = testCC.crearServicio(testDate, TipoServicio.INSTALACION, cFtDesde, cFtHasta);
    // probando liberar servicio sin cliente
    try {
      testCC.liberarServicioCallcenter(s1);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento liberar servicio falta CLIENTE  => " + e.getMessage());
    }

    // Asignando servicio X a cliente Y
    Servicio sTestCC = new Servicio(new Date(2022, 8, 1), TipoServicio.INSTALACION, Turno.MANANA, 0, 5);
    testCC.asignarServicio(sTestCC, new Cliente("Cliente prueba WORKFLOW CALLCENTER"));
    System.out.println("ASIGNADO EXITOSAMENTE CLIENTE 'clienteCC'");

    // probando liberar servicio sin tecnicos
    try {
      testCC.liberarServicioCallcenter(sTestCC);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento liberar servicio falta TECNICO  => " + e.getMessage());
    }

    // Asignando servicio a tecnico
    testCC.asignarServicio(sTestCC, tecnicoCC);
    System.out.println("ASIGNADO EXITOSAMENTE TECNICO 'tecnicoCC'");

    // Probando asignar el mismo tecnico al servicio
    try {
      testCC.asignarServicio(s1, tecnicoCC);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Forzado intento asignar TECNICO al mismo servicio  => "
          + e.getMessage());
    }

    // Liberando servicio
    testCC.liberarServicioCallcenter(sTestCC);
    System.out.println("LIBERADO EXITOSAMENTE SERVICIO 's1'");

    // Probando cancelar servicio en curso
    try {
      testCC.cancelarServicio(s1);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Forzado intento cancelar servicio  => " + e.getMessage());
    }

    System.out.println("\n------- EXITO WORKFLOW CALLCENTER -------");
  }

  /**
   * Workflow normal de un TECNICO
   * 
   * 0) Se logea. 1) Visualiza los servicios asignados liberados desde callcenter
   * 2) Inicializa un servicio. 3) Sobre un servicio EN_CURSO puede añadir
   * articulos, añadir otros articulos, declarar si almorzo. 4) Finaliza servicio
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
    Tecnico testTec = new Tecnico("Tecnico [Prueba Workflow Tecnico]", "", Seniority.JUNIOR);
    FraccionTurno tFtDesde = testTec.getAgenda().getDias().get(0).obtenerFraccionTurno(0, Turno.MANANA);
    FraccionTurno tFtHasta = testTec.getAgenda().getDias().get(0).obtenerFraccionTurno(3, Turno.MANANA);

    // Preasignando un sFervicio
    Callcenter auxCC = new Callcenter("", "");
    Date fWFTecnico = DateAux.getToday();

    while (fWFTecnico.getDay() == 0 || fWFTecnico.getDay() >= 5) {
      fWFTecnico = DateAux.getNextDay(fWFTecnico);
    }

    Servicio auxS = auxCC.crearServicio(fWFTecnico, TipoServicio.INSTALACION, tFtDesde, tFtHasta);
    auxCC.asignarServicio(auxS, new Cliente("Cliente [Prueba Workflow Tecnico]"));
    auxCC.asignarServicio(auxS, testTec);
    auxCC.liberarServicioCallcenter(auxS);

    // Inicio tareas TECNICO
    ArrayList<Servicio> servsAsignados = testTec.getServiciosPendientes();
    System.out.println("\ntestTec tiene estos servicios: ");
    for (Servicio s : servsAsignados) {
      System.out.println("\tServicio nro: " + s.getNro() + ", Cliente: " + s.getCliente().getNombre());
    }
    System.out.println("Fin servicios testTec\n");

    // Selecciona un servicio: lo haria desde get(Servicio)
    Servicio servTrabajado = servsAsignados.get(0);

    // intenta finalizar servicio sin iniciarlo
    try {
      testTec.finalizarServicio(servTrabajado);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Forzado intento finalizar servicio sin iniciarlo  => "
          + e.getMessage());
    }

    // intenta añadir articulo sin estar iniciado
    try {
      Articulo ejemploArticulo = testTec.getArticulos(1);
      testTec.anadirArticuloServicio(servTrabajado, 50, ejemploArticulo);
    } catch (Exception e) {
      System.out
          .println("***** Control funcionando: Forzado intento añadir articulo a servicio sin iniciarlo  => "
              + e.getMessage());
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
      testTec.anadirArticuloServicio(servTrabajado, 50, ejemploArticulo);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Forzado intento añadir articulo INVALIDO a servicio  => "
          + e.getMessage());
    }

    // intenta añadir articulo con stock insuficiente
    try {
      Articulo ejemploArticulo = testTec.getArticulos(1);
      testTec.anadirArticuloServicio(servTrabajado, 999999999, ejemploArticulo);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento añadir articulo STOCK INSUFICIENTE a servicio  => "
              + e.getMessage());
    }

    // anade articulo valido
    Articulo ejemploArticulo = testTec.getArticulos(1);
    testTec.anadirArticuloServicio(servTrabajado, 5, ejemploArticulo);

    // finaliza servicio
    testTec.finalizarServicio(servTrabajado);
    System.out.println("Servicio n" + servTrabajado.getNro() + ": FINALIZADO por -> " + testTec.getNombre());

    // Intenta editar servicio marcado finalizado
    try {
      testTec.toggleAlmuerzoServicio(servTrabajado);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Forzado intento editar almuerzo a servicio FINALIZADO => "
          + e.getMessage());
    }

    // El tecnico tenia un solo servicio
    // tras su finalizacion, no deberia aparecer en su listado.
    ArrayList<Servicio> servsAsignados2 = testTec.getServiciosPendientes();
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
   * 0) Se logea 1) Visualiza los servicios finalizados por los tecnicos 2) Puede
   * optar por anadir articulos extras (campo libre) 3) Puede facturar un
   * servicio. 4) Tras facturar un servicio, puede visualizar las facturas y sus
   * datos
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
    Administrativo admTest = new Administrativo("Administrativo [Prueba Workflow Administrativo]", "");

    // Listando Servicios finalizados
    ArrayList<Servicio> serviciosAFacturar = admTest.getServiciosAFacturar();

    System.out.println("El administrativo obtiene los servicios finalizados");
    System.out.println("\n--------- Servicios disponibles para facturar ----------");
    for (int i = 0; i < serviciosAFacturar.size(); i++) {
      Servicio s = serviciosAFacturar.get(i);
      System.out.println("\t" + (i + 1) + ") Servicio " + s.getEstadoServicio() + " " + s.getNro() + ", Fecha: "
          + s.getFecha() + ", estado: " + s.getEstadoServicio());
    }
    System.out.println("-------------- Fin servicios finalizados ---------------");

    // Selecciona un servicio finalizado para visualizarlo o agregar articulos extra
    Servicio s = serviciosAFacturar.get(0);
    System.out.println("Servicio obtenido por Administrativo: Nro " + admTest.getServicio(s).getNro());

    // Crea un articulo extra
    ArticuloExtra aeTest = admTest.crearArticuloExtra("Extra creado [Prueba Workflow Administrativo]", 5000);
    System.out.println("admTest creo nuevo: " + aeTest);

    // Intenta crear un articulo con valor invalido
    try {
      admTest.crearArticuloExtra("Debo fallar por tener costo '-1'", -1);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento crear Articulo Extra - parametros no validos => "
              + e.getMessage());
    }

    // Intenta anadir un articulo y coloca cantidad no valida
    try {
      ArticuloExtra aeErrorTest = admTest
          .crearArticuloExtra("Fallara en un paso siguiente por informar cantidad negativa", 1);
      admTest.agregarArticuloExtraServicio(aeErrorTest, -1, s);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Forzado intento anadir A Extra con cantidad negativa => "
          + e.getMessage());
    }

    // Anadiendo exitosamente Art extra al servicio
    admTest.agregarArticuloExtraServicio(aeTest, 1, s);
    System.out.println("Extras del servicio tras modificacion del administrativo " + s.getArticulosExtra());

    // Decide facturar el servicio
    Factura f = admTest.facturarServicio(s);
    System.out.println("admTest finalizo servicio nro " + s.getNro() + " y obtuvo factura Numero:" + f.getNro());

    // Intenta anadir Art extra al servicio ya facturado
    try {
      admTest.agregarArticuloExtraServicio(aeTest, 1, s);
    } catch (Exception e) {
      System.out
          .println("***** Control funcionando: Forzado intento anadir A Extra a un servicio ya facturado => "
              + e.getMessage());
    }

    // Visualiza el margen obtenido de la factura
    System.out.println(
        "admTest visualiza margen de factura nro" + f.getNro() + ": " + admTest.getMargenFacturaString(f));

    System.out.println("\n----- EXITO WORKFLOW ADMINISTRATIVO -----");
  }

  public static Interno logInConsola() {
    System.out.println("INICIE SESION");
    Interno i;

    do {
      System.out.print("Ingrese numero de legajo (0 para salir) => ");
      int legajo = -1;

      String input = scanner.nextLine();

      while (0 > legajo) {

        try {
          legajo = Integer.parseInt(input);
        } catch (Exception e) {
          System.out.println("Debe ingresar un entero, ingreso: '" + input + "'");
        }
      }

      if (legajo == 0) {
        close();
      }

      System.out.println("Ingrese contrasena de legajo " + legajo + " => ");
      String contrasena = scanner.nextLine();

      i = e.login(legajo, contrasena);
    } while (i == null);

    System.out.println("Inicio sesion como " + i.getNombre() + " «" + i.getClass().getSimpleName() + "»");
    return i;
  }

  public static void consolaInicio() {
    System.out.println("----------------------------------------------");
    System.out.println("Bienvenido al sistema de gestión de la empresa");
    System.out.println("----------------------------------------------");
    System.out.println("USUARIOS DE PRUEBA: ");
    System.out.println("Legajo => 4 : Callcenter | pass ''(vacio, pulse directamente enter) ");
    System.out.println("Legajo => 3 : Tecnico | pass ''(vacio, pulse directamente enter) ");
    System.out.println("Legajo => 2 : Administrativo | pass ''(vacio, pulse directamente enter) ");
    System.out.println("Legajo => 1 : ADMIN | pass ''(vacio, pulse directamente enter) ");
    System.out.println("----------------------------------------------");

    Interno i = logInConsola();
    Consola menu = null;

    if (i.getClass() == Admin.class) {
      menu = new ConsolaAdmin((Admin) i, scanner);
    }
    if (i.getClass() == Administrativo.class) {
      menu = new ConsolaAdministrativo((Administrativo) i, scanner);
    }
    if (i.getClass() == Tecnico.class) {
      menu = new ConsolaTecnico((Tecnico) i, scanner);
    }
    if (i.getClass() == Callcenter.class) {
      menu = new ConsolaCallcenter((Callcenter) i, scanner);
    }

    if (menu == null) {
      System.out.println("No puedes acceder a este sistema");
      isRunning = false;
      return;
    }

    menu.iniciar();
  }

  public static void close() {
    System.out.print("Se cerrara el programa.\nIngrese cualquier caracter. =>");
    scanner.nextLine();
    System.out.println("Saliendo del programa...");
    scanner.close();
    System.exit(0);
  }
}
