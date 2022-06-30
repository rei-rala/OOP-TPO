package gui.parciales;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.border.TitledBorder;

import comercial.*;
import empresa.Empresa;
import gui.Gui;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.FlowLayout;

public class PanelListFacturas extends JPanel implements ActionListener {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private JPanel pListadoFacturas;
  private JPanel pFacturaDetalle;

  private Gui g = Gui.getInstance();
  private Empresa e = Empresa.getInstance();
  private ArrayList<Factura> fs = e.getFacturas();

  private Factura currentFactura;

  private JLabel lblNewLabel;
  private JLabel lblNewLabel_5;
  private JLabel lblNewLabel_1;
  private JLabel lblNewLabel_2;
  private JLabel lblNewLabel_3;
  private JLabel lblNewLabel_4;
  private JTextField tfNroFactura;
  private JButton tfClienteFactura;
  private JButton tfServicioAsociado;
  private JTextField tfSubtotal;
  private JTextField tfIVA;
  private JTextField tfTotal;
  private JTextField tfMargenFactura;
  private JLabel lblNewLabel_6;
  private JTextField tfGanancia;

  /**
   * Create the panel.
   */
  public PanelListFacturas() {
    setBorder(new TitledBorder(null, "Facturas realizadas", TitledBorder.CENTER, TitledBorder.TOP, null, null));
    setLayout(new GridLayout(0, 2, 0, 0));

    pListadoFacturas = new JPanel();
    add(pListadoFacturas);
    pListadoFacturas.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    pFacturaDetalle = new JPanel();
    pFacturaDetalle.setBorder(new TitledBorder(
        new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Detalle",
        TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    add(pFacturaDetalle);
    pFacturaDetalle.setLayout(new GridLayout(0, 2, 0, 0));

    pFacturaDetalle.add(new JLabel("Nro"));

    tfNroFactura = new JTextField();
    pFacturaDetalle.add(tfNroFactura);
    tfNroFactura.setColumns(10);

    lblNewLabel = new JLabel("Cliente");
    pFacturaDetalle.add(lblNewLabel);

    tfClienteFactura = new JButton("Ver");
    tfClienteFactura.addActionListener(this);
    pFacturaDetalle.add(tfClienteFactura);

    lblNewLabel_1 = new JLabel("Servicio asociado");
    pFacturaDetalle.add(lblNewLabel_1);

    tfServicioAsociado = new JButton("Ver");
    tfServicioAsociado.addActionListener(this);
    pFacturaDetalle.add(tfServicioAsociado);

    lblNewLabel_2 = new JLabel("Subtotal");
    pFacturaDetalle.add(lblNewLabel_2);

    tfSubtotal = new JTextField();
    pFacturaDetalle.add(tfSubtotal);
    tfSubtotal.setColumns(10);

    lblNewLabel_6 = new JLabel("Ganancia");
    pFacturaDetalle.add(lblNewLabel_6);

    tfGanancia = new JTextField();
    pFacturaDetalle.add(tfGanancia);
    tfGanancia.setColumns(10);

    lblNewLabel_4 = new JLabel("Total");
    pFacturaDetalle.add(lblNewLabel_4);

    tfTotal = new JTextField();
    pFacturaDetalle.add(tfTotal);
    tfTotal.setColumns(10);

    lblNewLabel_3 = new JLabel("IVA");
    pFacturaDetalle.add(lblNewLabel_3);

    tfIVA = new JTextField();
    pFacturaDetalle.add(tfIVA);
    tfIVA.setColumns(10);
    
    lblNewLabel_5 = new JLabel("Margen servicio");
    pFacturaDetalle.add(lblNewLabel_5);

    tfMargenFactura = new JTextField();
    pFacturaDetalle.add(tfMargenFactura);
    tfMargenFactura.setColumns(10);

    for (Component c : pFacturaDetalle.getComponents()) {
      if (c.getClass() == JTextField.class || c.getClass() == JButton.class) {
        c.setEnabled(false);
      }
    }

    poblarFacturas();
  }

  private void poblarFacturas() {
    pListadoFacturas.removeAll();

    for (Factura f : fs) {
      JButton btnFactura = new JButton(g.obtenerTituloFactura(f));
      btnFactura.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          currentFactura = f;
          llenarFormulario();
        }
      });
      pListadoFacturas.add(btnFactura);
    }
  }

  private void llenarFormulario() {
    if (currentFactura == null) {
      tfNroFactura.setText("");
      tfSubtotal.setText("");
      tfIVA.setText("");
      tfTotal.setText("");
      tfGanancia.setText("");
      tfMargenFactura.setText("");

      tfClienteFactura.setEnabled(false);
      tfServicioAsociado.setEnabled(false);
      return;
    }

    tfNroFactura.setText(g.getNro(currentFactura));
    tfSubtotal.setText(g.getSubtotal(currentFactura));
    tfIVA.setText(g.getIVA(currentFactura));
    tfTotal.setText(g.getTotal(currentFactura));
    tfGanancia.setText(g.getGanancia(currentFactura));
    tfMargenFactura.setText(g.getMargen(currentFactura));

    tfClienteFactura.setEnabled(true);
    tfServicioAsociado.setEnabled(true);
  }

  public void mostrarMensaje(String mensaje) throws Exception {
    if (currentFactura == null) {
      g.errorHandler(new Exception("Factura no valida"));
    }
    if (mensaje.isBlank()) {
      g.errorHandler(new Exception("Error desconocido"));
    }

    JOptionPane.showMessageDialog(null, mensaje);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object src = e.getSource();

    try {
      if (src == tfClienteFactura) {
        mostrarMensaje(g.getCliente(currentFactura));
      }

      if (src == tfServicioAsociado) {
        mostrarMensaje(g.getServicioStr(currentFactura));
      }
    } catch (Exception exception) {
      g.errorHandler(exception);
    }
  }

}
