package personas;

import agenda.Agenda;
import empresa.Empresa;

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

	@Override
	public String toString() {
		return "Cliente [nro=" + nro + ", agenda=" + agenda + ", nombre=" + nombre + ", dni=" + dni + ", direccion="
				+ direccion + ", telefono=" + telefono + "]";
	}

}
