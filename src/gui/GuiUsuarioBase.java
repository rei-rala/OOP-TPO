package gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public abstract class GuiUsuarioBase extends JPanel implements ActionListener, GuiMethods {

	private JLabel lblTitulo;
	private JButton btnLogout;

	public GuiUsuarioBase(String titulo) {
		setBorder(null);
		setLayout(new BorderLayout(0, 0));

		lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitulo, BorderLayout.NORTH);

		btnLogout = new JButton("LOGOUT");
		btnLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GuiMain.getInstance().logUserOut();
			}
		});
		add(btnLogout, BorderLayout.SOUTH);
	}

	protected void reemplazarContenido(JPanel jpContenido) {
		setVisible(false);
		removeAll();
		add(jpContenido);
		setVisible(true);
	}

	@Override
	abstract public void actionPerformed(ActionEvent e);

}
