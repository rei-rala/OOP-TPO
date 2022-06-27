package gui.parciales;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import comercial.articulos.Articulo;
import empresa.Empresa;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gui.Gui;
import personas.Admin;
import personas.Callcenter;
import personas.Interno;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelEdicionArticulos extends JPanel {
	private Gui gui = Gui.getInstance();
	private Empresa e = Empresa.getInstance();
	private Interno i = gui.getUsuarioLogeado();
	private ArrayList<Articulo> artsEmpresa = e.getArticulos();

	/**
	 * Create the panel.
	 */
	public PanelEdicionArticulos() {
		setLayout(new GridLayout(0, 1, 0, 0));
		JLabel tituloPanel = new JLabel("Articulos de empresa");
		tituloPanel.setHorizontalAlignment(SwingConstants.CENTER);
		tituloPanel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(tituloPanel);

		for (int i = 0; i < artsEmpresa.size(); i++) {
			Articulo a = artsEmpresa.get(i);
			JLabel lblTituloArticulo = new JLabel(getTituloArticulo(a));

			JButton stockArticulo = new JButton("Editar stock");
			stockArticulo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String input = JOptionPane.showInputDialog("Nuevo stock " + getSimpleName(a));
					if (editarStockExitoso(a, input)) {
						lblTituloArticulo.setText(getTituloArticulo(a));
					}
				}
			});

			JButton costoArticulo = new JButton("Editar costo");
			costoArticulo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String input = JOptionPane.showInputDialog("Nuevo costo " + getSimpleName(a));
					if (editarCostoExitoso(a, input)) {
						lblTituloArticulo.setText(getTituloArticulo(a));
					}
				}
			});

			JPanel subpanel = new JPanel();
			subpanel.setLayout(new GridLayout(0, 3, 0, 0));

			subpanel.add(lblTituloArticulo);
			subpanel.add(stockArticulo);
			subpanel.add(costoArticulo);
			add(subpanel);
		}
	}

	private String getSimpleName(Articulo a) {
		return a.getClass().getSimpleName();
	}

	private String getTituloArticulo(Articulo a) {
		return getSimpleName(a) + " Stock: " + (int) a.getStock() + " Costo: " + a.getCosto();
	}

	private void invocarEdicionStock(Articulo a, int nuevoStock) throws Exception {
		if (i.getClass() == Admin.class) {
			Admin adm = (Admin) i;
			adm.setStockArticulo(a, nuevoStock);
		}

		if (i.getClass() == Callcenter.class) {
			Callcenter cc = (Callcenter) i;
			cc.setStockArticulo(a, nuevoStock);
		}
	}

	private void invocarEdicionCosto(Articulo a, double nuevoCosto) throws Exception {
		if (i.getClass() == Admin.class) {
			Admin adm = (Admin) i;
			adm.setCostoArticulo(a, nuevoCosto);
		}

		if (i.getClass() == Callcenter.class) {
			Callcenter cc = (Callcenter) i;
			cc.setCostoArticulo(a, nuevoCosto);
		}
	}

	public boolean editarStockExitoso(Articulo a, String nvoStockIngresado) {
		try {
			if (nvoStockIngresado == null || nvoStockIngresado.trim().isEmpty()) {
				return false;
			}
			int stockValidado = gui.validarInt(nvoStockIngresado);
			invocarEdicionStock(a, stockValidado);
			return true;
		} catch (Exception e) {
			gui.errorHandler(e);
			System.out.println(e);
		}
		return false;
	}

	public boolean editarCostoExitoso(Articulo a, String nvoCostoIngresado) {
		try {
			if (nvoCostoIngresado == null || nvoCostoIngresado.trim().isEmpty()) {
				return false;
			}
			double stockValidado = gui.validarDouble(nvoCostoIngresado);
			invocarEdicionCosto(a, stockValidado);
			return true;
		} catch (Exception e) {
			gui.errorHandler(e);
		}
		return false;
	}

}
