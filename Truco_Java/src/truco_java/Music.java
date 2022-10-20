package truco_java;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class Music {
    Clip clip;
    AudioInputStream sound;
    Timer reproductorMPV;
    boolean sonidoARM;

    public void setFile(String soundFileName) {
        if(!Truco_Java.musica.isSelected())
            return;

        try {
            File file = new File(soundFileName);
            sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
            clip.loop(50);
            sonidoARM = false;
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException l) {
            JOptionPane.showMessageDialog(null, "Error con el música: " + l.getMessage());
        } catch (IllegalArgumentException ex) { // Si se tiene mpv, reproducir por eso
            sonidoARM = true;
            timerLoop(soundFileName, 1);
        }
    }

    // Lo mismo que el anterior pero modifica los parametros. Esto solo hace que se reproducta una vez
    public void setFile(String soundFileName, int x) {
        if(!Truco_Java.musica.isSelected())
            return;

        try {
            File file = new File(soundFileName);
            sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
            sonidoARM = false;
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException l) {
            JOptionPane.showMessageDialog(null, "Error con el sonido: " + l.getMessage());
        } catch (IllegalArgumentException ex){ // Si se tiene mpv, reproducir por eso
            sonidoARM = true;
            timerLoop(soundFileName, 0);
        }
    }

    public void play() {
        if(!Truco_Java.musica.isSelected())
            return;
        
        clip.start();
    }

    public void stop() throws IOException {
        if(sonidoARM) {
            reproductorMPV.cancel();
            return;
        }
        sound.close();
        clip.close();
        clip.stop();
    }
    
    private void timerLoop (String soundFileName, int repetir){
        reproductorMPV = new java.util.Timer();
                
        reproductorMPV.schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    try { 
                        Runtime.getRuntime().exec("mpv --audio-display=no '" + soundFileName + "'");
                        if(repetir==1)
                            timerLoop(soundFileName, repetir);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error con el sonido: " + ex.getMessage());
                    }
                }
            },
            1
            );
    }
}
