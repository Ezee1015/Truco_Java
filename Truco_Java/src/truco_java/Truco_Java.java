package truco_java;

import java.io.IOException;

public class Truco_Java {
    public static void main(String[] args) throws IOException {
        Music musicaFondo = new Music();
        musicaFondo.setFile("src/truco_java/musica/fondo.wav");
        musicaFondo.play();

        InterfazJuego juego = new InterfazJuego();
        juego.setBounds(0,0,500,800);
        juego.setLocationRelativeTo(null);
        juego.setVisible(true);
    }
}
