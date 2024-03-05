package model;

import view.GameInterface;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class ThreadManage extends Thread {

    private PointsManage pointsManage;
    private JLabel playerInfoLabel;
    private GameInterface gameGUI;
    private int playerNumber;
    private String timeZone; // Nuevo campo para almacenar el número de jugador
    private static int finishedThreads = 0;
    private int position;
    private static List<PlayerResult> playerResults = new ArrayList<>();
    private GameInterface gameInterface;


    public ThreadManage(int playerNumber, PointsManage pointsManage, JLabel playerInfoLabel, GameInterface gameGUI,String timeZone) {
        this.playerNumber = playerNumber;
        this.pointsManage = pointsManage;
        this.timeZone = timeZone;
        this.playerInfoLabel = playerInfoLabel;
        this.gameGUI = gameGUI;
    }

    @Override
    public void run() {
        Properties pp = new Properties(new Properties());
        try {
            pp.load(new FileInputStream("src/resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int maxPoints = Integer.parseInt(pp.getProperty("pointsToWin")); // Obtener el valor de pointsToWin

        do {
            int point = throwDices();
            pointsManage.sumPoints(point);

            try {
                sleep(timeToThrow());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Actualizar la tabla en la interfaz gráfica
            updateTable(playerNumber, pointsManage.getThrowsCount(), point,
                    pointsManage.getSumPoints(), maxPoints - pointsManage.getPoints());
        } while (pointsManage.getPoints() < maxPoints);


        synchronized (ThreadManage.class) {

            finishedThreads++;
            position = finishedThreads;
            if(position > 5){
                position = 1;
                finishedThreads = 1;
            }
            playerResults.add(new PlayerResult(playerNumber, pointsManage.getPoints(), position));

            JOptionPane.showMessageDialog(null,"El jugador " + playerNumber + " ha terminado en la posición " + position);
            updatePlayerResults();
        }
        
        
    }
    private void updatePlayerResults() {
        // Busca si ya existe el resultado del jugador en la lista
        PlayerResult playerResult = findPlayerResult(playerNumber);

        // Si no existe, crea un nuevo resultado y agrégalo a la lista
        if (playerResult == null) {
            playerResult = new PlayerResult(playerNumber, pointsManage.getPoints(), 0);
            playerResults.add(playerResult);
        } else {
            // Si ya existe, actualiza los puntos existentes
            playerResult.addPoints(pointsManage.getPoints());
        }

        // Actualiza la interfaz de usuario con la clasificación parcial
        SwingUtilities.invokeLater(() -> gameInterface.showPartialRanking());
    }

    private PlayerResult findPlayerResult(int playerNumber) {
        for (PlayerResult result : playerResults) {
            if (result.getPlayerNumber() == playerNumber) {
                return result;
            }
        }
        return null;
    }

    private void updateTable(int playerNumber, int throwsCount, int pointsInThrow, int sumPoints, int remainingPoints) {
        SwingUtilities.invokeLater(() -> {
            JPanel playerPanel = gameGUI.getPlayerPanel(playerNumber);
            JTable table = (JTable) ((JScrollPane) playerPanel.getComponent(0)).getViewport().getView();
            table.getModel().setValueAt(throwsCount, 2, 1);
            table.getModel().setValueAt(pointsInThrow, 3, 1);
            table.getModel().setValueAt(sumPoints, 4, 1);
            table.getModel().setValueAt(remainingPoints, 5, 1);

            // Obtener la zona horaria actual del jugador
            String timeZoneValue = getTimeZoneValue();

            // Obtener el índice de la fila correspondiente a "Zona Horaria"
            int timeZoneRow = getTimeZoneRow(table);

            // Asegurarse de que el valor sea un String antes de establecerlo en la tabla
            table.getModel().setValueAt(timeZoneValue, timeZoneRow, 1);
            table.repaint();
        });
    }

    // Método para obtener el índice de la fila correspondiente a "Zona Horaria"
    private int getTimeZoneRow(JTable table) {
        int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            if (table.getValueAt(i, 0).equals("Zona Horaria")) {
                return i;
            }
        }
        return -1; // Si no se encuentra la fila
    }

    private String getTimeZoneValue() {
        // Devolver la zona horaria actual
        return timeZone;
    }




    public Integer timeToThrow() {
        Random rd = new Random();
        return rd.nextInt(5) * 1000;
    }

    public Integer throwDices() {
        Random rd = new Random();
        return rd.nextInt(12) + 1;
    }

    public static List<PlayerResult> getPlayerResults() {
        return playerResults;
    }
}

