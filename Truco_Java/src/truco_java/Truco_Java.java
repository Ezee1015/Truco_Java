package truco_java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.awt.Desktop;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;

public class Truco_Java extends JFrame{

    //Swing
    public static JCheckBox musicCheckBox = new JCheckBox("Musica y Sonido", true);
    public JCheckBox easyCheckBox = new JCheckBox("Modo Fácil", false);
    public JCheckBox fastModeCheckBox = new JCheckBox("Modo Rápido", false);
    public static JCheckBox suicideCheckBox = new JCheckBox("Modo Suicida", false);
    public static JTextPane AiPoints, playerPoints;
    public static JLabel pointsBackground;
    private static JButton sessionButton, registerButton, passwordButton;
    private static JLabel welcomeLabel;
    private static JTextPane welcomePane;

    // Variables
    public static Music music = new Music();
    private static final Music effects = new Music();
    public static int gamesWonPlayer=0, gamesWonAi=0;
    private boolean easyMode = false;
    public static ArrayList<User> userList = new ArrayList<>();
    public static int userIndex=-1;

    public Truco_Java () throws IOException {

        setLayout(null);
        setDefaultCloseOperation(3);

        JLabel background = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/menu.png")).getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
        background.setBounds(0, 0, 500, 500);
        background.setVisible(true);
        add(background);

        JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/logo.png")).getScaledInstance(300, 100, Image.SCALE_SMOOTH)));
        logo.setBounds(100, 35, 300, 100);
        logo.setVisible(true);
        background.add(logo);

        JButton playButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugarBoton.png")).getScaledInstance(300, 60, Image.SCALE_SMOOTH)));
        playButton.setBounds(100, 330, 300, 60);
        playButton.setVisible(true);
        playButton.setOpaque(false);
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        background.add(playButton);
        playButton.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            setVisible(false);
            PlayMenu gameMenu;
            try {
                gameMenu = new PlayMenu(this);
                gameMenu.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                gameMenu.setResizable(false);
                gameMenu.setTitle("Juego Truco");
                gameMenu.setBounds(0,0,500,500);
                gameMenu.setLocationRelativeTo(null);
                gameMenu.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el juego: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
        });

        JButton aboutButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/acercaBoton.png")).getScaledInstance(140, 40, Image.SCALE_SMOOTH)));
        aboutButton.setBounds(100, 400, 140, 40);
        aboutButton.setVisible(true);
        aboutButton.setOpaque(false);
        aboutButton.setContentAreaFilled(false);
        aboutButton.setBorderPainted(false);
        background.add(aboutButton);
        aboutButton.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            setVisible(false);
            About about;
            ImageIcon icono = new ImageIcon("src/truco_java/fondos/icono.png");
            try {
                about = new About(this);
                about.setIconImage(icono.getImage());
                about.setBounds(0,0,500,500);
                about.setTitle("Acerca del Juego");
                about.setResizable(false);
                about.setLocationRelativeTo(null);
                about.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar la información acerca del juego: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
        });

        passwordButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/contraseñaBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
        passwordButton.setBounds(410, 280, 75, 40);
        passwordButton.setVisible(false);
        passwordButton.setOpaque(false);
        passwordButton.setContentAreaFilled(false);
        passwordButton.setBorderPainted(false);
        background.add(passwordButton);
        passwordButton.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            ChangePassword pass;

            try {
                pass = new ChangePassword(this);
                pass.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                pass.setBounds(0,0,500,350);
                pass.setTitle("Acerca del Juego");
                pass.setResizable(false);
                pass.setLocationRelativeTo(null);
                pass.setUndecorated(true);
                pass.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar la información acerca del juego: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
        });

        sessionButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/iniciarBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
        sessionButton.setBounds(410, 340, 75, 40);
        sessionButton.setVisible(true);
        sessionButton.setOpaque(false);
        sessionButton.setContentAreaFilled(false);
        sessionButton.setBorderPainted(false);
        background.add(sessionButton);
        sessionButton.addActionListener((ActionEvent e) -> {
            if(userIndex!=-1){
                effects.setFile("src/truco_java/musica/pop.wav", 1);
                effects.play();
                try {
                    sessionAction(false,0, false);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cerrar la cuenta: " + ex.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
                return;
            }
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();

            User.loadUsers();
            if(Truco_Java.userList.isEmpty()){
                JOptionPane.showMessageDialog(null, "No existen usuarios registrados.");
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
                return;
            }

            Session session;
            try {
                session = new Session(this);
                session.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                session.setBounds(0,0,500,350);
                session.setTitle("Acerca del Juego");
                session.setResizable(false);
                session.setLocationRelativeTo(null);
                session.setUndecorated(true);
                session.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar la información acerca del juego: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
        });

        registerButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/registrarBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
        registerButton.setBounds(410, 400, 75, 40);
        registerButton.setVisible(true);
        registerButton.setOpaque(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        background.add(registerButton);
        registerButton.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            if(userIndex!=-1){
                int dialogResult = JOptionPane.showConfirmDialog(null, "Está a punto de eliminar su usuario ¿Desea continuar con la operación?","Aclaración",JOptionPane.YES_NO_OPTION);
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
                if(dialogResult == JOptionPane.YES_OPTION){
                    userList.get(userIndex).saveChanges(true, 0);
                    userList.remove(userIndex);
                    try {
                        sessionAction(false,0, true);
                    } catch (IOException ex) {
                        Logger.getLogger(Truco_Java.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return;
            }

            User.loadUsers();
            Register register;
            try {
                register = new Register(this);
                register.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                register.setBounds(0,0,500,350);
                register.setTitle("Acerca del Juego");
                register.setResizable(false);
                register.setLocationRelativeTo(null);
                register.setUndecorated(true);
                register.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar la información acerca del juego: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
        });

        JButton exit = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/salirBoton.png")).getScaledInstance(140, 40, Image.SCALE_SMOOTH)));
        exit.setBounds(260, 400, 140, 40);
        exit.setVisible(true);
        exit.setOpaque(false);
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        background.add(exit);
        exit.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            System.exit(0);
        });

        musicCheckBox.setBounds(100, 300, 140, 20);
        musicCheckBox.setOpaque(false);
        background.add(musicCheckBox);
        musicCheckBox.addItemListener((ItemEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            if(e.getStateChange()==1){
                music.setFile("src/truco_java/musica/fondo.wav");
                music.play();
            } else{
                try {
                    music.stop();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al detener la música: " + ex.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        });

        easyCheckBox.setBounds(280, 300, 140, 20);
        easyCheckBox.setOpaque(false);
        background.add(easyCheckBox);
        easyCheckBox.addItemListener((ItemEvent e) -> {
            if(e.getStateChange()==1 && !easyMode){
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
                int dialogResult = JOptionPane.showConfirmDialog(null, "Al estar en modo facil, no se sumará puntaje! Este está diseñado exclusivamente para practicar. ¿Aún así desea jugar en modo facil?","Aclaración",JOptionPane.YES_NO_OPTION);
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
                if(dialogResult == JOptionPane.NO_OPTION)
                    easyCheckBox.setSelected(false);
                else {
                    easyMode=true;
                    easyCheckBox.setSelected(true);
                }
            }
        });

        fastModeCheckBox.setBounds(100, 270, 140, 40);
        fastModeCheckBox.setOpaque(false);
        background.add(fastModeCheckBox);

        suicideCheckBox.setBounds(280, 270, 140, 40);
        suicideCheckBox.setOpaque(false);
        background.add(suicideCheckBox);
        suicideCheckBox.addItemListener((ItemEvent e) -> {
          String musicFile;
          if(e.getStateChange()==1){
            musicFile="src/truco_java/musica/smode.wav";
          } else {
            musicFile="src/truco_java/musica/fondo.wav";
          }

          try {
            music.stop();
            music.setFile(musicFile);
            music.play();
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al modificar la musica de fondo: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
          }
        });

        JLabel creditsLabel = new JLabel("    Versión 5.0 | Creado por Leonardo D.S. - Licencia GPL");
        creditsLabel.setBounds(0, 445, 500, 30);
        creditsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        creditsLabel.setForeground(Color.WHITE);
        creditsLabel.setVisible(true);
        background.add(creditsLabel);
        creditsLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URL("https://github.com/Ezee1015/Truco_Java").toURI());
                } catch (Exception ex) {}
            }
        });

        pointsBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg0.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        pointsBackground.setBounds(10, 325, 75, 100);
        pointsBackground.setVisible(false);
        background.add(pointsBackground);

        playerPoints = new JTextPane();
        playerPoints.setText(Integer.toString(gamesWonPlayer));
        playerPoints.setFont(new Font("Arial", Font.BOLD, 20));
        playerPoints.setBounds(5, 50, 25, 30);
        playerPoints.setVisible(true);
        playerPoints.setEditable(false);
        playerPoints.setOpaque(false);
        pointsBackground.add(playerPoints);
        // Center text
        StyledDocument doc = playerPoints.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        AiPoints = new JTextPane();
        AiPoints.setText(Integer.toString(gamesWonAi));
        AiPoints.setFont(new Font("Arial", Font.BOLD, 20));
        AiPoints.setBounds(42, 50, 25, 30);
        AiPoints.setEditable(false);
        AiPoints.setOpaque(false);
        AiPoints.setVisible(true);
        pointsBackground.add(AiPoints);
        // Center text
        StyledDocument doc2 = AiPoints.getStyledDocument();
        SimpleAttributeSet center2 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
        doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);

        welcomeLabel = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/cartelBienvenida.png")).getScaledInstance(360, 35, Image.SCALE_SMOOTH)));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setBounds(70, 5, 360, 35);
        welcomeLabel.setVisible(false);
        background.add(welcomeLabel);
        welcomePane = new JTextPane();
        welcomePane.setBounds(0, 3, 360, 35);
        welcomePane.setFont(new Font("Arial", Font.BOLD, 22));
        welcomePane.setEditable(false);
        welcomePane.setForeground(Color.decode("#7b3e00"));
        welcomePane.setOpaque(false);
        welcomePane.setVisible(true);
        welcomeLabel.add(welcomePane);
        // Center text
        StyledDocument doc3 = welcomePane.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
    }

    public static void main(String[] args) throws IOException {
        Truco_Java menu;

        try {
            menu = new Truco_Java();
            menu.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
            menu.setBounds(0,0,500,500);
            menu.setResizable(false);
            menu.setTitle("Menu - Truco");
            menu.setLocationRelativeTo(null);
            menu.setVisible(true);
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el menu: " + e.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            return;
        }

        music.setFile("src/truco_java/musica/fondo.wav");
        music.play();
    }

    public static void sessionAction (boolean loggedIn, int index, boolean deleted) throws IOException{
        if(!loggedIn){
            registerButton.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/registrarBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
            sessionButton.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/iniciarBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
            welcomePane.setText(null);
            welcomeLabel.setVisible(false);
            welcomeLabel.setText(null);
            if(!deleted){
                userList.get(userIndex).encryptPoints();
                Truco_Java.userList.get(userIndex).saveChanges(false, 0);
            }
            gamesWonAi=0;
            gamesWonPlayer=0;
            AiPoints.setText(String.valueOf(gamesWonAi));
            playerPoints.setText(String.valueOf(gamesWonPlayer));
            pointsBackground.setVisible(false);
            passwordButton.setVisible(false);
            userIndex=-1;
            return;
        }
        effects.setFile("src/truco_java/musica/log-in.wav", 1);
        effects.play();
        userIndex=index;
        registerButton.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/eliminarBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
        sessionButton.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/cerrarSesionBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));

        String message[] = new String[3];
        message[0]="¡Bienvenido " + userList.get(userIndex).getName() + "!";
        message[1]=userList.get(userIndex).getName();
        message[2]="¡Bienvenido!";
        if(textPaneLinesCount(welcomePane, message[0]) == 1) welcomePane.setText(message[0]);
        else if(textPaneLinesCount(welcomePane, message[1]) == 1) welcomePane.setText(message[1]);
        else welcomePane.setText(message[2]);

        welcomeLabel.setVisible(true);
        if(userList.get(userIndex).getAiPoints()>0 || userList.get(userIndex).getPlayerPoints()>0)
            pointsBackground.setVisible(true);
        else
            pointsBackground.setVisible(false);
        passwordButton.setVisible(true);
    }

    private static int textPaneLinesCount(JTextPane panel, String text){
        String oldText = panel.getText();
        panel.setText(text);

        int chars = panel.getText().length();
        int lines = (chars == 0) ? 1 : 0;

        try {
            int offset = chars;
            while (offset > 0) {
                offset = Utilities.getRowStart(panel, offset) - 1;
                lines++;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        panel.setText(oldText);
        return lines;
    }
}
