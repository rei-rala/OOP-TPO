package comercial.articulos;

import comercial.Recurso;

public class Costo {
  @Override
  public String toString() {
    return "[articulo=" + articulo + ", cantidad=" + cantidad + "]";
  }

  private int cantidad;
  private Recurso articulo;

  public Costo(int cantidad, Recurso articulo) {
    this.cantidad = cantidad;
    this.articulo = articulo;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }

  public Recurso getArticulo() {
    return articulo;
  }

  public void setArticulo(Recurso articulo) {
    this.articulo = articulo;
  }

  public double obtenerTotalCosto() {
    return articulo.getCosto() * cantidad;
  }
}
