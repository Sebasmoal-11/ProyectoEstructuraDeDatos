public class EfectoBufo {
    private int tipo;
    private Paleta jugador;
    private Paleta rival;
    private Bola bola;
    private long inicio;
    private final long duracion;
    
    // Valores originales
    private int velocidadJugadorOriginal;
    private int velocidadRivalOriginal;
    private int alturaJugadorOriginal;
    private int alturaRivalOriginal;
    private int tamañoOriginalBola; 


    public EfectoBufo(int tipo, Paleta jugador, Paleta rival, Bola bola) {
        this.tipo = tipo;
        this.jugador = jugador;
        this.rival = rival;
        this.bola = bola;
        this.inicio = System.currentTimeMillis();
        this.duracion = Bufo.DURACIONES_EFECTO[tipo]; // Usa la duración definida en Bufo
        guardarValoresOriginales();
        aplicar();
    }
    
    private void guardarValoresOriginales() {
        velocidadJugadorOriginal = jugador.speed;
        velocidadRivalOriginal = rival.speed;
        alturaJugadorOriginal = jugador.height;
        alturaRivalOriginal = rival.height;
        this.tamañoOriginalBola = bola.width;
    }
    
    public void aplicar() {
        switch(tipo) {
            case 0: //Reducir velocidad rival
                rival.speed = velocidadRivalOriginal / 2; break;
            case 1: //Aumentar velocidad jugador
                jugador.speed = velocidadJugadorOriginal * 2; break;
            case 2:  // Aumentar tamaño Jugador
                jugador.height = (int)(alturaJugadorOriginal * 1.5); break;
            case 3: // Disminuir tamaño rival
                rival.height = (int)(alturaRivalOriginal * 0.5); break;
            case 4: // Aumentar velocidad de la bola
                bola.xVelocidad *= 1.5;
                bola.yVelocidad *= 1.5;
                break;
            case 5: // Aumentar el tamaño de la bola 
                bola.width = (int)(tamañoOriginalBola * 2);
                bola.height = (int)(tamañoOriginalBola * 2);
                break;
            
        }
    }
    
    public void revertir() {
        switch(tipo) {
            case 0: //Reducir velocidad rival
                rival.speed = velocidadRivalOriginal; break;
            case 1://Aumentar velocidad jugador
                jugador.speed = velocidadJugadorOriginal; break;
            case 2:// Aumentar tamaño Jugador
                jugador.height = alturaJugadorOriginal; break;
            case 3: // Disminuir tamaño rival
                rival.height = alturaRivalOriginal; break;
            case 4: 
                break; // Aumentar velocidad de la bola ,no necesita revertir
            case 5: // Aumentar el tamaño de la bola
                bola.width = tamañoOriginalBola;
                bola.height = tamañoOriginalBola;
            break;
        }
    }
    
    public boolean expirado() {
        return tipo == 4 || System.currentTimeMillis() - inicio > duracion;
    }
}