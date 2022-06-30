package main;

import empresa.CostoHorasTecnico;
import empresa.Empresa;
import comercial.*;
import comercial.articulos.*;
import personas.*;
import gui.Gui;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import agenda.*;

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

		// -------------------------------------------------
		// PRUEBA COMPLETA DE PROCESO
		// Segmentado por rol, en orden
		runWorkflowAdmin();
		runWorkflowCallcenter();
		runWorkflowTecnico();
		runWorkflowAdministrativo();
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
			System.out.println("Bienvenido al sistema de gestión de la empresa");
			Interno i = logIn();
			pantallaPrincipal(i);
		}

		// GUI

		close();
	}

	public static void runWorkflowAdmin() throws Exception {
		Admin testAdm = new Admin("", "");
		// POR ORDEN DE EJECUCION, CABLE SERIA sku=1 (inicia con stock 0 en linea 30)
		Articulo a = testAdm.buscarArticulos(1);
		testAdm.setStockArticulo(a, 99);
	}

	public static void runWorkflowCallcenter() throws Exception {
		Callcenter testCC = new Callcenter("", "");
		// Callcenter crea servicios tras verificar verificar disponibilidad del cliente
		// destino en el horario definido.
		Cliente clienteCC = testCC.getCliente(1);
		boolean estaDisponible = clienteCC.verificarDisponibilidad(DateAux.getToday(), Turno.MANANA, 0, -1);
		
		if (!estaDisponible) {
			System.out.println("Turno no disponible, verifique ingreso");
		}
		
		// Damos por sentado que este si esta disponible
		estaDisponible = clienteCC.verificarDisponibilidad(DateAux.getToday(), Turno.MANANA, 0, 2);

	//	Servicio s1 = testCC.crearNuevoServicioServicio(cliente, fecha, ts, t, desde, hasta)

	//	Servicio serv1 = new Servicio(e.getClientes(1), new Date(), TipoServicio.INSTALACION, 10);

	}

	public static void runWorkflowTecnico() throws Exception {

	}

	public static void runWorkflowAdministrativo() throws Exception {

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
			System.out.println("Costo de combustible modificado a " + costo);
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

		System.out.println("Ingrese numero de articulo a editar (0 para volver al menu anterior): ");
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
			mGestionarServiciosFinalizados(adm);
		} else if (opcion == 2) {
			mVisualizarFacturas(adm);
		}
	}

	public static void mGestionarServiciosFinalizados(Administrativo adm) throws Exception {
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
			mGestionarServiciosFinalizados(adm);
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
