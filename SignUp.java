package javatwo;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JPasswordField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class SignUp extends JFrame implements ActionListener {

	Color fgColor = Color.YELLOW, bgColor = Color.BLACK;
	Font font;
	JTextField userNameField, firstnameField, lastNameField;
	JPasswordField passwordField, confirmPasswordField;
	JButton submit, clear;
	Statement st2;

	public SignUp() {

		font = new Font("Ink Free", Font.ITALIC, 30);

		// Create database connection and statement
		st2 = createStatement();

		JPanel northPanel = new JPanel();
		add(northPanel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(bgColor);
		add(centerPanel);

		centerPanel.setLayout(new GridLayout(5, 2));

		centerPanel.add(createLabel("First Name"));
		firstnameField = createTextField();
		centerPanel.add(firstnameField);

		centerPanel.add(createLabel("Last Name"));
		lastNameField = createTextField();
		centerPanel.add(lastNameField);

		centerPanel.add(createLabel("User Name"));
		userNameField = createTextField();
		centerPanel.add(userNameField);

		centerPanel.add(createLabel("Password"));
		passwordField = createPasswordField();
		centerPanel.add(passwordField);

		centerPanel.add(createLabel(" Confirm Password"));
		confirmPasswordField = createPasswordField();
		centerPanel.add(confirmPasswordField);

		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.BLUE);
		add(southPanel, BorderLayout.SOUTH);

		clear = createButton("Reset");
		southPanel.add(clear);

		submit = createButton("Submit");
		southPanel.add(submit);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Sign Up");
		setVisible(true);
		pack();
		setSize(500, 500);
		setLocationRelativeTo(null);

	}

	private JButton createButton(String txt) {
		JButton btn = new JButton(txt);
		btn.setForeground(fgColor);
		btn.setBackground(bgColor);
		btn.setFont(font);
		btn.setFocusable(false);
		btn.addActionListener(this);

		return btn;
	}

	private JTextField createTextField() {
		JTextField txtField = new JTextField(50);
		txtField.setFont(font);
		txtField.setForeground(fgColor);
		txtField.setBackground(bgColor);
		txtField.setCaretColor(Color.CYAN);

		return txtField;
	}

	private JPasswordField createPasswordField() {
		JPasswordField txtField = new JPasswordField(50);
		txtField.setFont(font);
		txtField.setForeground(fgColor);
		txtField.setBackground(bgColor);
		txtField.setCaretColor(Color.CYAN);

		return txtField;
	}

	private JLabel createLabel(String txt) {
		JLabel label = new JLabel(txt);
		label.setFont(font);
		label.setForeground(fgColor);
		label.setHorizontalAlignment(JLabel.CENTER);

		return label;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == clear) {
			firstnameField.setText("");
			lastNameField.setText("");
			userNameField.setText("");
			passwordField.setText("");
			confirmPasswordField.setText("");
		}

		else if (e.getSource() == submit) {

			// Get input values from text fields
			String firstName = firstnameField.getText();
			String lastName = lastNameField.getText();
			String userName = userNameField.getText();
			String password = new String(passwordField.getPassword());
			String confirmPassword = new String(confirmPasswordField.getPassword());
			
			// All Fields must completed before it can submit.
			if (firstName.isBlank() || lastName.isBlank() || userName.isBlank() || password.isBlank()
					|| confirmPassword.isBlank()) {
				JOptionPane.showMessageDialog(this, "Please fill in all fields.");
				return;
			}
			// First name and last name must only accept letters
			if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
	            JOptionPane.showMessageDialog(this, "First name and last name should only contain letters.");
	            return;
	        }
			
			//User name 
			if (!userName.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$")) {
	            JOptionPane.showMessageDialog(this, "Invalid username. Username should contain at least one letter and one number.");
	            return;
	        }
			// ConfirmPassword
			if (password.equals(confirmPassword)) {
				JOptionPane.showMessageDialog(this, "Password confirmed!");
			} else {
				JOptionPane.showMessageDialog(this, "Passwords do not match!");
				return;
			}

			// Inserting data into the database table
			try {
				String insertValue = String.format("INSERT INTO TES (first_name, last_name, username, password)"
						+ " VALUES ('%s', '%s', '%s', '%s')", firstName, lastName, userName, password);
				st2.executeUpdate(insertValue);
				JOptionPane.showMessageDialog(this, "Data submitted successfully!");
				//After Data submits clear page
				firstnameField.setText("");
				lastNameField.setText("");
				userNameField.setText("");
				passwordField.setText("");
				confirmPasswordField.setText("");
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage());
			}
		}
    }
	

	public static void main(String[] args) {
		new SignUp();
	}

	public Statement createStatement() {
		String user = "root", password = "", url = "jdbc:mysql://localhost:3306/welcome";
		Statement st = null;
		try {
			Connection conn = DriverManager.getConnection(url, user, password);
			System.out.println("Driver loaded");
			st = conn.createStatement();
			System.out.println("Statement created");
			String query = "CREATE TABLE TES (ID INT AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(50),"
					+ " last_name VARCHAR(50), username VARCHAR(50), password VARCHAR(50));";
			st.execute(query);
			System.out.println("Query executed");
		} catch (SQLException e) {
			System.out.println("Error occured: " + e.getMessage());
		}
		return st;
	}

}
