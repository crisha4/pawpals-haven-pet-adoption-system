package pawpals_haven;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class LogIn {
        ClassLoader cl = getClass().getClassLoader();  
        
    public void loginView() {
        JFrame frame = new JFrame();
        Font textFont = new Font("Verdana", Font.PLAIN, 15);

        //-------------------------LOGO--------------------------
        
        ImageIcon logo = new ImageIcon(cl.getResource("rsc/pawpallogo.png"));
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBounds(160,120,200,200);
        frame.add(logoLabel);
        
        JLabel pawpalsLabel = new JLabel("PawPals Haven");
        pawpalsLabel.setForeground(Color.decode("#e24c54"));
        pawpalsLabel.setBounds(80, 270, 400, 100);
        pawpalsLabel.setFont(new Font("Verdana", Font.BOLD, 40));
        frame.add(pawpalsLabel);
       
        JLabel management = new JLabel(" Pet Adoption Management System");
        management.setForeground(Color.decode("#ffffff"));
        management.setBounds(110, 300, 400, 100);
        management.setFont(new Font("Verdana", Font.BOLD, 15));
        frame.add(management);
        //-------------------------------------------------------

        //PANEL
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 500, 600);
        panel.setBackground(Color.decode("#7dd1cb"));
        frame.add(panel);

        //------------------------CLOSE---------------------------
        JLabel exit = new JLabel("X");
        exit.setForeground(Color.decode("#ffffff"));
        exit.setBounds(965, 20, 100, 20);
        exit.setFont(new Font("Century Gothic", Font.BOLD, 20));
        frame.add(exit);
        exit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        //LOGINTEXT
        JLabel loginTitle = new JLabel("LOGIN");
        loginTitle.setForeground(Color.decode("#ffffff"));
        loginTitle.setBounds(675, 100, 350, 75);
        loginTitle.setFont(new Font("Century Gothic", Font.BOLD, 50));
        frame.add(loginTitle);

      //USERNAME LABEL
        JLabel user = new JLabel("Username");
        user.setForeground(Color.decode("#DEE4E7"));
        user.setBounds(570, 210, 100, 20);
        user.setFont(textFont);
        frame.add(user);

        //USERFIELD
        JTextField usernameField = new JTextField();  
        usernameField.setBounds(570, 235, 360, 35);  
        usernameField.setBackground(Color.decode("#DEE4E7"));
        usernameField.setForeground(Color.decode("#37474F"));
        usernameField.setFont(textFont);
        frame.add(usernameField);  
        //---------------------------------------------------------

        //PASSWORD LABEL
        JLabel pass = new JLabel("Password");
        pass.setForeground(Color.decode("#DEE4E7"));
        pass.setBounds(570, 290, 100, 20);
        pass.setFont(textFont);
        frame.add(pass);
        //-------------------------------------------------------

        //PASSWORDFIELD
        JPasswordField passwordField = new JPasswordField();  
        passwordField.setBounds(570, 315, 360, 35);  
        passwordField.setBackground(Color.decode("#DEE4E7"));
        passwordField.setForeground(Color.decode("#37474F"));
        frame.add(passwordField); 
        //---------------------------------------------------------

        //LOGIN BUTTON
        JButton login = new JButton("LOGIN");
        login.setBounds(660, 390, 190, 50);
        login.setFont(new Font("Verdana", Font.BOLD, 20));
        login.setBackground(Color.decode("#44abaa"));
        login.setForeground(Color.decode("#ffffff"));
        frame.add(login);

login.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String userName = usernameField.getText(); 
        String password = new String(passwordField.getPassword());  
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/pawpals_db", "root", "eggs"); // try "root", "root" pag ayaw

            PreparedStatement st = connection.prepareStatement("SELECT name, password FROM users WHERE name=? AND password=?");

            st.setString(1, userName);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                frame.dispose();
                home homeWindow = new home();  
                homeWindow.homeView();  
                JOptionPane.showMessageDialog(login, "You have successfully logged in");
            } else {
                JOptionPane.showMessageDialog(login, "Wrong Username & Password");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
});

        frame.setSize(1000, 600);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.getContentPane().setBackground(Color.decode("#cb444c"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
    }
}