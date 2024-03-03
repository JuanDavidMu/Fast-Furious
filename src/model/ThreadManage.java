package model;

import view.GameGUI;

import javax.swing.*;
import java.util.Random;

public class ThreadManage extends Thread {

    private PointsManage pointsManage;
    private JLabel playerInfoLabel;
    private GameGUI gameGUI;

    public ThreadManage(PointsManage pointsManage, JLabel playerInfoLabel, GameGUI gameGUI) {
        this.pointsManage = pointsManage;
        this.playerInfoLabel = playerInfoLabel;
        this.gameGUI = gameGUI;
    }

    @Override
    public void run() {
        int maxPoints = gameGUI.getMaxPoints();

        do {
            int point = throwDices();
            pointsManage.sumPoints(point);

            // Actualizar la información en la interfaz gráfica
            SwingUtilities.invokeLater(() -> {
                gameGUI.updatePlayerInfo(playerInfoLabel, Thread.currentThread().getName(), point, pointsManage.getPoints(), maxPoints);
                gameGUI.updateGameInfo(1); // Actualizar información de la partida
            });

            try {
                sleep(timeToThrow());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (pointsManage.getPoints() < maxPoints);

        // Informar a GameGUI que el jugador ha terminado la partida
        SwingUtilities.invokeLater(gameGUI::playerFinishedGame);

        // Finalizar el juego
        gameGUI.endGame();
    }

    public Integer timeToThrow() {
        Random rd = new Random();
        return rd.nextInt(5) * 1000;
    }

    public Integer throwDices() {
        Random rd = new Random();
        return rd.nextInt(6) + 1;
    }
}

