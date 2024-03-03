package logic;

import view.JuegoView;

import java.util.ArrayList;
import java.util.List;

public class Juego {
    private int numeroDePartidas;
    private int puntosACompletar;
    private List<Jugador> jugadores;
    private JuegoView juegoView;
    private boolean partidaEnProgreso;

    public Juego(int numeroDePartidas, int puntosACompletar, String[] nombresJugadores, JuegoView juegoView) {
        this.numeroDePartidas = numeroDePartidas;
        this.puntosACompletar = puntosACompletar;
        this.juegoView = juegoView;
        this.jugadores = new ArrayList<>();
        this.partidaEnProgreso = false;
        for (String nombre : nombresJugadores) {
            Jugador jugador = new Jugador(nombre, numeroDePartidas, puntosACompletar); // Modificado para pasar el número de partidas
            jugador.setJuego(this);
            jugador.setJuegoView(juegoView); // Asignar juegoView a cada jugador
            this.jugadores.add(jugador);
        }
    }


    public int getNumeroDePartidas() {
        return numeroDePartidas;
    }

    public void iniciarPartidas() {
        partidaEnProgreso = true;
        for (int i = 1; i <= numeroDePartidas; i++) {
            System.out.println("Iniciando partida " + i);
            for (Jugador jugador : jugadores) {
                Thread thread = new Thread(jugador);
                thread.start();
            }
            esperarJugadores();
            calcularClasificacion(i);
        }
        partidaEnProgreso = false;
    }


    private void esperarJugadores() {
        for (Jugador jugador : jugadores) {
            try {
                jugador.getThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void calcularClasificacion(int numeroPartida) {
        List<Jugador> jugadoresOrdenados = new ArrayList<>(jugadores);
        jugadoresOrdenados.sort((j1, j2) -> Integer.compare(j2.getPuntaje(), j1.getPuntaje()));
        juegoView.mostrarClasificacion(jugadoresOrdenados);
    }

    public synchronized void jugadorCompletoPartida(Jugador jugador) {
        System.out.println(jugador.getNombre() + " ha completado la partida.");
        boolean todosCompletaron = true;
        for (Jugador j : jugadores) {
            if (!j.haCompletadoPartida()) {
                todosCompletaron = false;
                break;
            }
        }
        if (todosCompletaron) {
            notify();
        }
    }


    public boolean isPartidaEnProgreso() {
        return partidaEnProgreso;
    }
}





