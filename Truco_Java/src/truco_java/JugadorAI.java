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
    private int envidoJugadorCantado = 0;

    public JugadorAI(ArrayList<Carta> mano, boolean esMano) {
        super(mano, esMano);
    }


  public Carta jugarTurno(Persona p, InterfazJuego mesa) throws IOException{
    System.out.println("llama a tirar carta");
      if(p.cartasJugadas.isEmpty()) // si es la primera vez que tiro
          return tirarCartaRandom();

      if(p.getCartasJugadas().size()-1 == cartasJugadas.size()) { // Si el jugado ya tiro y me toca a mi, intento ganar
          for(int i=0;i<mano.size();i++) { // Recorre desde la peor a la mejor carta
                // Si hay una carta que empata la que ya tiro, y soy ganador de la primera mano. Tirarla
                if(p.getCartasJugadas().get(p.getCartasJugadas().size()-1).rankingCarta() == mano.get(i).rankingCarta() && p.getCartasJugadas().get(0).rankingCarta() == mano.get(0).rankingCarta() )
                    return tirarCartaPos(i);
                // Buscar la carta de menor rango que le gane
                if(p.getCartasJugadas().get(p.getCartasJugadas().size()-1).rankingCarta() < mano.get(i).rankingCarta())
                    return tirarCartaPos(i);
          }
          return tirarPeorCarta();
        }


      else if(cartasJugadas.size()>=1){
          if(p.getCartasJugadas().get(0).rankingCarta() == cartasJugadas.get(0).rankingCarta())
              return tirarMejorCarta();
          if(p.getCartasJugadas().get(0).rankingCarta() > cartasJugadas.get(0).rankingCarta()){
            for(int i=mano.size()-1;i>=0;i--) { // Recorre desde la peor a la mejor carta
                // Si hay una carta que empata la que ya tiro, y soy ganador de la primera mano. Tirarla
                if(p.getCartasJugadas().get(p.getCartasJugadas().size()-1).rankingCarta() == mano.get(i).rankingCarta() && p.getCartasJugadas().get(0).rankingCarta() == mano.get(0).rankingCarta() )
                    return tirarCartaPos(i);
                // Buscar la carta de menor rango que le gane
                if(p.getCartasJugadas().get(p.getCartasJugadas().size()-1).rankingCarta() < mano.get(i).rankingCarta())
                    return tirarCartaPos(i);
            }
              return tirarMejorCarta();
          }
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

  public Carta tirarCartaPos (int pos){
    Carta aTirar = mano.get(pos);
    mano.remove(pos);
    cartasJugadas.add(aTirar);
    return aTirar;
  }

  public Carta tirarMejorCarta (){
    if(mano.isEmpty()) return null;

    int mejor = 0;
    for(int i=1; i<mano.size(); i++){
      if(mano.get(mejor).rankingCarta() < mano.get(i).rankingCarta())
        mejor = i;
    }

    Carta aTirar = mano.get(mejor);
    mano.remove(mejor);
    cartasJugadas.add(aTirar);
    return aTirar;
  }

  public Carta tirarPeorCarta (){
    if(mano.isEmpty()) return null;

    int peor = 0;
    for(int i=1; i<mano.size(); i++){
      if(mano.get(peor).rankingCarta() > mano.get(i).rankingCarta())
        peor = i;
    }

    Carta aTirar = mano.get(peor);
    mano.remove(peor);
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
        if(random.nextInt(8) == 7)            //El 7 es un numero de ejemplo para la probabilidad de 1/8
            desicion = estado + 1;
        return desicion;
      }

      if(estado==0 && calcularEnvido()>25)   // Más de 25 de envido y no se cantó, obliga a cantar
        obligado = 1;

      if(calcularEnvido()>30){                 // Más de 30 de envido, canta
        if(estado==4)
          return 4;
        desicion = obligado + random.nextInt(4-obligado);
      }

      else if(calcularEnvido()>27){
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
          switch (estado) {
              case 1:
                  desicion = 1;
                  break;
              case 0:
                  desicion = random.nextInt(1-estado);
                  break;
              default:
                  desicion = 0;
                  break;
          }
      }

      if(desicion==2 && estado!=1){
        if(calcularEnvido()>27)
          return 3;
        else {
          if(estado<2) return 1;
          else return 0;
        }
      }

      if(desicion<estado && desicion!=0) // Si retruca en menor nivel del que está la apuesta, no quiere
          return 0;

      if(desicion==0 && estado>=3 && calcularEnvido()>26 && random.nextInt(2)==0) //Agrega una opcion para arriesgar
        return estado;

      if(desicion==0 && estado==0 && esMano==false && random.nextInt(4) == 3)
        return 1;

      return desicion;

      /* FUNCION 1:
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

      /* FUNCION 2:
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

  private int cantBuenasCartas () {
    int cant=0;

    for (int i = 0; i < mano.size(); i++)
      if(mano.get(i).rankingCarta()>7)
        cant++;

    return cant;
  }

  private int cantMedianasCartas () {
    int cant=0;

    for (int i = 0; i < mano.size(); i++)
      if(mano.get(i).rankingCarta()>4 && mano.get(i).rankingCarta()<8)
        cant++;

    return cant;
  }

  public int desidirTruco (int estado, Persona p) {
      /*
      Estados:
        0 --> No quiero / No se cantó
        1 --> Truco
        2 --> Retruco
        3 --> Vale 4
      *** Si devuelve el mismo numero que se le paso a la funcion, se le interpreta como un quiero
      */
    Random random = new Random();

  int cantCartasTiradas = p.getCartasJugadas().size() + cartasJugadas.size();

  switch(cantCartasTiradas){
    case 0:
    case 1: // Primera mano
      if(estado == 0)            // Si no se canto nada..
        return 0;                // .. No cantar.
      else {                     // Pero si me cantaron..
        if(cantBuenasCartas()>=1 && cantMedianasCartas()>=1) // .. Solo aceptar si tengo más de una buena carta y una media
          return estado;
      }
      break;
    case 4:
    case 5: // Tercera Mano
      // Si esta en la ultima mano y solo falta tirar...
      if(p.getCartasJugadas().size()-1 == cartasJugadas.size()){ //ELIMINADO:  && p.getCartasJugadas().size() == 3
          if(mano.get(0).rankingCarta() > p.getCartasJugadas().get(2).rankingCarta()) // Si le gano, canto
              return estado+random.nextInt(4-estado);
          // Si le empata, pero gano la primera
          else if(mano.get(0).rankingCarta() > p.getCartasJugadas().get(2).rankingCarta() && p.getCartasJugadas().get(0).rankingCarta() == mano.get(0).rankingCarta())
              return estado+random.nextInt(4-estado);
          else if(random.nextInt(4)==3 && estado!=3) // Si pierdo: Random, retruca si no estoy en vale 4
              return estado+random.nextInt(3-estado)+1;
      } else if(cartasJugadas.size() == 3 && cartasJugadas.get(2).rankingCarta()>9) { // Si queda una buena carta
        if(estado == 3)
          return 3;
        return estado+random.nextInt(3-estado);      // Apostar todo
      } else if(random.nextInt(4)==2)
        return estado;
      break;
    case 2:
    case 3: // Segunda mano
      if(cantBuenasCartas()>1)  // Si tengo mas de una buena carta apostar todo
        return estado+random.nextInt(3-estado);
      // Si empaté la anterior...
        if(p.getCartasJugadas().get(p.getCartasJugadas().size()-1).rankingCarta() == cartasJugadas.get(cartasJugadas.size()-1).rankingCarta()){
          if(cantBuenasCartas()>=1)
            return estado+random.nextInt(3-estado);
          if(cantMedianasCartas()>=1 && random.nextInt(2)==1)
            return estado;
        }
      if(cantMedianasCartas()>1 && estado<=2 && random.nextInt(3)==2) // Si tengo más de una carta mediana, y el estado es menos de retruco, de manera random aceptar
        return estado;
      break;
  }

    return 0;
  }

    public int getEnvidoJugadorCantado() {
        return envidoJugadorCantado;
    }

    public void setEnvidoJugadorCantado(int envidoJugadorCantado) {
        this.envidoJugadorCantado = envidoJugadorCantado;
    }
  
  private int calcularGanoSegunEnvidoCantado(ArrayList<Carta> cartasJugadasJugador) { // Calcula si gano segun lo que canto de envido (que puedo deducir la carta)
      /*
      Devuelve:
        0 --> No sé
        1 --> Le gano
        2 --> Hay posibilidades que le gane
        3 --> No le gano
      */
      System.out.println("AAAAAAAAH. Envido que se cantó: " + envidoJugadorCantado);
      ArrayList<String> palos = new ArrayList<>(); // Variable que se usa despues en el codigo para almacenar los palos tirados
        for(int i=0;i<2;i++)
            palos.add(cartasJugadasJugador.get(i).getPalo());
      ArrayList<Integer> numeros = new ArrayList<>(); // Variable que se usa despues en el codigo para almacenar los numeros tirados
        for(int i=0;i<2;i++)
            numeros.add(cartasJugadasJugador.get(i).getNumero());
      
      if(cartasJugadasJugador.size()!=3) // Si no está en la ultima ronda
          return -1;// Devuelve nada
      
      Carta ultimaCarta;
      if(mano.size()==1)
          ultimaCarta=mano.get(0);
      else
          ultimaCarta=cartasJugadas.get(2);
      
      
      if(envidoJugadorCantado==0){
          if(ultimaCarta.rankingCarta()<4) // Si la carta que tengo es menor de 10
              return 3;
          if(ultimaCarta.rankingCarta()>6) // Si la carta que tengo es mayor de 12
              return 1;
          return 2; // Si la carta que está entre 10 y 12, hay probabilidades que le gane
      }
      
      
      if(envidoJugadorCantado==1){
          if(palos.contains("espada") || palos.contains("basto")){
              if(ultimaCarta.rankingCarta()==13)
                  return 1;
              return 3;
          }
            if(ultimaCarta.rankingCarta()>7)
                    return 1;
            if(ultimaCarta.rankingCarta()==7)
                    return 2;
            return 3;
      }
      
      if(envidoJugadorCantado<4) { // Si tiene un dos o un tres
          if(ultimaCarta.rankingCarta()>9) // Si tengo mas de un 3
                  return 1;
          if(ultimaCarta.rankingCarta()>7) // Si tengo un 2 o un 3
                  return 2;
          return 3;
      }
      
      if(envidoJugadorCantado==7) { // Si tiene un siete
          if(palos.contains("espada") || palos.contains("oro")){
            if(ultimaCarta.rankingCarta()>10) // Si tengo un 7 de espada en adelante
                    return 1;
            return 3;
          }
          if(ultimaCarta.rankingCarta()>3) // Si tengo mas de un 7 de copas o de basto
                  return 1;
          return 3;
      }
      
      if(envidoJugadorCantado==20){
          if(numeros.get(0) >= 10 && numeros.get(0) <= 12) // Si en la primer tiro un rey...
            if(numeros.get(1) >= 10 && numeros.get(1) <= 12) // Y en la Segunda tiro otro rey...
                if(palos.get(0).equals(palos.get(1))) // Y si son del mismo palo
                    return 0; // Significa que ya los tiro, por lo tanto no sé
          if(ultimaCarta.rankingCarta()<4) // Si la carta que tengo es menor de 10
              return 3;
          if(ultimaCarta.rankingCarta()>6) // Si la carta que tengo es mayor de 12
              return 1;
          return 2; // Si la carta que está entre 10 y 12, hay probabilidades que le gane
      }
      
      if(envidoJugadorCantado<23){
          if(numeros.contains(1) && numeros.contains(2))
                if(palos.get(0).equals(palos.get(1))) // Y si son del mismo palo
                    return 0;
          
          // Si ya tiro el ancho de espada o de basto, le queda un dos del otro palo
          if((palos.contains("espada") || palos.contains("basto")) && numeros.contains(1)) 
              if(ultimaCarta.rankingCarta()>=8)
                  return 1;
          
          if(ultimaCarta.rankingCarta()<7) // Si la carta que tengo es menor de ancho falso
              return 3;
          if(ultimaCarta.rankingCarta()>7) // Si la carta que tengo es mayor de ancho falso
              return 1;
          return 2; // Si la carta es un ancho falso
      }
      
      if(envidoJugadorCantado<28){
          
      }
      
      
  }
}
