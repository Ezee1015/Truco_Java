/*
    Menu de Inicio
*/
package truco_java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Truco_Java {


    public static void main(String[] args) throws IOException {


        /*
        for(int x=0;x<mazo.size();x++){
            System.out.println(mazo.get(x).getNumero() + " de " + mazo.get(x).getPalo());
        }*/

        Music musicaFondo = new Music();
        musicaFondo.setFile("src/truco_java/musica/fondo.wav");
        musicaFondo.play();

        InterfazJuego juego = new InterfazJuego();
        juego.setBounds(0,0,500,800);
        juego.setLocationRelativeTo(null);
        juego.setVisible(true);
    }

}
