package truco_java;

import java.io.IOException;

public class Client extends Connection {

    public Client(String ip, int port) throws IOException{
        super("client", ip, port);
    }

    public String sendIrAlMazo () throws IOException {
        sendMessage("deck");
        return receiveMessage();
    }

    public void throwCard(int posCard) throws IOException{
        sendMessage("throw " + String.valueOf(posCard));
    }
}
