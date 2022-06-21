package main;

import empresa.Empresa;
import comercial.*;
import personas.*;
// import agenda --> LEONARDO
import excepciones.*;
import controlador.*;

import java.io.IOException;
import java.util.Date;

public class Main {
	public static void main(String[] args) throws IOException {
		Empresa EMPRESA = new Empresa();

		// VARIABLES ARTICULOS PRUEBA
		Articulo cable = new Articulo("Cable x METRO", 50, TipoArticulo.CABLE, 500);
		Articulo ccrg6 = new Articulo("Conectores coaxiales x unidad", 15, TipoArticulo.CONECTOR_COAXIAL_RG6, 40);
		Articulo divc1a2 = new Articulo("Divisor coaxial de 1 a 2 x unidad", 15, TipoArticulo.CONECTOR_COAXIAL_RG6, 40);
		Articulo modems = new Articulo("Modem internet x unidad", 200, TipoArticulo.MODEM_INTERNET, 40);
		Articulo decoTv = new Articulo("Decodificador TV x unidad", 125, TipoArticulo.DECODIFICADOR_TV, 60);

		Articulo[] artDefault = { cable, ccrg6, divc1a2, modems, decoTv };
		for (Articulo a : artDefault) {
			EMPRESA.agregarArticulo(a);
		}

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

		// VARIABLES SERVICIO -> FACTURA
		// creacion de servicios (callcenter)
		try {
			serv1 = cc1.crearNuevoServicioServicio(cl1, new Date(), TipoServicio.INSTALACION);
			serv2 = cc1.crearNuevoServicioServicio(cl2, new Date(), TipoServicio.INSTALACION);
			serv3 = cc1.crearNuevoServicioServicio(cl2, new Date(), TipoServicio.INSTALACION);
			serv4 = cc1.crearNuevoServicioServicio(cl2, new Date(), TipoServicio.INSTALACION);

			cc1.asignarServicioATecnico(1, 3);
			cc1.asignarServicioATecnico(4, 3);
		} catch (AsignacionException e) {
			System.out.println(e);
		}

		try {
			tec1.editarTiempoServicio(serv1, 500);
			tec1.finalizarServicio(serv1);
			tec1.anadirMaterialServicio(serv1, 1, modems);
			tec1.finalizarServicio(serv1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Factura nuevaFactura = null;
		try {
			nuevaFactura = usuarioLogeado.facturarServicio(serv1);
			tec1.anadirMaterialServicio(serv1, 1, modems);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Nueva Factura: " + nuevaFactura);
		System.out.println();

		// VISUALIZANDO DATOS EMPRESA
		System.out.println("------- EMPRESA --------");
		System.out.println("INTERNOS: " + EMPRESA.getInternos());
		System.out.println("TECNICOS: " + EMPRESA.getTecnicos());
		System.out.println("CLIENTES: " + EMPRESA.getClientes());
		System.out.println("ARTICULOS: " + EMPRESA.getArticulos());
		System.out.println("SERVICIOS: " + EMPRESA.getServicios());
		System.out.println("FACTURAS: " + EMPRESA.getFacturas());
	}
}
