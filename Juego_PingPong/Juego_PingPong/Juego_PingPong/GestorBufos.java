import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GestorBufos {
    private ArrayList<Bufo> bufosActivos;
    private ArrayList<EfectoBufo> efectosActivos;
    private Random random;
    private long ultimoSpawn;
    private static final long INTERVALO_SPAWN = 15000;
    
    // Sistema de mensajes
    private String mensajeActual = "";
    private long tiempoMensaje = 0;
    private final Color COLOR_JUGADOR1 = new Color(0, 100, 255);
    private final Color COLOR_JUGADOR2 = new Color(255, 50, 50);

    /* Declaracion de Sonido */
    private transient Sonido sonidos = new Sonido();

    public GestorBufos() {
        this.bufosActivos = new ArrayList<>();
        this.efectosActivos = new ArrayList<>();
        this.random = new Random();
        this.ultimoSpawn = System.currentTimeMillis();
    }

    public void actualizar(Paleta jugador1, Paleta jugador2, Bola bola) {
        // Generar nuevos bufos
        if (System.currentTimeMillis() - ultimoSpawn > INTERVALO_SPAWN && bufosActivos.isEmpty()) {
            generarBufo(jugador1, jugador2, bola);
            ultimoSpawn = System.currentTimeMillis();
        }
        
        // Limpiar mensaje después de 3 segundos
        if (!mensajeActual.isEmpty() && System.currentTimeMillis() - tiempoMensaje > 3000) {
            mensajeActual = "";
        }
        
        // Manejar efectos activos
        efectosActivos.removeIf(efecto -> {
            if (efecto.expirado()) {
                efecto.revertir();
                return true;
            }
            return false;
        });
    }

    private void generarBufo(Paleta jugador1, Paleta jugador2, Bola bola) {
        int y = random.nextInt(Panel.Pantalla_Altura - Bufo.TAMAÑO - 50) + 25;
        int x = (bola.xVelocidad > 0) ? 0 : Panel.Pantalla_Ancho - Bufo.TAMAÑO;
        
        // 6 tipos de bufos (0-5)
        int tipo = random.nextInt(6);
        Bufo nuevoBufo = new Bufo(x, y);
        bufosActivos.add(nuevoBufo);
    }

    public void verificarColisiones(Paleta jugador1, Paleta jugador2, Bola bola) {
        bufosActivos.removeIf(bufo -> {
            if (!bufo.estaActivo()) return true;
            
            boolean colisionJ1 = bufo.intersects(jugador1);
            boolean colisionJ2 = bufo.intersects(jugador2);
            
            if (colisionJ1 || colisionJ2) {
                sonidos.reproducir(Sonido.PODER);
                Paleta jugador = colisionJ1 ? jugador1 : jugador2;
                Paleta rival = colisionJ1 ? jugador2 : jugador1;
                
                efectosActivos.add(new EfectoBufo(bufo.getTipo(), jugador, rival, bola));
                
                // Configurar mensaje
                String[] nombresBufo = {
                    "RALENTIZAR RIVAL", 
                    "VELOCIDAD ++", 
                    "PALETA GIGANTE", 
                    "RIVAL ENCOGIDO", 
                    "BOLA TURBO",
                    "BOLA GIGANTE"  
                };
                mensajeActual = (colisionJ1 ? "Jugador 1" : "Jugador 2") + " obtuvo: " + nombresBufo[bufo.getTipo()];
                tiempoMensaje = System.currentTimeMillis();
                
                return true; // Eliminar el bufo
            }
            return false;
        });
    }

    public void dibujar(Graphics g) {
        // Dibujar bufos
        bufosActivos.forEach(bufo -> bufo.dibujar(g));
        
        // Dibujar mensaje centrado
        if (!mensajeActual.isEmpty() && System.currentTimeMillis() - tiempoMensaje <= 3000) {
            Graphics2D g2d = (Graphics2D)g;
            
            // Calcular posición central
            int centroX = Panel.Pantalla_Ancho / 2;
            int centroY = Panel.Pantalla_Altura / 2;
            
            // Fondo semitransparente (ajustar tamaño según necesidad)
            g2d.setColor(new Color(0, 0, 0, 180));
            int anchoMensaje = g2d.getFontMetrics().stringWidth(mensajeActual) + 40;
            g2d.fillRoundRect(centroX - anchoMensaje/2, centroY - 25, anchoMensaje, 50, 20, 20);
            
            // Texto del mensaje
            g2d.setColor(mensajeActual.contains("Jugador 1") ? COLOR_JUGADOR1 : COLOR_JUGADOR2);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            
            // Sombra para mejor legibilidad
            g2d.setColor(Color.GRAY);
            g2d.drawString(mensajeActual, centroX - g2d.getFontMetrics().stringWidth(mensajeActual)/2 + 2, centroY + 10 + 2);
            
            // Texto principal
            g2d.setColor(mensajeActual.contains("Jugador 1") ? COLOR_JUGADOR1 : COLOR_JUGADOR2);
            g2d.drawString(mensajeActual, centroX - g2d.getFontMetrics().stringWidth(mensajeActual)/2, centroY + 10);
            
            // Borde decorativo
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.drawRoundRect(centroX - anchoMensaje/2, centroY - 25, anchoMensaje, 50, 20, 20);
        }
    }
    
    public void reiniciar() {
        efectosActivos.forEach(EfectoBufo::revertir);
        bufosActivos.clear();
        efectosActivos.clear();
        mensajeActual = "";
    }
}
