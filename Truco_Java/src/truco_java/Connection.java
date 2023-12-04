package truco_java;

import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public abstract class Connection {
    protected int PORT = 1234;
    protected String HOST = "localhost";
    protected BufferedReader input;
    protected String mensajeServidor;
    protected ServerSocket ss;
    protected Socket cs;
    protected DataOutputStream outputServer, salidaCliente;
    private char endOfCommand = '\n';

    protected abstract void reconnect() throws IOException;

    public Connection(String ip, int puerto) {
        HOST = ip;
        PORT = puerto;
    }

    public String receiveMessage() throws IOException{
        try {
            String message="";

            while(!input.ready());
            while(input.ready()){
                char char_msg = (char) input.read();
                if( char_msg == endOfCommand )
                    break;
                if ( char_msg != 0 )
                  message += char_msg;
            }

            // Just in case. This can happen if the 'endOfCommand' gets sent twice
            // or there's an error in some communication and puts a \n in the beginning
            if ( message.isEmpty() )
              return receiveMessage();

            return message;
        } catch (Exception e) {
            if(e.getMessage().equalsIgnoreCase("Socket is closed")){
                reconnect();
                return receiveMessage();
            }
            System.out.println("Error en la conexión: " + e.getMessage());
        }
        return "";
    }

    protected void sendMessage(String message) throws IOException{
        try {
            outputServer = new DataOutputStream(cs.getOutputStream());
            outputServer.writeUTF(message + " " + endOfCommand);
        } catch (Exception e) {
            if(e.getMessage().equalsIgnoreCase("Socket is closed")){
                reconnect();
                sendMessage(message);
                return;
            }
        }
    }

    public void sendEnvido(ArrayList<Integer> envidosDeclared, int trucoLevel, int enabledToRetrucar) throws IOException{
        sendMessage("envido " + String.valueOf(envidosDeclared.get(envidosDeclared.size()-1)) + " " + trucoLevel + " " + enabledToRetrucar);
    }

    public void sendTruco(int trucoLevel, int enabledToRetrucar) throws IOException{
        sendMessage("truco " + trucoLevel + " " + enabledToRetrucar);
    }

    public void sendPerson(int number, String name) throws IOException{
        sendMessage("player " + number + " " + name);
    }

    public void sendWithdraw() throws IOException{
        sendMessage("withdraw");
    }

}
