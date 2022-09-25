package truco_java;

import java.util.ArrayList;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Jugador {
  protected ArrayList<Carta> mano = new ArrayList<Carta>();
  protected ArrayList<Carta> cartasJugadas = new ArrayList<Carta>();
  protected int puntaje=0;
  protected boolean esMano;

    public Jugador(ArrayList<Carta> mano, boolean esMano) {
        this.mano = mano;
        this.esMano = esMano;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje, InterfazJuego interfaz) {
      this.puntaje = puntaje;
      try {
        interfaz.dibujarPuntaje();
      } catch (IOException ex) {
        Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
        ordenarMano();
        for(int i=0;i<mano.size();i++)
            System.out.println("Carta: " + mano.get(i).texto());
        System.out.println("_________");
    }

    public void ordenarMano(){
      ArrayList<Carta> temp = new ArrayList<>();
      int posMenor=0;

      for(int x=1;x<mano.size();x++){
        for(int i=1;i<mano.size();i++){
          if(mano.get(i).rankingCarta()>mano.get(posMenor).rankingCarta()){
            posMenor=i;
          }
        }
        temp.add(mano.get(posMenor));
        mano.remove(posMenor);
      }

      mano.addAll(temp);
    }

    public ArrayList<Carta> getCartasJugadas() {
        return cartasJugadas;
    }

    public void setCartasJugadas(ArrayList<Carta> cartasJugadas) {
        this.cartasJugadas = cartasJugadas;
    }

    public boolean isMano() {
        return esMano;
    }

    public void setEsMano(boolean esMano) {
        this.esMano = esMano;
    }

    protected int calcularEnvido(){
      int envido=0;
      ArrayList<Carta> manoOrigi = mano;
      manoOrigi.addAll(cartasJugadas);

      // Busca por pares por un envido de 20
      for(int i=0; i<manoOrigi.size();i++){
        int m1=i%(manoOrigi.size()+1);
        int m2=(i+1)%(manoOrigi.size());

        if(manoOrigi.get(m1).getPalo().equals(manoOrigi.get(m2).getPalo())) {
          int envidoTemp=20;

          if(manoOrigi.get(m1).getNumero() < 10)
            envidoTemp += manoOrigi.get(m1).getNumero();
          if(manoOrigi.get(m2).getNumero() < 10)
            envidoTemp += manoOrigi.get(m2).getNumero();

          if (envidoTemp>envido) envido=envidoTemp;
        }
      }

      // Si no hubo más de 20 en envido (no hubo dos del mismo palo)
      if (envido==0){
        for(int i=0; i<manoOrigi.size();i++){
          int num = manoOrigi.get(i).getNumero();
          if(num < 10 && num > envido) envido= num;
        }
      }

      return envido;
    }

  public void agregarCartaJugada(Carta c){
      cartasJugadas.add(c);
  }

}
