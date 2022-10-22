package truco_java;

import java.util.ArrayList;

public class Persona extends Jugador {

    public Persona(ArrayList<Carta> mano, boolean esMano) {
        super(mano, esMano);
    }

    public Carta tirarCarta(int pos){
        Carta aTirar = mano.get(pos);
        mano.remove(pos);
        cartasJugadas.add(aTirar);
        return aTirar;
    }
}
