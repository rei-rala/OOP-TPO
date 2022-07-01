package consola;

import empresa.*;
import personas.*;
import comercial.articulos.*;

import java.util.Scanner;
import java.util.ArrayList;

public class ConsolaAdmin extends Consola {
  private final Admin admin;

  public ConsolaAdmin(Admin admin, Scanner sc) {
    super(sc);
    this.admin = admin;
  }

  public void iniciar() {
    pantallaAdmin();
  }

  private void pantallaAdmin() {
    System.out.println("\n--- Menu ADMIN ---");
    System.out.println("1. Editar costo combustible");
    System.out.println("2. Editar costo viaje");
    System.out.println("3. Editar stock y costo de articulo");
    System.out.println("4. Editar valor hora de los Tecnicos");
    System.out.println("0. Logout");

    System.out.print("Ingrese opcion => ");
    int opcion = ch.scIntParse(sc, 0);

    if (opcion == 0) {
      return;
    } else if (opcion == 1) {
      mCostoCombustible();
    } else if (opcion == 2) {
      mCostoViaje();
    } else if (opcion == 3) {
      mEdicionArticulos();
    } else if (opcion == 4) {
      mEdicionHorasTecnico();
    } else {
      System.out.println("Opcion invalida");
      pantallaAdmin();
    }
  }

  private void mCostoCombustible() {
    double costoAnterior = admin.getCostoCombustible();

    while (true) {
      System.out.println("\nEDITANDO COSTO COMBUSTIBLE");
      System.out.print("Ingrese nuevo costo (Actual: " + costoAnterior + ") o 0 para volver al menu anterior => ");
      double costo = ch.scDoubleParse(sc, 0);

      if (costo == 0) {
        pantallaAdmin();
        break;
      }
      try {
        admin.modificarCostoCombustible(costo);
      } catch (Exception e) {
        System.out.println("ERROR CAMBIANDO VALOR COMBUSTIBLE: " + e.getMessage());
        return;
      }
      System.out.print("Costo de combustible modificado a " + costo);
      break;
    }
    pantallaAdmin();
  }

  private void mCostoViaje() {
    double costoAnterior = admin.getCostoViaje();

    while (true) {
      System.out.println("\nEDITANDO COSTO DE VIAJE");
      System.out.print("Ingrese nuevo costo (Actual: " + costoAnterior + ") o 0 para volver al menu anterior => ");
      double costo = ch.scDoubleParse(sc, 0);

      if (costo == 0) {
        pantallaAdmin();
        break;
      }

      try {
        admin.modificarCostoViaje(costo);
      } catch (Exception e) {
        System.out.println("ERROR CAMBIANDO COSTO VIAJE: " + e.getMessage());
        return;
      }

      System.out.println("Costo de viaje modificado a " + costo);
      break;
    }
    pantallaAdmin();
  }

  private void mEdicionArticulos() {
    ArrayList<Articulo> articulos = admin.getArticulos();

    System.out.println("\nEDITANDO STOCK Y COSTOS DE LOS ARTICULOS");
    System.out.println("---------------- Articulos ----------------");

    for (int i = 0; i < articulos.size(); i++) {
      Articulo articulo = articulos.get(i);
      System.out.println((i + 1) + ". " + articulo);
    }

    System.out.print("Ingrese numero de articulo a editar (0 para volver al menu anterior): => ");
    int opcion = ch.scIntParse(sc, 0, articulos.size());

    if (opcion == 0) {
      pantallaAdmin();
      return;
    }

    Articulo articulo = articulos.get(opcion - 1);
    mEdicionArticulo(articulo);
  }

  private void mEdicionArticulo(Articulo articulo) {
    System.out.println("\nEDITANDO STOCK Y COSTOS DE " + articulo.getDescripcion());
    System.out.print("Ingrese nuevo stock (Actual: " + articulo.getStock()
        + "), 0 para mantener stock anterior o -1 para volver al menu anterior =>");
    int stock = ch.scIntParse(sc, 0);

    if (stock == -1) {
      pantallaAdmin();
      return;
    }

    System.out.println("Ingrese nuevo costo (Actual: " + articulo.getCosto()
        + "), 0 para mantener stock anterior o -1 para volver al menu anterior\n=> ");
    double costo = ch.scDoubleParse(sc, (double) 0);

    if (costo == -1) {
      pantallaAdmin();
      return;
    }

    if (stock == 0) {
      stock = articulo.getStock();
    }

    if (costo == 0) {
      costo = articulo.getCosto();
    }

    System.out.println("Confirmar nuevo stock " + stock + " y costo " + costo + " para " + articulo + "?");
    System.out.print("(0. No, 1. Si) =>");
    int opcion = ch.scIntParse(sc, 0, 1);

    if (opcion == 1) {
      try {
        admin.editarCifrasArticulo(articulo, stock, costo);
      } catch (Exception e) {
        System.out.println("ERROR CAMBIANDO CIFRAS DE ARTICULO: " + e.getMessage());
        return;
      }
      System.out.println("Stock y costo modificados");
    }
    pantallaAdmin();
  }

  private void mEdicionHorasTecnico() {
    CostoHorasTecnico cht = admin.getCostoHoraTecnico();

    System.out.println("\n---EDITANDO VALOR HORA DE LOS TECNICOS---");
    System.out.println("-- Valor horas actual  segun seniority --");
    System.out.println(cht);

    System.out.println("Seleccione Seniority a editar");
    System.out.println("1. Junior");
    System.out.println("2. SemiSenior");
    System.out.println("3. Senior");
    System.out.println("0. Volver al menu anterior");
    System.out.print("=>");

    int seniority = ch.scIntParse(sc, 0, 3);

    if (seniority == 3) {
      mEdicionHorasTecnico(Seniority.SENIOR);
    } else if (seniority == 2) {
      mEdicionHorasTecnico(Seniority.SEMI_SENIOR);
    } else if (seniority == 1) {
      mEdicionHorasTecnico(Seniority.JUNIOR);
    }
    pantallaAdmin();
  }

  private void mEdicionHorasTecnico(Seniority s) {
    System.out.println("\nEDITANDO HORAS DE " + s);
    System.out.print("Ingrese nuevo costo hora (Actual: " + admin.getCostoHoraTecnico(s)
        + "), 0 para sin cambios, -1 para volver al menu anterior =>");
    double costoHora = ch.scDoubleParse(sc, -1);

    if (costoHora == -1) {
      return;
    }
    boolean sinCambios = costoHora == 0;

    try {
      if (sinCambios) {
        costoHora = admin.getCostoHoraTecnico(s);
      }
      admin.setCostoHoraTecnico(s, costoHora);

      System.out.println("Costo hora de " + s + (sinCambios ? " sin cambios: $" : " modificado a $") + costoHora);

    } catch (Exception e) {
      System.out.println("ERROR CAMBIANDO COSTO HORAS TECNICO: " + e.getMessage());
      return;
    }

  }

}
