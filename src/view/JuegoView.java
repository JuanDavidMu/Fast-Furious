package view;

import logic.Jugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JuegoView extends JFrame {

    private JPanel mainPanel;
    private JTable tablaJugadores;
    private DefaultTableModel modeloTabla;
    private JTextArea txtAreaClasificacion;

    public JuegoView(int numJugadores) {
        setTitle("Juego - Rápidos y Furiosos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Panel para la tabla de jugadores
        JPanel panelTabla = new JPanel(new BorderLayout());
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Partida");
        modeloTabla.addColumn("Hora");
        modeloTabla.addColumn("Nombre Jugador");
        modeloTabla.addColumn("Lanzamiento");
        modeloTabla.addColumn("Puntaje Lanzamiento");
        modeloTabla.addColumn("Suma Puntajes");
        modeloTabla.addColumn("Puntaje Restante");
        tablaJugadores = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaJugadores);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        // Panel para la clasificación
        JPanel panelClasificacion = new JPanel(new BorderLayout());
        txtAreaClasificacion = new JTextArea();
        txtAreaClasificacion.setEditable(false);
        JScrollPane scrollClasificacion = new JScrollPane(txtAreaClasificacion);
        panelClasificacion.add(new JLabel("Clasificación"), BorderLayout.NORTH);
        panelClasificacion.add(scrollClasificacion, BorderLayout.CENTER);

        mainPanel.add(panelTabla, BorderLayout.CENTER);
        mainPanel.add(panelClasificacion, BorderLayout.EAST);

        add(mainPanel);
        setVisible(true);
    }

    public void actualizarDatosJugador(int numeroPartida, String nombre, int lanzamiento, int puntajeLanzamiento, int sumaPuntajes, int puntajeRestante, int numeroDePartidas) {
        Object[] fila = {numeroPartida + "/" + numeroDePartidas, obtenerHoraActual(), nombre, lanzamiento, puntajeLanzamiento, sumaPuntajes, puntajeRestante};
        modeloTabla.addRow(fila);
    }

    public void mostrarPuesto(int puesto) {
        // No se muestra en un JOptionPane, puede implementarse de otra forma según tus necesidades
    }

    public void mostrarClasificacion(List<Jugador> clasificacion) {
        StringBuilder sb = new StringBuilder();
        int puesto = 1;
        for (Jugador jugador : clasificacion) {
            sb.append(puesto).append(". ")
                    .append(jugador.getNombre())
                    .append(" - Puntaje: ")
                    .append(jugador.getPuntaje())
                    .append("\n");
            puesto++;
        }
        txtAreaClasificacion.setText(sb.toString());
    }

    private String obtenerHoraActual() {
        ZonedDateTime horaActual = ZonedDateTime.now(ZoneId.systemDefault());
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        return formatoHora.format(horaActual);
    }
}













