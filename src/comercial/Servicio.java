package comercial;

import java.util.ArrayList;
import java.util.Date;

import agenda.Turno;
import empresa.Empresa;
import comercial.articulos.*;
import personas.*;
import excepciones.*;

public class Servicio {
	static int contadorServicios = 0;

	public final int nro;
	private final Cliente cliente;
	private Date fecha;

	private double tiempoTrabajado;
	private TipoServicio tipoServicio;
	private EstadoServicio estadoServicio;
	private ArrayList<Tecnico> tecnicosAsignados = new ArrayList<Tecnico>();
	private ArrayList<Costo> articulosUtilizados = new ArrayList<Costo>();
	private ArrayList<Costo> otrosCostos = new ArrayList<Costo>();
	private final double costoViaje = Empresa.getInstance().getCostoViaje();
	private boolean almuerzo;
	private boolean enPoderTecnico;
	private boolean facturado;

	private Turno turno;
	private int tnoInicio = -1;
	private int tnoFin = -1;

	public Servicio(Cliente cliente, Date fecha, TipoServicio tipoServicio, double tiempoTrabajado) throws Exception {
		preVerificarTiempoTrabajado(tiempoTrabajado);

		this.nro = ++contadorServicios;
		this.cliente = cliente;
		this.fecha = fecha;
		this.tipoServicio = tipoServicio;
		this.tiempoTrabajado = tiempoTrabajado;
		this.estadoServicio = EstadoServicio.PROGRAMADO;
		this.almuerzo = false;
		this.enPoderTecnico = false;
		this.facturado = false;

		preAnadirArticulos();
		Empresa.getInstance().agregarServicio(this);
	}

	private void preVerificarTiempoTrabajado(double tiempoTrabajado) throws Exception {
		if (tipoServicio == TipoServicio.REPARACION) {
			if (0.5 > tiempoTrabajado) {
				throw new ServicioException("El tiempo de reparacion debe ser de al menos media hora.");
			}
			return;
		}

		if (1 > tiempoTrabajado) {
			throw new ServicioException("El tiempo de instalacion debe ser de al menos una hora.");
		}
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

	public ArrayList<Costo> getOtrosCostos() {
		return otrosCostos;
	}

	public double getCostoViaje() {
		return costoViaje;
	}

	public boolean isFacturado() {
		return facturado;
	}

	// EDICION DE SERVICIO
	public void anadirTecnico(Tecnico t) throws Exception {
		if (isEnPoderTecnico()) {
			throw new ServicioException("El servicio se encuentra en poder de lo/s tecnico/s");
		}
		if (isFacturado()) {
			throw new ServicioException("Servicio ya facturado");
		}
		if (t == null || tecnicosAsignados.contains(t)) {
			throw new AsignacionException("Tecnico ya asignado a este servicio");
		}

		t.getAgenda().verificarDisponibilidad(fecha, turno, tnoInicio, tnoFin);
		tecnicosAsignados.add(t);
	}

	public boolean anadirArticulo(Articulo art, int cantidad) throws Exception {
		if (isFacturado()) {
			throw new ServicioException("Servicio ya facturado");
		}

		art.consumirStock(cantidad);

		Costo nuevoCosto = new Costo(cantidad, art);
		return articulosUtilizados.add(nuevoCosto);
	}

	public boolean anadirOtroCostos(ArticuloExtra extraArt, int cantidad) throws ServicioException {
		if (isFacturado()) {
			throw new ServicioException("Servicio ya facturado");
		}

		Costo nuevoOtroCosto = new Costo(cantidad, extraArt);
		return otrosCostos.add(nuevoOtroCosto);
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

	public boolean isEnPoderTecnico() {
		return enPoderTecnico;
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

	public void liberarDesdeCallcenter() throws ServicioException {
		if (this.enPoderTecnico) {
			throw new ServicioException("El servicio se encuentra en poder de lo/s tecnico/s.");
		}
		if (0 >= this.tecnicosAsignados.size()) {
			throw new ServicioException("El servicio debe contar con al menos 1 (un) tecnico");
		}

		enPoderTecnico = true;
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

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		if (this.turno == null) {
			this.turno = turno;
		}
	}

	public int getTnoInicio() {
		return tnoInicio;
	}

	public void setTnoInicio(int tnoInicio) {
		if (this.tnoInicio == -1) {
			this.tnoInicio = tnoInicio;
		}
	}

	public int getTnoFin() {
		return tnoFin;
	}

	public void setTnoFin(int tnoFin) {
		if (this.tnoFin == -1) {
			this.tnoFin = tnoFin;
		}
	}

}
