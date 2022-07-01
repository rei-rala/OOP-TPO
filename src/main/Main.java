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

    instanciarObjectosInicialesEmpresa();
    instanciarObjectosAdicionalesEmpresa();
    testWorkflowInicial();

    // GUI
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

    close();
  }

  /**
   * Instancia objetos para la empresa
   * EL codigo implementado incorpora cada instancia automaticamente a la empresa
   */
  public static void instanciarObjectosInicialesEmpresa() {
    System.out.println("\n\n---------------------------------------------------------");
    System.out.println("------------ INSTANCIANDO OBJETOS INICIALES -------------");
    System.out.println("---------------------------------------------------------");
    // VARIABLES ARTICULOS PRUEBA
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
   * Instancia objetos para la empresa
   * EL codigo implementado incorpora cada instancia automaticamente a la empresa
   */
  public static void instanciarObjectosAdicionalesEmpresa() throws Exception {
    System.out.println("\n\n---------------------------------------------------------");
    System.out.println("----------- INSTANCIANDO OBJETOS Adicionales ------------");
    System.out.println("---------------------------------------------------------");

    // De acuerdo a la funcion instanciarObjectosInicialesEmpresa,
    // los legajos corresponderian a los siguientes roles de acuerdo al orden en el
    // que se instancian.
    // 4: Callcenter | 3: Tecnico Senior | 2: Administrativo | 1: ADMIN
    Callcenter cc = (Callcenter) e.getInternos(4);
    Tecnico t = (Tecnico) e.getInternos(3);
    Administrativo a = (Administrativo) e.getInternos(2);

    // HARDCODED:
    // A. 3 servicios creados sin ninguna asignacion.
    // B. 3 servicios creados sin asignacion de tecnico.
    // C. 1 servicio programado
    // D. 1 servicio en curso
    // E. 2 servicios finalizados (pre factura).
    // F. 1 servicio ya facturado.
    // TOTAL: 11 servicios asignados al mismo tecnico (pobre tipo).

    Date fecha = DateAux.getStartDay(new Date(DateAux.getToday().getTime()));
    for (int i = 0; i < 12; i++) {

      int siguienteEtapa = 2;

      while (fecha.getDay() == 0 || fecha.getDay() == 6) {
        fecha = DateAux.getNextDay(fecha);
      }
      TipoServicio tipoServAlternar = i % 2 == 0 ? TipoServicio.INSTALACION : TipoServicio.REPARACION;
      Turno turnoAlternar = i % 2 == 0 ? Turno.MANANA : Turno.TARDE;
      int turnoInicio = 0, turnoFin = 5;
      Cliente clienteDinamico = new Cliente("Cliente dinamico nro " + (i + 1));

      // Punto A
      Servicio currentServicio = new Servicio(fecha, tipoServAlternar, turnoAlternar, turnoInicio, turnoFin);

      if (siguienteEtapa > i) {
        continue;
      }
      siguienteEtapa += 3;
      // Punto B
      currentServicio.asignarCliente(clienteDinamico);

      if (siguienteEtapa > i) {
        continue;
      }
      siguienteEtapa += 1;
      // Punto C
      currentServicio.asignarTecnico(t);

      if (siguienteEtapa > i) {
        continue;
      }
      siguienteEtapa += 1;
      // Punto D
      cc.liberarServicioCallcenter(currentServicio);

      if (siguienteEtapa > i) {
        continue;
      }
      siguienteEtapa += 2;

      // Punto E
      t.ejecutarServicio(currentServicio);
      t.finalizarServicio(currentServicio);

      if (siguienteEtapa > i) {
        continue;
      }

      // Punto F
      a.facturarServicio(currentServicio);
    }

    System.out.println("\n----- EMPRESA tras instanciar los objetos adicionales -----");
    System.out.println(e);
    System.out.println("---------------------------------------------------------");
    System.out.println("------------- FIN PREVIEW ADICIONAL EMPRESA! -------------");
    System.out.println("---------------------------------------------------------\n\n");
  }

  /**
   * Prueba todos los workflow en un solo metodo
   * Incluye cada rol
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

      System.out.println("\n//////////////////////////////////////////");
      System.out.println("/// Fin codigo prueba flujo de trabajo ///");
      System.out.println("//////////////////////////////////////////");
    }
    // Si entra en este catch, el error en el workflow es grave.
    catch (Exception e) {
      System.out.println("NOT HANDLED ERROR EN WORKFLOW TEST");
      e.printStackTrace();
    }
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
    Date testDate = DateAux.getStartDay(new Date(2022, 06, 19));

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
    try {
      clienteCC.verificarDisponibilidad(sobreturnado);
    } catch (Exception e) {
      System.out.println("***** Control funcionando: Verifica CLIENTE con turno ocupado " + e.getMessage());
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
    Servicio auxS = auxCC.crearNuevoServicioServicio(new Date(), TipoServicio.INSTALACION, Turno.MANANA, 0, 2);
    auxCC.asignarServicio(auxS, new Cliente("Cliente workflow test"));
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
      System.out
          .println("***** Control funcionando: Forzado intento finalizar servicio sin iniciarlo  => " + e.getMessage());
    }

    // intenta añadir articulo sin estar iniciado
    try {
      Articulo ejemploArticulo = testTec.getArticulos(1);
      testTec.anadirArticuloServicio(servTrabajado, 50, ejemploArticulo);
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
      testTec.anadirArticuloServicio(servTrabajado, 50, ejemploArticulo);
    } catch (Exception e) {
      System.out.println(
          "***** Control funcionando: Forzado intento añadir articulo INVALIDO a servicio  => " + e.getMessage());
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
      System.out.println(
          "***** Control funcionando: Forzado intento editar almuerzo a servicio FINALIZADO => "
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
      System.out.println("\t" + (i + 1) + ") Servicio " + s.getEstadoServicio() + " " + s.getNro() + ", Fecha: "
          + s.getFecha() + ", estado: "
          + s.getEstadoServicio());
    }
    System.out.println("-------------- Fin servicios finalizados ---------------");

    // Selecciona un servicio finalizado para visualizarlo o agregar articulos extra
    Servicio s = serviciosAFacturar.get(0);
    System.out.println("Servicio obtenido por Administrativo: Nro " + admTest.getServicio(s).getNro());

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
    System.out.println("Extras del servicio tras modificacion del administrativo " + s.getArticulosExtra());

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

  public static Interno logIn() {
    System.out.println("INICIE SESION");
    Interno i;

    do {
      System.out.print("Ingrese numero de legajo (0 para salir) => ");
      int legajo = ch.scIntParse(sc, 0);

      if (legajo == 0) {
        close();
      }

      System.out.print("Ingrese contrasena de legajo " + legajo + " => ");
      String contrasena = sc.nextLine();

      i = e.login(legajo, contrasena);
    } while (i == null);

    System.out.println("Inicio sesion como " + i.getNombre() + " «" + i.getClass().getSimpleName() + "»");
    return i;
  }

  public static void pantallaPrincipal(Interno i) {
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
      System.out.println("No se puede acceder a este sistema");
      isRunning = false;
    }
  }

  public static void pantallaAdmin(Admin a) {
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

  public static void mCostoCombustible(Admin a) {
    double costoAnterior = e.getCostoCombustible();

    while (true) {
      System.out.println("\nEDITANDO COSTO COMBUSTIBLE");
      System.out.print("Ingrese nuevo costo (Actual: " + costoAnterior + ") o 0 para volver al menu anterior => ");
      double costo = ch.scDoubleParse(sc, 0);

      if (costo == 0) {
        pantallaAdmin(a);
        break;
      }
      try {
        a.modificarCostoCombustible(costo);
      } catch (Exception e) {
        System.out.println("ERROR CAMBIANDO VALOR COMBUSTIBLE: " + e.getMessage());
        close();
      }
      System.out.print("Costo de combustible modificado a " + costo);
      break;
    }
    pantallaAdmin(a);
  }

  public static void mCostoViaje(Admin a) {
    double costoAnterior = e.getCostoViaje();

    while (true) {
      System.out.println("\nEDITANDO COSTO DE VIAJE");
      System.out.print("Ingrese nuevo costo (Actual: " + costoAnterior + ") o 0 para volver al menu anterior => ");
      double costo = ch.scDoubleParse(sc, 0);

      if (costo == 0) {
        pantallaAdmin(a);
        break;
      }

      try {
        a.modificarCostoViaje(costo);
      } catch (Exception e) {
        System.out.println("ERROR CAMBIANDO COSTO VIAJE: " + e.getMessage());
        close();
      }

      System.out.println("Costo de viaje modificado a " + costo);
      break;
    }
    pantallaAdmin(a);
  }

  public static void mEdicionArticulos(Admin a) {
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

  public static void mEdicionArticulo(Admin a, Articulo articulo) {
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
      try {
        a.editarCifrasArticulo(articulo, stock, costo);
      } catch (Exception e) {
        System.out.println("ERROR CAMBIANDO CIFRAS DE ARTICULO: " + e.getMessage());
        close();
      }
      System.out.println("Stock y costo modificados");
    }
    pantallaAdmin(a);
  }

  public static void mEdicionHorasTecnico(Admin a) {
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

  public static void mEdicionHorasTecnico(Admin a, Seniority s) {
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

    try {
      a.setCostoHoraTecnico(s, costoHora);
    } catch (Exception e) {
      System.out.println("ERROR CAMBIANDO COSTO HORAS TECNICO: " + e.getMessage());
      close();
    }

    System.out.println("Costo hora de " + s + " modificado a " + costoHora);
  }

  public static void pantallaAdministrativo(Administrativo adm) {
    System.out.println("\n--- Menu ADMINISTRATIVO ---");
    System.out.println("1. Gestionar servicios finalizados");
    System.out.println("2. Visualizar facturas");
    System.out.println("0. Log out");
    System.out.print("=> ");

    int opcion = ch.scIntParse(sc, 0, 2);

    if (opcion == 0) {
      logIn();
    } else if (opcion == 1) {
      mGestionarServiciosAFacturar(adm);
    } else if (opcion == 2) {
      mVisualizarFacturas(adm);
    }
  }

  public static void mGestionarServiciosAFacturar(Administrativo adm) {
    ArrayList<Servicio> servicios = adm.getServiciosAFacturar();

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
    int opcion = ch.scIntParse(sc, 0, servicios.size());

    if (opcion == 0) {
      pantallaAdministrativo(adm);
      return;
    }

    Servicio servicio = servicios.get(opcion - 1);
    mGestionarServicio(adm, servicio);
  }

  public static void mGestionarServicio(Administrativo adm, Servicio s) {
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

    int opc = ch.scIntParse(sc, 0, 5);

    if (opc == 0) {
      pantallaAdministrativo(adm);
      return;
    } else if (opc == 1) {
      mMostrarTecnicos(adm, s);
    } else if (opc == 2) {
      mMostrarArticulos(adm, s);
    } else if (opc == 3) {
      mMostrarArticulosExtra(adm, s);
    } else if (opc == 4) {
      mAnadirArticuloExtra(adm, s);
    } else if (opc == 5) {
      mFacturarServicio(adm, s);
      return;
    }
    mGestionarServicio(adm, s);
  }

  public static void mMostrarTecnicos(Administrativo adm, Servicio s) {
    ArrayList<Tecnico> tecnicosServicio = adm.getTecnicos(s);
    System.out.println("MOSTRANDO " + tecnicosServicio.size() + " TECNICOS DE SERVICIO Nro " + s.getNro());
    for (Tecnico t : tecnicosServicio) {
      System.out.println(
          "\tTecnico legajo: " + t.getLegajo() + " - Nombre: " + t.getNombre() + " - Seniority: " + t.getSeniority());
    }
  }

  public static void mMostrarArticulos(Administrativo adm, Servicio s) {
    ArrayList<Costo> articulos = adm.getArticulos(s);
    System.out.println("MOSTRANDO " + articulos.size() + " ARTICULOS DE SERVICIO Nro " + s.getNro() + " total costo: $"
        + s.calcularCostoArticulos());
    for (Costo c : articulos) {
      System.out.println("\t" + c);
    }

    if (0 >= articulos.size()) {
      System.out.println("<NO HAY ARTICULOS>");
    }
  }

  public static void mMostrarArticulosExtra(Administrativo adm, Servicio s) {
    ArrayList<Costo> articulos = adm.getArticulosExtra(s);
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

  public static void mAnadirArticuloExtra(Administrativo adm, Servicio s) {
    System.out.println("Anadir articulo extra para el servicio numero " + s.getNro());
    System.out.print("Ingrese la descripcion para el articulo => ");
    String descripcion = sc.nextLine();

    System.out.print("Ingrese el costo para el articulo => ");
    double costo = ch.scDoubleParse(sc, 0.1);

    ArticuloExtra nuevoArticuloExtra = null;

    try {
      nuevoArticuloExtra = adm.crearArticuloExtra(descripcion, costo);
    } catch (Exception e) {
      System.out.println("ERROR CREANDO ARTICULO EXTRA: " + e.getMessage());
      close();
    }

    System.out.println("Confirmar agregar el articulo extra creado? (" + nuevoArticuloExtra + ")");
    System.out.print("(1 Confirmar, 0 cancelar)");

    int confirma = ch.scIntParse(sc, 0, 1);

    if (confirma == 1) {
      try {
        adm.agregarArticuloExtraServicio(nuevoArticuloExtra, 1, s);
      } catch (Exception e) {
        System.out.println("ERROR AGREGANDO ARTICULO EXTRA: " + e.getMessage());
        close();
      }
    }
  }

  public static void mFacturarServicio(Administrativo adm, Servicio s) {
    System.out.println("CONFIRMA FACTURAR EL SERVICIO Nro " + s.getNro() + "?");
    System.out.println("La accion no es reversible");
    System.out.print("(1 Confirmar, 0 cancelar)");

    int confirmaFacturar = ch.scIntParse(sc, 0, 1);
    if (confirmaFacturar == 1) {
      Factura f = null;
      try {
        f = adm.facturarServicio(s);
      } catch (Exception e) {
        System.out.println("ERROR FACTURANDO SERVICIO: " + e.getMessage());
        close();
      }
      System.out
          .println(adm.getNombre() + " FACTURO el servicio nro " + s.getNro() + " => Factura numero: " + f.getNro());
      mGestionarServiciosAFacturar(adm);
    } else {
      System.out.println("CANCELO LA FACTURACION");
    }
  }

  public static void mVisualizarFacturas(Administrativo adm) {
    ArrayList<Factura> facturas = adm.getFacturas();

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

    int opcion = ch.scIntParse(sc, 0, facturas.size());

    if (opcion != 0) {
      Factura f = facturas.get(opcion - 1);
      mostrarFactura(adm, f);
    }
    pantallaAdministrativo(adm);
  }

  public static void mostrarFactura(Administrativo adm, Factura f) {
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

  public static void pantallaTecnico(Tecnico t) {
    System.out.println("\n--- Menu TECNICO ---");
    System.out.println("1. Gestionar servicios asignados");
    System.out.println("0. Log out");
    System.out.print("=> ");

    int opcion = ch.scIntParse(sc, 0, 1);

    if (opcion == 0) {
      logIn();
      return;
    }
    mListarServicios(t);
  }

  public static void mListarServicios(Tecnico t) {
    ArrayList<Servicio> servs = t.getServiciosPendientes();

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

    int opcion = ch.scIntParse(sc, 0, servs.size());

    if (opcion != 0) {
      Servicio elegido = servs.get(opcion - 1);
      mVerServicio(t, elegido);
    }
    pantallaTecnico(t);
  }

  public static void mVerServicio(Tecnico t, Servicio s) {
    String nroServicio = "\nNumero servicio " + s.getNro();
    String fechaCreacion = "\nCreado " + DateAux.getDateString(s.getFechaCreacion());
    String diaServicio = "\nFecha Servicio " + DateAux.getDateString(s.getFecha());
    String horarioServicio = "\nHorario " + DateAux.getHorarioCompleto(s);
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

    int opc = ch.scIntParse(sc, 0, 4);

    if (opc == 0) {
      pantallaTecnico(t);
      return;
    }

    if (opc == 4) {
      mAvanzarEstadoServicio(t, s);

      if (s.getEstadoServicio() == EstadoServicio.EN_CURSO) {
        mVerServicio(t, s);
      }
      return;
    }

    if (es != EstadoServicio.EN_CURSO) {
      System.out.println("Para acceder a estas opciones primero debe comenzar el servicio (opcion 4)");
      mVerServicio(t, s);
      return;
    }

    if (opc == 1) {
      mAnadirArticulo(t, s);
    }

    if (opc == 3) {
      mToggleAlmuerzo(t, s);
    }

    mVerServicio(t, s);
  }

  public static void mAvanzarEstadoServicio(Tecnico t, Servicio s) {
    System.out.println("\nConfirmar avanzar el estado a "
        + (s.getEstadoServicio() == EstadoServicio.PROGRAMADO ? EstadoServicio.EN_CURSO : EstadoServicio.FINALIZADO)
        + "?");
    System.out.print("(1 para confirmar, 0 para cancelar) => ");
    int confirmaAvance = ch.scIntParse(sc, 0, 1);

    if (confirmaAvance == 1) {
      try {
        t.avanzarServicio(s);
        System.out.println("El estado de servicio cambio a: " + s.getEstadoServicio());
      } catch (Exception e) {
        System.out.println("ERROR AVANZANDO ESTADO SERVICIO: " + e.getMessage());
        close();
      }
    }
  }

  public static void mToggleAlmuerzo(Tecnico t, Servicio s) {
    try {
      t.toggleAlmuerzoServicio(s);
    } catch (Exception e) {
      System.out.println("ERROR CAMBIANDO ALMUERZO SERVICIO: " + e.getMessage());
      close();
    }
    System.out.println("Has indicado " + (s.isIncluyeAlmuerzo() ? "" : "NO ") + "incluir almuerzo en el servicio");
  }

  public static void mAnadirArticulo(Tecnico t, Servicio s) {
    System.out.println("\nAnadir articulo a ingresar para el servicio numero " + s.getNro());
    ArrayList<Articulo> arts = t.getArticulos();

    for (int i = 0; i < arts.size(); i++) {
      System.out.println("" + (i + 1) + ") " + arts.get(i));
    }
    System.out.print("Seleccione su opcion (0 para cancelar) => ");
    int inputEleccionArticulo = ch.scIntParse(sc, 0, arts.size());
    if (inputEleccionArticulo == 0) {
      System.out.println("Cancelo agregar articulo");
      return;
    }
    Articulo articuloSeleccionado = arts.get(inputEleccionArticulo - 1);

    System.out.print("Ingrese el cantidad utilizada => ");
    int cantidadEleccion = ch.scIntParse(sc, 0, articuloSeleccionado.getStock());

    if (cantidadEleccion == 0) {
      System.out.println("Cancelo agregar articulo");
      return;
    }

    System.out.println("Confirmar agregar el articulo al servicio " + s.getNro() + "? ("
        + articuloSeleccionado.getDescripcion() + ")");
    System.out.print("(1 Confirmar, 0 cancelar) => ");

    int confirma = ch.scIntParse(sc, 0, 1);

    if (confirma == 1) {
      try {
        t.anadirArticuloServicio(s, cantidadEleccion, articuloSeleccionado);
        System.out.println("Anadido " + cantidadEleccion + " unidades de " + articuloSeleccionado.getDescripcion());
      } catch (Exception e) {
        System.out.println("ERROR ANADIENDO ARTICULO A SERVICIO: " + e.getMessage());
        close();
      }
    }
  }

  public static void mAnadirArticuloExtra(Tecnico t, Servicio s) {
    System.out.println("\nAnadir articulo extra para el servicio numero " + s.getNro());
    System.out.print("Ingrese la descripcion para el articulo => ");
    String descripcion = sc.nextLine();

    System.out.print("Ingrese el costo para el articulo => ");
    double costo = ch.scDoubleParse(sc, 0.1);

    ArticuloExtra nuevoArticuloExtra = t.crearArticuloExtra(descripcion, costo);

    System.out.println("Confirmar agregar el articulo extra creado? (" + nuevoArticuloExtra + ")");
    System.out.print("(1 Confirmar, 0 cancelar)");

    int confirma = ch.scIntParse(sc, 0, 1);

    if (confirma == 1) {
      try {
        t.anadirArticuloExtraServicio(s, 1, nuevoArticuloExtra);
      } catch (Exception e) {
        System.out.println("ERROR ANADIENDO ARTICULO EXTRA A SERVICIO: " + e.getMessage());
        close();
      }
    }
  }

  public static void pantallaCallcenter(Callcenter cc) {
    System.out.println("--- CALLCENTER ---");
  }

  public static void mEdicionArticulo(Callcenter cc, Articulo articulo) {
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
      try {
        cc.setStockArticulo(articulo, stock);
      } catch (Exception e) {
        System.out.println("ERROR MODIFICANDO STOCK DE ARTICULO: " + e.getMessage());
        close();
      }
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
