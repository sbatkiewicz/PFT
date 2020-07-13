import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import java.awt.Font;

public class LoginFrame {

	private JFrame frame;
	private JLabel lblPassword;
	private JPasswordField passwordField;
	private JTextField usernameField;
	private JLabel lblUsername;
	private JLabel lblMessage;
	 
	Database db; // Reference to the database class
	
/**
 * Creates the Login Menu UI components 
 * @param existingConnection The original reference to the database
 */
public LoginFrame(Database existingConnection) 
	{
		db = existingConnection;
		frame = new JFrame("PFT - Log in");
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				db.closeDatabase();
			}
		});
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		ImageIcon img = new ImageIcon("PFT_icon.png");
		frame.setIconImage(img.getImage());
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Log In");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String u = usernameField.getText();
				@SuppressWarnings("deprecation")
				String p = passwordField.getText();

				AttemptLogin(u, p);
			}
		});
		btnNewButton.setBounds(147, 272, 106, 23);
		panel.add(btnNewButton);
		
		lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
		lblUsername.setBounds(171, 184, 68, 23);
		panel.add(lblUsername);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		lblPassword.setBounds(174, 218, 65, 23);
		panel.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(249, 219, 130, 23);
		panel.add(passwordField);
		
		usernameField = new JTextField();
		usernameField.setBounds(249, 185, 130, 23);
		panel.add(usernameField);
		usernameField.setColumns(10);
		
		lblMessage = new JLabel("");
		lblMessage.setBounds(260, 245, 130, 23);
		panel.add(lblMessage);
		
		JButton btnNewButton_1 = new JButton("Create Account");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewAccountFrame newAccountWindow = new NewAccountFrame(db);
			}
		});
		btnNewButton_1.setBounds(284, 272, 130, 23);
		panel.add(btnNewButton_1);
		
		JLabel imageLabel = new JLabel(" ");
		imageLabel.setIcon(new ImageIcon("PFT_image.png"));
		imageLabel.setBounds(10, 10, 195, 80);
		panel.add(imageLabel);

	}

/**
 * Checks if login is succesful, and creates next window if it is, otherwise displays incorrect message
 * @param u The username to be checked against the database
 * @param p The password to be combined with the username to check if user exists
 */
public void AttemptLogin(String u, String p) {
	if(db.checkUser(u,p)) {
		lblMessage.setText("Welcome!");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisplayInfoFrame info = new DisplayInfoFrame(db);
					info.currentUser = u;
					info.setUserData(db.getUserData(u));
					info.displayPurchases();
					info.displayCostOfCategories();

				} catch (Exception e) {
					System.out.println("Issue occured during creation of DisplayInfoFrame.");
					e.printStackTrace();
				}
			}
		});
		System.out.println("Finished creating the DisplayInfoFrame.");
		frame.dispose();

	}
	else {
		lblMessage.setText("Incorrect!");
	}
}


}
