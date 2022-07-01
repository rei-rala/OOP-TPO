package main;

import java.util.Scanner;

public class ConsoleHelper {

  public ConsoleHelper() {
  }

  public int scIntParse(Scanner sc) {
    try {
      return Integer.parseInt(sc.nextLine());
    } catch (Exception e) {
      System.out.println("Debe ingresar un entero.");
      return scIntParse(sc);
    }
  }

  public int scIntParse(Scanner sc, int min, int max) {
    int num = scIntParse(sc);
    if (num >= min && max >= num) {
      return num;
    }
    System.out.println("Debe ingresar un entero entre " + min + " y " + max + ".");
    return scIntParse(sc, min, max);
  }

  public int scIntParse(Scanner sc, int min) {
    int num = scIntParse(sc);
    if (num < min) {
      System.out.println("Debe ingresar un entero mayor a" + min + ".");
      return scIntParse(sc, min);
    }
    return num;
  }

  public double scDoubleParse(Scanner sc) {
    try {
      return Double.parseDouble(sc.nextLine());
    } catch (Exception e) {
      System.out.println("Debe ingresar un numero (double).");
      return scIntParse(sc);
    }
  }

  public double scDoubleParse(Scanner sc, int min, int max) {
    double num = scDoubleParse(sc);
    if (num >= min && max >= num) {
      return num;
    }
    System.out.println("Debe ingresar un double entre " + min + " y " + max + ".");
    return scIntParse(sc, min, max);
  }

  public double scDoubleParse(Scanner sc, double min) {
    double num = scDoubleParse(sc);

    if (num < min) {
      System.out.println("Debe ingresar un double entre mayor a " + min + ".");
      return scDoubleParse(sc, min);
    }
    return num;
  }
}
