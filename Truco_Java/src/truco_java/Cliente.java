package truco_java;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Cliente extends Conexion {
    public Cliente() throws IOException{
        super("cliente");
    }

    public void conectarYEnviar(/*Datos*/){
        try {
            salidaServidor = new DataOutputStream(cs.getOutputStream());
            for (int i = 0; i < 2; i++) {
                salidaServidor.writeUTF("Este es el mensaje número " + (i+1) + "\n");
            }
            cs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String enviaEnvido(ArrayList<Integer> envidosCantados){
        // Envia la peticion
        try {
            salidaServidor = new DataOutputStream(cs.getOutputStream());
            for (int i = 0; i < 2; i++) {
                salidaServidor.writeUTF("envido " + envidosCantados.get(envidosCantados.size()-1));
            }
            cs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // Escucha la respuesta
        try {
            return new Servidor().recibirMensaje();
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
            return 0;
        }
    }

    public void enviaMensaje(String mensaje){
        // Envia la peticion
        try {
            salidaServidor = new DataOutputStream(cs.getOutputStream());
            for (int i = 0; i < 2; i++) {
                salidaServidor.writeUTF(mensaje);
            }
            cs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
