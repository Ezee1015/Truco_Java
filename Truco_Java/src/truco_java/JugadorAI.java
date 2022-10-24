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
import javax.swing.JOptionPane;

public class JugadorAI extends Jugador {
    private int envidoJugadorCantado = -1;
    private static final Music efectos = new Music();

    public JugadorAI(ArrayList<Carta> mano, boolean esMano) {
        super(mano, esMano);
    }


  public Carta jugarTurno(Persona p, InterfazJuego mesa) throws IOException{
      if(p.cartasJugadas.isEmpty() && p.getCartasJugadas().isEmpty()) // si es la primera vez que tiro y el jugador no tiró
          return tirarCartaRandom();

      if(p.getCartasJugadas().size()-1 == cartasJugadas.size()) { // Si el jugado ya tiro y me toca a mi
        for(int i=0;i<mano.size();i++) { // Recorre desde la peor a la mejor carta
                                         // Si hay una carta que empata la que ya tiro, y soy ganador/empate la primera mano. Tirarla
          if(p.getCartasJugadas().size()>=2 && p.getCartasJugadas().get(p.getCartasJugadas().size()-1).rankingCarta() == mano.get(i).rankingCarta() && p.getCartasJugadas().get(0).rankingCarta() <= mano.get(0).rankingCarta() )
            return tirarCartaPos(i);
          // Buscar la carta de menor rango que le gane
          if(p.getCartasJugadas().get(p.getCartasJugadas().size()-1).rankingCarta() < mano.get(i).rankingCarta())
            return tirarCartaPos(i);
        }
        // Si no le gano con ninguna de las carta que tenía, tirar la peor
        return tirarPeorCarta();
      }


      if(cartasJugadas.size()>=1){
        if(p.getCartasJugadas().get(0).rankingCarta() == cartasJugadas.get(0).rankingCarta())
          return tirarMejorCarta();
        if(p.getCartasJugadas().get(0).rankingCarta() > cartasJugadas.get(0).rankingCarta()){
          for(int i=0;i<mano.size();i++) { // Recorre desde la peor a la mejor carta
            // Si hay una carta que empata la que ya tiro, y soy ganador/empate la primera mano. Tirarla
            if(p.getCartasJugadas().size()>=2 && p.getCartasJugadas().get(p.getCartasJugadas().size()-1).rankingCarta() == mano.get(i).rankingCarta() && p.getCartasJugadas().get(0).rankingCarta() <= mano.get(0).rankingCarta() )
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

  public int desidirEnvido(int estado, Persona p, Truco_Java menu){
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
        if(random.nextInt(8) == 7 && !menu.facil.isSelected()) //El 7 es un numero de ejemplo para la probabilidad de 1/8
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

      // Si se canta envido y el jugador le falta un punto para ganar, aceptar si o si.
      if(estado>0 && p.getPuntaje()==14){
        if(estado<3)
          desicion = estado+1;
        else
          desicion = estado;
      }

      if(desicion==2 && estado!=1){
        if(calcularEnvido()>27)
          return 3;
        else {
          if(estado<2) return 1;
          else return 0;
        }
      }

      // Si retruca en menor nivel del que está la apuesta, no quiere
      if(desicion<estado && desicion!=0)
          return 0;

      if(desicion==0 && estado>=3 && calcularEnvido()>26 && random.nextInt(2)==0 && !menu.facil.isSelected()) //Agrega una opcion para arriesgar
        return estado;

      if(desicion==0 && estado==0 && esMano==false && random.nextInt(4) == 3 && !menu.facil.isSelected())
        return 1;

      return desicion;
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

  public int desidirTruco (int estado, Persona p, Truco_Java menu) {
      /*
      Estados:
        0 --> No quiero / No se cantó
        1 --> Truco
        2 --> Retruco
        3 --> Vale 4
      *** Si devuelve el mismo numero que se le paso a la funcion, se le interpreta como un quiero
      */

  if(menu.facil.isSelected())
    return desidirTrucoFacil(estado, p);

  Random random = new Random();

  int cantCartasTiradas = p.getCartasJugadas().size() + cartasJugadas.size();

  // Si se canta truco y el jugador le falta un punto para ganar, aceptar si o si o retrucar hasta donde se pueda.
  if(estado>0 && p.getPuntaje()==14){
    if(estado!=3)
      return estado+1;
    else
      return estado;
  }

  switch(cantCartasTiradas){
    case 0:
    case 1: // Primera mano
      if(estado == 0)            // Si no se canto nada..
        return 0;                // .. No cantar.
      else {                     // Pero si me cantaron..
        if(cantBuenasCartas()>=1) {// .. Si tengo más de una buena carta
          if(cantMedianasCartas()>=1) // Si también tengo una mediana, retruca
            return estado+1;
          if(random.nextInt(2)==1) // Si solamente tengo una carta buena, una en dos de aceptar
            return estado;
        }
      }
      break;
    case 4:
    case 5: // Tercera Mano
            // Calcula según el envido del jugador qué carta le queda
      int jugarEnv = calcularGanoSegunEnvidoCantado(p.getCartasJugadas());
      switch(jugarEnv){
        case 0: break;
        case 1:
                if(estado==3)
                  return 3;
                return estado+1;
        case 2:
                if(random.nextInt(3)==1){
                  if(estado==3)
                    return 3;
                  return estado+1;
                }
                break;
        case 3:
                return 0;
      }
      // Si esta en la ultima mano y solo falta tirar la AI...
      if(p.getCartasJugadas().size()-1 == cartasJugadas.size()){
        if(mano.get(0).rankingCarta() > p.getCartasJugadas().get(2).rankingCarta()) // Si le gano, canto
          return estado+random.nextInt(4-estado);
        // Si le empata, pero gano la primera
        else if(mano.get(0).rankingCarta() > p.getCartasJugadas().get(2).rankingCarta() && p.getCartasJugadas().get(0).rankingCarta() == mano.get(0).rankingCarta())
          return estado+random.nextInt(4-estado);
        else if(random.nextInt(3)==1 && estado!=3 && p.getCartasJugadas().get(2).rankingCarta()<8) // Si pierdo: Random, retruca si no estoy en vale 4 y la carta que tiro es menor que un ancho falso
          return estado+random.nextInt(3-estado)+1;
      } else if(cartasJugadas.size() == 3 && cartasJugadas.get(2).rankingCarta()>9) { // Si queda una buena carta
        if(estado == 3)
          return 3;
        return estado+random.nextInt(3-estado);      // Apostar todo
      } else if(random.nextInt(4)==2)
        return estado;
      // Si esta en la ultima mano y solo falta tirar la Persona...
      if(getCartasJugadas().size()-1 == p.getCartasJugadas().size()){
        if(getCartasJugadas().get(2).rankingCarta() > 6){ // Si es buena, canto
            if(estado == 3)
              return 3;
            return estado+random.nextInt(3-estado);      // Apostar todo
        }
        if(getCartasJugadas().get(2).rankingCarta() > 1 && getCartasJugadas().get(2).rankingCarta() < 6 && random.nextInt(3)==1)
            return estado+random.nextInt(3-estado);      // Apostar todo
      }
      break;
    case 2:
    case 3: // Segunda mano
      if(cantBuenasCartas()==1 && cantMedianasCartas()==1){  // Si tengo mas una buena carta y una media
        if(estado==3)
          return 3;
        return estado+random.nextInt(3-estado);
      }

      if(cantBuenasCartas()>1){  // Si tengo mas de una buena carta apostar todo
        if(estado==3)
          return 3;
        return estado+random.nextInt(3-estado);
      }

      if(cantBuenasCartas()==1 && estado!=0)  // Si tengo una buena carta, aceptar
        return estado;

      // Si empaté la anterior...
      if(p.getCartasJugadas().get(p.getCartasJugadas().size()-1).rankingCarta() == cartasJugadas.get(cartasJugadas.size()-1).rankingCarta()){
        if(cantBuenasCartas()>=1){
          if(estado==3)
            return 3;
          return estado+random.nextInt(3-estado);
        }
        if(cantMedianasCartas()>=1 && random.nextInt(2)==1)
          return estado;
      }
      // Si ya tiré pero es el turno del jugador, y en la mano no me queda una buena carta, porque ya la tiré
      if(p.getCartasJugadas().size()+1==cartasJugadas.size()){
        if(cartasJugadas.get(cartasJugadas.size()-1).rankingCarta()>7) {
          if(estado==3)
            return 3;
          return estado+random.nextInt(3-estado);
        }
        // Si la que tiré no era una carta buena sino una carta mediana
        if(cartasJugadas.get(cartasJugadas.size()-1).rankingCarta()>5) {
          if(estado==3)
            return 3;
          return estado+random.nextInt(3-estado);
        }
      }
      // Si tengo más de una carta mediana, y el estado es menos de retruco, de manera random aceptar
      if(cantMedianasCartas()>1 && estado<=2 && random.nextInt(3)==2)
        return estado;
      break;
  }

    return 0;
  }

  public int desidirTrucoFacil (int estado, Persona p) {
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
      // Si esta en la ultima mano y solo falta tirar la AI...
      if(p.getCartasJugadas().size()-1 == cartasJugadas.size()){
          if(mano.get(0).rankingCarta() > p.getCartasJugadas().get(2).rankingCarta()) // Si le gano, canto
              return estado+random.nextInt(4-estado);
          // Si le empata, pero gano la primera
          else if(mano.get(0).rankingCarta() > p.getCartasJugadas().get(2).rankingCarta() && p.getCartasJugadas().get(0).rankingCarta() == mano.get(0).rankingCarta())
              return estado+random.nextInt(4-estado);
      } else if(cartasJugadas.size() == 3 && cartasJugadas.get(2).rankingCarta()>9) { // Si queda una buena carta
        if(estado == 3)
          return 3;
        return estado+random.nextInt(3-estado);      // Apostar todo
      }
      break;
    case 2:
    case 3: // Segunda mano
      if(cantBuenasCartas()>1){  // Si tengo mas de una buena carta apostar todo
        if(estado==3)
          return 3;
        return estado+random.nextInt(3-estado);
      }
      // Si empaté la anterior...
        if(p.getCartasJugadas().get(p.getCartasJugadas().size()-1).rankingCarta() == cartasJugadas.get(cartasJugadas.size()-1).rankingCarta()){
          if(cantBuenasCartas()>=1){
            if(estado==3)
              return 3;
            return estado+random.nextInt(3-estado);
          }
        }
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

    // SOLO LLAMAR A ESTA FUNCION CUANDO EL JUGADOR Y LA AI TIRARON SOLAMENTE 2 CARTAS
    private int calcularGanoSegunEnvidoCantado(ArrayList<Carta> cartasJugadasJugador) { // Calcula si gano segun lo que canto de envido (que puedo deducir la carta)
      if((cartasJugadasJugador.size()!=2 && cartasJugadas.size()>=2) || envidoJugadorCantado==-1) // Solamente juega si es la ultima ronda y si se jugó el envido
        return 0;
      /*
        Devuelve:
        0 --> No sé
        1 --> Le gano
        2 --> Hay posibilidades que le gane
        3 --> No le gano
        */
      ArrayList<String> palos = new ArrayList<>(); // Variable que se usa despues en el codigo para almacenar los palos tirados
      for(int i=0;i<2;i++)
        palos.add(cartasJugadasJugador.get(i).getPalo());
      ArrayList<Integer> numeros = new ArrayList<>(); // Variable que se usa despues en el codigo para almacenar los numeros tirados
      for(int i=0;i<2;i++)
        numeros.add(cartasJugadasJugador.get(i).getNumero());

      Carta ultimaCarta;
      if(cartasJugadas.size()==2) // Si tiro 2 cartas, toma la que tiene en la mano
        ultimaCarta=mano.get(0);
      else // Si tiro las tres cartas, toma la última que tiró
        ultimaCarta=cartasJugadas.get(2);


      if(envidoJugadorCantado==0){
        if(ultimaCarta.rankingCarta()<4) // Si la carta que tengo es menor de 10
          return 3;
        if(ultimaCarta.rankingCarta()>6) // Si la carta que tengo es mayor de 12
          return 1;
        return 2; // Si la carta que está entre 10 y 12, hay probabilidades que le gane
      }


      if(envidoJugadorCantado==1){
        if(numeros.contains(1)) // Si ya tiró esa carta
          return 0;
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
        if(numeros.contains(2) || numeros.contains(3)) // Si ya tiró esa carta
          return 0;
        if(ultimaCarta.rankingCarta()>9) // Si tengo mas de un 3
          return 1;
        if(ultimaCarta.rankingCarta()>7) // Si tengo un 2 o un 3
          return 2;
        return 3;
      }

      if(envidoJugadorCantado<7){ // Si tiene un 4,5 o 6
        if(numeros.contains(envidoJugadorCantado)) // Si ya tiró esa carta
          return 0;

        if(ultimaCarta.rankingCarta() > new Carta(envidoJugadorCantado, "basto").rankingCarta()) // Si le gano al numero del envido (el palo "basto" que puse es aleatorio)
          return 1;
      }

      if(envidoJugadorCantado==7) { // Si tiene un siete
        if(numeros.contains(7)) // Si ya tiró esa carta
          return 0;
        if(palos.contains("basto") || palos.contains("copa")){
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

      //       if(envidoJugadorCantado<23){
      //           if(numeros.contains(1) && numeros.contains(2))
      //                 if(palos.get(0).equals(palos.get(1))) // Y si son del mismo palo
      //                     return 0;

      //           // Si ya tiro el ancho de espada o de basto, le queda un dos del otro palo
      //           if((palos.contains("espada") || palos.contains("basto")) && numeros.contains(1))
      //               if(ultimaCarta.rankingCarta()>=8)
      //                   return 1;

      //           if(ultimaCarta.rankingCarta()<7) // Si la carta que tengo es menor de ancho falso
      //               return 3;
      //           if(ultimaCarta.rankingCarta()>7) // Si la carta que tengo es mayor de ancho falso
      //               return 1;
      //           return 2; // Si la carta es un ancho falso
      //       }

      // Para cuando es más de 20
      if(envidoJugadorCantado>20){
        ArrayList<Carta> posibilidades = new ArrayList<>();
        //Busca las posibilidades de las cartas que puede tener
        for (int i = 0; i < 2; i++) {
          int numPosibili;
          if(numeros.get(i)<10) // Si es del 1 al 7 lo cuenta normal
            numPosibili = envidoJugadorCantado - numeros.get(i) - 20;
          else // Si es un 10,11,12 lo cuenta como envido 0
            numPosibili = envidoJugadorCantado - 20;
          if(numPosibili==0){ //Si le queda un rey
            posibilidades.add(new Carta(10, palos.get(i)));
            posibilidades.add(new Carta(11, palos.get(i)));
            posibilidades.add(new Carta(12, palos.get(i)));
          }
          if (numPosibili>0 && numPosibili<8) // Si el le queda un numero de carta
            posibilidades.add(new Carta(numPosibili, palos.get(i)));
        }

        // Eliminar de las posibilidades de la carta que le queda al contrincante las cartas que yo pudiese tener o haber tirado
        for (int i = 0; i < posibilidades.size(); i++) {
          for (int x = 0; x < mano.size(); x++)
            if(posibilidades.get(i).equals(mano.get(x)))
              posibilidades.remove(i);
          for (int x = 0; x < cartasJugadas.size(); x++)
            if(posibilidades.get(i).equals(cartasJugadas.get(x)))
              posibilidades.remove(i);
          for (int x = 0; x < cartasJugadasJugador.size(); x++)
            if(posibilidades.get(i).equals(cartasJugadasJugador.get(x)))
              posibilidades.remove(i);
        }

        // si las dos cartas que tiró conforman el nivel de envido que tiene
        // significa que las cartas que tiró fueron del envido, por lo tanto
        // devuelve un 0, o sea, que no sabe
          if(cartasJugadasJugador.size() == 2){
            Persona temp = new Persona(null,false);
            temp.setMano(cartasJugadasJugador);
            if(temp.calcularEnvido() == envidoJugadorCantado)
              return 0;
          }

        // Recorre las posibilidades en busqueda de 10,11,12 y convertir lo que encuentre en su carta mayor.
        // Si hay 2 o más de 10s (10, 11, 12) en el array de posibilidades, convierte las 2 o 3 que haya en la carta mayor. Ejemplo si hay 10 y 12, se convierte en un 12, si hay un 10 y 11, se convierte en un 11. Esto para que cuando calcule las posibilidades, no tome en cuenta 3 cartas del mismo tipo ( o sea reyes). Me paso que tocó en las posibilidades: 10,11,12,3. Pero no le ganaba al 3 pero si a los reyes, así que apostó con todo. Con esta modificación hubiese tenido un 50% en vez de un 75%.
        // Ej: 10,11,12 --> 12
        //     11,12    -->12
        //     11, 10   --> 11
        for(int i=0; i<posibilidades.size()-1; i++){
          if(posibilidades.get(i).getPalo().equals(posibilidades.get(i+1).getPalo())) // Si las cartas son del mismo palo
            if(posibilidades.get(i).getNumero() >= 10 && posibilidades.get(i).getNumero() <= 12 ) // Si la carta 1 está entre 10,11,12
              if(posibilidades.get(i+1).getNumero() >= 10 && posibilidades.get(i+1).getNumero() <= 12 ){ // Si la carta 2 está entre 10,11,12
                if(posibilidades.get(i).rankingCarta() > posibilidades.get(i+1).rankingCarta())
                  posibilidades.remove(i+1);
                else
                  posibilidades.remove(i);
                i--;
              }
        }

        //Busca a cuantas cartas les gano de las probabilidades
        int cantCartasQueLeGano=0;
        for (int i = 0; i < posibilidades.size(); i++)
          if(ultimaCarta.rankingCarta() > posibilidades.get(i).rankingCarta())
            cantCartasQueLeGano++;

        // Hace un calculo para ver si conviene o no jugar
        double probabiliGanar;
        try {
          probabiliGanar = ((double)cantCartasQueLeGano / (double)posibilidades.size()) * 100.0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error en la Inteligencia de la PC: " + e.getMessage());
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            return 0;
        }
        if (probabiliGanar>80)
          return 1;
        if (probabiliGanar>40)
          return 2;
        return 3;
      }
      return 0;
  }
}
