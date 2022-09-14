package truco_java;

public class Carta {
  private int numero;
  private String palo;

    public Carta(int numero, String palo) {
        this.numero = numero;
        this.palo = palo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getPalo() {
        return palo;
    }

    public void setPalo(String palo) {
        this.palo = palo;
    }

  public int rankingCarta () { //Obtiene un numero según el palo y el numero que es
    /*
     * 4 de Cualquiera --> 0
     * 1 de espada     --> 13
    */
    switch(numero){
      case 4: return 0;
      case 5: return 1;
      case 6: return 2;
      case 7:
        if(palo.equals("basto") || palo.equals("copa"))
          return 3;
        if(palo.equals("oro"))
          return 10;
        else // espada
          return 11;
      case 10: return 4;
      case 11: return 5;
      case 12: return 6;
      case 1:
        if(palo.equals("oro") || palo.equals("copa"))
          return 7;
        if(palo.equals("basto"))
          return 12;
        else // espada
          return 13;
      case 2: return 8;
      case 3: return 9;
    }
    return -1;
  }
  
  public String linkCarta(){
      return "mazo/" + numero + " de " + palo;
  }
}
