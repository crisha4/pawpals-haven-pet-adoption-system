package pawpals_haven;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.FlowLayout;
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

public class AvailablePet {
    
    private JPanel imagePanel;

    public void availView() {
        JFrame frame = new JFrame();

        // CLOSE
        JLabel exit = new JLabel("X");
        exit.setForeground(Color.decode("#ffffff"));
        exit.setBounds(965, 20, 100, 20);
        exit.setFont(new Font("Century Gothic", Font.BOLD, 20));
        frame.add(exit);
        exit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
            }
        });
        // TOP PANEL
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 1000, 60);
        panel.setBackground(Color.decode("#44abaa"));
        frame.add(panel);

        // ADD A PET Label
        JLabel petLabel = new JLabel("AVAILABLE PETS");
        petLabel.setForeground(Color.decode("#ffffff"));
        petLabel.setFont(new Font("Century Gothic", Font.BOLD, 40));
        panel.add(petLabel);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/pawpals_db", "root", "eggs"); // here
            String selectQuery = "SELECT * FROM available_pets ORDER BY name";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            int xOffset = 30;
            int yOffset = 80;
            int columnCount = 0; 

            while (resultSet.next()) {
                String petName = resultSet.getString("name");
                String petType = resultSet.getString("type");
                String petBreed = resultSet.getString("breed");
                String petAge = resultSet.getString("age");
                String imagePath = resultSet.getString("image_path");

                JPanel petPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                petPanel.setBounds(xOffset, yOffset, 300, 150);
                petPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

                ImageIcon petImage = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(petImage);
                petPanel.add(imageLabel);

                JLabel nameLabel = new JLabel("Pet Name: " + petName);
                nameLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));

                JLabel typeLabel = new JLabel("Type: " + petType);
                typeLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));

                JLabel breedLabel = new JLabel("Breed: " + petBreed);
                breedLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));

                JLabel ageLabel = new JLabel("Age: " + petAge);
                ageLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));

                JButton editButton = new JButton("Edit");
                editButton.setFont(new Font("Century Gothic", Font.BOLD, 15));
                editButton.setBackground(Color.decode("#7dd1cb"));
                editButton.setForeground(Color.decode("#ffffff"));
                editButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        editPet(petName, petType, petBreed, petAge, imagePath);
                    }
                });

                JButton reserveButton = new JButton("Reserve");
                reserveButton.setFont(new Font("Century Gothic", Font.BOLD, 15));
                reserveButton.setBackground(Color.decode("#7dd1cb"));
                reserveButton.setForeground(Color.decode("#ffffff"));

                reserveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String reservedBy = JOptionPane.showInputDialog(frame,
                                "Please enter the name of the person who will reserve the pet:");
                        if (reservedBy != null) {
                            try {
                                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/pawpals_db", "root", "eggs"); // here

                                String selectPetQuery = "SELECT * FROM available_pets WHERE name = ? AND type = ? AND breed = ? AND age = ?";
                                PreparedStatement selectPetStatement = connection.prepareStatement(selectPetQuery);
                                selectPetStatement.setString(1, petName);
                                selectPetStatement.setString(2, petType);
                                selectPetStatement.setString(3, petBreed);
                                selectPetStatement.setString(4, petAge);
                                ResultSet petResult = selectPetStatement.executeQuery();

                                if (petResult.next()) {
                                    String imagePath = petResult.getString("image_path");

                                    // Remove from available_pets
                                    String deleteQuery = "DELETE FROM available_pets WHERE name = ? AND type = ? AND breed = ? AND age = ?";
                                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                                    deleteStatement.setString(1, petName);
                                    deleteStatement.setString(2, petType);
                                    deleteStatement.setString(3, petBreed);
                                    deleteStatement.setString(4, petAge);
                                    deleteStatement.executeUpdate();

                                    // Add to reserved_pets 
                                    String insertQuery = "INSERT INTO reserved_pets (name, type, breed, age, reserved_by, image_path) VALUES (?, ?, ?, ?, ?, ?)";
                                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                                    insertStatement.setString(1, petName);
                                    insertStatement.setString(2, petType);
                                    insertStatement.setString(3, petBreed);
                                    insertStatement.setString(4, petAge);
                                    insertStatement.setString(5, reservedBy);
                                    insertStatement.setString(6, imagePath);
                                    insertStatement.executeUpdate();

                                    // Refresh the table
                                    availView();
                                    frame.dispose();
                                }

                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });

                petPanel.add(nameLabel);
                petPanel.add(typeLabel);
                petPanel.add(breedLabel);
                petPanel.add(ageLabel);
                petPanel.add(editButton);
                petPanel.add(reserveButton);
                frame.add(petPanel);

                xOffset += 320; // X offset for the next pet
                columnCount++;

                // Check if the maximum number of columns (3) has been reached
                if (columnCount == 3) {
                    columnCount = 0; // Reset column count
                    yOffset += 160; // Move to the next row
                    xOffset = 30;   // Reset X offset for the new row
                }
            }

            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        frame.setSize(1000, 600);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.getContentPane().setBackground(Color.decode("#e55e65"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void editPet(String petName, String petType, String petBreed, String petAge, String imagePath) {
        JFrame editFrame = new JFrame("Edit Pet");
        editFrame.setSize(750, 450);
        editFrame.setLayout(null);
        editFrame.setUndecorated(true);
        editFrame.setLocationRelativeTo(null);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.getContentPane().setBackground(Color.decode("#e55e65"));

        // CLOSE
        JLabel exit = new JLabel("X");
        exit.setForeground(Color.decode("#ffffff"));
        exit.setBounds(715, 20, 100, 20);
        exit.setFont(new Font("Century Gothic", Font.BOLD, 20));
        editFrame.add(exit);
        exit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                editFrame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 750, 60);
        panel.setBackground(Color.decode("#44abaa"));
        editFrame.add(panel);

        // ADD A PET Label
        JLabel addPetLabel = new JLabel("EDIT PET INFO");
        addPetLabel.setForeground(Color.decode("#ffffff"));
        addPetLabel.setFont(new Font("Century Gothic", Font.BOLD, 40));
        panel.add(addPetLabel);

        // IMAGE DISPLAY
        imagePanel = new JPanel();
        imagePanel.setBounds(450, 80, 250, 250);
        imagePanel.setBackground(Color.decode("#ffffff"));
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        editFrame.add(imagePanel);

        // Display the current pet's image immediately
        displayImage(imagePath);

        // Existing values
        JTextField nameField = new JTextField(petName);
        nameField.setBounds(150, 80, 250, 30);

        JTextField typeField = new JTextField(petType);
        typeField.setBounds(150, 130, 250, 30);

        JTextField breedField = new JTextField(petBreed);
        breedField.setBounds(150, 180, 250, 30);

        JTextField ageField = new JTextField(petAge);
        ageField.setBounds(150, 230, 250, 30);

        JTextField imagePathField = new JTextField(imagePath);
        imagePathField.setBounds(150, 270, 250, 30);

        // Labels
        JLabel nameLabel = new JLabel("Pet Name:");
        nameLabel.setBounds(60, 80, 110, 40);
        nameLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(80, 130, 60, 30);
        typeLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));

        JLabel breedLabel = new JLabel("Breed:");
        breedLabel.setBounds(80, 180, 60, 30);
        breedLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(80, 230, 60, 30);
        ageLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));

        JLabel imagePathLabel = new JLabel("Image Path:");
        imagePathLabel.setBounds(50, 270, 100, 30);
        imagePathLabel.setFont(new Font("Century Gothic", Font.BOLD, 15));

        // Choose Image Button and Action
        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.setBounds(450, 340, 250, 30);
        chooseImageButton.setBackground(Color.decode("#7dd1cb"));
        chooseImageButton.setForeground(Color.decode("#ffffff"));
        chooseImageButton.setFont(new Font("Century Gothic", Font.PLAIN, 15));

        chooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(editFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String selectedImagePath = fileChooser.getSelectedFile().getAbsolutePath();
                    imagePathField.setText(selectedImagePath);
                    displayImage(selectedImagePath);
                }
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(200, 340, 100, 30);
        saveButton.setBackground(Color.decode("#7dd1cb"));
        saveButton.setForeground(Color.decode("#ffffff"));
        saveButton.setFont(new Font("Century Gothic", Font.PLAIN, 15));

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String updatedName = nameField.getText();
                String updatedType = typeField.getText();
                String updatedBreed = breedField.getText();
                String updatedAge = ageField.getText();
                String updatedImagePath = imagePathField.getText();

                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/pawpals_db",
                            "root", "eggs");

                    // Update the pet information
                    String updateQuery = "UPDATE available_pets SET name = ?, type = ?, breed = ?, age = ?, image_path = ? WHERE name = ? AND type = ? AND breed = ? AND age = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, updatedName);
                        updateStatement.setString(2, updatedType);
                        updateStatement.setString(3, updatedBreed);
                        updateStatement.setString(4, updatedAge);
                        updateStatement.setString(5, updatedImagePath);
                        updateStatement.setString(6, petName);
                        updateStatement.setString(7, petType);
                        updateStatement.setString(8, petBreed);
                        updateStatement.setString(9, petAge);
                        updateStatement.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(editFrame, "Pet information updated successfully");

                    availView();
                    editFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        editFrame.add(nameLabel);
        editFrame.add(nameField); 
        editFrame.add(typeLabel);
        editFrame.add(typeField);
        editFrame.add(breedLabel);
        editFrame.add(breedField);
        editFrame.add(ageLabel);
        editFrame.add(ageField);
        editFrame.add(imagePathLabel);
        editFrame.add(imagePathField);
        editFrame.add(chooseImageButton);
        editFrame.add(saveButton);

        editFrame.setVisible(true);
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
