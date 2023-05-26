package truco_java;

import java.io.IOException;

public class Cliente extends Conexion {

    public Cliente(String ip, int port) throws IOException{
        super("client", ip, port);
    }

    public String sendIrAlMazo () throws IOException {
        sendMessage("mazo");
        return receiveMessage();
    }

    public void throwCard(int posCard) throws IOException{
        sendMessage("tira " + String.valueOf(posCard));
    }
}
