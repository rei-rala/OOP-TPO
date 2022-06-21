package comercial;

import java.util.ArrayList;

import empresa.Empresa;
import personas.*;

public class Factura {
	static public int contadorFacturas = 0;
	static public ArrayList<Factura> facturas = new ArrayList<Factura>();

	private final int nro;
	private final Servicio servicio;
	private final double subtotal;
	private final double iva;
	private final double total;

	public Factura(Servicio servicio) {
		this.nro = ++contadorFacturas;
		this.servicio = servicio;
		this.subtotal = calcularSubTotal();
		this.iva = calcularIVA();
		this.total = calcularTotal();
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
		double tiempoTrabajado = servicio.getTiempoTrabajado();

		double stHorasTecnico = 0;
		for (int i = 0; i < servicio.getTecnicos().size(); i++) {
			Tecnico t = servicio.getTecnicos().get(i);

			stHorasTecnico += tiempoTrabajado * Empresa.getInstance().obtenerCostoHoraTecnico(t.getSeniority());
		}

		double subtotal = stHorasTecnico;
		return subtotal;
	}

	public double calcularGanancias() {
		return calcularSubTotal() * Empresa.getInstance().getRENTABILIDAD();
	}

	public double calcularIVA() {
		return calcularGanancias() * Empresa.getInstance().getIVA();
	}

	public double calcularTotal() {
		return calcularSubTotal() + calcularGanancias() + calcularIVA();
	}
}
