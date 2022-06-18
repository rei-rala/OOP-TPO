
public class Main {
	public static void main(String[] args) {
		Empresa EMPRESA = new Empresa();

		admArticulos.crearArticulo("pri", 5, 5);
		admArticulos.crearArticulo("seg", 5, 5);
		admArticulos.crearArticulo("ter", 5, 5);

		// Administrativo asistente1 = new Administrativo("ASDASDASD");
		Administrativo asistente2 = new Administrativo("ASDASDASD");
		Administrativo asistente3 = new Administrativo("ASDASDASD");

		System.out.println(asistente3);

		// Tecnico tecnico1 = new Tecnico("Tec1", Seniority.JUNIOR);
		// Tecnico tecnico2 = new Tecnico("Tec2", Seniority.SEMI_SENIOR);
		Tecnico tecnico3 = new Tecnico("Tec3", Seniority.SENIOR);

		System.out.println(tecnico3);
		System.out.printf("Costo %s = $%f", Seniority.SEMI_SENIOR.getSeniority(), EMPRESA.obtenerCostoHoraTecnico(Seniority.SEMI_SENIOR));
		System.out.println();
		System.out.println(Interno.internosBackup);
		System.out.println(EMPRESA.obtenerInternos());
		// admArticulos.mostrar();
		// admArticulos.eliminar(1);
		// admArticulos.mostrar();
		// admArticulos.mostrar(0);

	}
}
