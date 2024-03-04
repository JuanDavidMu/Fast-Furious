package view;


import model.PointsManage;
import model.ThreadManage;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
            // Obtener información del jugador desde las propiedades
            String playerName = properties.getProperty("namePlayer" + i);
            String playerZone = properties.getProperty("zonePlayer" + i);

            JPanel playerPanel = createPlayerPanel(i, playerName, playerZone);
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


    private JPanel createPlayerPanel(int playerNumber, String playerName, String playerZone) {
        JPanel playerPanel = new JPanel();
        playerPanel.setName("PlayerPanel" + playerNumber); // Establecer el nombre del panel
        playerPanel.setLayout(new BorderLayout()); // Diseño de borde para organizar las tablas

        // Crear tabla para mostrar la información del jugador
        String[] columnNames = {"Propiedad", "Valor"};
        Object[][] rowData = {
                {"Nombre", playerName},
                {"Zona Horaria", ""},
                {"Número de Lanzamiento", ""},
                {"Puntaje en el Lanzamiento", ""},
                {"Suma de Puntajes", ""},
                {"Puntaje Restante para Meta", ""}
        };

        JTable playerTable = new JTable(rowData, columnNames);
        JScrollPane scrollPane = new JScrollPane(playerTable);
        playerPanel.add(scrollPane, BorderLayout.CENTER);
        Thread updateTimeThread = new Thread(() -> {
            while (true) {
                // Obtener la hora actual en la zona horaria del jugador
                String currentTime = getCurrentTime(playerZone);

                // Actualizar la tabla con la hora actual
                SwingUtilities.invokeLater(() -> playerTable.setValueAt(currentTime, 1, 1));

                // Esperar un segundo antes de actualizar nuevamente
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updateTimeThread.start();

        return playerPanel;
    }

    private String getCurrentTime(String zoneId) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of(zoneId));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return currentTime.format(formatter);
    }



    public void updatePlayerTimeZone(int playerNumber, String hora) {
        // Obtener la tabla correspondiente al jugador
        JTable playerTable = getPlayerTable(playerNumber);

        if (playerTable != null) {
            // Actualizar el valor en la celda de la zona horaria
            playerTable.setValueAt(hora, 1, 1); // Cambiado a 1 para la fila de Zona Horaria

            // Notificar a la tabla para que actualice la vista
            ((AbstractTableModel) playerTable.getModel()).fireTableCellUpdated(1, 1); // Cambiado a 1
        }
    }



    public void updatePlayerData(int playerNumber, String hora, int numeroLanzamiento, int puntajeLanzamiento, int sumaPuntajes, int puntosRestantes) {
        // Obtener la tabla correspondiente al jugador
        JTable playerTable = getPlayerTable(playerNumber);

        if (playerTable != null) {
            // Actualizar los valores en las celdas apropiadas
            playerTable.setValueAt(hora, 1, 1); // Hora
            playerTable.setValueAt(String.valueOf(numeroLanzamiento), 2, 1); // Número de lanzamiento
            playerTable.setValueAt(String.valueOf(puntajeLanzamiento), 3, 1); // Puntaje de lanzamiento
            playerTable.setValueAt(String.valueOf(sumaPuntajes), 4, 1); // Suma de puntajes
            playerTable.setValueAt(String.valueOf(puntosRestantes), 5, 1); // Puntos restantes para meta

            // Notificar a la tabla para que actualice la vista
            ((AbstractTableModel) playerTable.getModel()).fireTableCellUpdated(1, 1);
            ((AbstractTableModel) playerTable.getModel()).fireTableCellUpdated(2, 1);
            ((AbstractTableModel) playerTable.getModel()).fireTableCellUpdated(3, 1);
            ((AbstractTableModel) playerTable.getModel()).fireTableCellUpdated(4, 1);
            ((AbstractTableModel) playerTable.getModel()).fireTableCellUpdated(5, 1);
        }
    }



    public JTable getPlayerTable(int playerNumber) {
        Component[] components = getContentPane().getComponents();

        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel playerPanel = (JPanel) component;
                Component[] subComponents = playerPanel.getComponents(); // Obtener los componentes del panel del jugador
                for (Component subComponent : subComponents) {
                    if (subComponent instanceof JScrollPane) {
                        JScrollPane scrollPane = (JScrollPane) subComponent;
                        // Obtener la vista del panel de desplazamiento (que debería ser la tabla)
                        Component view = scrollPane.getViewport().getView();
                        if (view instanceof JTable && playerPanel.getName() != null && playerPanel.getName().equals("PlayerPanel" + playerNumber)) {
                            return (JTable) view;
                        }
                    }
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
            // Obtener el valor de la zona horaria desde las propiedades
            String timeZone = properties.getProperty("zonePlayer" + i);
            // Pasar el valor de la zona horaria al constructor de ThreadManage
            ThreadManage threadManage = new ThreadManage(i, pointsManage, playerInfoLabel, this, timeZone);

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







