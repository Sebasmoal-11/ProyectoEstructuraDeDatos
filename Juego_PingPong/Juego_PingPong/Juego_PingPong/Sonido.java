import javax.sound.sampled.*;

public class Sonido {
    public static final int REBOTE = 0;  // "Ploink" corto (rebotes)
    public static final int PUNTO = 1;   // Triple tono ascendente (anotación)
    public static final int PODER = 2;   // "Zwoop" (poderes)
    
    public void reproducir(int tipoSonido) {
        new Thread(() -> {
            try {
                AudioFormat af = new AudioFormat(44100, 8, 1, true, false);
                SourceDataLine line = AudioSystem.getSourceDataLine(af);
                line.open(af);
                line.start();
                
                byte[] sonido = generarSonido(tipoSonido);
                line.write(sonido, 0, sonido.length);
                
                line.drain();
                line.close();
            } catch (Exception e) {
                System.err.println("Error de audio: " + e.getMessage());
            }
        }).start();
    }
    
    private byte[] generarSonido(int tipo) {
        int duracionMs = 500; // Más largo para el punto
        byte[] sonido = new byte[44100 * duracionMs / 1000];
        
        switch (tipo) {
            case REBOTE: // Sonido original (rebote)
                for (int i = 0; i < sonido.length; i++) {
                    double freq = 440 + (i / (double)sonido.length) * 880;
                    double angle = i / (44100.0 / freq) * 2.0 * Math.PI;
                    sonido[i] = (byte)(Math.sin(angle) * 127 * Math.exp(-i / 10000.0));
                }
                break;
                
            case PUNTO: // Nuevo sonido: Triple tono ascendente (¡anotación!)
                for (int i = 0; i < sonido.length; i++) {
                    double freq;
                    if (i < sonido.length / 3) {
                        freq = 440; // Nota La (primer tono)
                    } else if (i < 2 * sonido.length / 3) {
                        freq = 554.37; // Nota Do# (segundo tono)
                    } else {
                        freq = 659.25; // Nota Mi (tercer tono)
                    }
                    double angle = i / (44100.0 / freq) * 2.0 * Math.PI;
                    sonido[i] = (byte)(Math.sin(angle) * 127 * (1.0 - (i % (sonido.length/3)) / (double)(sonido.length/3)));
                }
                break;
                
            case PODER: // Sonido original (poder)
                for (int i = 0; i < sonido.length; i++) {
                    double freq = 220 + (i / (double)sonido.length) * 1760;
                    double angle = i / (44100.0 / freq) * 2.0 * Math.PI;
                    sonido[i] = (byte)(Math.sin(angle) * 127 * (1.0 - i / (double)sonido.length));
                }
                break;
        }
        return sonido;
    }
}
