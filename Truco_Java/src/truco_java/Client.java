package truco_java;

import java.io.IOException;
import java.net.Socket;

public class Client extends Connection {

    public Client(String ip, int port) throws IOException{
        super(ip, port);
        cs = new Socket(HOST, PORT);
    }

    protected void reconnect() throws IOException {
            cs = new Socket(HOST, PORT);
    }

    public String sendIrAlMazo () throws IOException {
        sendMessage("deck");
        return receiveMessage();
    }

    public void throwCard(int posCard) throws IOException{
        sendMessage("throw " + String.valueOf(posCard));
    }
}
