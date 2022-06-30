package comercial;

import java.util.ArrayList;
import java.util.Date;

import agenda.Turno;
import empresa.Empresa;
import comercial.articulos.*;
import personas.*;
import excepciones.*;
import main.DateAux;

public class Servicio {
	static int contadorServicios = 0;

	public final int nro;
	private Cliente cliente;
	private final Date fecha;

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
	private int turnoInicio = -1;
	private int turnoFin = -1;

	public Servicio(Date fecha, TipoServicio tipoServicio, Turno turno, int turnoInicio, int turnoFin) throws Exception {
		this.nro = ++contadorServicios;
		this.fecha = fecha;
		this.tipoServicio = tipoServicio;
		this.turno = turno;
		this.turnoInicio = turnoInicio;
		this.turnoFin = turnoFin;
		this.tiempoTrabajado = DateAux.calcularHoras(turnoInicio, turnoFin);
		this.estadoServicio = EstadoServicio.PROGRAMADO;
		this.almuerzo = false;
		this.enPoderTecnico = false;
		this.facturado = false;

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

	public int getNro() {
		return nro;
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

	public TipoServicio getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(TipoServicio tipoServicio) throws ServicioException {
		if (facturado || enPoderTecnico) {
			throw new ServicioException("No se puede cambiar el tipo de servicio una vez facturado o en poder del tecnico");
		}
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

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public int getturnoInicio() {
		return turnoInicio;
	}

	public void setturnoInicio(int turnoInicio) {
		this.turnoInicio = turnoInicio;
	}

	public int getturnoFin() {
		return turnoFin;
	}

	public void setturnoFin(int turnoFin) {
		this.turnoFin = turnoFin;
	}

	// EDICION DE SERVICIO
	public void anadirTecnico(Tecnico t) throws Exception {
		if (facturado || enPoderTecnico) {
			throw new ServicioException(
					"No se puede cambiar añadir tecnico una vez facturado o estando el servicio en poder del tecnico");
		}
		if (t == null || tecnicosAsignados.contains(t)) {
			throw new AsignacionException("Tecnico ya asignado a este servicio");
		}

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

	public void liberarDesdeCallcenter() throws ServicioException {
		if (this.enPoderTecnico) {
			throw new ServicioException("El servicio se encuentra en poder de lo/s tecnico/s.");
		}
		if (0 >= this.tecnicosAsignados.size()) {
			throw new ServicioException("El servicio debe contar con al menos 1 (un) tecnico");
		}

		enPoderTecnico = true;
	}

	public double obtenerValorHoraServicio() {
		double aux = 0;

		for (Tecnico t : getTecnicos()) {
			aux += tiempoTrabajado * Empresa.getInstance().getCostoHoraTecnico(t.getSeniority());
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
}
