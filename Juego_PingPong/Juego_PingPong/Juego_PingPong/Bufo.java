import java.awt.*;
import java.util.Random;

public class Bufo extends Rectangle {
    public static final int TAMAÑO = 25;
    public static final int DURACION_EN_PANTALLA = 3000; // 3 segundos

    // Duración específica por tipo de bufo (en milisegundos)
    public static final int[] DURACIONES_EFECTO = {
        4000,  // 0: Ralentizar rival (4 segundos)
        3000,  // 1: Velocidad+ (3 segundos)
        5000,  // 2: Paleta grande (5 segundos)
        4000,  // 3: Rival pequeño (4 segundos)
        0,     // 4: Bola rápida (instantáneo)
        3000   // 5: Bola grande (3 segundos)
    };
    
    private int tipo;
    private long tiempoAparicion;
    private boolean activo;
    private float tonoColor = 0f; // Para efecto arcoíris

    public Bufo(int x, int y) {
        super(x, y, TAMAÑO, TAMAÑO);
        Random rand = new Random();
        this.tipo = rand.nextInt(6); // Ahora incluye el tipo 5 (Bola Grande)
        this.tiempoAparicion = System.currentTimeMillis();
        this.activo = true;
    }
    
    public void dibujar(Graphics g) {
        if (!activo) return;
        
        Graphics2D g2d = (Graphics2D)g;
        
        // Efecto arcoíris
        tonoColor += 0.01f;
        if(tonoColor > 1f) tonoColor = 0f;
        Color colorEstrella = Color.getHSBColor(tonoColor, 1f, 1f);
        
        // Dibujar estrella
        dibujarEstrella(g2d, x + width/2, y + height/2, width/2, colorEstrella);
    }
    
    private void dibujarEstrella(Graphics2D g, int centroX, int centroY, int radio, Color color) {
        int puntas = 5;
        int[] puntosX = new int[puntas*2];
        int[] puntosY = new int[puntas*2];
        
        for(int i = 0; i < puntas*2; i++) {
            double angulo = Math.PI * i / puntas;
            int radioActual = (i % 2 == 0) ? radio : radio/2;
            puntosX[i] = centroX + (int)(radioActual * Math.cos(angulo - Math.PI/2));
            puntosY[i] = centroY + (int)(radioActual * Math.sin(angulo - Math.PI/2));
        }
        
        g.setColor(color);
        g.fillPolygon(puntosX, puntosY, puntas*2);
        
        // Borde amarillo
        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(2));
        g.drawPolygon(puntosX, puntosY, puntas*2);
    }
    
    public boolean expirado() {
        return System.currentTimeMillis() - tiempoAparicion > DURACION_EN_PANTALLA;
    }
    
    public int getTipo() { return tipo; }
    public boolean estaActivo() { return activo; }
    public void desactivar() { this.activo = false; }
}

