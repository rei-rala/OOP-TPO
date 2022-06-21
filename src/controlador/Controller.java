package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import comercial.Servicio;
import empresa.Empresa;
import personas.*;

public class Controller {
	private static Scanner sc = new Scanner(System.in);
	private final TipoController tipoController;
	private Interno usuario;

	public Controller() throws IOException {
		System.out.print("Ingrese cualquier tecla para GUI o ningun dato para CONSOLA => ");
		String TIPO = sc.nextLine();

		this.tipoController = TIPO.length() > 0 ? TipoController.GUI : TipoController.CONSOLA;
		System.out.println("Seleccionado tipo: " + tipoController);

		sc.useDelimiter("\\R");
	}

	public Interno getUsuario() {
		return usuario;
	}

	public void setUsuario(Interno usuario) {
		this.usuario = usuario;
	}

	public TipoController getTipoController() {
		return tipoController;
	}

	public void inicializar() throws IOException {

		do {
			logIn();
			if (usuario == null) {
				System.out.println("CREDENCIALES INCORRECTAS");
			}
		} while (usuario == null);

		System.out.printf("Bienvenido %s «%s»", usuario.getNombre(), usuario.getClass().getSimpleName());

		menus(usuario);
	}

	public void logIn() throws IOException {
		int legajo = -1;

		do {
			try {
				System.out.println("Ingrese legajo: ");
				legajo = Integer.parseInt(sc.next());
			} catch (Exception e) {
				System.out.println("Ingrese un legajo valido");
			}
		} while (legajo == -1);

		System.out.println("Ingrese contrasena: ");
		String contrasena = sc.next();

		setUsuario(Empresa.getInstance().login(legajo, contrasena));
	}

	public void menus(Interno i) {
		Class<? extends Interno> tipoInterno = i.getClass();

		if (tipoInterno == Admin.class) {
		}
		if (tipoInterno == Tecnico.class) {
			Tecnico t = (Tecnico) i;
			System.out.println("\n------------");
			System.out.println("MENU TECNICO");
			System.out.println("------------");
			System.out.println("1. Listar servicios");
			System.out.println("x. Salir(cualquier otra tecla)");
			System.out.println("------------");

			int opcionMenu = Integer.parseInt(sc.next());
			if (opcionMenu == 1) {
				ArrayList<Servicio> servsAsignados = t.verServiciosAsignados();

				for (Servicio sa : servsAsignados) {
					System.out.println(sa.nro + ". " + sa);
				}

				int opcionServicio = Integer.parseInt(sc.next());

				Servicio s = t.verServiciosAsignados(opcionServicio);

				System.out.println("Eleccion: " + s);

			} else {
				System.out.println("ADIOS");
			}
		}

	}

}