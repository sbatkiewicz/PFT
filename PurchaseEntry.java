import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class PurchaseEntry {

	private JFrame frame;
	private JTextField DescriptionTextField;
	private JTextField CostInDollarsTextField;
	private JTextField CostInCentsTextField;
	private JLabel WarningLabel;
	
	Database db;

/**
 *  Creates the Purchase Entry window 
 *  @param username A reference to the current user
 *  @param previousWindow A reference to the DisplayInfo Window to allow calls
 *  @param existingConnection A reference to the original database object
 */
public PurchaseEntry(String username, DisplayInfoFrame previousWindow, Database existingConnection ) {
		
	db = existingConnection;
		frame = new JFrame();
		frame.setResizable(false);
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.setBounds(100, 100, 450, 170);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JLabel lblDescriptionLabel = new JLabel("Description\r\n");
		lblDescriptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDescriptionLabel.setBounds(25, 11, 79, 14);
		frame.getContentPane().add(lblDescriptionLabel);
		
		JLabel lblCost = new JLabel("Cost");
		lblCost.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCost.setBounds(194, 11, 36, 14);
		frame.getContentPane().add(lblCost);
		
		JLabel lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCategory.setBounds(331, 6, 70, 25);
		frame.getContentPane().add(lblCategory);
		
		DescriptionTextField = new JTextField();
		DescriptionTextField.setBounds(10, 37, 127, 20);
		frame.getContentPane().add(DescriptionTextField);
		DescriptionTextField.setColumns(20);
		
		CostInDollarsTextField = new JTextField();
		CostInDollarsTextField.setColumns(8);
		CostInDollarsTextField.setBounds(160, 36, 55, 20);
		frame.getContentPane().add(CostInDollarsTextField);
		
		JLabel lblDollarSign = new JLabel("$");
		lblDollarSign.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDollarSign.setBounds(147, 30, 36, 30);
		frame.getContentPane().add(lblDollarSign);
		
		JComboBox<String> cbCategoryComboBox = new JComboBox<String>();
		cbCategoryComboBox.setBounds(291, 36, 121, 22);
		cbCategoryComboBox.addItem("Housing");
		cbCategoryComboBox.addItem("Transportation");
		cbCategoryComboBox.addItem("Groceries");
		cbCategoryComboBox.addItem("Utilities");
		cbCategoryComboBox.addItem("Clothing");
		cbCategoryComboBox.addItem("Insurance");
		cbCategoryComboBox.addItem("Debt");
		cbCategoryComboBox.addItem("Education");
		cbCategoryComboBox.addItem("Retirement");
		cbCategoryComboBox.addItem("Entertainment");
		cbCategoryComboBox.addItem("Self");
		cbCategoryComboBox.addItem("Gift/Donation");
		frame.getContentPane().add(cbCategoryComboBox);
		
		JButton SubmitButton = new JButton("Submit Purchase");
		SubmitButton.addActionListener(new ActionListener() {
			/*
			 * Checks to ensure all data is valid, if it is, updates the tables and database with purchase 
			 */
			public void actionPerformed(ActionEvent e) {
				
				boolean canProcessEntry = true;
				boolean isValidNumber = true; 
				double cost = 0;
				String formattedCents = "";
				String combinedCost = "";
			
				if(!isOnlyNumbers(CostInDollarsTextField.getText()) || CostInDollarsTextField.getText().length() < 1) {
					isValidNumber = false;
					WarningLabel.setText("Enter a number for the dollar amount.");
					System.out.println("Dollar amount is wrong.");
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
					cost = Double.parseDouble(combinedCost);
				}
				
				String desc = DescriptionTextField.getText();
				if(desc == null || DescriptionTextField.getText().length() < 1) {
					canProcessEntry = false;
					WarningLabel.setText("Description cannot be empty.");

				}
				String category = (String) cbCategoryComboBox.getSelectedItem();
						
				if(isValidNumber && canProcessEntry) {
					System.out.println("About to add purchase of: " + desc + " " + combinedCost + " " + category);
					db.InsertNewPurchase(username, desc, cost, category);				
					previousWindow.updateTable(desc, combinedCost, (String) category);				
					previousWindow.purchasesList.add(new Purchase(desc, cost, (String) category));
					previousWindow.displayCostOfCategories();
					previousWindow.updateSavings(0 - cost);
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
		CostInCentsTextField.setColumns(2);
		CostInCentsTextField.setBounds(225, 36, 24, 20);
		frame.getContentPane().add(CostInCentsTextField);
		
		JLabel lblNewLabel = new JLabel(".");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(218, 43, 31, 14);
		frame.getContentPane().add(lblNewLabel);
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

public String formatCents(String potentialCents) {
	System.out.println("Length of Cents is: " + CostInCentsTextField.getText().length());
	if(potentialCents.length() > 2) {
		String formattedCents = potentialCents.substring(0,3);
		System.out.println("Valid Number Cents is greather than 2.");
	}
	
	else if(potentialCents.length() == 1) {
		String formattedCents = potentialCents + "0";
		System.out.println("Valid Number Cents is 1.");
	}
	else if(potentialCents.length() == 0) {
		String formattedCents = potentialCents + "00";
		System.out.println("Valid Number Cents is empty.");
	}
	
	
	
	return potentialCents;
}

}
