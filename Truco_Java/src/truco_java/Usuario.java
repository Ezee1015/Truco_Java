package truco_java;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

public class Usuario {
    private static final Music efectos = new Music();
    private String nombre, contraseña=null, encriptado;
    private int puntajeJugador=0, puntajeAI=0;

    public Usuario(String nombre, String encriptado) {
        this.nombre = nombre;
        this.encriptado = encriptado;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntajeJugador() {
        return puntajeJugador;
    }

    public int getPuntajeAI() {
        return puntajeAI;
    }

    public String getEncriptado() {
        return encriptado;
    }

    public void setPuntajeJugador(int puntajeJugador) {
        this.puntajeJugador = puntajeJugador;
    }

    public void setPuntajeAI(int puntajeAI) {
        this.puntajeAI = puntajeAI;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void encriptaPuntaje(){
        puntajeAI = Truco_Java.ganadasAI;
        puntajeJugador = Truco_Java.ganadasJugador;
        try {
            String datosOriginales = puntajeJugador + ";sig;" + puntajeAI;

            encriptado = new Encriptacion().encriptar(datosOriginales, contraseña);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
                JOptionPane.showMessageDialog(null, "No se pudo encriptar los puntajes: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
        }
    }

    public void guardarCambios(boolean eliminar){
        File doc = new File("puntajes.txt");
        Scanner obj;
        String aGuardar="";

        try {
            boolean seGuardo=false;
            obj = new Scanner(doc);
            obj.useDelimiter(";sig;");

            while (obj.hasNext()){
                String nombreTemp = obj.next();
                String encriptadoTemp = obj.next();
                if(nombreTemp.equals(nombre)){
                    if(!eliminar)
                        aGuardar+=nombre+";sig;"+encriptado;
                    seGuardo=true;
                    if(eliminar) aGuardar=aGuardar.substring(0, aGuardar.length() - 5);
                } else {
                    aGuardar+=nombreTemp+";sig;"+encriptadoTemp;
                }
                if(obj.hasNext()) aGuardar+=";sig;";
            }

            if(!seGuardo && !eliminar){ // Si no se guardo, lo guarda
                if(doc.length()!=0) aGuardar+=";sig;";
                aGuardar+=nombre+";sig;"+encriptado;
            }
            obj.close();
        } catch (FileNotFoundException | NoSuchElementException ex) {
                JOptionPane.showMessageDialog(null, "No se pudo leer los usuarios. Intente más tarde\nError: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
                return;
        }

        try {
            FileWriter myWriter = new FileWriter("puntajes.txt");
            myWriter.write(aGuardar);
            myWriter.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo escribir los usuarios: " + ex.getMessage());
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
        }

    }

    public static void cargarUsuarios(){
        Truco_Java.listaUsuarios.clear();
        File doc = new File("puntajes.txt");
        Scanner obj;

        try {
            obj = new Scanner(doc);
            obj.useDelimiter(";sig;");

            while (obj.hasNext())
                Truco_Java.listaUsuarios.add(new Usuario(obj.next(),obj.next()));

            obj.close();
        } catch (FileNotFoundException | NoSuchElementException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo leer los usuarios. Intente más tarde\nError: " + ex.getMessage());
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            return;
        }
    }

    public boolean iniciarSesion(String contraseña){
        this.contraseña=contraseña;
        Scanner lector;
        try{
             lector = new Scanner(new Encriptacion().desencriptar(encriptado, contraseña));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta.");
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            return false;
        }
        lector.useDelimiter(";sig;");

        try{
            puntajeJugador=Integer.parseInt(lector.next());
            puntajeAI=Integer.parseInt(lector.next());
            Truco_Java.ganadasAI=puntajeAI;
            Truco_Java.ganadasJugador=puntajeJugador;
            Truco_Java.puntajeJugador.setText(Integer.toString(Truco_Java.ganadasJugador));
            Truco_Java.puntajeAI.setText(Integer.toString(Truco_Java.ganadasAI));
            lector.close();
        } catch(java.lang.NumberFormatException err){
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta. No se pudo descrifar correctamente");
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            return false;
        }

        return true;
    }

}
