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

    public String recibirMensaje() throws IOException{
        // if(ss==null)
            // reconectar();

        try {
            // salidaServidor = new DataOutputStream(cs.getOutputStream());
            // salidaCliente.writeUTF("Petición recibida y aceptada");

            BufferedReader entrada = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            String mensaje="";

            System.out.println("atrapado aca " + cs.isConnected());
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
        // Envia la peticion
        try {
            salidaServidor = new DataOutputStream(cs.getOutputStream());
            System.out.println(mensaje + " ç");
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

    public void actualizarInfo(int cantCartasJugador, ArrayList<Carta> cartasJugadasJugador,ArrayList<Carta> manoOponente, int[] posManoOponente,  int nivelTruco, boolean envidoFinalizado, int habilitadoARetrucar, boolean turnoOponente, int puntajeJugador, int puntajeOponente) throws IOException{
        // Envia la peticion
        String mensaje = "update " + String.valueOf(cantCartasJugador)+" ";
        for(int i=0;i<3-cantCartasJugador;i++){
            mensaje+=cartasJugadasJugador.get(i).getNumero()+" ";
            mensaje+=cartasJugadasJugador.get(i).getPalo()+" ";
        }
        for(int i=0;i<3;i++){
            mensaje+=manoOponente.get(i).getNumero()+" ";
            mensaje+=manoOponente.get(i).getPalo()+" ";
        }
        for(int i=0;i<3;i++)
            mensaje+=posManoOponente[i]+" ";
        mensaje+=String.valueOf(nivelTruco)+" "+String.valueOf(envidoFinalizado)+" "+String.valueOf(habilitadoARetrucar)+" "+turnoOponente + " " + puntajeJugador + " " + puntajeOponente;

        enviaMensaje(mensaje);
    }
}
