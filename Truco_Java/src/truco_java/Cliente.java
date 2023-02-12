package truco_java;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Cliente extends Conexion {
    BufferedReader entrada;

    public Cliente(String ip, int puerto) throws IOException{
        super("cliente", ip, puerto);
    }

    public String recibirMensaje() throws IOException{
        if(entrada==null)
            entrada  = new BufferedReader(new InputStreamReader(cs.getInputStream()));

        try {
            String mensaje="";

            while(!entrada.ready());
            while(entrada.ready()){
                mensaje+=(char) entrada.read();
                if(mensaje.charAt(mensaje.length()-1)=='ç'){
                    break;
                }
            }
            return mensaje;
        } catch (Exception e) {
            if(e.getMessage().equalsIgnoreCase("Socket is closed")){
                reconectar();
                return recibirMensaje();
            }
            System.out.println("Error en la clase Cliente: " + e.getMessage());
        }
        return "";
    }

    public void enviaMensaje(String mensaje) throws IOException{
        // Envia la peticion
        try {
            salidaServidor = new DataOutputStream(cs.getOutputStream());
            salidaServidor.writeUTF(mensaje + " ç");
        } catch (Exception e) {
            if(e.getMessage().equalsIgnoreCase("Socket is closed")){
                reconectar();
                enviaMensaje(mensaje);
                return;
            }
        }
    }

    public String enviaEnvido(ArrayList<Integer> envidosCantados, int nivelTruco, int habilitadoARetrucar) throws IOException{
        // Envia la peticion
        enviaMensaje("envido " + String.valueOf(envidosCantados.get(envidosCantados.size()-1)) + " " + nivelTruco + " " + habilitadoARetrucar);
        return recibirMensaje();
    }

    public String enviaTruco(int nivelTruco, int habilitadoARetrucar) throws IOException{
        enviaMensaje("truco " + nivelTruco + " " + habilitadoARetrucar);
        return recibirMensaje();
    }

    public void enviaPersona(int numero, String nombre) throws IOException{
        enviaMensaje("persona " + numero + " " + nombre);
    }

    public String enviaIrAlMazo () throws IOException {
        enviaMensaje("mazo");
        return recibirMensaje();
    }

    public void tirarCarta(int posCarta) throws IOException{
        enviaMensaje("tira " + String.valueOf(posCarta));
    }
}
