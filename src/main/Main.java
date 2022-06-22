package main;

import empresa.Empresa;
import comercial.*;
import personas.*;
// import agenda --> LEONARDO
import excepciones.*;
import controlador.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Main {
	public static void main(String[] args) throws IOException {
		Empresa EMPRESA = new Empresa();

		// VARIABLES ARTICULOS PRUEBA
		Articulo cable = new Articulo("Cable x METRO", 50, TipoArticulo.CABLE, 0);
		Articulo ccrg6 = new Articulo("Conectores coaxiales x unidad", 15, TipoArticulo.CONECTOR_COAXIAL_RG6, 40);
		Articulo divc1a2 = new Articulo("Divisor coaxial de 1 a 2 x unidad", 15, TipoArticulo.CONECTOR_COAXIAL_RG6, 40);
		Articulo modems = new Articulo("Modem internet x unidad", 200, TipoArticulo.MODEM_INTERNET, 40);
		Articulo decoTv = new Articulo("Decodificador TV x unidad", 125, TipoArticulo.DECODIFICADOR_TV, 60);

		// VARIABLES PERSONAS PRUEBA
		Admin ADMIN = new Admin("Nombre de administrador", ".");
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
		ArrayList<Articulo> articulosNoStock = null;
		Articulo ejemploNoStock = null;
		ArrayList<Tecnico> tecnicosDisponibles = null;

		// VARIABLES SERVICIO -> FACTURA
		// creacion de servicios (callcenter)
		try {
			/* aqui verificaria la disponibilidad del cliente */
			// ........

			// Disponibilidad de tecnicos
			tecnicosDisponibles = cc1.buscarTecnicosDisponibles(new Date());
			tecnicosDisponibles = cc1.buscarTecnicosDisponibles(new Date(), 1);

			// Verificando stock de articulos
			articulosNoStock = cc1.buscarArticulosSinStock();
			ejemploNoStock = cc1.buscarArticulos(cable);
			System.out.println("No tiene stock => " + ejemploNoStock);
			cc1.anadirStock(ejemploNoStock, 50);
			System.out.println("Tras adicionar stock => " + ejemploNoStock);

			// Tras verificar disponibilidad,
			serv1 = cc1.crearNuevoServicioServicio(cl1, new Date(), TipoServicio.INSTALACION);
			serv2 = cc1.crearNuevoServicioServicio(cl2, new Date(), TipoServicio.INSTALACION);
			serv3 = cc1.crearNuevoServicioServicio(cl2, new Date(), TipoServicio.INSTALACION);
			serv4 = cc1.crearNuevoServicioServicio(cl2, new Date(), TipoServicio.REPARACION);

			cc1.asignarServicioATecnico(1, 3);
			cc1.asignarServicioATecnico(4, 3);
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
			tec1.editarTiempoServicio(serv1, 500);

			ArticuloExtra costoCreadoPorTecnico = tec1.crearArticuloExtra("Gastos varios", 500);
			tec1.anadirOtroMaterialServicio(serv1, 1, costoCreadoPorTecnico);

			System.out.println("Servicio finalizado:" + serv1);
			tec1.finalizarServicio(serv1);
			tec1.anadirMaterialServicio(serv1, 1, modems);
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
			tec1.anadirMaterialServicio(serv1, 1, modems);
			
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
	}
}
