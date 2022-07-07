package ui.usuarios;

import javax.swing.JPanel;

import personas.Admin;
import personas.Seniority;
import ui.UiUsuariosBase;
import ui.UiApplicationWindow;
import ui.UiMethods;

import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JTextField;

import comercial.articulos.Articulo;
import excepciones.GuiException;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class UiAdmin extends UiUsuariosBase {
	private UiApplicationWindow ui = UiApplicationWindow.getInstance();
	private Admin admin;

	private JPanel pPrincipal;
	private JPanel pLayout;
	private JPanel subPLayout;

	private JButton btnCostoCombustible;
	private JButton btnCostoViaje;
	private JButton btnStockCostoArticulos;
	private JButton btnValorHoraTecnicos;

	/**
	 * Create the panel.
	 */
	public UiAdmin(Admin admin) {
		super("Menu admin");
		this.admin = admin;

		pPrincipal = new JPanel();
		add(pPrincipal, BorderLayout.CENTER);
		pPrincipal.setLayout(new GridLayout(0, 3, 0, 0));

		// Estetico
		pPrincipal.add(new JLabel(""));

		pLayout = new JPanel();
		pPrincipal.add(pLayout);
		pLayout.setLayout(new GridLayout(3, 0, 0, 0));

		// Estetico
		pLayout.add(new JLabel(""));

		subPLayout = new JPanel();
		pLayout.add(subPLayout);
		subPLayout.setLayout(new GridLayout(0, 1, 0, 0));

		btnCostoCombustible = new JButton("Editar costo combustible");
		btnCostoCombustible.addActionListener(this);
		subPLayout.add(btnCostoCombustible);

		btnCostoViaje = new JButton("Editar costo viaje");
		btnCostoViaje.addActionListener(this);
		subPLayout.add(btnCostoViaje);

		btnStockCostoArticulos = new JButton("Editar stock y costo Articulos");
		btnStockCostoArticulos.addActionListener(this);
		subPLayout.add(btnStockCostoArticulos);

		btnValorHoraTecnicos = new JButton("Editar valor hora tecnicos");
		btnValorHoraTecnicos.addActionListener(this);
		subPLayout.add(btnValorHoraTecnicos);
	}

	public void invocarEdicionCombustible() throws Exception {
		double nuevoCC = guiValidarDouble(
				"Ingrese nuevo costo para combustible <ACTUAL: " + admin.getCostoCombustible() + ">");

		admin.modificarCostoCombustible(nuevoCC);
		alert("Costo combustible cambiado a " + admin.getCostoCombustible());
	}

	public void invocarEdicionViaje() throws Exception {
		double nuevoCV = guiValidarDouble("Ingrese nuevo costo para viaje <ACTUAL: " + admin.getCostoViaje() + ">");

		admin.modificarCostoCombustible(nuevoCV);
		alert("Costo viaje cambiado a " + admin.getCostoViaje());
	}

	public void invocarEdicionCHT() throws Exception {
		String opciones = "\n\t1) Junior\n\t2) Semi-Senior\n\t3) Senior";
		int seleccion = guiValidarInt("Seleccione seniority:" + opciones, 1, 3);

		Seniority seleccionSeniority = null;

		switch (seleccion) {
		case 1:
			seleccionSeniority = Seniority.JUNIOR;
			break;
		case 2:
			seleccionSeniority = Seniority.SEMI_SENIOR;
			break;
		case 3:
			seleccionSeniority = Seniority.SENIOR;
			break;
		}

		if (seleccionSeniority == null) {
			throw new GuiException("Seleccion no valida");
		}

		double currentCHT = admin.getCostoHoraTecnico(seleccionSeniority);
		double nuevoCosto = guiValidarDouble(
				"Ingrese nuevo costo por hora para " + seleccionSeniority + "<ACTUAL " + currentCHT + ">");

		admin.setCostoHoraTecnico(seleccionSeniority, nuevoCosto);
		alert("CHT para " + seleccionSeniority + " ahora es " + admin.getCostoHoraTecnico(seleccionSeniority));
	}

	private void invocarEdicionArticulosAdmin() throws Exception {
		ArrayList<Articulo> arts = admin.getArticulos();
		String opciones = "Seleccione un articulo por su SKU:";

		for (Articulo a : arts) {
			opciones += "\n\tSKU: " + a.getSKU() + " | " + a.getDescripcion() + " [COSTO: " + a.getCosto() + ", STOCK"
					+ a.getStock() + "]";
		}

		int eleccion = guiValidarInt(opciones);

		Articulo seleccionArticulo = admin.getArticulos(eleccion);

		if (seleccionArticulo == null) {
			throw new GuiException("Seleccion no valida");
		}

		String descripcion = seleccionArticulo.getDescripcion();
		int nvoStock = guiValidarInt("Stock de" + descripcion + "<ACTUAL " + seleccionArticulo.getStock() + ">");
		double nvoCosto = guiValidarDouble("Coste de " + descripcion + "<ACTUAL " + seleccionArticulo.getCosto() + ">");
		boolean confirma = confirm("Acepta valores de " + descripcion + "? (S:" + nvoStock + "|C:" + nvoCosto + ")");

		if (confirma) {
			admin.editarCifrasArticulo(seleccionArticulo, nvoStock, nvoCosto);
			alert("Nuevos valores fijados: " + admin.getArticulos(eleccion));
			return;
		}

		alert("Cancelado por usuario");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		try {
			if (src == btnCostoCombustible) {
				invocarEdicionCombustible();
			} else if (src == btnCostoViaje) {
				invocarEdicionViaje();
			} else if (src == btnValorHoraTecnicos) {
				invocarEdicionCHT();
			} else if (src == btnStockCostoArticulos) {
				invocarEdicionArticulosAdmin();
			}
		} catch (Exception exception) {
			uiExceptionHandler(exception);
		}

	}

}
