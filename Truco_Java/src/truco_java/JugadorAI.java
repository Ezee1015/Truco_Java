/*
- primera carta random
- Si se empata, tirar siempre la mas alta a la segunda. Sino la segunda carta, si gano la primera, tiene la más alta, sino la más baja, la tercera es la que queda.
- Si se empatan las tres rondas, gana el que no tenia la mano
- Una posibilidad de 1 entre 15 de mentir si es facil o 1 entre 8 si es dificil para el envido (y una posibilidad de 1 a 15 de envido, 1 a 5 de real envido y de 1 de falta envido). Si el envido es mayor a 23 se canta envido, mayor de 27 real envido y mayor a 31 de falta envido
Plus:
- Crear una funcion: CalcularEnvidoRestante() para el modo dificil en JugadorAI
*/

package truco_java;

import java.util.ArrayList;
import java.util.Random;

public class JugadorAI extends Jugador {

    public JugadorAI(ArrayList<Carta> mano, boolean esMano) {
        super(mano, esMano);
    }

  public Carta tirarCartaRandom(){
    int pos = new Random().nextInt(mano.size());
    Carta aTirar = mano.get(pos);
    mano.remove(pos);
    cartasJugadas.add(aTirar);
    return aTirar;
  }

  public Carta tirarMejorCarta (){
    if(mano.isEmpty()) return null;

    int mejor = 0;
    for(int i=1; i<mano.size(); i++){
      if(mano.get(i).rankingCarta() > mano.get(i).rankingCarta())
        mejor = i;
    }

    Carta aTirar = mano.get(mejor);
    mano.remove(mejor);
    cartasJugadas.add(aTirar);
    return aTirar;
  }

  public Carta tirarPeorCarta (){
    if(mano.isEmpty()) return null;

    int mejor = 0;
    for(int i=1; i<mano.size(); i++){
      if(mano.get(i).rankingCarta() > mano.get(i).rankingCarta())
        mejor = i;
    }

    Carta aTirar = mano.get(mejor);
    mano.remove(mejor);
    cartasJugadas.add(aTirar);
    return aTirar;
  }
  
  public int desidirEnvido(int estado){
      /*
      Estados:
        0 --> No quiero / No se cantó
        1 --> Envido
        2 --> Envido - Envido
        3 --> Real Envido
        4 --> Falta Envido
      *** Si devuelve el mismo numero que se le paso a la funcion, se le interpreta como un quiero
      */
      int desicion=0, obligado=estado;
      Random random = new Random();
      
      if(calcularEnvido()>27 && estado==0)   // Obliga a jugar a la AI si tiene como mínimo más de 27 de envido
          obligado=1;
      
      if(calcularEnvido()>=31){                     // Envido más de 31
          if (obligado==4) desicion = desidirSiONoRandom(4);
          else desicion = random.nextInt(obligado,4);
      }else if(calcularEnvido()>=27 && estado < 3) // Envido más de 27 y se cantó hasta Real Envido
          desicion = random.nextInt(obligado,3);
      else if(calcularEnvido()>=25 && estado <= 2) // Envido más de 25 y se cantó envido-envido para abajo
          desicion = random.nextInt(obligado,3);
      else if(calcularEnvido()>=23 && estado == 0) // Envido más de 23 y no se cantó nada 
          desicion = desidirSiONoRandom(random.nextInt(obligado,1));
      
      else if(random.nextInt(8) == 8) { //El 7 es un numero de ejemplo para la probabilidad de 1/8
          if(random.nextInt(0,4) == 3 && estado < 4) // El 3 representa un probabilidad entre 1/4
            desicion = estado++;
          desicion = estado; // Acepta
      }
      
      if(desicion==2 && estado!=1){
          if(estado==0)
              desicion = 1;
          else
              desicion = 3;
      }
      
      if(desicion<estado) // Soluciona un bug
          desicion=estado;
      
      return desicion;
  }
  
  public int desidirTruco (int estado) {
      /*
      Estados:
        0 --> No quiero / No se cantó
        1 --> Truco
        2 --> Retruco
        3 --> Vale 4
      *** Si devuelve el mismo numero que se le paso a la funcion, se le interpreta como un quiero
      */
      
      /***********/
      
      return 0;
  }
  
  private int desidirSiONoRandom(int num){ // Funcion para agregarle aleatoriedad al juego
      Random random = new Random();
      if(random.nextInt(0,5) == 3) // El 3 representa una probabilidad de 1/5 para no jugar 
          return 0;
      return num;
  }
}
