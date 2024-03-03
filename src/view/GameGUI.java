package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.PointsManage;
import model.ThreadManage;

public class GameGUI extends JFrame {

    private int numPlayers;
    private int maxPoints;
    private int numGames;
    private ExecutorService executorService;
    private JLabel[] lblGameInfo;
    private JLabel[] lblPlayerInfo;
    private PointsManage[] players;
    private ThreadManage[] playerThreads;
    private JPanel playerInfoPanel;

    public GameGUI(int numPlayers, int maxPoints, int numGames) {
        this.numPlayers = numPlayers;
        this.maxPoints = maxPoints;
        this.numGames = numGames;
        lblPlayerInfo = new JLabel[numPlayers];

        setTitle("Juego de Puntos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Configurar paneles
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new GridLayout(1, 0));
        mainPanel.add(gameInfoPanel, BorderLayout.NORTH);

        playerInfoPanel = new JPanel(); // Usar el campo de clase playerInfoPanel en lugar de declararlo nuevamente
        playerInfoPanel.setLayout(new GridLayout(numPlayers + 1, 1)); // +1 para el título "Jugadores"
        mainPanel.add(playerInfoPanel, BorderLayout.CENTER);

        // Llama a loadConfigurationFromFile() primero
        initializePlayerInfoPanel(); // Luego llama a initializePlayerInfoPanel()

        // Configurar etiquetas de información del juego
        lblGameInfo = new JLabel[3];
        lblGameInfo[0] = new JLabel("Número de partida (0/" + numGames + "): ");
        lblGameInfo[1] = new JLabel("Hora: ");
        lblGameInfo[2] = new JLabel("Puntuación restante para llegar a la meta: " + maxPoints);
        for (JLabel lbl : lblGameInfo) {
            gameInfoPanel.add(lbl);
        }

        // Título para los jugadores
        JLabel lblPlayersTitle = new JLabel("Jugadores");
        lblPlayersTitle.setHorizontalAlignment(SwingConstants.CENTER);
        playerInfoPanel.add(lblPlayersTitle);

        // Configurar etiquetas de información de los jugadores
        players = new PointsManage[numPlayers];
        playerThreads = new ThreadManage[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new PointsManage();
            lblPlayerInfo[i] = new JLabel("Jugador " + (i + 1) + ": Puntos: 0");
            playerInfoPanel.add(lblPlayerInfo[i]);
            playerThreads[i] = new ThreadManage(players[i], lblPlayerInfo[i], this);
        }
        loadConfigurationFromFile();

        // Botón para iniciar el juego
        JButton btnStartGame = new JButton("Iniciar Juego");
        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        mainPanel.add(btnStartGame, BorderLayout.SOUTH);

        // Añadir mainPanel a la ventana
        add(mainPanel);

        setVisible(true);
        setResizable(false);
    }

    private void initializePlayerInfoPanel() {
        playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new GridLayout(numPlayers + 1, 1));
    }

    // Método para iniciar el juego
    private void startGame() {
        // Iniciar ejecutor de hilos
        executorService = Executors.newFixedThreadPool(numPlayers);
        // Iniciar los hilos de los jugadores
        for (ThreadManage playerThread : playerThreads) {
            executorService.execute(playerThread);
        }
        // Actualizar información de la partida
        updateGameInfo(1); // Empezamos en la partida 1
    }

    private void loadConfigurationFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/config.properties"))) {
            String line;
            String[] playerNames = new String[numPlayers];
            String[] playerZones = new String[numPlayers];

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "pointsToWin":
                            maxPoints = Integer.parseInt(value);
                            break;
                        case "namePlayer1":
                        case "namePlayer2":
                        case "namePlayer3":
                        case "namePlayer4":
                        case "namePlayer5":
                            int playerIndex = Integer.parseInt(key.substring(key.length() - 1)) - 1;
                            playerNames[playerIndex] = value;
                            break;
                        case "zonePlayer1":
                        case "zonePlayer2":
                        case "zonePlayer3":
                        case "zonePlayer4":
                        case "zonePlayer5":
                            int zoneIndex = Integer.parseInt(key.substring(key.length() - 1)) - 1;
                            playerZones[zoneIndex] = value;
                            break;
                        case "numberOfGames":
                            numGames = Integer.parseInt(value);
                            break;
                        default:
                            break;
                    }
                }
            }

            // Actualizar las etiquetas de información de los jugadores con la nueva información
            for (int i = 0; i < numPlayers; i++) {
                lblPlayerInfo[i].setText(playerNames[i] + " (" + playerZones[i] + "): Puntos: 0");
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    public void updatePlayerInfo(JLabel playerInfoLabel, String playerName, int rollResult, int totalPoints, int maxPoints) {
        String playerInfo = playerName + ": Lanzamiento: " + rollResult + " - Puntos: " + totalPoints + " - Puntos restantes: " + (maxPoints - totalPoints);
        playerInfoLabel.setText(playerInfo);
    }

    // Método para actualizar la información de la partida
    public void updateGameInfo(int gameNumber) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        // Actualizar número de partida y hora
        lblGameInfo[0].setText("Número de partida (" + gameNumber + "/" + numGames + "): ");
        lblGameInfo[1].setText("Hora: " + sdf.format(new Date()));
    }

    // Método para finalizar el juego
    public void endGame() {
        // Detener ejecución de hilos
        executorService.shutdown();

        // Calcular la clasificación
        StringBuilder classification = new StringBuilder("Clasificación de los jugadores:\n");
        for (int i = 0; i < numPlayers; i++) {
            classification.append("Jugador ").append(i + 1).append(": Puntos totales = ").append(players[i].getPoints()).append("\n");
        }

        // Mostrar la clasificación
        JOptionPane.showMessageDialog(this, "Juego finalizado. ¡Gracias por jugar!\n\n" + classification.toString());
    }

    // Obtener la puntuación máxima
    public int getMaxPoints() {
        return maxPoints;
    }

    // Método para manejar la finalización de una partida por un jugador
    public void playerFinishedGame() {
        // Esperar a que todos los jugadores terminen antes de iniciar la siguiente partida
        for (ThreadManage playerThread : playerThreads) {
            try {
                playerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Obtener el número de partida actual
        String gameInfoText = lblGameInfo[0].getText();
        int currentGame = Integer.parseInt(gameInfoText.substring(gameInfoText.indexOf("(") + 1, gameInfoText.indexOf("/")).trim());
        // Incrementar el número de partida
        updateGameInfo(currentGame + 1);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Ajustar estos valores según tus necesidades
            int numPlayers = 4;
            int maxPoints = 100;
            int numGames = 5;
            new GameGUI(numPlayers, maxPoints, numGames);
        });
    }
}






