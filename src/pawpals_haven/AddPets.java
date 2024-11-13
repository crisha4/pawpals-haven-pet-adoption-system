package pawpals_haven;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.sql.*;


public class AddPets {

    // Declare JTextField variables at the class level
	private JTextField nameField;
    private JTextField typeField;
    private JTextField breedField;
    private JTextField ageField;
    private String imagePath;
    private JFrame frame;
    private JPanel imagePanel;

    public void addView() {
        frame = new JFrame();
        Font btnFont = new Font("Century Gothic", Font.BOLD, 20);

        // CLOSE
        JLabel exit = new JLabel("X");
        exit.setForeground(Color.decode("#ffffff"));
        exit.setBounds(715, 20, 100, 20);
        exit.setFont(new Font("Century Gothic", Font.BOLD, 20));
        frame.add(exit);
        exit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
            }
        });

        // TOP PANEL
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 750, 60);
        panel.setBackground(Color.decode("#44abaa"));
        frame.add(panel);

        // ADD A PET Label
        JLabel addPetLabel = new JLabel("ADD A PET");
        addPetLabel.setForeground(Color.decode("#ffffff"));
        addPetLabel.setFont(new Font("Century Gothic", Font.BOLD, 40));
        panel.add(addPetLabel);

     // IMAGE DISPLAY
        imagePanel = new JPanel();
        imagePanel.setBounds(450, 80, 250, 250);
        imagePanel.setBackground(Color.decode("#ffffff"));
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        frame.add(imagePanel);

        // IMAGE CHOOSER BUTTON
        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.setBounds(450, 340, 250, 30);
        chooseImageButton.setBackground(Color.decode("#7dd1cb"));
        chooseImageButton.setForeground(Color.decode("#ffffff"));
        chooseImageButton.setFont(btnFont);
        frame.add(chooseImageButton);

        chooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    imagePath = fileChooser.getSelectedFile().getAbsolutePath();
                    displayImage(imagePath); 
                }
            }
        });

        // TEXT FIELDS
        nameField = new JTextField();
        nameField.setBounds(150, 80, 250, 30);
        frame.add(nameField);

        typeField = new JTextField();
        typeField.setBounds(150, 130, 250, 30);
        frame.add(typeField);

        breedField = new JTextField();
        breedField.setBounds(150, 180, 250, 30);
        frame.add(breedField);

        ageField = new JTextField();
        ageField.setBounds(150, 230, 250, 30);
        frame.add(ageField);

        // Labels for Text Fields
        JLabel nameLabel = new JLabel("Pet Name:");
        nameLabel.setBounds(60, 80, 110, 40);
        nameLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));
        frame.add(nameLabel);

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(80, 130, 60, 30);
        typeLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));
        frame.add(typeLabel);

        JLabel breedLabel = new JLabel("Breed:");
        breedLabel.setBounds(80, 180, 60, 30);
        breedLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));
        frame.add(breedLabel);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(80, 230, 60, 30);
        ageLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));
        frame.add(ageLabel);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(200, 280, 100, 30);
        saveButton.setBackground(Color.decode("#7dd1cb"));
        saveButton.setForeground(Color.decode("#ffffff"));
        saveButton.setFont(btnFont);
        frame.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String petName = nameField.getText();
                String petType = typeField.getText();
                String petBreed = breedField.getText();
                String petAgeText = ageField.getText();

                try {
                    int petAge = Integer.parseInt(petAgeText);

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/pawpals_db", "root", "eggs");

                    String insertQuery = "INSERT INTO pets (name, type, breed, age) VALUES (?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, petName);
                    preparedStatement.setString(2, petType);
                    preparedStatement.setString(3, petBreed);
                    preparedStatement.setInt(4, petAge); 
                    int rowsAffected = preparedStatement.executeUpdate();

                    String insertQueryAvailablePets = "INSERT INTO available_pets (name, type, breed, age, image_path) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatementAvailablePets = connection.prepareStatement(insertQueryAvailablePets);
                    preparedStatementAvailablePets.setString(1, petName);
                    preparedStatementAvailablePets.setString(2, petType);
                    preparedStatementAvailablePets.setString(3, petBreed);
                    preparedStatementAvailablePets.setInt(4, petAge); 
                    preparedStatementAvailablePets.setString(5, imagePath);
                    preparedStatementAvailablePets.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(frame, "Pet added successfully");
                        nameField.setText("");
                        typeField.setText("");
                        breedField.setText("");
                        ageField.setText("");
                        imagePath = null;
                        displayImage("");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to add pet");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid age input. Please enter a valid integer for age.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        

        frame.setSize(750, 450);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.getContentPane().setBackground(Color.decode("#e55e65"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void displayImage(String imagePath) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();

        Image resizedImage = originalImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH);

        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel imageLabel = new JLabel(resizedIcon);
        imagePanel.removeAll(); 
        imagePanel.add(imageLabel);
        imagePanel.revalidate();
        imagePanel.repaint();
    }
}
