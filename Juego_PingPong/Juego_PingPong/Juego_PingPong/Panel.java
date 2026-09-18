import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

enum EstadoJuego {
    INICIO, JUGANDO, PAUSA, FIN
}

public class Panel extends JPanel implements Runnable {
    // Imagen de fondo
    private BufferedImage fondo;

    // Definición de dimensiones de la pantalla
    static final int Pantalla_Ancho = 1100;
    static final int Pantalla_Altura = (int) (Pantalla_Ancho * 0.5555); // Relación de 16:9
    static final Dimension Pantalla_Dimension = new Dimension(Pantalla_Ancho, Pantalla_Altura);

    // Definición de tamaños de objetos en el juego (bola, paletas)
    static final int Diametro_Bola = 20;
    static final int Paleta_Ancho = 25;
    static final int Paleta_Altura = 100;

    // Variables del juego
    Thread gameThread;
    Random random;
    Paleta paleta1;
    Paleta paleta2;
    Bola bola;
    Contador contador;

    // Estado actual del juego (inicio, jugando, pausa, fin)
    EstadoJuego estado = EstadoJuego.INICIO;

    // Gestión de los bufos (elementos adicionales como efectos visuales)
    private GestorBufos gestorBufos;

    // Instancia para la gestión de sonidos
    private transient Sonido sonidos = new Sonido();

    // Lista de historial de partidas
    ListaPartidas historial = new ListaPartidas();
    int numeroPartida = 1;

    // Constructor del Panel
    public Panel() {
        // Intentamos cargar la imagen de fondo desde el archivo
        try {
            fondo = ImageIO.read(new File(
                    "F:\\Sebastian\\U\\Estructura de Datos y Algoritmos\\Proyecto\\Juego_PingPong\\Juego_PingPong\\Img\\Fondo.jpg"));
        } catch (IOException e) {
            e.printStackTrace(); // Si no se encuentra la imagen, mostrar error
        }

        // Configuración de la interfaz
        this.setPreferredSize(Pantalla_Dimension);
        this.setFocusable(true); // Permitir recibir eventos de teclado
        this.addKeyListener(new CaptarTeclas()); // Añadir escucha de teclas

        // Crear los objetos iniciales del juego (paletas, bola, contador)
        newPaletas();
        newBola();
        contador = new Contador(Pantalla_Ancho, Pantalla_Altura);
        gestorBufos = new GestorBufos();

        // Iniciar el hilo del juego
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Método que se ejecuta cada vez que se redibuja el Panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Si la imagen de fondo se ha cargado correctamente, dibujarla
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }

        // Llamamos al método que dibuja el contenido del juego (paletas, bola, puntos,
        // etc.)
        draw(g);
    }

    // Método para dibujar el contenido del juego en pantalla
    public void draw(Graphics g) {
        // Si el juego está en el estado de inicio, mostramos el mensaje de "presiona
        // ENTER"
        if (estado == EstadoJuego.INICIO) {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Presiona ENTER para empezar", 300, 250);

            // Si el juego terminó, mostramos quién ganó y el historial de partidas
        } else if (estado == EstadoJuego.FIN) {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String ganador = contador.jugador1 > contador.jugador2 ? "Jugador 1 GANA" : "Jugador 2 GANA";
            g.drawString(ganador, 400, 200);

            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString("Presiona R para reiniciar", 350, 240);

            // Dibujar historial de partidas
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Historial de Partidas:", 60, 300);
            java.util.List<String> lineas = obtenerLineasHistorial();
            int y = 330;
            for (String linea : lineas) {
                g.drawString(linea, 60, y);
                y += 25;
            }

            // Si el juego está en pausa, mostramos el mensaje de pausa
        } else if (estado == EstadoJuego.PAUSA) {
            paleta1.draw(g);
            paleta2.draw(g);
            bola.draw(g);

            g.setFont(new Font("Consolas", Font.BOLD, 50));
            g.setColor(Color.white);
            g.drawString("PAUSA", Pantalla_Ancho / 2 - 90, Pantalla_Altura / 2);

            // Mostrar los puntos en la pantalla
            g.setColor(Color.blue);
            g.drawString(String.valueOf(contador.jugador1), Pantalla_Ancho / 2 - 100, 60);
            g.setColor(Color.red);
            g.drawString(String.valueOf(contador.jugador2), Pantalla_Ancho / 2 + 70, 60);

            // Si el juego está en progreso, dibujamos los objetos y los puntos
        } else {
            paleta1.draw(g);
            paleta2.draw(g);
            bola.draw(g);
            gestorBufos.dibujar(g); // Dibuja los bufos (efectos visuales)

            g.setFont(new Font("Consolas", Font.BOLD, 50));
            g.setColor(Color.blue);
            g.drawString(String.valueOf(contador.jugador1), Pantalla_Ancho / 2 - 100, 60);
            g.setColor(Color.red);
            g.drawString(String.valueOf(contador.jugador2), Pantalla_Ancho / 2 + 70, 60);
        }

        Toolkit.getDefaultToolkit().sync(); // Asegura la sincronización de la pantalla
    }

    // Método para crear una nueva bola en el centro de la pantalla
    public void newBola() {
        random = new Random();
        bola = new Bola((Pantalla_Ancho / 2) - (Diametro_Bola / 2),
                random.nextInt(Pantalla_Altura - Diametro_Bola), Diametro_Bola, Diametro_Bola);
    }

    // Método para crear nuevas paletas en las posiciones iniciales
    public void newPaletas() {
        paleta1 = new Paleta(0, (Pantalla_Altura / 2) - (Paleta_Altura / 2), Paleta_Ancho, Paleta_Altura, 1);
        paleta2 = new Paleta(Pantalla_Ancho - Paleta_Ancho, (Pantalla_Altura / 2) - (Paleta_Altura / 2),
                Paleta_Ancho, Paleta_Altura, 2);
    }

    // Método para manejar los movimientos de los objetos
    public void movimiento() {
        if (estado == EstadoJuego.JUGANDO) {
            paleta1.movimiento();
            paleta2.movimiento();
            bola.movimiento();
        }
    }

    // Método para verificar las colisiones (bola con paletas, bordes, etc.)
    public void colision() {
        if (estado != EstadoJuego.JUGANDO)
            return;

        gestorBufos.verificarColisiones(paleta1, paleta2, bola); // Verifica colisiones con bufos

        // Si la bola toca el borde superior o inferior, se revierte su dirección
        if (bola.y <= 0 || bola.y >= Pantalla_Altura - Diametro_Bola) {
            bola.setYDirecion(-bola.yVelocidad);
            sonidos.reproducir(Sonido.REBOTE); // Reproduce el sonido de rebote
        }

        // Si la bola toca la paleta 1
        if (bola.intersects(paleta1)) {
            bola.xVelocidad = Math.abs(bola.xVelocidad) + 1; // Incrementa velocidad
            bola.yVelocidad += (bola.yVelocidad > 0) ? 1 : -1;
            sonidos.reproducir(Sonido.REBOTE); // Reproduce el sonido de rebote
        }

        // Si la bola toca la paleta 2
        if (bola.intersects(paleta2)) {
            bola.xVelocidad = -Math.abs(bola.xVelocidad) - 1; // Disminuye velocidad
            bola.yVelocidad += (bola.yVelocidad > 0) ? 1 : -1;
            sonidos.reproducir(Sonido.REBOTE); // Reproduce el sonido de rebote
        }

        // Limita la posición de las paletas para que no se salgan de la pantalla
        paleta1.y = Math.max(0, Math.min(paleta1.y, Pantalla_Altura - Paleta_Altura));
        paleta2.y = Math.max(0, Math.min(paleta2.y, Pantalla_Altura - Paleta_Altura));

        // Detecta cuando la bola pasa al lado de un jugador
        if (bola.x <= 0) {
            contador.jugador2++;
            sonidos.reproducir(Sonido.PUNTO); // Reproduce sonido de punto
            newPaletas();
            newBola();
        }

        if (bola.x >= Pantalla_Ancho - Diametro_Bola) {
            contador.jugador1++;
            sonidos.reproducir(Sonido.PUNTO); // Reproduce sonido de punto
            newPaletas();
            newBola();
        }

        // Si un jugador llega a 3 puntos, termina el juego y se guarda el resultado
        if (contador.jugador1 >= 1 || contador.jugador2 >= 1) {
            String ganador = contador.jugador1 > contador.jugador2 ? "Jugador 1" : "Jugador 2";
            Partida nueva = new Partida(numeroPartida++, ganador, contador.jugador1, contador.jugador2);
            historial.insertar(nueva);
            estado = EstadoJuego.FIN; // El juego termina
        }
    }

    // Método para obtener el historial de partidas en formato de lista de strings
    public java.util.List<String> obtenerLineasHistorial() {
        java.util.List<String> lineas = new ArrayList<>();
        Nodo actual = historial.cabeza;
        while (actual != null) {
            lineas.add(actual.partida.mostrar());
            actual = actual.siguiente;
        }
        return lineas;
    }

    // Método que ejecuta el ciclo principal del juego
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1_000_000_000 / amountOfTicks;
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                movimiento();
                colision();
                gestorBufos.actualizar(paleta1, paleta2, bola); // Actualiza los bufos
                repaint(); // Redibuja el panel
                delta--;
            }
        }
    }

    // Clase para captar las teclas presionadas y realizar acciones en el juego
    public class CaptarTeclas extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            // Acciones basadas en las teclas presionadas
            switch (estado) {
                case INICIO:
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        estado = EstadoJuego.JUGANDO;
                    }
                    break;
                case JUGANDO:
                    if (e.getKeyCode() == KeyEvent.VK_P) {
                        estado = EstadoJuego.PAUSA;
                    } else {
                        paleta1.presionarTecla(e);
                        paleta2.presionarTecla(e);
                    }
                    break;
                case PAUSA:
                    if (e.getKeyCode() == KeyEvent.VK_P)
                        estado = EstadoJuego.JUGANDO;
                    break;
                case FIN:
                    if (e.getKeyCode() == KeyEvent.VK_R && estado == EstadoJuego.FIN) {
                        contador.reiniciarCont();
                        newPaletas();
                        newBola();
                        estado = EstadoJuego.INICIO;
                    break;                   
                }
                
            }
        }

        // Soltar teclas para dejar de mover las paletas
        public void keyReleased(KeyEvent e) {
            if (estado == EstadoJuego.JUGANDO) {
                paleta1.soltarTecla(e);
                paleta2.soltarTecla(e);
            }
        }
    }
}