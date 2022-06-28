package gui.parciales;

import javax.swing.JPanel;

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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.TitledBorder;

public class PanelEdicionArticulos extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Gui g = Gui.getInstance();
	private Empresa e = Empresa.getInstance();
	private Interno interno = g.getUsuarioLogeado();
	private ArrayList<Articulo> artsEmpresa = e.getArticulos();

	/**
	 * Create the panel.
	 */
	public PanelEdicionArticulos() {
		setBorder(new TitledBorder(null, "Articulos de la empresa", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		setLayout(new GridLayout(0, 1, 0, 0));

		for (int i = 0; i < artsEmpresa.size(); i++) {
			Articulo a = artsEmpresa.get(i);
			JLabel lblTituloArticulo = new JLabel(g.getTituloArticulo(a));

			JButton stockArticulo = new JButton("Editar stock");
			stockArticulo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String input = JOptionPane.showInputDialog("Nuevo stock " + g.getSimpleName(a));
					if (editarStockExitoso(a, input)) {
						lblTituloArticulo.setText(g.getTituloArticulo(a));
					}
				}
			});

			JButton costoArticulo = new JButton("Editar costo");
			if (interno.getClass() == Admin.class) {
				costoArticulo.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String input = JOptionPane.showInputDialog("Nuevo costo " + g.getSimpleName(a));
						if (editarCostoExitoso(a, input)) {
							lblTituloArticulo.setText(g.getTituloArticulo(a));
						}
					}
				});
			} else {
				costoArticulo.setEnabled(false);
			}

			JPanel subpanel = new JPanel();
			subpanel.setLayout(new GridLayout(0, 3, 0, 0));

			subpanel.add(lblTituloArticulo);
			subpanel.add(stockArticulo);
			subpanel.add(costoArticulo);
			add(subpanel);
		}
	}

	private void invocarEdicionStock(Articulo a, int nuevoStock) throws Exception {
		if (interno.getClass() == Admin.class) {
			Admin adm = (Admin) interno;
			adm.setStockArticulo(a, nuevoStock);
		}

		if (interno.getClass() == Callcenter.class) {
			Callcenter cc = (Callcenter) interno;
			cc.setStockArticulo(a, nuevoStock);
		}
	}

	private void invocarEdicionCosto(Articulo a, double nuevoCosto) throws Exception {
		if (interno.getClass() == Admin.class) {
			Admin adm = (Admin) interno;
			adm.setCostoArticulo(a, nuevoCosto);
		}

		if (interno.getClass() == Callcenter.class) {
			Callcenter cc = (Callcenter) interno;
			cc.setCostoArticulo(a, nuevoCosto);
		}
	}

	public boolean editarStockExitoso(Articulo a, String nvoStockIngresado) {
		try {
			if (nvoStockIngresado == null || nvoStockIngresado.trim().isEmpty()) {
				return false;
			}
			int stockValidado = g.validarInt(nvoStockIngresado);
			invocarEdicionStock(a, stockValidado);
			return true;
		} catch (Exception e) {
			g.errorHandler(e);
			System.out.println(e);
		}
		return false;
	}

	public boolean editarCostoExitoso(Articulo a, String nvoCostoIngresado) {
		try {
			if (nvoCostoIngresado == null || nvoCostoIngresado.trim().isEmpty()) {
				return false;
			}
			double stockValidado = g.validarDouble(nvoCostoIngresado);
			invocarEdicionCosto(a, stockValidado);
			return true;
		} catch (Exception e) {
			g.errorHandler(e);
		}
		return false;
	}

}
