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
        System.out.println("cliente");
    }

    public String recibirMensaje() throws IOException{
        if(entrada==null)
            entrada  = new BufferedReader(new InputStreamReader(cs.getInputStream()));

        try {
            String mensaje="";

            System.out.println("Comienza la escucha");
            while(!entrada.ready());
            System.out.println("Conexion");
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
            System.out.println(e.getMessage());
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

    public void actualizarInfo(int cantCartasJugador, ArrayList<Carta> cartasJugadasJugador,ArrayList<Carta> manoOponente, int[] posManoOponente, ArrayList<Carta> jugadasOponente, int nivelTruco, boolean envidoFinalizado, int habilitadoARetrucar, boolean turnoOponente, int puntajeJugador, int puntajeOponente) throws IOException{
        // Envia la peticion
        String mensaje = "update " + String.valueOf(cantCartasJugador)+" ";
        for(int i=0;i<3-cantCartasJugador;i++){
            mensaje+=cartasJugadasJugador.get(i).getNumero()+" ";
            mensaje+=cartasJugadasJugador.get(i).getPalo()+" ";
        }

        mensaje+=manoOponente.size()+" ";
        for(int i=0;i<manoOponente.size();i++){
            mensaje+=manoOponente.get(i).getNumero()+" ";
            mensaje+=manoOponente.get(i).getPalo()+" ";
        }
        for(int i=0;i<3;i++)
            mensaje+=posManoOponente[i]+" ";
        for(int i=0;i<jugadasOponente.size();i++){
            mensaje+=jugadasOponente.get(i).getNumero()+" ";
            mensaje+=jugadasOponente.get(i).getPalo()+" ";
        }

        mensaje+=String.valueOf(nivelTruco)+" "+String.valueOf(envidoFinalizado)+" "+String.valueOf(habilitadoARetrucar)+" "+turnoOponente + " " + puntajeJugador + " " + puntajeOponente;

        enviaMensaje(mensaje);
    }

    public void enviaKill() throws IOException{
            enviaMensaje("kill");
    }

    public String enviaTruco(int nivelTruco, int habilitadoARetrucar) throws IOException{
        enviaMensaje("truco " + nivelTruco + " " + habilitadoARetrucar);
        return recibirMensaje();
    }

    public void enviaPuntaje(int puntajeJugador, int puntajeOponente) throws IOException{
        enviaMensaje("puntaje " + puntajeJugador + " " + puntajeOponente);
    }

    public String enviaPersona(int numero, String nombre) throws IOException{
        enviaMensaje("persona " + numero + " " + nombre);
        return recibirMensaje();
    }
}
