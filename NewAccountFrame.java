import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class NewAccountFrame {
	
	Database db;
	private JFrame frame;
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField savingsTextField;
	private JTextField usernameTextField;
	private JPasswordField passwordTextField;
	private JLabel WarningLabel;
	
	/**
	 * Creates the NewAccount window components
	 */
	public NewAccountFrame(Database existingConnection) {
		db = existingConnection;
		frame = new JFrame("PFT - New Account");
		frame.setResizable(false);
		frame.setSize(300, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JButton submitButton = new JButton("Create Account");
		submitButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			/**
			 * Checks if all fields contain valid information, and create a new user in the database if they are
			 */
			public void actionPerformed(ActionEvent e) {				
				boolean isNotNull = true;
				String[] textFields = new String[5];
				
				textFields[0] = firstNameTextField.getText();
				textFields[1] = lastNameTextField.getText();
				textFields[2] = savingsTextField.getText();
				if(!isOnlyNumbers(textFields[2])) {
					System.out.println("Enter only numbers for savings!");
					WarningLabel.setText("Enter only numbers for savings.");
					isNotNull = false;
				}
				textFields[3] = usernameTextField.getText();
				if(!db.checkExistingUserName(textFields[3])) {
					WarningLabel.setText("That Username already exists.");
					isNotNull = false;
				}
				textFields[4] = passwordTextField.getText();
				
				for( String field : textFields) {
					if (field == null) {
						isNotNull = false;
					}
				}
				
				if(isNotNull){					
					db.AddUserInfo(textFields);	
					frame.dispose();
				}
			
			}
		});
		submitButton.setBounds(72, 321, 125, 29);
		frame.getContentPane().add(submitButton);
		
		JLabel FirstNameLabel = new JLabel("First Name: ");
		FirstNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		FirstNameLabel.setBounds(44, 50, 73, 20);
		frame.getContentPane().add(FirstNameLabel);
		
		JLabel LastNameLabel = new JLabel("Last Name: ");
		LastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		LastNameLabel.setBounds(44, 100, 73, 20);
		frame.getContentPane().add(LastNameLabel);
		
		JLabel lblCurrentSavings = new JLabel("Current Savings: $");
		lblCurrentSavings.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCurrentSavings.setBounds(10, 151, 115, 20);
		frame.getContentPane().add(lblCurrentSavings);
		
		JLabel lblUsername = new JLabel("Username: ");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUsername.setBounds(44, 200, 73, 20);
		frame.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(50, 250, 67, 20);
		frame.getContentPane().add(lblPassword);
		
		firstNameTextField = new JTextField();
		firstNameTextField.setBounds(127, 53, 147, 20);
		frame.getContentPane().add(firstNameTextField);
		firstNameTextField.setColumns(10);
		
		lastNameTextField = new JTextField();
		lastNameTextField.setColumns(10);
		lastNameTextField.setBounds(127, 103, 147, 20);
		frame.getContentPane().add(lastNameTextField);
		
		savingsTextField = new JTextField();
		savingsTextField.setColumns(10);
		savingsTextField.setBounds(127, 153, 147, 20);
		frame.getContentPane().add(savingsTextField);
		
		usernameTextField = new JTextField();
		usernameTextField.setColumns(10);
		usernameTextField.setBounds(127, 203, 147, 20);
		frame.getContentPane().add(usernameTextField);
		
		passwordTextField = new JPasswordField();
		passwordTextField.setColumns(10);
		passwordTextField.setBounds(127, 253, 147, 20);
		frame.getContentPane().add(passwordTextField);
		
		JLabel TitleLabel = new JLabel("Account Creation");
		TitleLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		TitleLabel.setBounds(90, 11, 107, 20);
		frame.getContentPane().add(TitleLabel);
		
		WarningLabel = new JLabel(" ");
		WarningLabel.setHorizontalAlignment(SwingConstants.CENTER);
		WarningLabel.setBounds(10, 296, 264, 14);
		frame.getContentPane().add(WarningLabel);
		frame.setVisible(true);
		
		ImageIcon img = new ImageIcon("PFT_icon.png");
		frame.setIconImage(img.getImage());

	}
	
	/**
	 * Checks the provided String to see if it contains any letters
	 * @param potentialCost The provided String to be checked for letters
	 * @return Method returns true if provided String contains no letters, and returns false otherwise 
	 */
	public boolean isOnlyNumbers(String potentialCost) {
		for(int i = 0; i < potentialCost.length(); i++) {
			if(!Character.isDigit(potentialCost.charAt(i))){
				return false;
			}		
		}	
		return true;
	}
	
	
}
