package agenda;

import main.DateAux;
import comercial.*;
import excepciones.AsignacionException;

public class FraccionTurno {
	private final Dia dia;
	private final Turno turno;
	private final int nro;
  private Servicio servicioAsignado;
  private boolean estaOcupado;
  private String horario;

  public FraccionTurno(Dia dia, Turno turno, int nro) {
    this.dia = dia;
    this.turno = turno;
    this.nro = nro;
    this.horario = DateAux.getHorarioUnico(turno, nro);
    this.estaOcupado = false;
    this.servicioAsignado = null;
  }

  public String getHorario() {
    return horario;
  }

  public void asignarServicio(Servicio s) throws AsignacionException {
    if (getEstaOcupado()) {
      throw new AsignacionException("La fraccion del turno ya cuenta con asignacion");
    }
    servicioAsignado = s;
    marcarOcupado();
  }

  public boolean getEstaOcupado() {
    if (servicioAsignado == null) {
      estaOcupado = false;
    } else {
      String programado = EstadoServicio.PROGRAMADO.toString();
      String asignado = EstadoServicio.EN_CURSO.toString();
      String es = servicioAsignado.getEstadoServicio().toString();
      boolean esProgramado = es.contains(programado);
      boolean esAsignado = es.contains(asignado);

      estaOcupado = (esProgramado || esAsignado);
    }

    return estaOcupado;
  }

  public Servicio getServicioAsignado() {
    return servicioAsignado;
  }

  public void marcarOcupado() {
    estaOcupado = true;
  }

  public Dia getDia() {
    return dia;
  }

  public int getNro() {
    return nro;
  }

  public Turno getTurno() {
    return turno;
  }

  @Override
  public String toString() {
    return "FraccionTurno [servicioAsignado=" + servicioAsignado + ", estaOcupado=" + estaOcupado + ", diaSemana="
        + dia.getDiaSemana() + ", turno=" + turno + ", horario=" + horario + ", nro=" + nro + "]";
  }

}
