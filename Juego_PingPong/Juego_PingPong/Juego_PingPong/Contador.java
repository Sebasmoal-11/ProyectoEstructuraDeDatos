import java.awt.*;


public class Contador extends Rectangle{
    
    static int Pantalla_Ancho;
    static int Pantalla_Altura;
    int jugador1;
    int jugador2;
    
    Contador(int Pantalla_Ancho, int Pantalla_Altura){
        Contador.Pantalla_Ancho = Pantalla_Ancho;
        Contador.Pantalla_Altura = Pantalla_Altura;
    }
    public void draw(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 60));
        g.drawLine(Pantalla_Ancho/2, 0, Pantalla_Ancho/2, Pantalla_Altura);

        g.drawString(String.valueOf(jugador1), (Pantalla_Ancho/2)-85, 50);
        g.drawString(String.valueOf(jugador2), (Pantalla_Ancho/2)+20, 50);

    }
        // Metodo para reinicar el contador 
        public void reiniciarCont() {
            jugador1 = 0;
            jugador2 = 0;
        }
}
