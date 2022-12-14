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
    private Clip clip;
    private AudioInputStream sound;
    private Timer reproductorMPV;
    private boolean sonidoARM;

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
        } catch (UnsatisfiedLinkError | IllegalArgumentException ex) { // Si se tiene mpv, reproducir por eso
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

        if(sonidoARM)
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
                        //test command in linux
                        runCommand("mpv","--audio-display=no", soundFileName);
                        // Runtime.getRuntime().exec("mpv --audio-display=no '" + soundFileName + "'");
                    if(killTimer)
                        return;
                    if(repetir==1)
                        timerLoop(soundFileName, repetir);
                }
            },
            1
        );
}
boolean killTimer=false;
public void runCommand(String... command) {
    ProcessBuilder processBuilder = new ProcessBuilder().command(command);

    try {
        Process process = processBuilder.start();

        //wait for the process to complete
        // process.waitFor();
        while(process.isAlive()){
            if(!Truco_Java.musica.isSelected()){
                killTimer=true;
                process.destroy();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
