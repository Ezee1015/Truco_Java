package truco_java;

import java.util.ArrayList;

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

    public void setPuntaje(int puntaje) {
        System.out.println("gano puntajeeeeee --> " + puntaje);
        this.puntaje = puntaje;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
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
      
    // Busca por pares por un envido de 20
    for(int i=0; i<mano.size();i++){
      int m1=i%(mano.size()+1);
      int m2=(i+1)%(mano.size());
      
      if(mano.get(m1).getPalo().equals(mano.get(m2).getPalo())) {
        int envidoTemp=20;

        if(mano.get(m1).getNumero() < 10)
          envidoTemp += mano.get(m1).getNumero();
        if(mano.get(m2).getNumero() < 10)
          envidoTemp += mano.get(m2).getNumero();

        if (envidoTemp>envido) envido=envidoTemp;
      }
    }
    
    // Si no hubo más de 20 en envido (no hubo dos del mismo palo)
    if (envido==0){
        envido = mano.get(0).getNumero();
       for(int i=1; i<mano.size();i++){
           int num = mano.get(i).getNumero();
           if(num < 10 && num > envido) envido= num;
       }
    }
    
    return envido;
  }
  
  public void agregarCartaJugada(Carta c){
      cartasJugadas.add(c);
  }
  
}