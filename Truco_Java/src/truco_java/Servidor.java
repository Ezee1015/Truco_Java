package truco_java;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Servidor extends Conexion{
    public Servidor() throws IOException{
        super("servidor");
        cs = ss.accept();
        System.out.println("Cliente en línea");
    }

    public String recibirMensaje() throws IOException{
        if(ss==null)
            reconectar();

        try {
            salidaCliente = new DataOutputStream(cs.getOutputStream());
            // salidaCliente.writeUTF("Petición recibida y aceptada");

            System.out.println("atrapado aca");
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            String mensaje="";

            while(!entrada.ready());
            while(entrada.ready()){
                mensaje+=(char) entrada.read();
                if(mensaje.charAt(mensaje.length()-1)=='ç'){
                    break;
                }
            }
            // while(((mensajeServidor = entrada.readLine()) != null && mensajeServidor!="") ) {
            //     mensaje=mensajeServidor;
            //     System.out.println("AJKDHFALKJSDHFALKJSHDFLJAKSDHFLKJASHDLFKJAHSLDKFHALKSDHFLKAHDLKFJHSADLKFHALSDJKFHASLKDHFKLSAHDF");
            // }

            System.out.println("Fin de la conexión + " + mensaje);

            return mensaje;
        } catch (Exception e) {
            if(e.getMessage().equalsIgnoreCase("Socket is closed")){
                reconectar();
                return recibirMensaje();
            }
            System.out.println(e.getMessage());
        }
        return "";
    }

    public void enviaMensaje(String mensaje) throws IOException{
        try {
            salidaServidor = new DataOutputStream(cs.getOutputStream());
            System.out.println(mensaje);
            salidaServidor.writeUTF(mensaje);
        } catch (Exception e) {
            if(e.getMessage().equalsIgnoreCase("Socket is closed")){
                reconectar();
                enviaMensaje(mensaje);
                return;
            }
            System.out.println(e.getMessage());
        }
    }

    public String enviaEnvido(ArrayList<Integer> envidosCantados) throws IOException{
        // Envia la peticion
        enviaMensaje(String.valueOf(envidosCantados.get(envidosCantados.size()-1)));
        return recibirMensaje();
    }

}
