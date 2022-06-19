package personas;

import empresa.Empresa;

public class Cliente extends Persona {
	static int contadorClientes = 0;

	private final int nro;
	private final /* AGENDA */ String AGENDA;

	public Cliente(String nombre, long dni, String direccion, String telefono) {
		super(nombre, dni, direccion, telefono);
		this.nro = ++contadorClientes;
		this.AGENDA = "XXXX";

		Empresa.getInstance().agregarCliente(this);
	}

	public int getNro() {
		return nro;
	}

	public String getAgenda() {
		return AGENDA;
	}

	@Override
	public String toString() {
		return "Cliente [nro=" + nro + ", agenda=" + AGENDA + ", nombre=" + nombre + ", dni=" + dni + ", direccion="
				+ direccion + ", telefono=" + telefono + "]";
	}

}
