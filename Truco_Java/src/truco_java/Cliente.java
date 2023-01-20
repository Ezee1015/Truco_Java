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

    public String enviaEnvido(ArrayList<Integer> envidosCantados) throws IOException{
        // Envia la peticion
        enviaMensaje(String.valueOf(envidosCantados.get(envidosCantados.size()-1)));
        return recibirMensaje();
    }

    public void enviaMensaje(String mensaje) throws IOException{
        // Envia la peticion
        try {
            salidaServidor = new DataOutputStream(cs.getOutputStream());
            System.out.println(mensaje);
            salidaServidor.writeUTF(mensaje);
            cs.close();

        } catch (Exception e) {
            if(e.getMessage().equalsIgnoreCase("Socket is closed")){
                reconectar();
                enviaMensaje(mensaje);
                return;
            }
            System.out.println(e.getMessage());
        }
    }

    public String recibirMensaje() throws IOException{
        try {
            cs = ss.accept();

            salidaCliente = new DataOutputStream(cs.getOutputStream());

            BufferedReader entrada = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            String mensaje="";

            while((mensajeServidor = entrada.readLine()) != null) {
                mensaje=mensajeServidor;
            }
            ss.close();

            System.out.println("Mensaje recibbido en CLIENTE: " + mensaje);
            return mensaje;
        } catch (Exception e) {
            if(e.getMessage().equalsIgnoreCase("Socket is closed") || ss == null){
                reconectar();
                return recibirMensaje();
            }
            System.out.println(e.getMessage());
        }
        return "";
    }

    public void actualizarInfo(int cantCartasJugador, ArrayList<Carta> manoOponente, int[] posManoOponente,  int nivelTruco, boolean envidoFinalizado, int habilitadoARetrucar, boolean turnoOponente, int puntajeJugador, int puntajeOponente) throws IOException{
        // Envia la peticion
        String mensaje = "update " + String.valueOf(cantCartasJugador)+" ";
        for(int i=0;i<3;i++){
            mensaje+=manoOponente.get(i).getPalo()+" ";
            mensaje+=manoOponente.get(i).getNumero()+" ";
        }
        for(int i=0;i<3;i++)
            mensaje+=posManoOponente[i]+" ";
        mensaje+=String.valueOf(nivelTruco)+" "+String.valueOf(envidoFinalizado)+" "+String.valueOf(habilitadoARetrucar)+" "+turnoOponente + " " + puntajeJugador + " " + puntajeOponente;

        enviaMensaje(mensaje);
    }

    public void tirarCarta() throws IOException{
        enviaMensaje("tiraCarta");
    }
}
