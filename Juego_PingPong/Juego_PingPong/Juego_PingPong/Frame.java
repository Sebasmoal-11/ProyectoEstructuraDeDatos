import javax.swing.*;

public class Frame extends JFrame {

    Panel panel;

    public Frame() {
        panel = new Panel();
        this.add(panel);
        this.setTitle("Ping Pong");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new Frame();
    }
}