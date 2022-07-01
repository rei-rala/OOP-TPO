package comercial;

import java.util.Date;

import empresa.Empresa;

public class Factura {
  static public int contadorFacturas = 0;

  private final int nro;
  private final Servicio servicio;
  private final double subtotal;
  private final double iva;
  private final double total;

  private final Date fecha;

  public Factura(Servicio servicio) {
    this.nro = ++contadorFacturas;
    this.fecha = new Date();
    this.servicio = servicio;
    this.subtotal = calcularSubTotal();
    this.iva = calcularIVA();
    this.total = calcularTotal();

    Empresa.getInstance().agregarFactura(this);
  }

  @Override
  public String toString() {
    return "Factura [nro=" + nro + ", servicio=" + servicio + ", subtotal=" + subtotal + ", iva=" + iva + ", total="
        + total + "]";
  }

  public int getNro() {
    return nro;
  }

  public Servicio getServicio() {
    return servicio;
  }

  public double calcularSubTotal() {
    double subtotal = servicio.calcularTotalServicio();

    return Empresa.getInstance().redondear(subtotal);
  }

  public double calcularGanancias() {
    double costoACargo = 0;

    if (servicio.isIncluyeAlmuerzo()) {
      costoACargo = servicio.obtenerValorHoraServicio() / 2;
    }

    return Empresa.getInstance().redondear(
        calcularSubTotal() * Empresa.getInstance().getRENTABILIDAD() - costoACargo - servicio.getCostoViaje());
  }

  public double calcularTotal() {
    double total = calcularSubTotal() + calcularGanancias();

    if (servicio.isIncluyeAlmuerzo()) {
      total -= (servicio.obtenerValorHoraServicio() / 2);
    }
    return Empresa.getInstance().redondear(total);
  }

  public double calcularIVA() {
    return Empresa.getInstance().redondear(calcularTotal() * Empresa.getInstance().getIVA());
  }

  public double calcularMargen() {
    double total = Empresa.getInstance().redondear(calcularTotal());

    if (total > 0) {
      return calcularGanancias() / total;
    }
    return 0;
  }

  public String calcularMargenStr() {
    double total = Empresa.getInstance().redondear(calcularTotal());

    return "" + (total > 0 ? Empresa.getInstance().redondear(calcularGanancias() / total * 100) : "0") + "%";

  }

  public Date getFecha() {
    return fecha;
  }
}
