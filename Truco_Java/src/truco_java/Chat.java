package truco_java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

public class Chat extends JFrame {
    private static final Music effects = new Music();
    private final JTextArea history;
    private final String opponentName;

  public Chat (Connection socket, String opponentName) throws IOException {
        setLayout(null);
        setDefaultCloseOperation(2);

        this.opponentName = opponentName;

        JLabel background = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondo_acerca.png")).getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
        background.setBounds(0, 0, 500, 500);
        background.setVisible(true);
        add(background);

        JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/logo.png")).getScaledInstance(200, 50, Image.SCALE_SMOOTH)));
        logo.setBounds(100, 10, 200, 50);
        logo.setVisible(true);
        background.add(logo);

        JCheckBox notifications = new JCheckBox("Notificaciones", false);
        notifications.setBounds(300, 10, 100, 50);
        notifications.setOpaque(false);
        notifications.setSelected(true);
        background.add(notifications);

        history = new JTextArea("** Se han unido al Chat **");
        history.setBounds(10,70,380,280);
        history.setFont(new Font("Serif", Font.BOLD, 18));
        history.setVisible(true);
        history.setEditable(false);
        background.add(history);
        JScrollPane scrollHistory = new JScrollPane(history);
        scrollHistory.setVisible(true);
        scrollHistory.setOpaque(false);
        scrollHistory.getViewport().setOpaque(false);
        scrollHistory.setForeground(Color.WHITE);
        scrollHistory.setBorder(null);
        scrollHistory.setViewportBorder(null);
        scrollHistory.setBounds(10,70,380,280);
        background.add(scrollHistory);

        JTextArea messageArea = new JTextArea();
        messageArea.setText("Mensaje");
        messageArea.setBounds(10,360,320,100);
        messageArea.setLineWrap(true);
        messageArea.setFont(new Font("Serif", Font.BOLD, 18));
        messageArea.setVisible(true);
        background.add(messageArea);
        JScrollPane scrollMessage = new JScrollPane(messageArea);
        scrollMessage.setVisible(true);
        scrollMessage.setOpaque(false);
        scrollMessage.getViewport().setOpaque(false);
        scrollMessage.setForeground(Color.WHITE);
        scrollMessage.setBorder(null);
        scrollMessage.setViewportBorder(null);
        scrollMessage.setBounds(10,360,320,100);
        background.add(scrollMessage);

        JButton send = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugarBoton.png")).getScaledInstance(60, 100, Image.SCALE_SMOOTH)));
        send.setBounds(340, 360, 50, 100);
        send.setVisible(true);
        send.setOpaque(false);
        send.setContentAreaFilled(false);
        send.setBorderPainted(false);
        background.add(send);
        send.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();

            sendMessage(socket, messageArea.getText());
        });

        JButton exit = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        exit.setBounds(10, 10, 50, 50);
        exit.setVisible(true);
        exit.setOpaque(false);
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        background.add(exit);
        exit.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            setVisible(false);
            dispose();
        });

        Thread thread = new Thread(){
            public void run(){
                while(true){
                    try {
                        decodeMessage(socket.receiveMessage());
                        if( notifications.isSelected() ) {
                            effects.setFile("src/truco_java/musica/pop.wav", 1);
                            effects.play();
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al checkear la recepción de mensajes: " + ex.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
                }
            }
        };
        thread.start();
  }

    public void sendMessage(Connection socket, String message){
        try {
            socket.sendMessage("msg " + message);
            history.setText(history.getText() + "\nVos: "+message);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
    }

    public void decodeMessage (String message) {
        message=message.trim();

        if(message.isEmpty())
            return;
        Scanner scanner = new Scanner(message);
        String tag = scanner.next();

        switch(tag){
            case "msg":
                String msg_chat;
                msg_chat="\n"+opponentName+": ";
                while( scanner.hasNext() ){
                    msg_chat += scanner.next() + " ";
                }
                history.setText(history.getText() + msg_chat);
                break;
            default:
                // System.out.println("No se detecto la categoria del mensaje: " + cat);
                // for(int i=0;i<cat.length();i++)
                //     System.out.println(i+": "+cat.charAt(i));

                // Repeats the message without the last characters (it can be garbage from socket)
                String catTemp="";
                for(int i=1;i<tag.length();i++){
                    catTemp+=tag.charAt(i);
                }
                String mensajeTemp = catTemp + " ";
                while(scanner.hasNext())
                    mensajeTemp+=scanner.next() + " ";
                if(!message.equals(" ")) decodeMessage(mensajeTemp);
                break;
        }

        scanner.close();
    }
}
