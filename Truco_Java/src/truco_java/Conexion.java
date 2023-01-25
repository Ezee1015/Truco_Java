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
            System.out.println("reconecta servidor");
            ss = new ServerSocket(PUERTO);
            cs = new Socket();
        } else {
            System.out.println("reconecta cliente");
            cs = new Socket(HOST, PUERTO);
        }
    }
}
