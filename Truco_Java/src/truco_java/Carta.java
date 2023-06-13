package truco_java;

public class Carta {
  private int number;
  private String stick;

    public Carta(int number, String stick) {
        this.number = number;
        this.stick = stick;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStick() {
        return stick;
    }

    public void setStick(String stick) {
        this.stick = stick;
    }

  public int rankingCard () { //Obtiene un numero según el palo y el numero que es
    /*
     * 4 de Cualquiera --> 0
     * 1 de espada     --> 13
    */
    switch(number){
      case 4: return 0;
      case 5: return 1;
      case 6: return 2;
      case 7:
        if(stick.equals("basto") || stick.equals("copa"))
          return 3;
        if(stick.equals("oro"))
          return 10;
        else // espada
          return 11;
      case 10: return 4;
      case 11: return 5;
      case 12: return 6;
      case 1:
        if(stick.equals("oro") || stick.equals("copa"))
          return 7;
        if(stick.equals("basto"))
          return 12;
        else // espada
          return 13;
      case 2: return 8;
      case 3: return 9;
    }
    return -1;
  }

  public String linkCard(){
      return "src/truco_java/mazo/" + number + " de " + stick + ".png";
  }

  public String text(){
      return number + " de " + stick;
  }
}
