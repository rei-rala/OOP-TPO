package comercial;

import java.util.ArrayList;
import java.util.Date;

import empresa.Empresa;
import excepciones.ServicioException;
import personas.Cliente;
import personas.Tecnico;

public class Servicio {
	static ArrayList<Servicio> servicios = new ArrayList<Servicio>();
	static int contadorServicios = 0;

	public final int nro;
	private Cliente cliente;
	private Date fecha;

	// TODO: Agenda!
	private String TURNOS_TRABAJADOS;
	private double tiempoTrabajado;
	private TipoServicio tipoServicio;
	private EstadoServicio estadoServicio = EstadoServicio.PROGRAMADO;
	private ArrayList<Tecnico> tecnicosAsignados = new ArrayList<Tecnico>();
	private ArrayList<Costo> articulosUtilizados = new ArrayList<Costo>();
	private ArrayList<Costo> otrosCostos = new ArrayList<Costo>();
	private final double costoViaje = Empresa.getInstance().getCostoViaje();
	private boolean facturado = false;

	public Servicio(Cliente cliente, Date fecha, TipoServicio tipoServicio) {
		this.nro = ++contadorServicios;
		this.cliente = cliente;
		this.fecha = fecha;
		this.tipoServicio = tipoServicio;
		Empresa.getInstance().agregarServicio(this);
	}

	public Servicio(Cliente cliente, Date fecha, TipoServicio tipoServicio, EstadoServicio estadoServicio) {
		this.nro = ++contadorServicios;
		this.cliente = cliente;
		this.fecha = fecha;
		this.tipoServicio = tipoServicio;
		this.estadoServicio = estadoServicio;

		Empresa.getInstance().agregarServicio(this);
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

	public String getTURNOS_TRABAJADOS() {
		return TURNOS_TRABAJADOS;
	}

	public void setTURNOS_TRABAJADOS(String tURNOS_TRABAJADOS) {
		TURNOS_TRABAJADOS = tURNOS_TRABAJADOS;
	}

	public double getTiempoTrabajado() {
		return tiempoTrabajado;
	}

	public void setTiempoTrabajado(double tiempoTrabajado) {
		this.tiempoTrabajado = tiempoTrabajado;
	}

	public TipoServicio getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(TipoServicio tipoServicio) {
		this.tipoServicio = tipoServicio;
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

	public void setArticulos(ArrayList<Costo> articulos) {
		this.articulosUtilizados = articulos;
	}

	public ArrayList<Costo> getOtrosCostos() {
		return otrosCostos;
	}

	public void setOtrosCostos(ArrayList<Costo> otrosCostos) {
		this.otrosCostos = otrosCostos;
	}

	public double getCostoViaje() {
		return costoViaje;
	}

	public boolean isFacturado() {
		return facturado;
	}

	// EDICION DE SERVICIO
	public boolean anadirTecnico(Tecnico t) {
		if (t == null || tecnicosAsignados.contains(t)) {
			return false;
		}
		return tecnicosAsignados.add(t);
	}

	public boolean removerTecnico(Tecnico t) {
		return tecnicosAsignados.remove(t);
	}

	public boolean anadirArticulo(int cantidad, Articulo art) {
		Costo nuevoCosto = new Costo(cantidad, art);
		return articulosUtilizados.add(nuevoCosto);
	}

	public void removerArticulo(Costo costo) {
		articulosUtilizados.add(costo);
	}

	public boolean anadirOtroCostos(int cantidad, ArticuloExtra extraArt) {
		Costo nuevoOtroCosto = new Costo(cantidad, extraArt);
		return otrosCostos.add(nuevoOtroCosto);
	}

	public void removerOtroCostos(Costo otrosGastos) {
		otrosCostos.remove(otrosGastos);
	}

	public boolean facturarServicio() throws ServicioException {
		boolean minHoras = tiempoTrabajado > 0;
		boolean minTecnicos = tecnicosAsignados.size() > 0;
		boolean minArticulos = articulosUtilizados.size() > 0;

		if (minHoras && minTecnicos && minArticulos) {
			this.facturado = true;
		} else {
			throw new ServicioException("Verificar datos del servicio antes de facturar");
		}

		return this.facturado;
	}

	@Override
	public String toString() {
		return "Servicio [nro=" + nro + ", cliente=" + cliente + ", fecha=" + fecha + ", TURNOS_TRABAJADOS="
				+ TURNOS_TRABAJADOS + ", tiempoTrabajado=" + tiempoTrabajado + ", tipoServicio=" + tipoServicio
				+ ", estadoServicio=" + estadoServicio + ", tecnicosAsignados=" + tecnicosAsignados
				+ ", articulosUtilizados=" + articulosUtilizados + ", otrosCostos=" + otrosCostos + ", costoViaje="
				+ costoViaje + ", facturado=" + facturado + "]";
	}

}
