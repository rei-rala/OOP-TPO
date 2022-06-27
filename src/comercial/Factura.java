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
		double subtotal = servicio.obtenerTotalServicio();

		return subtotal;
	}

	public double calcularGanancias() {
		return calcularSubTotal() * Empresa.getInstance().getRENTABILIDAD();
	}

	public double calcularIVA() {
		return calcularGanancias() * Empresa.getInstance().getIVA();
	}

	public double calcularTotal() {
		double total = calcularSubTotal() + calcularGanancias() + calcularIVA();

		if (servicio.isIncluyeAlmuerzo()) {
			total -= (servicio.obtenerValorHoraServicio() / 2);
		}
		return total;
	}

	public double calcularMargen() {
		double total = calcularTotal();

		if (total > 0) {
			return calcularGanancias() / total;
		}
		return 0;
	}

	public Date getFecha() {
		return fecha;
	}
}
