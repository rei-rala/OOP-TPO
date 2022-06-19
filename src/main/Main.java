package main;

import empresa.Empresa;
import personas.*;

public class Main {
	public static void main(String[] args) {
		Empresa EMPRESA = Empresa.getInstance();

		Admin ADMIN_1 = new Admin("El mas admin", 12345678, "Casa 123", "0800-000-0000", "segura1");
		Tecnico tec1 = new Tecnico("Testnico", 0000000, "", "", "CONTRASENA", Seniority.SENIOR);
		Tecnico tec2 = new Tecnico("Testnico junior", 0000000, "", "", "2CONTRASENA", Seniority.JUNIOR);
		Administrativo adm1 = new Administrativo("administrativoooo", 1231233, "midire 11", "10101010", "passAdm");
		Cliente cl1 = new Cliente("Clientest", 1111111, "aqui", "123-123-123");

		System.out.println(EMPRESA.getInternos());
		System.out.println(EMPRESA.getClientes());

		Interno usuario = EMPRESA.login(1, "segura1");
		System.out.println(usuario != null ? "LOGEO EXITOSO, LEGAJO USER: " + usuario : "VERIFIQUE CREDENCIALES");
		System.out.println("Es admin? " + (usuario.getClass() == Admin.class));
	}
}
