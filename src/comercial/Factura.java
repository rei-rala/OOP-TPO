package comercial;

import java.util.ArrayList;

public class Factura {
	static public int contadorFacturas = 0;
	static public ArrayList<Factura> facturas = new ArrayList<Factura>();

	private int nro;
	private Servicio servicio;

	public Factura(int nro, Servicio servicio) {
		super();
		this.nro = nro;
		this.servicio = servicio;
	}

	public int getNro() {
		return nro;
	}

	public void setNro(int nro) {
		this.nro = nro;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	@Override
	public String toString() {
		return "Factura [nro=" + nro + ", servicio=" + servicio + "]";
	}
}
