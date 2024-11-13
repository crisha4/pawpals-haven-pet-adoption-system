package pawpals_haven;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class home {
    
    public void homeView(){
        JFrame frame = new JFrame();
        Font btnFont = new Font("Century Gothic", Font.BOLD, 35);

        int buttonWidth = 500;
        int buttonHeight = 100;
        
        //CLOSE
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
        //TOP PANEL
        JPanel panel = new  JPanel();
	panel.setBounds(0, 0, 1000, 60);
	panel.setBackground(Color.decode("#7dd1cb"));
	frame.add(panel);
        //ADD PETS
        JButton add = new JButton("ADD PETS");
        add.setBounds(250, 100, buttonWidth, buttonHeight);
        add.setFont(btnFont);
        add.setBackground(Color.decode("#7dd1cb"));
        add.setForeground(Color.decode("#ffffff"));
        frame.add(add);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddPets addPets = new AddPets();
                addPets.addView();

            }
        });
        //AVAILABLE PET
        JButton avail = new JButton("AVAILABLE PETS");
        avail.setBounds(250, 220, buttonWidth, buttonHeight);
        avail.setFont(btnFont);
        avail.setBackground(Color.decode("#7dd1cb"));
        avail.setForeground(Color.decode("#ffffff"));
        frame.add(avail);
        avail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AvailablePet available = new AvailablePet();
                available.availView();
            }
        });
        //RESERVED PETS
        JButton reserve = new JButton("RESERVED PETS");
        reserve.setBounds(250, 340, buttonWidth, buttonHeight);
        reserve.setFont(btnFont);
        reserve.setBackground(Color.decode("#7dd1cb"));
        reserve.setForeground(Color.decode("#ffffff"));
        frame.add(reserve);
        reserve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReservePet reserve = new ReservePet();
                reserve.reserveView();
            }
        });
        //ADOPTED PET
        JButton adopt = new JButton("ADOPTED PETS");
        adopt.setBounds(250, 460, buttonWidth, buttonHeight);
        adopt.setFont(btnFont);
        adopt.setBackground(Color.decode("#7dd1cb"));
        adopt.setForeground(Color.decode("#ffffff"));
        frame.add(adopt);
        adopt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdoptedPet adopt = new AdoptedPet();
                adopt.adoptView();
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