package comercial;

import java.util.ArrayList;
import java.util.Date;

import personas.Cliente;
import personas.Tecnico;

public class Servicio {
	static ArrayList<Servicio> servicios = new ArrayList<Servicio>();
	static int contadorServicios = 0;

	private Cliente cliente;
	private ArrayList<Tecnico> tecnicos = new ArrayList<Tecnico>();
	private Date fecha;
	private String TURNOS_TRABAJADOS;
	private double tiempoTrabajado;
	private TipoServicio tipoServicio;
	private EstadoServicio estadoServicio;
	private ArrayList<Recurso> articulos = new ArrayList<Recurso>();
	private ArrayList<Recurso> otrosCostos = new ArrayList<Recurso>();
	private double costoViaje;

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ArrayList<Tecnico> getTecnicos() {
		return tecnicos;
	}

	public void setTecnicos(ArrayList<Tecnico> tecnicos) {
		this.tecnicos = tecnicos;
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

	public ArrayList<Recurso> getArticulos() {
		return articulos;
	}

	public void setArticulos(ArrayList<Recurso> articulos) {
		this.articulos = articulos;
	}

	public ArrayList<Recurso> getOtrosCostos() {
		return otrosCostos;
	}

	public void setOtrosCostos(ArrayList<Recurso> otrosCostos) {
		this.otrosCostos = otrosCostos;
	}

	public double getCostoViaje() {
		return costoViaje;
	}

	public void setCostoViaje(double costoViaje) {
		this.costoViaje = costoViaje;
	}

}
