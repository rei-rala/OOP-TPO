package consola;

import java.util.Scanner;
import main.DateAux;

abstract public class Consola {
	final protected Scanner sc;
  final protected DateAux dateAux = DateAux.getInstance();

	public Consola(Scanner sc) {
		this.sc = sc;
	}

	abstract public void iniciar();

	public int scIntParse() {
		String input = sc.nextLine();
		try {
			return Integer.parseInt(input);
		} catch (Exception e) {
			System.out.println("Debe ingresar un entero, ingresó: '" + input + "'");
			return scIntParse();
		}
	}

	public int scIntParse(int min, int max) {
		int num = scIntParse();
		if (num >= min && max >= num) {
			return num;
		}
		System.out.println("Debe ingresar un entero entre " + min + " y " + max + ", ingresó '" + num + "'");
		return scIntParse(min, max);
	}

	public int scIntParse(int min) {
		int num = scIntParse();
		if (num < min) {
			System.out.println("Debe ingresar un entero mayor a" + min + ", ingresó '" + num + "'");
			return scIntParse(min);
		}
		return num;
	}

	public double scDoubleParse() {
		String input = sc.nextLine();
		try {
			return Double.parseDouble(input);
		} catch (Exception e) {
			System.out.println("Debe ingresar un numero (double), ingresó '" + input + "'");
			return scIntParse();
		}
	}

	public double scDoubleParse(int min, int max) {
		double num = scDoubleParse();
		if (num >= min && max >= num) {
			return num;
		}
		System.out.println("Debe ingresar un double entre " + min + " y " + max + ", ingresó '" + num + "'.");
		return scIntParse(min, max);
	}

	public double scDoubleParse(double min) {
		double num = scDoubleParse();

		if (num < min) {
			System.out.println("Debe ingresar un double entre mayor a " + min + ", ingresó '" + num + "'.");
			return scDoubleParse(min);
		}
		return num;
	}
}
