package truco_java;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Servidor extends Conexion{

    public Servidor() throws IOException{
        super("servidor");
    }

    public String recibirMensaje(){
        try {
            System.out.println("Esperando la conexión...");

            cs = ss.accept();
            System.out.println("Cliente en línea");

            salidaCliente = new DataOutputStream(cs.getOutputStream());
            salidaCliente.writeUTF("Petición recibida y aceptada");

            BufferedReader entrada = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            String mensaje="";

            while((mensajeServidor = entrada.readLine()) != null) {
                mensaje=mensajeServidor;
            }

            System.out.println("Fin de la conexión");

            ss.close();
            return mensaje;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

}
