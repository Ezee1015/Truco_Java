package truco_java;

import java.util.ArrayList;
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

    public void updateInfo(int sizePlayerCards, ArrayList<Card> playedPlayerCards,ArrayList<Card> oponentCards, int[] posOponentCards, ArrayList<Card> playedOponentCards, int trucoLevel, boolean envidoEnded, int enabledToRetrucar, boolean oponentTurn, int playerPoints, int oponentPoints, boolean isPlayerFirstHand) throws IOException{

        String message = "update " + String.valueOf(sizePlayerCards)+" ";
        for(int i=0;i<3-sizePlayerCards;i++){
            message+=playedPlayerCards.get(i).getNumber()+" ";
            message+=playedPlayerCards.get(i).getStick()+" ";
        }

        message+=oponentCards.size()+" ";
        for(int i=0;i<oponentCards.size();i++){
            message+=oponentCards.get(i).getNumber()+" ";
            message+=oponentCards.get(i).getStick()+" ";
        }
        for(int i=0;i<3;i++)
            message+=posOponentCards[i]+" ";
        for(int i=0;i<playedOponentCards.size();i++){
            message+=playedOponentCards.get(i).getNumber()+" ";
            message+=playedOponentCards.get(i).getStick()+" ";
        }

        message+=String.valueOf(trucoLevel)+" "+String.valueOf(envidoEnded)+" "+String.valueOf(enabledToRetrucar)+" "+oponentTurn + " " + playerPoints + " " + oponentPoints + " " + isPlayerFirstHand;

        sendMessage(message);
    }

    public void printMessage(String message) throws IOException{
        sendMessage("print " + message);
    }
}
