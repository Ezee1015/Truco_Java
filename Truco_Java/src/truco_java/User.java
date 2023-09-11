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

public class User {
    private static final Music effects = new Music();
    private String name, password=null, encrypt;
    private int playerPoints=0, AiPoints=0;

    public User(String name, String encrypt) {
        this.name = name;
        this.encrypt = encrypt;
    }

    public String getName() {
        return name;
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public int getAiPoints() {
        return AiPoints;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    public void setAiPoints(int AiPoints) {
        this.AiPoints = AiPoints;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void encryptPoints(){
        AiPoints = Truco_Java.gamesWonAi;
        playerPoints = Truco_Java.gamesWonPlayer;
        try {
            String data = playerPoints + ";sig;" + AiPoints;

            encrypt = new Encryption().encrypt(data, password);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
                JOptionPane.showMessageDialog(null, "No se pudo encriptar los puntajes: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
        }
    }

    public void saveChanges(boolean delete, int tries){
        File doc = new File("puntajes.txt");
        Scanner obj;
        String toSave="";

        try {
            boolean saved=false;
            obj = new Scanner(doc);
            obj.useDelimiter(";sig;");

            while (obj.hasNext()){
                String tempName = obj.next();
                String tempEncrypt = obj.next();
                if(tempName.equals(name)){
                    if(!delete)
                        toSave+=name+";sig;"+encrypt;
                    saved=true;
                    if(delete) toSave=toSave.substring(0, toSave.length() - 5);
                } else {
                    toSave+=tempName+";sig;"+tempEncrypt;
                }
                if(obj.hasNext()) toSave+=";sig;";
            }

            if(!saved && !delete){
                if(doc.length()!=0) toSave+=";sig;";
                toSave+=name+";sig;"+encrypt;
            }
            obj.close();
        } catch (FileNotFoundException | NoSuchElementException ex) {
            toSave=name+";sig;"+encrypt;
        }

        try {
            FileWriter myWriter = new FileWriter("puntajes.txt");
            myWriter.write(toSave);
            myWriter.close();
        } catch (IOException ex) {
            if(tries<30){
                try { Thread.sleep(500); } catch (Exception e) {}
                saveChanges(delete, tries+1);
                return;
            }
            JOptionPane.showMessageDialog(null, "No se pudo escribir los usuarios: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }

    }

    public static void loadUsers(){
        Truco_Java.userList.clear();
        File doc = new File("puntajes.txt");
        Scanner obj;

        try {
            obj = new Scanner(doc);
            obj.useDelimiter(";sig;");

            while (obj.hasNext())
                Truco_Java.userList.add(new User(obj.next(),obj.next()));

            obj.close();
        } catch (FileNotFoundException | NoSuchElementException ex) {
        }
    }

    public boolean logIn(String password){
        this.password=password;
        Scanner scanner;
        try{
             scanner = new Scanner(new Encryption().desencriptar(encrypt, password));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta.");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            return false;
        }
        scanner.useDelimiter(";sig;");

        try{
            playerPoints=Integer.parseInt(scanner.next());
            AiPoints=Integer.parseInt(scanner.next());
            Truco_Java.gamesWonAi=AiPoints;
            Truco_Java.gamesWonPlayer=playerPoints;
            Truco_Java.playerPoints.setText(Integer.toString(Truco_Java.gamesWonPlayer));
            Truco_Java.AiPoints.setText(Integer.toString(Truco_Java.gamesWonAi));
            scanner.close();
        } catch(java.lang.NumberFormatException err){
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta. No se pudo descrifar correctamente");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            return false;
        }

        return true;
    }
}
