package view;

import javax.swing.JFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AboutView extends JDialog {

    public AboutView(Frame parent) {
        super(parent, "About", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new GridLayout(2, 2));
        aboutPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        aboutPanel.setBackground(new Color(1, 1, 18)); // Fondo oscuro

        JLabel labelNombre = new JLabel("Nombre:");
        JLabel labelCodigo = new JLabel("Código:");

        JLabel labelNombreValor = new JLabel("Juan David Muñoz Eslava | Daniel Parra");
        JLabel labelCodigoValor = new JLabel("202213021 | 202210719");

        // Estilos para los labels
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        labelNombre.setFont(labelFont);
        labelCodigo.setFont(labelFont);
        labelNombreValor.setFont(labelFont);
        labelCodigoValor.setFont(labelFont);

        // Configuración de colores para los labels
        Color labelTextColor = new Color(255, 255, 255); // Texto blanco
        labelNombre.setForeground(labelTextColor);
        labelCodigo.setForeground(labelTextColor);
        labelNombreValor.setForeground(labelTextColor);
        labelCodigoValor.setForeground(labelTextColor);

        aboutPanel.add(labelNombre);
        aboutPanel.add(labelNombreValor);
        aboutPanel.add(labelCodigo);
        aboutPanel.add(labelCodigoValor);

        add(aboutPanel);
        setResizable(false);
        setVisible(true);
    }
}