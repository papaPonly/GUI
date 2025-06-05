package pages;

import dal.students.StudentDAO;
import models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class StudentPage extends JFrame {
    private final StudentDAO studentDao = new StudentDAO();
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField studentNumberField;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField programField;
    private final JSpinner levelSpinner;
    private JButton addButton, updateButton, deleteButton;

    public StudentPage() {
        setTitle("Student Management");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 250, 255)); // Soft blue

        // Font settings
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 20);

        // Title
        JLabel title = new JLabel("Student CRUD System", SwingConstants.CENTER);
        title.setFont(titleFont);
        title.setForeground(new Color(25, 25, 112));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        studentNumberField = new JTextField(15);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        programField = new JTextField(15);
        levelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        studentNumberField.setFont(fieldFont);
        firstNameField.setFont(fieldFont);
        lastNameField.setFont(fieldFont);
        programField.setFont(fieldFont);
        levelSpinner.setFont(fieldFont);

        int row = 0;
        addFormRow(formPanel, gbc, row++, "Student Number:", studentNumberField, labelFont);
        addFormRow(formPanel, gbc, row++, "First Name:", firstNameField, labelFont);
        addFormRow(formPanel, gbc, row++, "Last Name:", lastNameField, labelFont);
        addFormRow(formPanel, gbc, row++, "Program:", programField, labelFont);
        addFormRow(formPanel, gbc, row++, "Level:", levelSpinner, labelFont);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 250, 255));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 250, 255));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.WEST);

        // Table
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Student Number", "First Name", "Last Name", "Program", "Level"}, 0
        );
        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student Records"));
        add(scrollPane, BorderLayout.CENTER);

        // Events
        loadStudents();

        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                studentNumberField.setText(tableModel.getValueAt(row, 1).toString());
                firstNameField.setText(tableModel.getValueAt(row, 2).toString());
                lastNameField.setText(tableModel.getValueAt(row, 3).toString());
                programField.setText(tableModel.getValueAt(row, 4).toString());
                levelSpinner.setValue((int) tableModel.getValueAt(row, 5));
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field, Font font) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(font);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(65, 105, 225)); // Royal blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(100, 30));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 230, 250));
        table.getTableHeader().setForeground(new Color(0, 51, 102));
        table.setGridColor(new Color(220, 220, 220));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        List<Student> students = studentDao.getAllStudents();
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getStudentNumber(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getProgram(),
                    student.getLevel()
            });
        }
    }

    private void addStudent() {
        String studentNumber = studentNumberField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String program = programField.getText();
        int level = (int) levelSpinner.getValue();

        if (!studentNumber.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !program.isEmpty()) {
            studentDao.addStudent(new Student(0, studentNumber, firstName, lastName, program, level));
            loadStudents();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }

    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            String studentNumber = studentNumberField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String program = programField.getText();
            int level = (int) levelSpinner.getValue();

            if (!studentNumber.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !program.isEmpty()) {
                studentDao.updateStudent(new Student(id, studentNumber, firstName, lastName, program, level));
                loadStudents();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            studentDao.deleteStudent(id);
            loadStudents();
            clearFields();
        }
    }

    private void clearFields() {
        studentNumberField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        programField.setText("");
        levelSpinner.setValue(1);
    }
}

