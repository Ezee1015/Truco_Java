package truco_java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Sesion extends JFrame {
    JButton atras = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    JButton adelante = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/adelante.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    JButton salir = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/volverBoton.png")).getScaledInstance(220, 50, Image.SCALE_SMOOTH)));
    private static final Music efectos = new Music();
    private DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
    private JComboBox comboBoxUsu = new JComboBox(model);

  public Sesion (Truco_Java menu) throws IOException {
        setLayout(null);
        setDefaultCloseOperation(3);

        actualizarLista();
        menu.setVisible(false);

        // Fondo
        JLabel fondo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondo_acerca.png")).getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
        fondo.setBounds(0, 0, 500, 500);
        fondo.setVisible(true);
        add(fondo);

        // Logo
        JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/logo.png")).getScaledInstance(300, 100, Image.SCALE_SMOOTH)));
        logo.setBounds(90, 10, 300, 100);
        logo.setVisible(true);
        fondo.add(logo);

        // Atras
        atras.setOpaque(false);
        atras.setContentAreaFilled(false);
        atras.setBorderPainted(false);
        atras.setBounds(20, 20, 50, 50);
        atras.setVisible(true);
        atras.setEnabled(true);
        fondo.add(atras);
        atras.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            setVisible(false);
            menu.setVisible(true);
            dispose();
        });

        // Usuario
        JLabel usuariosText = new JLabel("Usuario:");
        usuariosText.setBounds(210, 135, 120, 15);
        usuariosText.setFont(new Font("Arial", Font.BOLD, 19));
        usuariosText.setForeground(Color.WHITE);
        usuariosText.setVisible(true);
        fondo.add(usuariosText);
        comboBoxUsu.setBounds(50,155,400,30);
        comboBoxUsu.setOpaque(false);
        fondo.add(comboBoxUsu);
        // Fondo para mejorar la apariencia
        JLabel fondoUsuario = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(400, 30, Image.SCALE_SMOOTH)));
        fondoUsuario.setBounds(50, 155, 400, 30);
        fondoUsuario.setVisible(true);
        fondo.add(fondoUsuario);

        JPasswordField contraseña = new JPasswordField();
        Action iniciarAccion = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0;i<Truco_Java.listaUsuarios.size();i++){
                    if(Truco_Java.listaUsuarios.get(i).getNombre().equals(comboBoxUsu.getSelectedItem())){
                        if(!Truco_Java.listaUsuarios.get(i).iniciarSesion(new String(contraseña.getPassword())))
                            return;
                        try {
                            Truco_Java.sesionAccion(true, i, false);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error en el inicio de sesion.");
                            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            efectos.play();
                        }
                        dispose();
                        menu.setVisible(true);
                    }
                }
            }
        };

        // Contraseña
        JLabel contraseñaText = new JLabel("Contraseña:");
        contraseñaText.setBounds(192, 195, 120, 15);
        contraseñaText.setFont(new Font("Arial", Font.BOLD, 19));
        contraseñaText.setForeground(Color.WHITE);
        contraseñaText.setVisible(true);
        fondo.add(contraseñaText);
        contraseña.setBounds(50,215,400,30);
        contraseña.setOpaque(false);
        contraseña.setBorder(null);
        contraseña.setHorizontalAlignment(JTextField.CENTER);
        contraseña.setForeground(Color.white);
        contraseña.setFont(new Font("Arial", Font.BOLD, 16));
        contraseña.addActionListener(iniciarAccion);
        fondo.add(contraseña);
        // Fondo para mejorar la apariencia
        JLabel fondoContraseña = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(400, 30, Image.SCALE_SMOOTH)));
        fondoContraseña.setBounds(50, 215, 400, 30);
        fondoContraseña.setVisible(true);
        fondo.add(fondoContraseña);

        // Boton de ingresar
        JButton ingresar = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/iniciarseBoton.png")).getScaledInstance(150, 50, Image.SCALE_SMOOTH)));
        ingresar.setBounds(175, 255, 150, 50);
        ingresar.setVisible(true);
        ingresar.setOpaque(false);
        ingresar.setContentAreaFilled(false);
        ingresar.setBorderPainted(false);
        fondo.add(ingresar);
        ingresar.addActionListener(iniciarAccion);
  }

    public void actualizarLista() {
        if(model.getSize()!=0)
            model.removeAllElements();

         for (int i = 0; i < Truco_Java.listaUsuarios.size(); i++) {
             model.addElement(Truco_Java.listaUsuarios.get(i).getNombre());
         }
         comboBoxUsu.setModel(model);

     }
}
