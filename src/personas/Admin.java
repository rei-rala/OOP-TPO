package personas;

import java.util.ArrayList;

import comercial.articulos.*;
import empresa.CostoHorasTecnico;
import empresa.Empresa;
import excepciones.*;

public class Admin extends Interno {
  public Admin(String nombre, long dni, String direccion, String telefono, String contrasena) {
    super(nombre, dni, direccion, telefono, contrasena);
  }

  // ALTERNATIVO SIN DATOS
  public Admin(String nombre, String contrasena) {
    super(nombre, contrasena);
  }

  @Override
  public String toString() {
    return "Admin [legajo=" + legajo + " nombre=" + nombre + ", dni=" + dni
        + ", direccion=" + direccion + ", telefono=" + telefono + "]";
  }

  public double getCostoCombustible() {
    return Empresa.getInstance().getCostoCombustible();
  }

  public void modificarCostoCombustible(double nvoCostoCombustible) throws ValorException, Exception {
    Empresa.getInstance().setCostoCombustible(nvoCostoCombustible);
  }

  public double getCostoViaje() {
    return Empresa.getInstance().getCostoViaje();
  }

  public void modificarCostoViaje(double nvoCostoViaje) throws ValorException, Exception {
    Empresa.getInstance().setCostoViaje(nvoCostoViaje);
  }

  public CostoHorasTecnico getCostoHoraTecnico() {
    return Empresa.getInstance().getCostoHoraTecnico();
  }

  public double getCostoHoraTecnico(Seniority s) {
    return Empresa.getInstance().getCostoHoraTecnico(s);
  }

  public void modificarCostoHoraTecnico(CostoHorasTecnico nuevoCHTObject) throws ValorException {
    Empresa.getInstance().setCostoHoraTecnico(nuevoCHTObject);
  }

  public void setCostoHoraTecnico(Seniority seniority, double nuevoCHT) throws ValorException {
    Empresa.getInstance().setCostoHoraTecnico(seniority, nuevoCHT);
  }

  public void modificarCostoHoraTecnico(double jr, double ssr, double sr) throws ValorException {
    Empresa.getInstance().setCostoHoraTecnico(jr, ssr, sr);
  }

  // METODOS ARTICULOS
  public Articulo getArticulos(int SKU) {
    return Empresa.getInstance().getArticulos(SKU);
  }

  public Articulo getArticulos(Articulo a) {
    return Empresa.getInstance().getArticulos(a);
  }

  public ArrayList<Articulo> getArticulos() {
    return Empresa.getInstance().getArticulos();
  }

  public ArrayList<Articulo> getArticulosNoStock() {
    ArrayList<Articulo> articulos = getArticulos();
    ArrayList<Articulo> articulosSinStock = new ArrayList<Articulo>();

    for (int i = 0; i < articulos.size(); i++) {
      Articulo current = articulos.get(i);
      if (current.getStock() == 0) {
        articulosSinStock.add(current);
      }
    }

    return articulosSinStock;
  }

  public void anadirStockArticulo(Articulo a, int cantidad) throws Exception {
    a.anadirStock(cantidad);
  }

  public void setStockArticulo(Articulo a, int stock) throws Exception {
    a.setStock(stock);
  }

  public void setCostoArticulo(Articulo a, double costo) throws Exception {
    a.setCosto(costo);
  }

  public void editarCifrasArticulo(Articulo a, int stock, double costo) throws Exception {
    a.setStock(stock);
    a.setCosto(costo);
  }
}
