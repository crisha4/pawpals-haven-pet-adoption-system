package pawpals_haven;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AdoptedPet {

    public void adoptView() {
        JFrame frame = new JFrame();

        // CLOSE
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

        // TOP PANEL
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 800, 60);
        panel.setBackground(Color.decode("#44abaa"));
        frame.add(panel);

        // ADD A PET Label
        JLabel petLabel = new JLabel("ADOPTED PETS");
        petLabel.setForeground(Color.decode("#ffffff"));
        petLabel.setFont(new Font("Century Gothic", Font.BOLD, 40));
        panel.add(petLabel);


        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/pawpals_db", "root", "eggs");

            String selectQuery = "SELECT * FROM adopted_pets";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            String[] columnNames = {"Name", "Type", "Breed", "Age", "Adopter"};

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                String petName = resultSet.getString("name");
                String petType = resultSet.getString("type");
                String petBreed = resultSet.getString("breed");
                String petAge = resultSet.getString("age");
                String adopter = resultSet.getString("adopter");

                Object[] rowData = {petName, petType, petBreed, petAge, adopter};
                model.addRow(rowData);
            }

            JTable table = new JTable(model);

            JScrollPane scrollPane = new JScrollPane(table);
            frame.getContentPane().add(scrollPane);
            scrollPane.setBounds(80, 80, 650, 430);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        frame.setSize(800, 550);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.getContentPane().setBackground(Color.decode("#e55e65"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}