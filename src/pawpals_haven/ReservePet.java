package pawpals_haven;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ReservePet {
	private JFrame frame;
    private JTable table;
    private String selectedImagePath;
    
    public void reserveView(){
           frame = new JFrame();
            
                //CLOSE
        JLabel exit = new JLabel("X");
        exit.setForeground(Color.decode("#ffffff"));
        exit.setBounds(765, 20, 100, 20);
        exit.setFont(new Font("Century Gothic", Font.BOLD, 20));
        frame.add(exit);
        exit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                 frame.dispose();
            }
        });
        //TOP PANEL
        JPanel panel = new  JPanel();
	panel.setBounds(0, 0, 800, 60);
	panel.setBackground(Color.decode("#44abaa"));
	frame.add(panel);
        
        // ADD A PET Label
        JLabel petLabel = new JLabel("RESERVED PETS");
        petLabel.setForeground(Color.decode("#ffffff"));
        petLabel.setFont(new Font("Century Gothic", Font.BOLD, 40));
        panel.add(petLabel);
        
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/pawpals_db", "root", "eggs");

            String selectQuery = "SELECT * FROM reserved_pets ORDER BY name";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();


            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                model.addRow(rowData);
            }

            table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedImagePath = (String) table.getValueAt(selectedRow, 5); 
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);
            frame.getContentPane().add(scrollPane);
            scrollPane.setBounds(50, 80, 500, 430);

            JButton confirmBtn = new JButton("Confirm Adaptation");
            confirmBtn.setBounds(570, 200, 200, 50);
            confirmBtn.setFont(new Font("Century Gothic", Font.BOLD, 15));
            confirmBtn.setBackground(Color.decode("#7dd1cb"));
            confirmBtn.setForeground(Color.decode("#ffffff"));
            confirmBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    confirmAdaptation();
                }
            });

            JButton cancelBtn = new JButton("Cancel Reservation");
            cancelBtn.setBounds(570, 275, 200, 50);
            cancelBtn.setFont(new Font("Century Gothic", Font.BOLD, 15));
            cancelBtn.setBackground(Color.decode("#7dd1cb"));
            cancelBtn.setForeground(Color.decode("#ffffff"));
            cancelBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cancelReservation();
                }
            });

            frame.add(confirmBtn);
            frame.add(cancelBtn);

            frame.setSize(800, 550);
            frame.setResizable(false);
            frame.setLayout(null);
            frame.setUndecorated(true);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setFocusable(true);
            frame.getContentPane().setBackground(Color.decode("#e55e65"));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void confirmAdaptation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/pawpals_db", "root", "eggs");

                Object petNameObj = table.getValueAt(selectedRow, 0);
                Object petTypeObj = table.getValueAt(selectedRow, 1);
                Object petBreedObj = table.getValueAt(selectedRow, 2);
                Object petAgeObj = table.getValueAt(selectedRow, 3);
                Object adopterObj = table.getValueAt(selectedRow, 4); 

                String petName = (petNameObj instanceof Integer) ? ((Integer) petNameObj).toString() : (String) petNameObj;
                String petType = (petTypeObj instanceof Integer) ? ((Integer) petTypeObj).toString() : (String) petTypeObj;
                String petBreed = (petBreedObj instanceof Integer) ? ((Integer) petBreedObj).toString() : (String) petBreedObj;
                String petAge = (petAgeObj instanceof Integer) ? ((Integer) petAgeObj).toString() : (String) petAgeObj;
                String adopter = (adopterObj instanceof Integer) ? ((Integer) adopterObj).toString() : (String) adopterObj;

                // Remove from reserved_pets
                String deleteQuery = "DELETE FROM reserved_pets WHERE name = ? AND type = ? AND breed = ? AND age = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setString(1, petName);
                deleteStatement.setString(2, petType);
                deleteStatement.setString(3, petBreed);
                deleteStatement.setString(4, petAge);
                deleteStatement.executeUpdate();

                // Add to adopted_pets
                String insertQuery = "INSERT INTO adopted_pets (name, type, breed, age, adopter) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, petName);
                insertStatement.setString(2, petType);
                insertStatement.setString(3, petBreed);
                insertStatement.setString(4, petAge);
                insertStatement.setString(5, adopter);
                insertStatement.executeUpdate();

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(selectedRow);


                frame.dispose();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void cancelReservation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/pawpals_db", "root", "eggs");

                Object petNameObj = table.getValueAt(selectedRow, 0);
                Object petTypeObj = table.getValueAt(selectedRow, 1);
                Object petBreedObj = table.getValueAt(selectedRow, 2);
                Object petAgeObj = table.getValueAt(selectedRow, 3);

                String petName = (petNameObj instanceof Integer) ? ((Integer) petNameObj).toString() : (String) petNameObj;
                String petType = (petTypeObj instanceof Integer) ? ((Integer) petTypeObj).toString() : (String) petTypeObj;
                String petBreed = (petBreedObj instanceof Integer) ? ((Integer) petBreedObj).toString() : (String) petBreedObj;
                String petAge = (petAgeObj instanceof Integer) ? ((Integer) petAgeObj).toString() : (String) petAgeObj;

                // Remove from reserved_pets
                String deleteQuery = "DELETE FROM reserved_pets WHERE name = ? AND type = ? AND breed = ? AND age = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setString(1, petName);
                deleteStatement.setString(2, petType);
                deleteStatement.setString(3, petBreed);
                deleteStatement.setString(4, petAge);
                deleteStatement.executeUpdate();

                // Add back to available_pets with the image path
                String insertQuery = "INSERT INTO available_pets (name, type, breed, age, image_path) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, petName);
                insertStatement.setString(2, petType);
                insertStatement.setString(3, petBreed);
                insertStatement.setString(4, petAge);
                insertStatement.setString(5, selectedImagePath);  // Use the stored image path
                insertStatement.executeUpdate();

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(selectedRow);

                frame.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}