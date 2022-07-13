package personas;

import agenda.*;
import comercial.Servicio;
import empresa.Empresa;
import excepciones.*;

public class Cliente extends Persona {
	private static int contadorClientes = 0;

	private final int nro;
	private final Agenda agenda;
	
	
	public Cliente(String nombre, long dni, String direccion, String telefono) {
		super(nombre, dni, direccion, telefono);
		this.nro = ++contadorClientes;
		this.agenda = new Agenda(this);

		Empresa.getInstance().agregarCliente(this);
	}

	public Cliente(String nombre) {
		
		super(nombre);
		
		this.nro = ++contadorClientes;
		this.agenda = new Agenda(this);

		Empresa.getInstance().agregarCliente(this);
	}
	


	public int getNro() {
		return nro;
	}

	public Agenda getAgenda() {
		return agenda;
	}

	/**
	 * 
	 * @returns verdadero si el cliente tiene un servicio vigente
	 * 
	 */
	public boolean verificarPoseeServicio() {
		boolean poseeServicio = false;

		for (Dia d : getAgenda().getDias()) {
			if (d.verificarDiaOcupado()) {
				poseeServicio = true;
				break;
			}
		}
		return poseeServicio;
	}

	public boolean existeCliente(int dni) {

		for (Cliente c : Empresa.getInstance().getClientes()) {
			if (c.getDni() == dni)
				return true;
		}

		return false;

	}

	/**
	 * Un cliente solo podra tener asignado un servicio por vez Esto verifica la
	 * disponibilidad para cierto servicio
	 * 
	 */
	public void verificarDisponibilidad(Servicio s) throws Exception {
		if (verificarPoseeServicio()) {
			throw new AgendaException("El cliente ya tiene un servicio vigente");
		}

		agenda.verificarDisponibilidad(s);
	}

	public void asignarServicio(Servicio s) throws Exception {
		if (s.getCliente() == this) {
			throw new AsignacionException("El servicio ya se encuentra asignado a este cliente");
		}

		verificarDisponibilidad(s);
    
		agenda.asignarServicio(s);
		s.setCliente(this);

	}

	public String toStringShort() {
		return "Cliente [nro=" + nro + ", nombre=" + nombre + ", dni=" + dni + ", direccion=" + direccion + "]";
	}

	@Override
	public String toString() {
		return "Cliente [nro=" + nro + ", agenda=" + agenda + ", nombre=" + nombre + ", dni=" + dni + ", direccion="
				+ direccion + ", telefono=" + telefono + "]";
	}

}
