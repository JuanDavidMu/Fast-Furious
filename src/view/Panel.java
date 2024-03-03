package view;

import logic.Juego;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Panel extends JFrame {

    private JuegoView juegoView;

    public Panel() {
        setTitle("Rápidos y Furiosos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon("src/resources/logo.jpeg");
        Image scaledImage = imageIcon.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledImageIcon);
        mainPanel.add(imageLabel, BorderLayout.NORTH);

        JButton btnStartGame = new JButton("Iniciar Juego");
        configureButtonStyle(btnStartGame);

        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJuego();
            }
        });

        JButton btnConfigGame = new JButton("Configurar Juego");
        configureButtonStyle(btnConfigGame);

        btnConfigGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configurarJuego();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2)); // Añadir botones en una fila
        buttonPanel.add(btnStartGame);
        buttonPanel.add(btnConfigGame);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setBackground(new Color(1, 1, 8));

        add(mainPanel);

        setVisible(true);
        setResizable(false);
    }

    private void configureButtonStyle(JButton button) {
        button.setBackground(new Color(50, 205, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        int borderRadius = 30;
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 255, 255), 3),
                new LineBorder(new Color(50, 205, 50), 3)
        ));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void iniciarJuego() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
            int numeroDePartidas = Integer.parseInt(properties.getProperty("numberOfGames"));
            int puntosACompletar = Integer.parseInt(properties.getProperty("pointsToWin"));
            String[] nombresJugadores = new String[5];
            for (int i = 1; i <= 5; i++) {
                nombresJugadores[i-1] = properties.getProperty("namePlayer" + i);
            }

            juegoView = new JuegoView(nombresJugadores.length); // Pasar el número de jugadores como parámetro
            Juego juego = new Juego(numeroDePartidas, puntosACompletar, nombresJugadores, juegoView);
            juego.iniciarPartidas();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar el archivo de configuración", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarJuego() {
        ConfiguracionJuegoDialog dialog = new ConfiguracionJuegoDialog(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Panel::new);
    }
}





