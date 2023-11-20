package truco_java;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private int PORT = 1234;
    private String HOST = "localhost";
    protected BufferedReader input;
    protected String mensajeServidor;
    protected ServerSocket ss;
    protected Socket cs;
    protected DataOutputStream outputServer, salidaCliente;
    private String tipo;
    private char endOfCommand = '\n';

    public Connection(String tipo, String ip, int puerto) throws IOException {
        HOST = ip;
        PORT = puerto;
        this.tipo=tipo;
        if(tipo.equalsIgnoreCase("server")) {
            ss = new ServerSocket(PORT);
            cs = new Socket();
        } else {
            cs = new Socket(HOST, PORT);
        }
    }

    protected void reconnect() throws IOException {
        if(tipo.equalsIgnoreCase("server")) {
            ss = new ServerSocket(PORT);
            cs = new Socket();
        } else {
            cs = new Socket(HOST, PORT);
        }
    }

    public void killServer(){
        if(!tipo.equals("server"))
            return;
        try {
            ss.close();
            cs.close();
        } catch (Exception e) {
        }
        ss = null;
        cs = null;
    }

    public String receiveMessage() throws IOException{
        if(input==null)
            input = new BufferedReader(new InputStreamReader(cs.getInputStream()));

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
            if ( message.isEmpty() || message.isBlank() )
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
