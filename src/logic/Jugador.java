package logic;

import view.JuegoView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Jugador implements Runnable {
    private String nombre;
    private int puntaje;
    private int puntajeRestante;
    private int puntosACompletar;
    private boolean haCompletadoPartida;
    private int numeroDePartidas;

    private Juego juego;
    private JuegoView juegoView;


    private Thread thread;

    public Jugador(String nombre,int numeroDePartidas, int puntosACompletar) {
        this.nombre = nombre;
        this.puntosACompletar = puntosACompletar;
        this.puntaje = 0;
        this.numeroDePartidas = numeroDePartidas;
        this.puntajeRestante = puntosACompletar;
        this.haCompletadoPartida = false;
        this.thread = new Thread(this);
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getNumeroDePartidas() {
        return numeroDePartidas;
    }

    public void setNumeroDePartidas(int numeroDePartidas) {
        this.numeroDePartidas = numeroDePartidas;
    }

    public int getPuntajeRestante() {
        return puntajeRestante;
    }

    public boolean haCompletadoPartida() {
        return haCompletadoPartida;
    }

    public Thread getThread() {
        return thread;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public void setJuegoView(JuegoView juegoView) {
        this.juegoView = juegoView;
    }

    @Override
    public void run() {
        thread = Thread.currentThread();
        for (int i = 1; i <= juego.getNumeroDePartidas(); i++) {
            // Simular el lanzamiento de dados y actualizar puntajes
            int lanzamiento = lanzarDado();
            puntaje += lanzamiento;
            puntajeRestante -= lanzamiento;

            // Obtener la hora actual
            LocalDateTime horaActual = LocalDateTime.now();
            // Formatear la hora según un formato específico
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String horaFormateada = horaActual.format(formatter);

            // Calcular la suma de puntajes
            int sumaPuntajes = puntaje - puntajeRestante;

            // Actualizar la vista de JuegoView
            juegoView.actualizarDatosJugador(i, nombre, lanzamiento, puntaje, sumaPuntajes, puntajeRestante, getNumeroDePartidas());

            // Si el puntaje llega al objetivo, el jugador ha completado la partida
            if (puntaje >= puntosACompletar) {
                break;
            }

            // Simular un retraso aleatorio entre lanzamientos
            try {
                // Usar el valor absoluto del tiempo de retraso para evitar valores negativos
                Thread.sleep(Math.abs(generarRetraso()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        haCompletadoPartida = true;
        juego.jugadorCompletoPartida(this);
    }


    private int lanzarDado() {
        Random random = new Random();
        return random.nextInt(6) + 1; // Simulación de lanzamiento de dado
    }

    private long generarRetraso() {
        Random random = new Random();
        return random.nextLong() % 1000; // Retraso aleatorio entre 0 y 1000 milisegundos
    }
}






