public class Partida {
    int numero;
    String ganador;
    int puntajeJugador1;
    int puntajeJugador2;

    public Partida(int numero, String ganador, int p1, int p2) {
        this.numero = numero;
        this.ganador = ganador;
        this.puntajeJugador1 = p1;
        this.puntajeJugador2 = p2;
    }

    public String mostrar() {
        return "Partida #" + numero + " - Ganador: " + ganador +
        " (" + puntajeJugador1 + " - " + puntajeJugador2 + ")";
    }
}
