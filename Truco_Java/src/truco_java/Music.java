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
    private Timer mpvPlayer;
    private boolean android; // If it's in Android

    public void setFile(String soundFileName) {
        if(!Truco_Java.musicCheckBox.isSelected())
            return;

        try {
            File file = new File(soundFileName);
            sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
            clip.loop(100);
            android = false;
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException l) {
            JOptionPane.showMessageDialog(null, "Error con la música: " + l.getMessage());
        } catch (UnsatisfiedLinkError | IllegalArgumentException ex) {
            android = true;
            playWithMpv(soundFileName, 1);
        }
    }

    // This plays only one time
    // The same that the before function but this has different parameters.
    public void setFile(String soundFileName, int x) {
        if(!Truco_Java.musicCheckBox.isSelected())
            return;

        try {
            File file = new File(soundFileName);
            sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
            android = false;
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException l) {
            JOptionPane.showMessageDialog(null, "Error con el sonido: " + l.getMessage());
        } catch (IllegalArgumentException ex){
            android = true;
            playWithMpv(soundFileName, 0);
        }
    }

    public void play() {
        if(!Truco_Java.musicCheckBox.isSelected())
            return;

        if(android)
            return;

        clip.start();
    }

    public void stop() throws IOException {
        if(android) {
            mpvPlayer.cancel();
            return;
        }
        sound.close();
        clip.close();
        clip.stop();
    }

    private void playWithMpv (String soundFileName, int repeat){
        mpvPlayer = new java.util.Timer();

        mpvPlayer.schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                        runCommand("mpv","--audio-display=no", soundFileName);
                    if(killMpv)
                        return;
                    if(repeat==1)
                        playWithMpv(soundFileName, repeat);
                }
            },
            1
        );
    }

    private boolean killMpv=false;
    public void runCommand(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder().command(command);

        try {
            Process process = processBuilder.start();

            while(process.isAlive()){
                if(!Truco_Java.musicCheckBox.isSelected()){
                    killMpv=true;
                    process.destroy();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
