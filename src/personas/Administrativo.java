package personas;

import java.util.ArrayList;

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
	public Servicio verServicio(int nroServicio) {
		return Empresa.getInstance().getServicios(nroServicio);
	}

	public ArrayList<Servicio> verServiciosAFacturar() {
		ArrayList<Servicio> listaServicios = Empresa.getInstance().getServicios();
		ArrayList<Servicio> listaSinFacturar = new ArrayList<Servicio>();

		for (int i = 0; i < listaServicios.size(); i++) {
			Servicio currentServicio = listaServicios.get(i);
			boolean esServicioFinalizado = currentServicio.getEstadoServicio() == EstadoServicio.FINALIZADO;
			boolean esServicioFacturado = currentServicio.isFacturado();

			if (esServicioFinalizado && esServicioFacturado == false) {
				listaSinFacturar.add(currentServicio);
			}
		}
		return listaSinFacturar;
	}

	public Factura facturarServicio(Servicio s) throws Exception {
		Servicio aFacturar = Empresa.getInstance().getServicios(s);
		Factura nuevaFactura = null;

		if (aFacturar == null) {
			throw new ServicioException("Servicio invalido");
		}

		boolean exitoAlFacturar = aFacturar.facturarServicio();

		if (exitoAlFacturar) {
			nuevaFactura = new Factura(aFacturar);
		} else {
			throw new ServicioException("Error desconocido al facturar");
		}
		return nuevaFactura;

	}

	// Metodos para FACTURAS (propios de administrativo)
	public Factura verFacturaEmpresa(int nroFactura) {
		return Empresa.getInstance().getFacturas(nroFactura);
	}

}
