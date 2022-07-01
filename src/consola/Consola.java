package consola;

import java.util.Scanner;

abstract public class Consola {
  final protected ConsoleHelper ch = new ConsoleHelper();
  final protected Scanner sc;

  public Consola(Scanner sc) {
    this.sc = sc;
  }

  abstract public void iniciar();

}
