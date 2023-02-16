package truco_java;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexion
{
    private int PUERTO = 1234;
    private String HOST = "localhost";
    protected String mensajeServidor;
    protected ServerSocket ss;
    protected Socket cs;
    protected DataOutputStream salidaServidor, salidaCliente;
    String tipo;

    public Conexion(String tipo, String ip, int puerto) throws IOException {
        HOST = ip;
        PUERTO = puerto;
        this.tipo=tipo;
        if(tipo.equalsIgnoreCase("servidor")) {
            ss = new ServerSocket(PUERTO);
            cs = new Socket();
        } else {
            cs = new Socket(HOST, PUERTO);
        }
    }

    protected void reconectar() throws IOException {
        if(tipo.equalsIgnoreCase("servidor")) {
            ss = new ServerSocket(PUERTO);
            cs = new Socket();
        } else {
            cs = new Socket(HOST, PUERTO);
        }
    }

    public void killServer(){
        if(!tipo.equals("servidor"))
            return;
        try {
            ss.close();
            cs.close();
        } catch (Exception e) {
            System.out.println("nooooo");
        }
        ss = null;
        cs = null;
    }
}
