package truco_java;

import java.io.IOException;

public class Server extends Connection{

    public Server(String ip, int port) throws IOException{
        super("server", ip, port);
        cs = ss.accept();
    }

    public void enviaKill() throws IOException{
        sendMessage("kill");
    }

    public void sendPoints(int playerPoints, int oponentPoints) throws IOException{
        sendMessage("points " + playerPoints + " " + oponentPoints);
    }

    public void updateInfo(int sizePlayerCards, SetOfCards playedPlayerCards,SetOfCards opponentCards, SetOfCards playedOpponentCards, int trucoLevel, boolean envidoEnded, int enabledToRetrucar, boolean oponentTurn, int playerPoints, int oponentPoints, boolean isPlayerFirstHand) throws IOException{

        String message = "update " + String.valueOf(sizePlayerCards)+" ";
        for(int i=0;i<3-sizePlayerCards;i++){
            message+=playedPlayerCards.getNotNullCards(i).getNumber()+" ";
            message+=playedPlayerCards.getNotNullCards(i).getStick()+" ";
        }

        for(int i=0;i<3;i++){
            Card tempCard = opponentCards.getPosAtVector(i);
            if (tempCard == null)
                message+="null null ";
            else {
                message+=tempCard.getNumber()+" ";
                message+=tempCard.getStick()+" ";
            }
        }
        for(int i=0;i<3;i++){
            Card tempCard = playedOpponentCards.getPosAtVector(i);
            if (tempCard == null)
                message+="null null ";
            else {
                message+=tempCard.getNumber()+" ";
                message+=tempCard.getStick()+" ";
            }
        }

        message+=String.valueOf(trucoLevel)+" "+String.valueOf(envidoEnded)+" "+String.valueOf(enabledToRetrucar)+" "+oponentTurn + " " + playerPoints + " " + oponentPoints + " " + isPlayerFirstHand;

        sendMessage(message);
    }

    public void printMessage(String message) throws IOException{
        sendMessage("print " + message);
    }
}
