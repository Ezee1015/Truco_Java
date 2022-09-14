/*
    Menu de Inicio
*/
package truco_java;

import java.util.ArrayList;
import java.util.Random;

public class Truco_Java {

    private static ArrayList<Carta> mazo = new ArrayList<>();

    public static void main(String[] args) {
        mazo.add(new Carta(1,"espada"));
        mazo.add(new Carta(2,"espada"));
        mazo.add(new Carta(3,"espada"));
        mazo.add(new Carta(4,"espada"));
        mazo.add(new Carta(5,"espada"));
        mazo.add(new Carta(6,"espada"));
        mazo.add(new Carta(7,"espada"));
        mazo.add(new Carta(10,"espada"));
        mazo.add(new Carta(11,"espada"));
        mazo.add(new Carta(12,"espada"));
        mazo.add(new Carta(1,"basto"));
        mazo.add(new Carta(2,"basto"));
        mazo.add(new Carta(3,"basto"));
        mazo.add(new Carta(4,"basto"));
        mazo.add(new Carta(5,"basto"));
        mazo.add(new Carta(6,"basto"));
        mazo.add(new Carta(7,"basto"));
        mazo.add(new Carta(10,"basto"));
        mazo.add(new Carta(11,"basto"));
        mazo.add(new Carta(12,"basto"));
        mazo.add(new Carta(1,"oro"));
        mazo.add(new Carta(2,"oro"));
        mazo.add(new Carta(3,"oro"));
        mazo.add(new Carta(4,"oro"));
        mazo.add(new Carta(5,"oro"));
        mazo.add(new Carta(6,"oro"));
        mazo.add(new Carta(7,"oro"));
        mazo.add(new Carta(10,"oro"));
        mazo.add(new Carta(11,"oro"));
        mazo.add(new Carta(12,"oro"));
        mazo.add(new Carta(1,"copa"));
        mazo.add(new Carta(2,"copa"));
        mazo.add(new Carta(3,"copa"));
        mazo.add(new Carta(4,"copa"));
        mazo.add(new Carta(5,"copa"));
        mazo.add(new Carta(6,"copa"));
        mazo.add(new Carta(7,"copa"));
        mazo.add(new Carta(10,"copa"));
        mazo.add(new Carta(11,"copa"));
        mazo.add(new Carta(12,"copa"));

        mezclarMazo();
        /*
        for(int x=0;x<mazo.size();x++){
            System.out.println(mazo.get(x).getNumero() + " de " + mazo.get(x).getPalo());
        }*/
/*
        ArrayList<Carta> mazo1 = new ArrayList<>();
        mazo1.add(mazo.get(0));
        mazo1.add(mazo.get(2));
        mazo1.add(mazo.get(4));
        ArrayList<Carta> mazo2 = new ArrayList<>();
        mazo2.add(mazo.get(1));
        mazo2.add(mazo.get(3));
        mazo2.add(mazo.get(5));

        JugadorAI pepe = new JugadorAI(mazo1,false);
        for(int x=0;x<mazo1.size();x++){
            System.out.println(mazo1.get(x).getNumero() + " de " + mazo1.get(x).getPalo());
        }
        int basura = pepe.desidirEnvido(0);
        System.out.println("Pepe: " + basura);
        if(basura > 0){
            System.out.println("Leo: " + (basura+1));
            System.out.println("Pepe: " + pepe.desidirEnvido(basura+1));
        }*/

        InterfazJuego juego = new InterfazJuego();
        juego.setBounds(0,0,500,800);
        juego.setVisible(true);
    }

    private static void mezclarMazo(){
        for(int i=0;i<100;i++){
            Random random = new Random();
            ArrayList<Carta> mazoTemp = new ArrayList<>();
            for(int x=0;x<mazo.size()+mazoTemp.size();x++){
                int posMezcla=random.nextInt(0,mazo.size());
                mazoTemp.add(mazo.get(posMezcla));
                mazo.remove(posMezcla);
            }
            mazo.addAll(mazoTemp);
        }
    }

}
