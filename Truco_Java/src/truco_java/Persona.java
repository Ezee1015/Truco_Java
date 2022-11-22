package truco_java;

import java.util.ArrayList;

public class Persona extends Jugador {
    public int posMano[] = new int[3];

    public Persona(ArrayList<Carta> mano, boolean esMano) {
        super(mano, esMano);
    }

    public int[] getPosMano(){
        return posMano;
    }

    public void setPosMano(int[] p){
        posMano=p;
    }

    public Carta tirarCarta(int pos){
        Carta aTirar = mano.get(pos);
        mano.remove(pos);
        cartasJugadas.add(aTirar);
        return aTirar;
    }
}
