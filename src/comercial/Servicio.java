package comercial;

import java.util.ArrayList;
import java.util.Date;

import empresa.Empresa;
import comercial.articulos.*;
import personas.*;
import excepciones.*;

public class Servicio {
	static int contadorServicios = 0;

	public final int nro;
	private Cliente cliente;
	private Date fecha;

	private double tiempoTrabajado;
	private TipoServicio tipoServicio;
	private EstadoServicio estadoServicio;
	private ArrayList<Tecnico> tecnicosAsignados = new ArrayList<Tecnico>();
	private ArrayList<Costo> articulosUtilizados = new ArrayList<Costo>();
	private ArrayList<Costo> otrosCostos = new ArrayList<Costo>();
	private final double costoViaje = Empresa.getInstance().getCostoViaje();
	private boolean almuerzo;
	private boolean facturado = false;

	public Servicio(Cliente cliente, Date fecha, TipoServicio tipoServicio, int tiempoTrabajado) throws Exception {
		this.nro = ++contadorServicios;
		this.cliente = cliente;
		this.fecha = fecha;
		this.tipoServicio = tipoServicio;
		this.tiempoTrabajado = tiempoTrabajado;
		this.estadoServicio = EstadoServicio.PROGRAMADO;
		this.almuerzo = false;

		preAnadirArticulos();
		Empresa.getInstance().agregarServicio(this);
	}

	private void preAnadirArticulos() throws Exception {
		if (tipoServicio == TipoServicio.INSTALACION) {
			Articulo cable = Empresa.getInstance().getArticulo(Cable.class);
			Articulo decoTV = Empresa.getInstance().getArticulo(DecodificadorTV.class);
			Articulo modem = Empresa.getInstance().getArticulo(Modem.class);
			Articulo divCoax = Empresa.getInstance().getArticulo(DivisorCoaxial.class);
			Articulo conCoax = Empresa.getInstance().getArticulo(ConectorCoaxial.class);

			anadirArticulo(cable, 3);
			anadirArticulo(decoTV, 1);
			anadirArticulo(modem, 1);
			anadirArticulo(divCoax, 1);
			anadirArticulo(conCoax, 6);
		}
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ArrayList<Tecnico> getTecnicos() {
		return tecnicosAsignados;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public TipoServicio getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(TipoServicio tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public double getTiempoTrabajado() {
		return tiempoTrabajado;
	}

	public EstadoServicio getEstadoServicio() {
		return estadoServicio;
	}

	public void setEstadoServicio(EstadoServicio estadoServicio) {
		this.estadoServicio = estadoServicio;
	}

	public ArrayList<Costo> getArticulos() {
		return articulosUtilizados;
	}

	private void setArticulos(ArrayList<Costo> articulos) {
		this.articulosUtilizados = articulos;
	}

	public ArrayList<Costo> getOtrosCostos() {
		return otrosCostos;
	}

	private void setOtrosCostos(ArrayList<Costo> otrosCostos) {
		this.otrosCostos = otrosCostos;
	}

	public double getCostoViaje() {
		return costoViaje;
	}

	public boolean isFacturado() {
		return facturado;
	}

	// EDICION DE SERVICIO
	public boolean anadirTecnico(Tecnico t) throws ServicioException {
		if (isFacturado()) {
			throw new ServicioException("Servicio ya facturado");
		}
		if (t == null || tecnicosAsignados.contains(t)) {
			return false;
		}
		return tecnicosAsignados.add(t);
	}

	public boolean removerTecnico(Tecnico t) throws Exception {
		if (isFacturado()) {
			throw new ServicioException("Servicio ya facturado");
		}
		return tecnicosAsignados.remove(t);
	}

	public boolean anadirArticulo(Articulo art, int cantidad) throws Exception {
		if (isFacturado()) {
			throw new ServicioException("Servicio ya facturado");
		}

		art.consumirStock(cantidad);

		Costo nuevoCosto = new Costo(cantidad, art);
		return articulosUtilizados.add(nuevoCosto);
	}

	public void removerArticulo(Articulo a) throws Exception {
		if (isFacturado()) {
			throw new ServicioException("Servicio ya facturado");
		}

		ArrayList<Costo> costos = new ArrayList<Costo>();

		for (Costo c : articulosUtilizados) {
			if (c.getArticulo().getClass() != a.getClass()) {
				costos.add(c);
			}
		}

		setArticulos(costos);
	}

	public boolean anadirOtroCostos(ArticuloExtra extraArt, int cantidad) throws ServicioException {
		if (isFacturado()) {
			throw new ServicioException("Servicio ya facturado");
		}

		Costo nuevoOtroCosto = new Costo(cantidad, extraArt);
		return otrosCostos.add(nuevoOtroCosto);
	}

	public void removerOtroCostos(ArticuloExtra ae) throws Exception {
		if (isFacturado()) {
			throw new ServicioException("Servicio ya facturado");
		}

		ArrayList<Costo> costos = new ArrayList<Costo>();

		for (Costo c : articulosUtilizados) {
			if (c.getArticulo().getClass() != ae.getClass()) {
				costos.add(c);
			}
		}

		setOtrosCostos(costos);
	}

	public double obtenerValorHoraServicio() {
		double aux = 0;

		for (Tecnico t : getTecnicos()) {
			aux += tiempoTrabajado * Empresa.getInstance().obtenerCostoHoraTecnico(t.getSeniority());
		}
		return aux;
	}

	public double obtenerTotalServicio() {
		double tiempoTrabajado = getTiempoTrabajado();

		double stHorasTecnico = obtenerValorHoraServicio() * tiempoTrabajado;
		double stArtsUtilizados = 0;
		double stOtrosArtsUtilizados = 0;

		for (Costo ca : articulosUtilizados) {
			stArtsUtilizados += ca.obtenerTotalCosto();
		}

		for (Costo co : otrosCostos) {
			stOtrosArtsUtilizados += co.obtenerTotalCosto();
		}

		return stHorasTecnico + stArtsUtilizados + stOtrosArtsUtilizados + costoViaje;
	}

	public boolean facturarServicio() throws ServicioException {
		if (isFacturado()) {
			throw new ServicioException("Servicio ya facturado");
		}

		boolean minHoras = tiempoTrabajado >= 0.5;
		boolean minTecnicos = tecnicosAsignados.size() > 0;

		if (minHoras && minTecnicos) {
			this.facturado = true;
		} else {
			throw new ServicioException("Verificar datos del servicio antes de facturar");
		}

		return this.facturado;
	}

	@Override
	public String toString() {
		return "Servicio [nro=" + nro + ", cliente=" + cliente + ", fecha=" + fecha + ", tiempoTrabajado="
				+ tiempoTrabajado + ", tipoServicio=" + tipoServicio + ", estadoServicio=" + estadoServicio
				+ ", tecnicosAsignados=" + tecnicosAsignados + ", articulosUtilizados=" + articulosUtilizados
				+ ", otrosCostos=" + otrosCostos + ", costoViaje=" + costoViaje + ", facturado=" + facturado + "]";
	}

	public boolean isIncluyeAlmuerzo() {
		return almuerzo;
	}

	public void setIncluyeAlmuerzo(boolean almuerzo) {
		this.almuerzo = almuerzo;
	}

	public void toggleIncluyeAlmuerzo() {
		this.almuerzo = !almuerzo;
	}

}
