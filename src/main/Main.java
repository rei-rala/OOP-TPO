package main;

import empresa.Empresa;
import comercial.*;
import comercial.articulos.*;
import personas.*;
import excepciones.*;
import gui.Gui;
import controlador.*;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import agenda.*;

public class Main {
	public static void main(String[] args) throws IOException {
		Empresa EMPRESA = new Empresa();

		// VARIABLES ARTICULOS PRUEBA
		Cable c = new Cable(30, 500);
		ConectorCoaxial cc = new ConectorCoaxial(50, 100);
		DivisorCoaxial dc = new DivisorCoaxial(50, 80);
		Modem m = new Modem(200, 0);
		DecodificadorTV dtv = new DecodificadorTV(40, 120);

		// VARIABLES PERSONAS PRUEBA
		Admin ADMIN = new Admin("Nombre de administrador", "");
		Administrativo adm1 = new Administrativo("Administrativo UNO", "passADM");
		Tecnico tec1 = new Tecnico("tec Senior", "passTSR", Seniority.SENIOR);
		Tecnico tec2 = new Tecnico("tec Junior", "passTJR", Seniority.JUNIOR);
		Callcenter cc1 = new Callcenter("Callcenter UNO", "passCC");
		Cliente cl1 = new Cliente("Cliente UNO");
		Cliente cl2 = new Cliente("Cliente DOS");

		// LOG IN y PRUEBA ADMINISTRATIVO
		Interno usuario = EMPRESA.login(2, "passADM");
		System.out.println(usuario != null ? "LOGEO EXITOSO, LEGAJO USER: " + usuario : "VERIFIQUE CREDENCIALES");
		// Con esto obtendria el HOME para gui segun permisos???
		boolean esAdmin = usuario != null && usuario.getClass() == Admin.class;
		System.out.println("Es admin? " + esAdmin);
		// Casteando usuario manualmente para ejemplo
		Administrativo usuarioLogeado = (Administrativo) usuario;

		Servicio serv1 = null, serv2 = null, serv3 = null, serv4 = null;
		ArrayList<Articulo> articulosNoStock = cc1.buscarArticulosSinStock();
		Articulo ejemploNoStock = null;
		ArrayList<Tecnico> tecnicosDisponibles = null;

		System.out.println("ARTICULOS =>" + EMPRESA.getArticulos());
		// VARIABLES SERVICIO -> FACTURA
		// creacion de servicios (callcenter)
		try {
			/* aqui verificaria la disponibilidad del cliente */
			// ........

			// Disponibilidad de tecnicos
			tecnicosDisponibles = cc1.buscarTecnicosDisponibles(new Date());
			tecnicosDisponibles = cc1.buscarTecnicosDisponibles(new Date(), Turno.MANANA, 1);

			// Verificando stock de articulos
			ejemploNoStock = cc1.buscarArticulos(articulosNoStock.get(0));
			System.out.println("No tiene stock => " + ejemploNoStock);
			cc1.anadirStock(ejemploNoStock, 2);
			System.out.println("Tras adicionar stock => " + ejemploNoStock);

			// Tras verificar disponibilidad,
			serv1 = cc1.crearNuevoServicioServicio(cl1, new Date(), TipoServicio.INSTALACION);
			serv2 = cc1.crearNuevoServicioServicio(cl2, new Date(), TipoServicio.REPARACION);
			serv3 = cc1.crearNuevoServicioServicio(cl2, new Date(), TipoServicio.REPARACION);
			serv4 = cc1.crearNuevoServicioServicio(cl2, new Date(), TipoServicio.REPARACION);

			cc1.asignarServicioATecnico(serv1, tec1);
			cc1.asignarServicioATecnico(serv1, tec2);
			cc1.asignarServicioATecnico(serv1, tec2);
		} catch (AsignacionException e) {
			System.out.print("Error en asignacion =>");
			System.out.println(e);
		} catch (StockException e) {
			System.out.print("Error en edicion de stock =>");
			System.out.println(e);
		}

		// Tecnico asignado puede editar algunas propiedades del servicio y finalizarlo
		// Tras finalizar un servicio, no pueden hacerse nuevas ediciones
		try {
			ArticuloExtra costoCreadoPorTecnico = tec1.crearArticuloExtra("Gastos varios", 500);
			tec1.anadirOtroMaterialServicio(serv1, 1, costoCreadoPorTecnico);

			System.out.println("Servicio finalizado:" + serv1);
			tec1.finalizarServicio(serv1);
			tec1.anadirMaterialServicio(serv1, 1, c);
			tec1.finalizarServicio(serv1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Ejemplo de usuario logeado (administrativo) facturando un servicio
		Factura nuevaFactura = null;
		try {
			ArrayList<Servicio> serviciosPendientes = usuarioLogeado.verServiciosAFacturar();
			int seleccionNroServicio = 0;
			Servicio servicioSeleccionado = serviciosPendientes.get(seleccionNroServicio);
			// administrativo "vaciando" otros costos
			ArrayList<Costo> costosVacios = new ArrayList<Costo>();
			usuarioLogeado.editarOtrosCostosServicio(servicioSeleccionado, costosVacios);
			nuevaFactura = usuarioLogeado.facturarServicio(servicioSeleccionado);

			// ejemplo de tecnico editando el servicio tras ser facturado
			tec1.anadirMaterialServicio(serv1, 1, m);

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Nueva Factura: " + nuevaFactura);

		// VISUALIZANDO DATOS EMPRESA
		System.out.println("------- EMPRESA --------");
		System.out.println("INTERNOS =>" + EMPRESA.getInternos());
		System.out.println("TECNICOS =>" + EMPRESA.getTecnicos());
		System.out.println("CLIENTES =>" + EMPRESA.getClientes());
		System.out.println("ARTICULOS =>" + EMPRESA.getArticulos());
		System.out.println("SERVICIOS =>" + EMPRESA.getServicios());
		System.out.println("FACTURAS =>" + EMPRESA.getFacturas());
		System.out.println("-----------------------");
		// ------------------------------

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui.getInstance().initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
