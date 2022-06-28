package personas;

import java.util.ArrayList;
import java.util.Date;

import agenda.*;
import comercial.Servicio;
import empresa.Empresa;
import excepciones.AsignacionException;

public class Cliente extends Persona {
	static int contadorClientes = 0;

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

	public void asignarServicio(Servicio s, Date fecha, Turno t, int desde, int hasta) throws Exception {
		if (verificarServicioVigente()) {
			throw new AsignacionException("El cliente ya tiene agendado un servicio");
		}

		verificarDisponibilidad(fecha, t, desde, hasta);
		agenda.asignarServicio(s, fecha, t, desde, hasta);
	}

	public void asignarServicio(Servicio s, Date fecha, Turno t, int nroTurno) throws Exception {
		if (verificarServicioVigente()) {
			throw new AsignacionException("El cliente ya tiene agendado un servicio");
		}
		verificarDisponibilidad(fecha, t, nroTurno);

		agenda.asignarServicio(s, fecha, t, nroTurno);
	}

	public ArrayList<FraccionTurno> verTurnosDisponibles(Turno t) {
		return agenda.obtenerTodosTurnosDisponible(t);
	}

	public ArrayList<FraccionTurno> verTurnosDisponibles() {
		return agenda.obtenerTodosTurnosDisponible();
	}

	public boolean verificarServicioVigente() {
		return agenda.verificarServicioVigente();
	}

	public void verificarDisponibilidad(Date fecha, Turno turno, int nroTurno) throws Exception {
		agenda.verificarDisponibilidad(fecha, turno, nroTurno);
	}

	public void verificarDisponibilidad(Date fecha, Turno turno, int desde, int hasta) throws Exception {
		agenda.verificarDisponibilidad(fecha, turno, desde, hasta);
	}

	@Override
	public String toString() {
		return "Cliente [nro=" + nro + ", agenda=" + agenda + ", nombre=" + nombre + ", dni=" + dni + ", direccion="
				+ direccion + ", telefono=" + telefono + "]";
	}

}
