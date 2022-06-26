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
	private boolean facturado = false;

	public Servicio(Cliente cliente, Date fecha, TipoServicio tipoServicio, int tiempoTrabajado) throws StockException {
		this.nro = ++contadorServicios;
		this.cliente = cliente;
		this.fecha = fecha;
		this.tipoServicio = tipoServicio;
		this.tiempoTrabajado = tiempoTrabajado;
		this.estadoServicio = EstadoServicio.PROGRAMADO;

		preAnadirArticulos();
		Empresa.getInstance().agregarServicio(this);
	}

	private void preAnadirArticulos() throws StockException {
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

	public boolean anadirArticulo(Articulo art, int cantidad) throws StockException {
		art.consumirStock(cantidad);

		Costo nuevoCosto = new Costo(cantidad, art);
		return articulosUtilizados.add(nuevoCosto);
	}

	public void removerArticulo(Costo costo) {
		articulosUtilizados.add(costo);
	}

	public boolean anadirOtroCostos(ArticuloExtra extraArt, int cantidad) {
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
		return "Servicio [nro=" + nro + ", cliente=" + cliente + ", fecha=" + fecha + ", tiempoTrabajado="
				+ tiempoTrabajado + ", tipoServicio=" + tipoServicio + ", estadoServicio=" + estadoServicio
				+ ", tecnicosAsignados=" + tecnicosAsignados + ", articulosUtilizados=" + articulosUtilizados
				+ ", otrosCostos=" + otrosCostos + ", costoViaje=" + costoViaje + ", facturado=" + facturado + "]";
	}

}
