/*
- primera carta random
- Si se empata, tirar siempre la mas alta a la segunda. Sino la segunda carta, si gano la primera, tiene la más alta, sino la más baja, la tercera es la que queda.
- Si se empatan las tres rondas, gana el que no tenia la mano
- Una posibilidad de 1 entre 15 de mentir si es facil o 1 entre 8 si es dificil para el envido (y una posibilidad de 1 a 15 de envido, 1 a 5 de real envido y de 1 de falta envido). Si el envido es mayor a 23 se canta envido, mayor de 27 real envido y mayor a 31 de falta envido
Plus:
- Crear una funcion: CalcularEnvidoRestante() para el modo dificil en JugadorAI
*/

package truco_java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class JugadorAI extends Jugador {

    public JugadorAI(ArrayList<Carta> mano, boolean esMano) {
        super(mano, esMano);
    }

    
  public Carta jugarTurno(Persona p, InterfazJuego mesa) throws IOException{
      Random random = new Random();
      Carta aTirar;
      /*
      if(cartasJugadas.isEmpty() && mesa.envidoFinalizado==false){
          mesa.AICantaEnvido();
          return;
      }*/
      System.out.println("** Tira Carta **");
      
      if(cartasJugadas.isEmpty())
          return tirarCartaRandom();
      
      else if(cartasJugadas.size()==1){
          if(p.getCartasJugadas().get(0).rankingCarta() == cartasJugadas.get(0).rankingCarta())
              return tirarMejorCarta();
          if(p.getCartasJugadas().get(0).rankingCarta() > cartasJugadas.get(0).rankingCarta())
              return tirarMejorCarta();
          if(p.getCartasJugadas().get(0).rankingCarta() < cartasJugadas.get(0).rankingCarta())
              return tirarPeorCarta();
      }
      
      return tirarMejorCarta(); // Tira la ultima carta que queda
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

      if(calcularEnvido()<=23){               // Menos de 23 de envido, nada
        return 0;
      }

      if(estado==0 && calcularEnvido()>25)    // Más de 25 de envido y no se cantó, obliga a cantar
        obligado = 1;

      if(calcularEnvido()>30){                 // Más de 30 de envido, canta
        if(estado==4)
          return 4;
        return obligado + random.nextInt(4-obligado);
      }

      if(calcularEnvido()>27){
        if(estado==3)
          desicion = 3;
        else if(estado<4)
          desicion = obligado + random.nextInt(3-obligado);
        else
          desicion = 0;
      }

      else if(calcularEnvido()>25){
        if(estado<3) {
          if (estado==2)
            desicion = 2;
          else
            desicion = obligado + random.nextInt(2 - obligado);
        } else
          desicion = 0;
      }

      else if(calcularEnvido()>23){
        if(estado==1)
          desicion = 1;
        else if(estado<2)
          desicion = estado + random.nextInt(1-estado);
        else
          desicion = 0;
      }

      if(desicion==2 && estado!=1){
        if(calcularEnvido()>27)
          return 3;
        else {
          if(estado==2)
            return 2;
          return 1;
        }
      }

      if(desicion==0 && estado>=3 && calcularEnvido()>26 && random.nextInt(2)==2) //Agrega una opcion para arriesgar
        return estado;

      if(desicion==0 && estado==0 && esMano==false && random.nextInt(4) == 3)
        return 1;

      return desicion;
      /*
      if(calcularEnvido()<=23){               // Menos de 23 de envido, nada
        return 0;
      }

      if(calcularEnvido()>30)                 // Más de 30 de envido, canta
        desicion = random.nextInt(obligado,4);

      if(estado==0 && calcularEnvido()>25)    // Más de 25 de envido y no se cantó, obliga a cantar
        obligado = 1;

      if(calcularEnvido()>=27 && estado<=3)   // Más o igual 27 de envido y menor que Real, puede cantar hasta real
        desicion = random.nextInt(obligado,3);

      if(calcularEnvido()>23 && estado<2){    // Entre 23 y 27 de envido solo puede cantar hasta envido-envido o envido
        if(random.nextInt(4)==3)              // Probabilidad de 1/4 que retruque a envido envido
          desicion = random.nextInt(estado,2);
        else
          desicion = estado;
      }

      if(random.nextInt(8) == 7) { //El 7 es un numero de ejemplo para la probabilidad de 1/8
          // if(random.nextInt(4) == 3 && estado < 4) // El 3 representa un probabilidad entre 1/4 para retrucar sin nada
            desicion = estado + 1;
          // desicion = estado; // Acepta sin nada
      }

      return desicion;
      */

      /*
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
      */
  }

  public int desidirTruco () {
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
      if(random.nextInt(5) == 3) // El 3 representa una probabilidad de 1/5 para no jugar
          return 0;
      return num;
  }
}
