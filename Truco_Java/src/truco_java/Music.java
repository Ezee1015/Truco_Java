package truco_java;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

    //Permite la utilizacion de musica en hardcore
    public class Music {
        Clip clip;
        AudioInputStream sound;

        public void setFile(String soundFileName) {
            if(!Truco_Java.musica.isSelected())
                return;

            try {
                File file = new File(soundFileName);
                sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
                clip.loop(50);
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException l) {
                JOptionPane.showMessageDialog(null, "Error con el música: " + l.getMessage());
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
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException l) {
                JOptionPane.showMessageDialog(null, "Error con el sonido: " + l.getMessage());
            }
        }

        public void play() {
            if(!Truco_Java.musica.isSelected())
                return;

            clip.start();
        }

        public void stop() throws IOException {
            sound.close();
            clip.close();
            clip.stop();
        }
    }
