package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigProperty extends JFrame {

    private Properties properties;

    public ConfigProperty(Panel panel) {
        setTitle("Configuración de Partida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar la ventana en toda la pantalla
        setResizable(false); // Hacer la ventana no resizable

        // Cargar las propiedades desde el archivo
        properties = new Properties();
        try {
            properties.load(new FileInputStream("src/resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crear panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 2, 10, 10)); // GridLayout de 2 columnas
        mainPanel.setBackground(new Color(1, 1, 18)); // Fondo oscuro

        // Agregar componentes para los puntos a ganar
        JLabel lblPointsToWin = new JLabel("Puntos para ganar:");
        JTextField txtPointsToWin = new JTextField(properties.getProperty("pointsToWin"));
        configureComponent(lblPointsToWin);
        configureComponent(txtPointsToWin);
        mainPanel.add(lblPointsToWin);
        mainPanel.add(txtPointsToWin);

        // Agregar componentes para el número de juegos
        JLabel lblNumberOfGames = new JLabel("Número de juegos:");
        JTextField txtNumberOfGames = new JTextField(properties.getProperty("numberOfGames"));
        configureComponent(lblNumberOfGames);
        configureComponent(txtNumberOfGames);
        mainPanel.add(lblNumberOfGames);
        mainPanel.add(txtNumberOfGames);

        // Agregar componentes para los jugadores
        for (int i = 1; i <= 5; i++) {
            JLabel lblNamePlayer = new JLabel("Nombre del Jugador " + i + ":");
            JTextField txtNamePlayer = new JTextField(properties.getProperty("namePlayer" + i));
            configureComponent(lblNamePlayer);
            configureComponent(txtNamePlayer);
            mainPanel.add(lblNamePlayer);
            mainPanel.add(txtNamePlayer);

            JLabel lblZonePlayer = new JLabel("Zona horaria del Jugador " + i + ":");
            JComboBox<String> cbZonePlayer = new JComboBox<>(new String[] { "Asia/Tokyo", "Australia/Sydney",
                    "Europe/Berlin", "America/Denver", "America/Chicago" });
            cbZonePlayer.setSelectedItem(properties.getProperty("zonePlayer" + i));
            configureComponent(lblZonePlayer);
            configureComponent(cbZonePlayer);
            mainPanel.add(lblZonePlayer);
            mainPanel.add(cbZonePlayer);
        }

        // Agregar botón para guardar cambios
        JButton btnSaveChanges = new JButton("Guardar Cambios");
        configureButton(btnSaveChanges);
        btnSaveChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Guardar los cambios en el archivo de propiedades
                properties.setProperty("pointsToWin", txtPointsToWin.getText());
                properties.setProperty("numberOfGames", txtNumberOfGames.getText());

                for (int i = 1; i <= 5; i++) {
                    // Obtener el nombre del jugador
                    String playerName = ((JTextField) mainPanel.getComponent((i) * 4 + 1)).getText();
                    properties.setProperty("namePlayer" + i, playerName);

                    // Obtener la zona horaria del jugador
                    Component component = mainPanel.getComponent((i) * 4 + 3);
                    if (component instanceof JComboBox<?>) {
                        JComboBox<?> zoneComboBox = (JComboBox<?>) component;
                        String playerZone = zoneComboBox.getSelectedItem().toString();
                        properties.setProperty("zonePlayer" + i, playerZone);
                    }
                }

                try {
                    // Guardar las propiedades en un archivo plano
                    properties.store(new FileOutputStream("src/resources/config.properties"), null);
                    JOptionPane.showMessageDialog(ConfigProperty.this, "Cambios guardados correctamente", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    panel.setVisible(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ConfigProperty.this, "Error al guardar los cambios", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mainPanel.add(btnSaveChanges);


        add(mainPanel);

        setLocationRelativeTo(null);

        
        setVisible(true);
    }

    private void configureComponent(JComponent component) {
        component.setForeground(new Color(255, 255, 255)); // Texto blanco
        component.setFont(new Font("Arial", Font.PLAIN, 14));
        component.setBackground(new Color(1, 1, 18)); // Fondo oscuro
    }

    private void configureButton(JButton button) {
        button.setBackground(new Color(50, 205, 50)); // Verde
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 20, 10, 20),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 3),
                        BorderFactory.createLineBorder(new Color(50, 205, 50), 3))));
    }

}
