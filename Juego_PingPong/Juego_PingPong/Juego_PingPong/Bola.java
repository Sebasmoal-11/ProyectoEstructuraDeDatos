import java.awt.*;
import java.util.*;

public class Bola extends Rectangle {

  Random random;
  int xVelocidad;
  int yVelocidad;
  int inicialRapidez = 2;
  private int tamañoOriginalBola;

  Bola(int x, int y, int width, int height) {
    super(x, y, width, height);
    random = new Random();
    int randomXDirection = random.nextInt(2) == 0 ? -1 : 1;
    if (randomXDirection == 0)
      randomXDirection--;
    setXDirecion(randomXDirection * inicialRapidez);

    int randomYDirection = random.nextInt(2) == 0 ? -1 : 1;
    if (randomYDirection == 0)
      randomYDirection--;
    setYDirecion(randomYDirection * inicialRapidez);

    this.tamañoOriginalBola = width;
  }

  public void setYDirecion(int randomYDirecion) {
    yVelocidad = randomYDirecion;

  }

  public void setXDirecion(int randomXDirecion) {
    xVelocidad = randomXDirecion;

  }

  public void movimiento() {
    x += xVelocidad;
    y += yVelocidad;


        
  }

  public void draw(Graphics g) {
    // Dibuja el efecto de halo si la bola está agrandada
    if (width > tamañoOriginalBola) {
      g.setColor(new Color(255, 255, 0, 100)); // Amarillo semitransparente
      g.fillOval(x - 5, y - 5, width + 10, height + 10); // Halo más grande
    }
    // Color bola normal
    g.setColor(Color.white);
    g.fillOval(x, y, width, height);
  }
}
