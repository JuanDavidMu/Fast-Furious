package view;

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

        JButton btnStartGame = new JButton("Start Game");
        JButton btnConfig = new JButton("Config. Games");
        JButton btnAbout = new JButton("About");

        configureButtonStyle(btnStartGame);
        configureButtonStyle(btnAbout);
        configureButtonStyle(btnConfig);
        Panel panel = this;
        btnAbout.addActionListener(e -> new AboutView(this));
        btnConfig.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigProperty cp = new ConfigProperty(panel);
                setVisible(false);
            };
        });

        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear una instancia de GameGUI con los valores configurados
                int numPlayers = 5; // Número fijo de jugadores
                int maxPoints = 0;
                int numGames = 0;
                try {
                    Properties properties = new Properties();
                    properties.load(new FileInputStream("src/resources/config.properties"));
                    maxPoints = Integer.parseInt(properties.getProperty("pointsToWin"));
                    numGames = Integer.parseInt(properties.getProperty("numberOfGames"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                GameGUI gameGUI = new GameGUI(numPlayers, maxPoints, numGames);
                gameGUI.setVisible(true);
                setVisible(false);
            }
        });


        btnStartGame.setBackground(new Color(50, 205, 50)); // Green color
        btnAbout.setBackground(new Color(255, 69, 0)); // Orange color
        btnConfig.setBackground(new Color(143, 163, 194));
        Font buttonsFont = new Font("Arial", Font.BOLD, 16);
        btnStartGame.setFont(buttonsFont);
        btnAbout.setFont(buttonsFont);
        btnConfig.setFont(buttonsFont);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnStartGame);
        buttonPanel.add(btnAbout);
        buttonPanel.add(btnConfig);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setBackground(new Color(1,1,8));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Panel());
    }
}


