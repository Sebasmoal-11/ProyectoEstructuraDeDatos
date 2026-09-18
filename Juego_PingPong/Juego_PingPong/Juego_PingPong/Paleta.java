import java.awt.*;
import java.awt.event.*;

public class Paleta extends Rectangle{
     
        int id;
        int yVelocidad;
        int speed = 10;
        
        Paleta(int x, int y, int Paleta_Ancho, int Paleta_Altura, int id) {
        super(x,y,Paleta_Ancho,Paleta_Altura);
		    this.id=id;
    
        }

        public void setYDirecion(int yDireccion) {
            yVelocidad = yDireccion;
      
          }
  
          public void movimiento() {
            y= y + yVelocidad;
      
          }
  
          public void draw(Graphics g) {
            if(id==1)
              g.setColor(Color.blue);
          else
              g.setColor(Color.red);
          g.fillRect(x, y, width, height);
      
      }
  

        public void presionarTecla(KeyEvent e) {
          switch(id) {
		case 1:
			if(e.getKeyCode()==KeyEvent.VK_W) {
				setYDirecion(-speed);
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				setYDirecion(speed);
			}
			break;
		case 2:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirecion(-speed);
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirecion(speed);
			}
			break;
		}
	}
        public void soltarTecla(KeyEvent e) {
          switch(id) {
		case 1:
			if(e.getKeyCode()==KeyEvent.VK_W) {
				setYDirecion(0);
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				setYDirecion(0);
			}
			break;
		case 2:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirecion(0);
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirecion(0);
			}
			break;
		}
	}
            
        
    
}


