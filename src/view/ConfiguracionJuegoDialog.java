package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracionJuegoDialog extends JDialog {

    private JTextField txtNumeroDePartidas;
    private JTextField txtPuntosACompletar;
    private JTextField[] txtNombresJugadores;

    public ConfiguracionJuegoDialog(JFrame parent) {
        super(parent, "Configuración del Juego", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel requisitosPanel = new JPanel(new GridLayout(0, 1));
        JPanel fieldsPanel = new JPanel(new GridLayout(0, 1));

        // Requisitos del juego
        String[] requisitos = {"Número de partidas:", "Puntos a completar:", "Nombre jugador 1:",
                "Nombre jugador 2:", "Nombre jugador 3:", "Nombre jugador 4:", "Nombre jugador 5:"};

        for (String req : requisitos) {
            requisitosPanel.add(new JLabel(req));
        }

        // Campos de texto
        txtNumeroDePartidas = new JTextField();
        txtPuntosACompletar = new JTextField();
        txtNombresJugadores = new JTextField[5];
        for (int i = 0; i < 5; i++) {
            txtNombresJugadores[i] = new JTextField();
        }

        fieldsPanel.add(txtNumeroDePartidas);
        fieldsPanel.add(txtPuntosACompletar);
        for (JTextField txt : txtNombresJugadores) {
            fieldsPanel.add(txt);
        }

        panel.add(requisitosPanel, BorderLayout.WEST);
        panel.add(fieldsPanel, BorderLayout.CENTER);

        JButton btnSave = new JButton("Guardar");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarConfiguracion();
            }
        });

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void guardarConfiguracion() {
        Properties properties = new Properties();
        properties.setProperty("numberOfGames", txtNumeroDePartidas.getText());
        properties.setProperty("pointsToWin", txtPuntosACompletar.getText());
        for (int i = 0; i < 5; i++) {
            properties.setProperty("namePlayer" + (i + 1), txtNombresJugadores[i].getText());
        }

        try {
            properties.store(new FileOutputStream("config.properties"), null);
            JOptionPane.showMessageDialog(this, "Configuración guardada correctamente");
            dispose();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la configuración", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


