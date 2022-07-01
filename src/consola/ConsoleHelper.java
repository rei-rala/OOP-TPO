package consola;

import java.util.Scanner;

public class ConsoleHelper {

  public ConsoleHelper() {
  }

  public int scIntParse(Scanner sc) {
    String input = sc.nextLine();
    try {
      return Integer.parseInt(input);
    } catch (Exception e) {
      System.out.println("Debe ingresar un entero, ingreso: '" + input + "'");
      return scIntParse(sc);
    }
  }

  public int scIntParse(Scanner sc, int min, int max) {
    int num = scIntParse(sc);
    if (num >= min && max >= num) {
      return num;
    }
    System.out.println("Debe ingresar un entero entre " + min + " y " + max + ", ingreso '" + num + "'");
    return scIntParse(sc, min, max);
  }

  public int scIntParse(Scanner sc, int min) {
    int num = scIntParse(sc);
    if (num < min) {
      System.out.println("Debe ingresar un entero mayor a" + min + ", ingreso '" + num + "'");
      return scIntParse(sc, min);
    }
    return num;
  }

  public double scDoubleParse(Scanner sc) {
    String input = sc.nextLine();
    try {
      return Double.parseDouble(input);
    } catch (Exception e) {
      System.out.println("Debe ingresar un numero (double), ingreso '" + input + "'");
      return scIntParse(sc);
    }
  }

  public double scDoubleParse(Scanner sc, int min, int max) {
    double num = scDoubleParse(sc);
    if (num >= min && max >= num) {
      return num;
    }
    System.out.println("Debe ingresar un double entre " + min + " y " + max + ", ingreso '" + num + "'.");
    return scIntParse(sc, min, max);
  }

  public double scDoubleParse(Scanner sc, double min) {
    double num = scDoubleParse(sc);

    if (num < min) {
      System.out.println("Debe ingresar un double entre mayor a " + min + ", ingreso '" + num + "'.");
      return scDoubleParse(sc, min);
    }
    return num;
  }
}
