package truco_java;

import java.util.ArrayList;

public class SetOfCards {
  private Card cards[] = new Card[3];

  public SetOfCards(){
    cards[0]=null;
    cards[1]=null;
    cards[2]=null;
  }

  public void replaceAllCards(Card card1, Card card2, Card card3){
    cards[0]=card1;
    cards[1]=card2;
    cards[2]=card3;
    sort();
  }

  // Quantity of cards that are not null
  public int size(){
    int size=0;
    for(int i=0;i<3;i++)
      if(cards[i]!=null) size++;
    return size;
  }

  // Add to the last position that is free
  public void add(Card card){
    try {
      if(size()==3)
        new RuntimeException("[Error] No se pudo añadir la carta. No hay más espacio");
      if(cards[size()]!=null)
        new RuntimeException("[Error] No se pudo añadir la carta. En la posición " + size() + " ya existe una carta" );

      cards[size()]=card;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
  }

  // Add to the last position that is free
  public void add(int pos, Card card){
    try {
      if(pos>=3 || pos<0)
        new RuntimeException("[Error] No se pudo añadir la carta. No hay más espacio");
      if(cards[pos]!=null)
        new RuntimeException("[Error] No se pudo añadir la carta. En la posición " + pos + " ya existe una carta" );

      cards[pos]=card;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
  }

  // Gets the card at the position `pos`, not counting the null cards
  public Card getNotNullCards(int pos){
    try {
      if(pos>=size() || pos<0)
        new RuntimeException("[Error] No se pudo obtener la carta. Supero el tamaño del Array");

      for(int i=0;i<3;i++) {
        if(getPosAtVector(i)!=null){
          if(pos==0)
            return getPosAtVector(i);
          pos--;
        }
      }

      new RuntimeException("[Error] No se pudo obtener la carta: No se encontró");
      return null;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
      return null;
    }
  }

  // Gets the card that is in the position `pos`, and it can return null if null
  // is the card in that place
  public Card getPosAtVector(int pos){
    try {
      if(pos>=3 || pos<0)
        new RuntimeException("[Error] No se pudo obtener la carta: La posicion " + pos + " no se puede obtener");

      return cards[pos];
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
      return null;
    }
  }

  // Behaves as a vector
  public void removeAtVector(int pos){
    try {
      if(pos>=size() || pos<0)
        new RuntimeException("[Error] No se pudo eliminar la carta: Supero el tamaño del Array");

      if(getPosAtVector(pos)==null)
        new RuntimeException("[Error] No se pudo eliminar la carta: No hay carta en esa posición");
      cards[pos]=null;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
  }

  // Behaves as a list
  public void removeNotNull(int pos){
    try {
      if(pos>=size() || pos<0)
        new RuntimeException("[Error] No se pudo obtener la carta. Supero el tamaño del Array");

      for(int i=0;i<3;i++) {
        if(getPosAtVector(i)!=null){
          if(pos==0){
            cards[i]=null;
            return;
          }
          pos--;
        }
      }

      new RuntimeException("[Error] No se pudo obtener la carta: No se encontró");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
  }

  public ArrayList<Card> toArrayList(){
    ArrayList<Card> cardList = new ArrayList<>();
    for (int i=0;i<3; i++)
        if(getNotNullCards(i)!=null) cardList.add(getNotNullCards(i));
    return cardList;
  }

  public void addAll(ArrayList<Card> array){
    try {
      if(array.size()!=3)
        new RuntimeException("[Error] No se agregar las cartas: El tamaño del array no es de 3");

      replaceAllCards(null, null, null);

      for(int i=0;i<3;i++)
        cards[i]=array.get(i);

      new RuntimeException("[Error] No se pudo eliminar la carta: No se encontró");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
  }

  public boolean isEmpty(){
    return size()==0 ? true : false;
  }

  public void sort(){
    // ...
  }

}
