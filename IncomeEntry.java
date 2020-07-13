import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingConstants;

public class IncomeEntry {

	private JFrame frame;
	private JTextField CostInDollarsTextField;
	private JTextField CostInCentsTextField;
	private JLabel WarningLabel;

/**
 *  Creates the Purchase Entry window 
 *  @param username A reference to the current user
 *  @param previousWindow A reference to the DisplayInfo Window to allow calls
 */
public IncomeEntry(String username, DisplayInfoFrame previousWindow ) {
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.setBounds(100, 100, 450, 170);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JLabel lblCost = new JLabel("How much money did you receive?");
		lblCost.setHorizontalAlignment(SwingConstants.CENTER);
		lblCost.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCost.setBounds(20, 27, 237, 27);
		frame.getContentPane().add(lblCost);
		
		CostInDollarsTextField = new JTextField();
		CostInDollarsTextField.setColumns(20);
		CostInDollarsTextField.setBounds(279, 34, 56, 20);
		frame.getContentPane().add(CostInDollarsTextField);
		
		JLabel lblDollarSign = new JLabel("$");
		lblDollarSign.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDollarSign.setBounds(267, 27, 36, 30);
		frame.getContentPane().add(lblDollarSign);
		
		JButton SubmitButton = new JButton("Submit Income");
		SubmitButton.addActionListener(new ActionListener() {
			/*
			 * Checks to ensure all data is valid, if it is, updates the tables and database with purchase 
			 */
			public void actionPerformed(ActionEvent e) {
				
				boolean canProcessEntry = true;
				boolean isValidNumber = true; 
				double savings = 0;
				String formattedCents = "";
				String combinedCost = "";
				
				if(!isOnlyNumbers(CostInDollarsTextField.getText()) || CostInDollarsTextField.getText().length() < 1) {
					isValidNumber = false;
					WarningLabel.setText("Enter a value for the dollar amount.");
					}
				
				if(CostInCentsTextField.getText() != null) {
					if(!isOnlyNumbers(CostInCentsTextField.getText())) {
						isValidNumber = false; 
						WarningLabel.setText("Enter a number for the cents amount.");
						System.out.println("Cents amount is wrong.");
					}
				}
					
				if(isValidNumber) {
					System.out.println("Length of Cents is: " + CostInCentsTextField.getText().length());
					if(CostInCentsTextField.getText().length() >= 2) {
						formattedCents = CostInCentsTextField.getText().substring(0,2);
						System.out.println(">2 Formatted Cents = " + formattedCents);
						System.out.println("Cents is greather than 2.");
					}
					
					else if(CostInCentsTextField.getText().length() == 1) {
						formattedCents = CostInCentsTextField.getText() + "0";
						System.out.println("Cents is 1.");
					}
					else if(CostInCentsTextField.getText().length() == 0) {
						formattedCents =  "00";
						System.out.println("Cents is empty.");
					}
									
					combinedCost = CostInDollarsTextField.getText() + "." + formattedCents;
					savings = Double.parseDouble(combinedCost);
				}
				
				if(canProcessEntry && isValidNumber) {
					previousWindow.updateSavings(savings);
					frame.dispose();
				}

			}
		});
		SubmitButton.setBounds(53, 95, 134, 25);
		frame.getContentPane().add(SubmitButton);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnCancel.setBounds(267, 96, 134, 25);
		frame.getContentPane().add(btnCancel);
		
		WarningLabel = new JLabel(" ");
		WarningLabel.setHorizontalAlignment(SwingConstants.CENTER);
		WarningLabel.setBounds(20, 67, 392, 14);
		frame.getContentPane().add(WarningLabel);
		
		CostInCentsTextField = new JTextField();
		CostInCentsTextField.setColumns(20);
		CostInCentsTextField.setBounds(345, 34, 21, 20);
		frame.getContentPane().add(CostInCentsTextField);
		
		JLabel lblDecimal = new JLabel(".");
		lblDecimal.setHorizontalAlignment(SwingConstants.CENTER);
		lblDecimal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDecimal.setBounds(330, 30, 21, 30);
		frame.getContentPane().add(lblDecimal);
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
