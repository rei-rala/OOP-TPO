package gui.parciales;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import agenda.*;
import comercial.*;
import excepciones.*;
import gui.Gui;
import main.DateAux;
import personas.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JTextField;

public class PanelCreacionServicio extends JPanel {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Gui g = Gui.getInstance();
  private Callcenter callcenterUser = (Callcenter) g.getUsuarioLogeado();

  private Servicio currentEdicionServicio;
  private JPanel pListadoClientes;
  private JPanel pEdicionServicioCC;
  private JPanel panel;
  private JPanel pListadoServicios;

  private ArrayList<Dia> diaSeleccionCliente = new ArrayList<Dia>();
  private ArrayList<FraccionTurno> fraccionesSeleccionCliente = new ArrayList<FraccionTurno>();
  private JLabel lblNewLabel;
  private JTextField tfNroServicio;
  private JLabel lblNewLabel_1;
  private JTextField tfFechaServ;
  private JLabel lblNewLabel_2;
  private JTextField tfTipoServ;
  private JLabel lblNewLabel_3;
  private JTextField tfEstadoServ;
  private JLabel lblNewLabel_4;
  private JPanel pTecAsignados;
  private JButton btnVerTecnicos;
  private JButton btnAgregarTecnico;
  private JLabel lblNewLabel_5;
  private JTextField tfTiempoServicio;
  private JLabel lblNewLabel_6;
  private JButton btnAvanzarEstado;
  private JLabel lblNewLabel_7;
  private JTextField tfCliente;

  /**
   * Create the panel.
   */
  public PanelCreacionServicio() {
    setBorder(new TitledBorder(null, "Crear servicio", TitledBorder.CENTER, TitledBorder.TOP, null, null));
    setLayout(new GridLayout(0, 2, 0, 0));

    panel = new JPanel();
    add(panel);
    panel.setLayout(new GridLayout(0, 1, 0, 0));

    pListadoClientes = new JPanel();
    panel.add(pListadoClientes);
    pListadoClientes.setBorder(new TitledBorder(
        new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Clientes",
        TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    pListadoClientes.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    pListadoServicios = new JPanel();
    pListadoServicios
        .setBorder(new TitledBorder(null, "Servicios", TitledBorder.CENTER, TitledBorder.TOP, null, null));
    panel.add(pListadoServicios);
    pListadoServicios.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    pEdicionServicioCC = new JPanel();
    pEdicionServicioCC.setBorder(
        new TitledBorder(null, "Edicion del servicio", TitledBorder.CENTER, TitledBorder.TOP, null, null));
    add(pEdicionServicioCC);
    pEdicionServicioCC.setLayout(new GridLayout(0, 2, 0, 0));

    lblNewLabel = new JLabel("Numero");
    pEdicionServicioCC.add(lblNewLabel);

    tfNroServicio = new JTextField();
    tfNroServicio.setEnabled(false);
    tfNroServicio.setEditable(false);
    pEdicionServicioCC.add(tfNroServicio);
    tfNroServicio.setColumns(10);

    lblNewLabel_1 = new JLabel("Creacion");
    pEdicionServicioCC.add(lblNewLabel_1);

    tfFechaServ = new JTextField();
    pEdicionServicioCC.add(tfFechaServ);
    tfFechaServ.setColumns(10);

    lblNewLabel_7 = new JLabel("Cliente");
    pEdicionServicioCC.add(lblNewLabel_7);

    tfCliente = new JTextField();
    pEdicionServicioCC.add(tfCliente);
    tfCliente.setColumns(10);

    lblNewLabel_2 = new JLabel("Tipo");
    pEdicionServicioCC.add(lblNewLabel_2);

    tfTipoServ = new JTextField();
    pEdicionServicioCC.add(tfTipoServ);
    tfTipoServ.setColumns(10);

    lblNewLabel_3 = new JLabel("Estado");
    pEdicionServicioCC.add(lblNewLabel_3);

    tfEstadoServ = new JTextField();
    pEdicionServicioCC.add(tfEstadoServ);
    tfEstadoServ.setColumns(10);

    lblNewLabel_4 = new JLabel("Tecnicos asignados");
    pEdicionServicioCC.add(lblNewLabel_4);

    pTecAsignados = new JPanel();
    pEdicionServicioCC.add(pTecAsignados);
    pTecAsignados.setLayout(new GridLayout(0, 1, 0, 0));

    btnVerTecnicos = new JButton("Ver");
    pTecAsignados.add(btnVerTecnicos);

    btnAgregarTecnico = new JButton("Agregar");
    pTecAsignados.add(btnAgregarTecnico);

    lblNewLabel_5 = new JLabel("Tiempo inicial");
    pEdicionServicioCC.add(lblNewLabel_5);

    tfTiempoServicio = new JTextField();
    pEdicionServicioCC.add(tfTiempoServicio);
    tfTiempoServicio.setColumns(10);

    lblNewLabel_6 = new JLabel("AVANZAR ESTADO");
    pEdicionServicioCC.add(lblNewLabel_6);

    btnAvanzarEstado = new JButton("GO");
    btnAvanzarEstado.setEnabled(false);
    pEdicionServicioCC.add(btnAvanzarEstado);

    refrescarTodo();
  }

  public void setCurrentEdicionServicio(Servicio s) {
    currentEdicionServicio = s;
    refrescarTodo();
  }

  private void refrescarTodo() {
    setVisible(false);

    poblarClientes();
    poblarServicios();

    Servicio ces = currentEdicionServicio;
    boolean cesNull = ces == null;
    boolean isBtnEnabled = !cesNull;

    for (Component c : pEdicionServicioCC.getComponents()) {
      c.setEnabled(isBtnEnabled);
    }

    for (Component c : pTecAsignados.getComponents()) {
      c.setEnabled(isBtnEnabled);
    }

    if (cesNull) {
      tfNroServicio.setText("");
      tfFechaServ.setText("");
      tfTipoServ.setText("");
      tfEstadoServ.setText("");
      tfTiempoServicio.setText("");
      tfCliente.setText("");
      btnAvanzarEstado.setText("Seleccione servicio");

      btnVerTecnicos.removeActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
      });
      btnAvanzarEstado.removeActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
      });
      btnAgregarTecnico.removeActionListener(null);
      setVisible(true);
      return;
    }

    if (!ces.isEnPoderTecnico()) {
      btnAvanzarEstado.setText("Enviar a tecnico");
    }

    tfNroServicio.setText("" + ces.nro);
    tfFechaServ.setText("" + ces.getFecha());
    tfCliente.setText(g.getCliente(ces));
    tfTipoServ.setText("" + ces.getTipoServicio());
    tfEstadoServ.setText("" + ces.getEstadoServicio());
    tfTiempoServicio.setText("" + ces.getTiempoTrabajado());
    btnAgregarTecnico.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        anadirTecnicoServicio(ces);
      }
    });
    btnVerTecnicos.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mostrarTecnicos(ces);
      }
    });
    btnAvanzarEstado.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        avanzarEstadoServicio(ces);
      }
    });

    setVisible(true);
  }

  private void poblarClientes() {
    pListadoClientes.setVisible(false);
    pListadoClientes.removeAll();

    ArrayList<Cliente> clEmpresa = g.empresa.getClientes();

    if (0 >= clEmpresa.size()) {
      pListadoClientes.add(new JLabel("No hay clientes en la empresa :("));
    }

    for (Cliente c : clEmpresa) {

      JButton btnCliente = new JButton(g.getClienteShort(c));

      if (c.verificarServicioVigente()) {
        btnCliente.setEnabled(false);
        btnCliente.setText(btnCliente.getText() + " [Serv agendado]");
        btnCliente.removeActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
          }
        });
      } else {
        btnCliente.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            intentarCrearServicio(c);

          }
        });
      }

      pListadoClientes.add(btnCliente);
    }

    pListadoClientes.setVisible(true);
  }

  private void poblarServicios() {
    pListadoServicios.setVisible(false);
    pListadoServicios.removeAll();

    ArrayList<Servicio> sEmpresa = g.empresa.getServicios();

    if (0 >= sEmpresa.size()) {
      pListadoServicios.add(new JLabel("No hay servicios en la empresa :("));
    }
    for (Servicio s : sEmpresa) {
      if (s.isEnPoderTecnico()) {
        continue;
      }
      if (s.getEstadoServicio() != EstadoServicio.PROGRAMADO) {
        continue;
      }
      JButton btnServicio = new JButton(g.getServicioTitle(s));
      btnServicio.addActionListener(e -> {
        setCurrentEdicionServicio(s);
      });
      pListadoServicios.add(btnServicio);
    }

    pListadoServicios.setVisible(true);
  }

  private String formatearTecnicos(Servicio s) {
    ArrayList<Tecnico> alT = s.getTecnicos();
    String format = "Tecnicos: " + alT.size();

    for (Tecnico t : alT) {
      format += "\nLegajo: " + t.getLegajo() + ", Nombre: " + t.getNombre() + ", Seniority: " + t.getSeniority();
    }
    return format;
  }

  private void mostrarTecnicos(Servicio s) {
    String formateado = formatearTecnicos(s);

    JOptionPane.showMessageDialog(null, formateado);
  }

  private ArrayList<String> obtenerTurnosDisponibles(ArrayList<FraccionTurno> alFT) throws Exception {
    fraccionesSeleccionCliente.clear();

    ArrayList<String> fraccionesDia = new ArrayList<String>();

    for (int i = 0; i < alFT.size(); i++) {
      FraccionTurno fr = alFT.get(i);

      if (fr.getEstaOcupado()) {
        continue;
      }
      fraccionesDia.add("" + (i + 1) + ") Horario: " + fr.getHorario() + " (" + fr.getTurno() + ")");
      fraccionesSeleccionCliente.add(fr);
    }

    if (0 >= diaSeleccionCliente.size()) {
      throw new AsignacionException("Dia no valido");
    }

    return fraccionesDia;
  }

  private void avanzarEstadoServicio(Servicio s) {
    try {
      if (JOptionPane.showConfirmDialog(null,
          "Desea liberar el servicio desde Callcenter? Pasara a poder de los tecnicos asignados.") == 1) {
        callcenterUser.liberarServicioCallcenter(s);
        setCurrentEdicionServicio(null);
      }

      JOptionPane.showMessageDialog(null, "Servicio Nro " + s.getNro() + " en poder de los tecnicos.");
    } catch (Exception e) {
      g.errorHandler(e);
    }
  }

  private String formatearTurnosDisponibles(ArrayList<String> alS) {
    String result = "Turnos disponibles:\n";
    for (String s : alS) {
      result += s + "\n";
    }
    return result;
  }

  private String obtenerTurnosFormateados(Dia d) throws Exception {
    return formatearTurnosDisponibles(obtenerTurnosDisponibles(d.obtenerFraccionesTurno()));
  }

  private FraccionTurno obtenerSeleccionTurno(Dia d, String instruccion) throws Exception {
    String tf = obtenerTurnosFormateados(d);
    String turnoSeleccion = JOptionPane.showInputDialog(null, tf,
        "Seleccione turno -> " + instruccion.toUpperCase(), JOptionPane.DEFAULT_OPTION);
    int turnoNro = g.validarInt(turnoSeleccion);

    if (1 > turnoNro || turnoNro > fraccionesSeleccionCliente.size()) {
      throw new AsignacionException("Turno no valido");
    }

    return fraccionesSeleccionCliente.get(turnoNro - 1);
  }

  @SuppressWarnings("deprecation")
  private ArrayList<String> obtenerDiasDisponibles(Agenda a) {
    diaSeleccionCliente.clear();
    ArrayList<String> diasDisp = new ArrayList<String>();

    Date date = DateAux.getNextDay(DateAux.getToday());
    while (diasDisp.size() < 6) {

      Dia d = a.obtenerDiaAgenda(date);
      date = DateAux.getNextDay(date);

      if (d == null) {
        continue;
      }
      if (d.verificarDiaOcupado()) {
        continue;
      }
      diasDisp.add("\t" + (diasDisp.size() + 1) + ") " + d.getNombreDiaSemana() + " " + d.getFecha().getDate());
      diaSeleccionCliente.add(d);
    }

    return diasDisp;
  }

  private String formatearDiasDisponibles(ArrayList<String> alS) {
    String result = "Dias disponibles:\n";
    for (String s : alS) {
      result += s + "\n";
    }
    return result;
  }

  private String obtenerDiasFormateados(Agenda a) {
    return formatearDiasDisponibles(obtenerDiasDisponibles(a));
  }

  private Dia obtenerSeleccionDia(Cliente c) throws Exception {
    String dfc = obtenerDiasFormateados(c.getAgenda());
    String diaSeleccion = JOptionPane.showInputDialog(null, dfc, "Seleccione dia", JOptionPane.DEFAULT_OPTION);
    int diaNro = g.validarInt(diaSeleccion);

    if (1 > diaNro || diaNro > diaSeleccionCliente.size()) {
      throw new AsignacionException("Dia no valido");
    }

    return diaSeleccionCliente.get(diaNro - 1);
  }

  private TipoServicio obtenerSeleccionServicio() throws Exception {
    String msg = "Ingrese 1 para " + TipoServicio.INSTALACION + ". 2 para " + TipoServicio.REPARACION;
    String input = JOptionPane.showInputDialog(null, msg, "Seleccione tipo servicio", JOptionPane.DEFAULT_OPTION);
    int sel = g.validarInt(input);

    if (sel == 1) {
      return TipoServicio.INSTALACION;
    } else if (sel == 2) {
      return TipoServicio.REPARACION;
    }

    throw new ValorException("Valor ingresado no valido");
  }

  private void intentarCrearServicio(Cliente c) {
    Servicio nuevoServicio = null;

    try {
      if (callcenterUser.getClass() != Callcenter.class) {
        throw new CredencialException("Credenciales invalidas para Call Center");
      }

      TipoServicio ts = obtenerSeleccionServicio();
      Dia seleccionado = obtenerSeleccionDia(c);
      FraccionTurno turnoDesde = obtenerSeleccionTurno(seleccionado, "inicio");
      FraccionTurno turnoHasta = obtenerSeleccionTurno(seleccionado, "final");

      if (turnoDesde.getNro() > turnoHasta.getNro()) {
        throw new AsignacionException("El final del turno debe ser igual o posterior a su inicio.");
      }
      if (turnoDesde.getTurno() != turnoHasta.getTurno()) {
        throw new AsignacionException("El comienzo y final deben ser del mismo turno.");
      }

      c.getAgenda().verificarDisponibilidad(turnoDesde.getDia().getFecha(), turnoDesde.getTurno(),
          turnoDesde.getNro(), turnoHasta.getNro());
      nuevoServicio = callcenterUser.crearNuevoServicioServicio(DateAux.getToday(), ts, turnoDesde.getTurno(),
          turnoDesde.getNro(), turnoHasta.getNro());

      JOptionPane.showMessageDialog(null, "Servicio creado con Numero " + nuevoServicio.nro);

    } catch (Exception exception) {
      g.errorHandler(exception);

      if (nuevoServicio != null) {
        nuevoServicio.setEstadoServicio(EstadoServicio.CANCELADO);
      }

    }
    refrescarTodo();
  }

  private void anadirTecnicoServicio(Servicio s) {
    try {
      if (callcenterUser.getClass() != Callcenter.class) {
        throw new CredencialException("Credenciales invalidas para Call Center");
      }

      ArrayList<Tecnico> tecs = g.obtenerTecnicos();
      String msg = g.listarTecnicos();

      int eleccion = g.validarInt(
          JOptionPane.showInputDialog(null, msg, "Seleccione tecnico", JOptionPane.DEFAULT_OPTION)) - 1;

      if (0 > eleccion || eleccion >= tecs.size()) {
        throw new AsignacionException("Eleecion no valida");
      }

      Tecnico t = tecs.get(eleccion);

      callcenterUser.asignarServicio(s, t);

      JOptionPane.showMessageDialog(null, "Tecnico " + t.getNombre() + " asignado a servicio nro" + s.nro);

    } catch (Exception exception) {
      g.errorHandler(exception);

    }
    diaSeleccionCliente.clear();
    fraccionesSeleccionCliente.clear();
  }

}
