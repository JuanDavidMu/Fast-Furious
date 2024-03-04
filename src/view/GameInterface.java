package view;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import model.PointsManage;
import model.ThreadManage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GameInterface extends JFrame {

    private Properties properties;

    public GameInterface() {
        // Cargar las propiedades desde el archivo
        properties = new Properties();
        try {
            properties.load(new FileInputStream("src/resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Configuración de la ventana principal
        setTitle("Game Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 3, 10, 10)); // Distribución en tres filas y dos columnas con espaciado

        // Crear y agregar paneles para cada jugador con separadores
        for (int i = 1; i <= 5; i++) {
            JPanel playerPanel = createPlayerPanel(i);
            add(playerPanel);
        }

        // Sección para el número de partida y el botón para iniciar la partida
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 1, 10, 10)); // Espaciado entre componentes

        // Número de Partida
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(1, 1));
        JLabel gameLabel = new JLabel("Número de Partida: " + properties.getProperty("numberOfGames"));
        gameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gamePanel.add(gameLabel);
        controlPanel.add(gamePanel);

        // Botón para iniciar la partida (tamaño reducido)
        JButton startButton = new JButton("Iniciar");
        startButton.addActionListener(e -> startGame());
        controlPanel.add(startButton);

        add(controlPanel);

        // Ajustar el tamaño y hacer visible la ventana
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createPlayerPanel(int playerNumber) {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BorderLayout()); // Diseño de borde para organizar las tablas

        // Obtener información del jugador desde las propiedades
        String playerName = properties.getProperty("namePlayer" + playerNumber);
        String playerZone = properties.getProperty("zonePlayer" + playerNumber);

        // Crear tabla para mostrar la información del jugador
        String[] columnNames = {"Propiedad", "Valor"};
        String[][] rowData = {
                {"Nombre", playerName},
                {"Zona Horaria", playerZone},
                {"Número de Lanzamiento", ""},
                {"Puntaje en el Lanzamiento", ""},
                {"Suma de Puntajes", ""},
                {"Puntaje Restante para Meta", ""}
        };

        JTable playerTable = new JTable(rowData, columnNames);
        JScrollPane scrollPane = new JScrollPane(playerTable);
        playerPanel.add(scrollPane, BorderLayout.CENTER);

        return playerPanel;
    }

    private void updatePlayerData(int playerNumber, String hora, String puntos, String puntosSacados, String puntosRestantes) {
        // Obtener la tabla correspondiente al jugador
        JTable playerTable = getPlayerTable(playerNumber);

        if (playerTable != null) {
            // Actualizar los valores en las celdas apropiadas
            playerTable.setValueAt(hora, 2, 1); // Hora
            playerTable.setValueAt(puntos, 3, 1); // Puntos
            playerTable.setValueAt(puntosSacados, 4, 1); // Puntos Sacados
            playerTable.setValueAt(puntosRestantes, 5, 1); // Puntos Restantes

            // Notificar a la tabla para que actualice la vista
            ((AbstractTableModel) playerTable.getModel()).fireTableCellUpdated(2, 1);
            ((AbstractTableModel) playerTable.getModel()).fireTableCellUpdated(3, 1);
            ((AbstractTableModel) playerTable.getModel()).fireTableCellUpdated(4, 1);
            ((AbstractTableModel) playerTable.getModel()).fireTableCellUpdated(5, 1);
        }
    }

    private JTable getPlayerTable(int playerNumber) {
        Component[] components = getContentPane().getComponents();
    
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel playerPanel = (JPanel) component;
                if (playerPanel.getName() != null && playerPanel.getName().equals("PlayerPanel" + playerNumber)) {
                    JScrollPane scrollPane = (JScrollPane) playerPanel.getComponent(0);
                    return (JTable) scrollPane.getViewport().getView();
                }
            }
        }
    
        return null;
    }
    

    private void startGame() {
        // Lógica para iniciar la partida

        // Obtener el número total de juegos desde las propiedades
        int numberOfGames = Integer.parseInt(properties.getProperty("numberOfGames"));

        // Iniciar un hilo para cada jugador
        for (int i = 1; i <= 5; i++) {
            PointsManage pointsManage = new PointsManage();
            JLabel playerInfoLabel = new JLabel("Información del Jugador " + i);
            ThreadManage threadManage = new ThreadManage(i, pointsManage, playerInfoLabel, this);

            // Agregar la etiqueta al panel principal
            JPanel playerPanel = getPlayerPanel(i);
            playerPanel.add(playerInfoLabel, BorderLayout.NORTH);

            // Iniciar el hilo
            threadManage.start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameInterface::new);
    }

    public JPanel getPlayerPanel(int playerNumber) {
        // Buscar y devolver el panel correspondiente al jugador especificado
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JPanel && component.getName() != null && component.getName().equals("PlayerPanel" + playerNumber)) {
                return (JPanel) component;
            }
        }
        return null;  // Devolver null si no se encuentra el panel del jugador
    }

    

}






