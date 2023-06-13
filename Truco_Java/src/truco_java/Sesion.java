package truco_java;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Sesion extends JFrame {
    private JButton back = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    private static final Music effects = new Music();
    private DefaultComboBoxModel<String> modelComboBox = new DefaultComboBoxModel<String>();
    private JComboBox<String> comboBoxUser = new JComboBox<String>(modelComboBox);

  public Sesion (Truco_Java menu) throws IOException {
        setLayout(null);
        setDefaultCloseOperation(3);

        updateList();
        menu.setVisible(false);

        JLabel background = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondo_acerca.png")).getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
        background.setBounds(0, 0, 500, 500);
        background.setVisible(true);
        add(background);

        JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/logo.png")).getScaledInstance(300, 100, Image.SCALE_SMOOTH)));
        logo.setBounds(90, 10, 300, 100);
        logo.setVisible(true);
        background.add(logo);

        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.setBounds(20, 20, 50, 50);
        back.setVisible(true);
        back.setEnabled(true);
        background.add(back);
        back.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            setVisible(false);
            menu.setVisible(true);
            dispose();
        });

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setBounds(210, 135, 220, 15);
        userLabel.setFont(new Font("Arial", Font.BOLD, 19));
        userLabel.setForeground(Color.WHITE);
        userLabel.setVisible(true);
        background.add(userLabel);
        comboBoxUser.setBounds(50,155,415,30);
        comboBoxUser.setFont(new Font("Arial", Font.BOLD, 16));
        comboBoxUser.setForeground(Color.white);
        comboBoxUser.setBackground(Color.orange);
        // Transparent
        comboBoxUser.setOpaque(false);
        comboBoxUser.setRenderer(new DefaultListCellRenderer(){
              @Override
              public Component getListCellRendererComponent(JList list, Object value,
                          int index, boolean isSelected, boolean cellHasFocus) {
                    JComponent result = (JComponent)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    result.setOpaque(false);
                    setHorizontalAlignment(JLabel.CENTER);
                    return result;
              }});
        background.add(comboBoxUser);
        JLabel userBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(400, 30, Image.SCALE_SMOOTH)));
        userBackground.setBounds(50, 155, 400, 30);
        userBackground.setVisible(true);
        background.add(userBackground);

        JPasswordField passwordField = new JPasswordField();
        Action loginAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0;i<Truco_Java.userList.size();i++){
                    if(Truco_Java.userList.get(i).getName().equals(comboBoxUser.getSelectedItem())){
                        if(!Truco_Java.userList.get(i).logIn(new String(passwordField.getPassword())))
                            return;
                        try {
                            Truco_Java.sessionAction(true, i, false);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error en el inicio de sesion.");
                            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            effects.play();
                        }
                        dispose();
                        menu.setVisible(true);
                    }
                }
            }
        };

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(192, 195, 220, 15);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 19));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setVisible(true);
        background.add(passwordLabel);
        passwordField.setBounds(50,215,400,30);
        passwordField.setOpaque(false);
        passwordField.setBorder(null);
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setForeground(Color.white);
        passwordField.setFont(new Font("Arial", Font.BOLD, 16));
        passwordField.addActionListener(loginAction);
        background.add(passwordField);
        JLabel passwordBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(400, 30, Image.SCALE_SMOOTH)));
        passwordBackground.setBounds(50, 215, 400, 30);
        passwordBackground.setVisible(true);
        background.add(passwordBackground);

        JButton loginButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/iniciarseBoton.png")).getScaledInstance(150, 50, Image.SCALE_SMOOTH)));
        loginButton.setBounds(175, 255, 150, 50);
        loginButton.setVisible(true);
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        background.add(loginButton);
        loginButton.addActionListener(loginAction);
  }

    public void updateList() {
        if(modelComboBox.getSize()!=0)
            modelComboBox.removeAllElements();

         for (int i = 0; i < Truco_Java.userList.size(); i++) {
             modelComboBox.addElement(Truco_Java.userList.get(i).getName());
         }
         comboBoxUser.setModel(modelComboBox);
     }
}
