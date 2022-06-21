package personas;

import java.util.ArrayList;
import java.util.Date;

import comercial.*;
import empresa.Empresa;
import excepciones.ServicioException;

public class Administrativo extends Interno {

	public Administrativo(String nombre, long dni, String direccion, String telefono, String contrasena) {
		super(nombre, dni, direccion, telefono, contrasena);
	}

	// ALTERNATIVO SIN DATOS
	public Administrativo(String nombre, String contrasena) {
		super(nombre, contrasena);
	}

	@Override
	public String toString() {
		return "Administrativo [legajo=" + legajo + ", nombre=" + nombre + ", dni=" + dni + ", direccion=" + direccion
				+ ", telefono=" + telefono + "]";
	}

	// Metodos para SERVICIOS
	public Factura facturarServicio(Servicio s) throws ServicioException {
		Servicio aFacturar = Empresa.getInstance().getServicios(s);
		Factura nuevaFactura = null;

		if (aFacturar != null) {
			boolean exitoAlFacturar = aFacturar.facturarServicio();

			if (exitoAlFacturar) {
				nuevaFactura = new Factura(aFacturar);
				Empresa.getInstance().agregarFactura(nuevaFactura);
			}
		}
		return nuevaFactura;
	}

	// Metodos para FACTURAS (propios de administrativo)
	public Factura verFacturaEmpresa(int nroFactura) {
		return Empresa.getInstance().getFacturas(nroFactura);
	}

	public Servicio crearServicio(Cliente cliente, Date fecha, TipoServicio tipoServicio) {
		return new Servicio(cliente, fecha, tipoServicio);
	}

	public Servicio verServicio(int nroServicio) {
		return Empresa.getInstance().getServicios(nroServicio);
	}

	public ArrayList<Servicio> verServiciosAsignados(int legajoTecnico) {
		Tecnico tecnicoBuscado = Empresa.getInstance().getTecnicos(legajoTecnico);
		ArrayList<Servicio> servAsignados = null;

		if (tecnicoBuscado != null) {
			servAsignados = tecnicoBuscado.getServiciosAsignados();
		}

		return servAsignados;
	}

	public void editarEstadoServicio(int SKU, EstadoServicio estadoServicio) {
		Empresa.getInstance().getServicios(SKU).setEstadoServicio(estadoServicio);
	}

}
