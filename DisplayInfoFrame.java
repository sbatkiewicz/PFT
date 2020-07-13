import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.text.DecimalFormat;
import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;

public class DisplayInfoFrame {

	private JFrame frame;
	private JLabel Label_Name;
	private JLabel Label_Savings;
	private JLabel Label_FrequentCategory;
	private JTable purchaseTable;
	private TableModel purchaseTabelModel;
	private JScrollPane scrollPanePurchases;
	private JTable categoryTable;	
	private JScrollPane scrollPaneCategories;
	
	DecimalFormat costFormat = new DecimalFormat("##.##");
	// User's current savings from database, updated after changes
	double currentSavings = 0;
	// refernce to currently logged in user
	public String currentUser = null; 
	// List to be filled with all purchases that belong to logged in user
	ArrayList<Purchase> purchasesList = new ArrayList<>();   
	
	Database db;
	
	/**
	 * Constructs the Info window's components
	 * @param existingConnection A reference to the database class
	 */
	public DisplayInfoFrame(Database existingConnection) {
		db = existingConnection;
			
		frame = new JFrame("PFT - Purchases - View");
		frame.setResizable(false);
		frame.setSize(700, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				db.updateUserSavings(currentUser, currentSavings);
				db.closeDatabase();
			}
		});
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		ImageIcon img = new ImageIcon("PFT_icon.png");
		frame.setIconImage(img.getImage());
		
		Label_Name = new JLabel("First Name: ");
		Label_Name.setBounds(214, 18, 112, 20);
		frame.getContentPane().add(Label_Name);
		
		
		Label_Savings = new JLabel("Current Savings: $");
		Label_Savings.setBounds(459, 17, 178, 20);
		frame.getContentPane().add(Label_Savings);
		
		JLabel lblWelcome = new JLabel("Welcome,");
		lblWelcome.setFont(new Font("Arial", Font.PLAIN, 20));
		lblWelcome.setBounds(120, 10, 98, 30);
		frame.getContentPane().add(lblWelcome);
		
		JLabel Label_FrequentLabel = new JLabel("Most Frequent Category: ");
		Label_FrequentLabel.setBounds(10, 62, 142, 20);
		frame.getContentPane().add(Label_FrequentLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 43, 694, 8);
		frame.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator(SwingConstants.VERTICAL);
		separator_1.setBounds(294, 49, 9, 529);
		frame.getContentPane().add(separator_1);
		
		JLabel purchaseHeaderLabel = new JLabel("                    Purchase History");
		purchaseHeaderLabel.setVerticalAlignment(SwingConstants.TOP);
		purchaseHeaderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		purchaseHeaderLabel.setBounds(356, 49, 248, 20);
		frame.getContentPane().add(purchaseHeaderLabel);
		
		JButton PurchaseButton = new JButton("New Purchase");
		PurchaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PurchaseEntry entryForm = new PurchaseEntry(currentUser, getSelf(), db);
				
			}
		});
		PurchaseButton.setBounds(349, 519, 119, 30);
		frame.getContentPane().add(PurchaseButton);
		
		JButton IncomeButton = new JButton("Add Income");
		IncomeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IncomeEntry entryForm = new IncomeEntry(currentUser, getSelf());
				
			}
		});
		IncomeButton.setBounds(514, 519, 119, 30);
		frame.getContentPane().add(IncomeButton);
		
		JLabel imageLabel = new JLabel("");
		imageLabel.setIcon(new ImageIcon("PFT_imageSmall.png"));
		imageLabel.setBounds(10, 10, 85, 28);
		frame.getContentPane().add(imageLabel);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 85, 28);
		frame.getContentPane().add(panel);
		
		ImageIcon logoutIcon = new ImageIcon("logoutIcon.png");
		JButton LogoutButton = new JButton(logoutIcon);
		LogoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.updateUserSavings(currentUser, currentSavings);
				System.out.println("You've logged out!");
				frame.dispose();
				LoginFrame loginWindow = new LoginFrame(db);
			}
		});
		LogoutButton.setBounds(634, 10, 30, 30);
		Border emptyBorder = BorderFactory.createEmptyBorder();
		LogoutButton.setBorder(emptyBorder);
		frame.getContentPane().add(LogoutButton);
		
	}
	
	/**
	 * Assigns provided value to the Savings label
	 * @param savings The integer value to assign to the Savings label
	 */		
	public void setSavings(double savings) {
		Label_Savings.setText("Current Savings: " + costFormat.format(savings));
	}
	
	/**
	 * Assigns concatinated values to the Name and Savings labels with provided values
	 * @param userData A String array used to populate the labels
	 */
	public void setUserData(String[] userData) {				
		Label_Name.setText("" + userData[0] + " " + userData[1]);
		currentSavings = Double.parseDouble(userData[2]);
		Label_Savings.setText("Current Savings:  $" + costFormat.format(currentSavings));
		System.out.println("Finished updating userdata.");	
	}
	
	/**
	 * Populates the Purchase Table with the Purchase Arraylist. Table has three coloumns and Purchase list.size coloumns
	 */
	public void createPurchaseTableData() {
		purchasesList = (ArrayList<Purchase>) db.getPurchaseHistory(currentUser);
		String[] columnNames = { "Description", "Cost", "Category" };
		String[][] data = new String[purchasesList.size()][3];
		
		int count = 0;
		for(Purchase purchase : purchasesList) {		
			
			data[count][0] = purchase.getDescription();
			data[count][1] = "$" + purchase.getFormattedCost();
			data[count][2] = purchase.getCategory();
				
			count++;
		}
		purchaseTabelModel = new DefaultTableModel(data, columnNames);
		purchaseTable = new JTable(purchaseTabelModel);
	}
	
	/**
	 * Adds Purchase table to the window, and formats the table's data
	 */
	public void drawPurchaseTable() {
		scrollPanePurchases = new JScrollPane();
		scrollPanePurchases.setBounds(330, 77, 330, 409);
		frame.getContentPane().add(scrollPanePurchases);
		
		setColoumnWidths(purchaseTable, 130, 70, 110);
			
		purchaseTable.setFont(new Font("Arial", Font.PLAIN, 14));
		purchaseTable.setRowHeight(25);
		
		allignTableColoumns(purchaseTable, scrollPanePurchases, JLabel.LEFT);
	}

	/**
	 * Simplified function to be called on the DisplayInfoFrame object
	 */
	public void displayPurchases() {
		createPurchaseTableData();
		drawPurchaseTable();

		}
	
	/**
	 * Displays the most frequent purchase category in the current user's Purchase List
	 */
	public void findMostFrequentCategory() {
		
		Map<String, Integer> frequency = new HashMap<String, Integer>();
		for(Purchase purchase : purchasesList) {
			if(!frequency.containsKey(purchase.getCategory())) {
				frequency.put(purchase.getCategory(), 0);
			}
			else {
				frequency.put(purchase.getCategory(), frequency.get(purchase.getCategory()) + 1 );
			}

		}

		Map.Entry<String, Integer> mostFrequent = null;
		for(Map.Entry<String, Integer> currentEntry : frequency.entrySet()) {
			
			if(mostFrequent == null || currentEntry.getValue().compareTo(mostFrequent.getValue()) > 0) {
				mostFrequent = currentEntry;
			}
						
		}		
		Label_FrequentCategory = new JLabel(mostFrequent.getKey());
		Label_FrequentCategory.setFont(new Font("Arial", Font.PLAIN, 20));
		Label_FrequentCategory.setBounds(10, 81, 125, 20);
		frame.getContentPane().add(Label_FrequentCategory);
	}
	
	/**
	 * Adds sorted categories and total cost of each category to Category table
	 */
	public void createCategoryTableData(){
		
		Map<String, Double> categoryMap = new HashMap<String, Double>();
		double totalMoneySpent = 0;

		// Populates categoryMap with existing categories 
		for(Purchase purchase : purchasesList) {
			if(!categoryMap.containsKey(purchase.getCategory())) {
				categoryMap.put(purchase.getCategory(), purchase.getCost());
				totalMoneySpent += purchase.getCost();
			}
			else {
				categoryMap.put(purchase.getCategory(), (categoryMap.get(purchase.getCategory()) + purchase.getCost()));
				totalMoneySpent += purchase.getCost();
			}
		}

		LinkedHashMap<String, Double> sortedCategoryMap = new LinkedHashMap<>();
		// Adds categoryMap data to sortedCategoryMap in Descending order of total cost 
		categoryMap.entrySet()
		.stream()
   		.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
   		.forEachOrdered(x -> sortedCategoryMap.put(x.getKey(), x.getValue()));

		String[] columnNames = { "Category", "Total Cost", "Percent" };
		String[][] data = new String[categoryMap.size()][3];

		int count = 0;
		for(Map.Entry<String,Double> entry : sortedCategoryMap.entrySet()) {		
	
			data[count][0] = entry.getKey();
			data[count][1] = "$" + getFormattedCost(entry.getValue());	
			
			// Calculating and rounding percentages for each category for cost
			double percentageFormatted = Math. round((entry.getValue() / totalMoneySpent * 100) * 10) / 10.0;
			data[count][2] = percentageFormatted + "%";
		
			count++;
		}
		TableModel model = new DefaultTableModel(data, columnNames);
		categoryTable = new JTable(model);
		}
	
	/**
	 * Adds Category table to the window, and formats the table's data 
	 */
	public void drawCategoriesTable() {
		scrollPaneCategories = new JScrollPane();
		scrollPaneCategories.setBounds(10, 135, 250, 225);
		frame.getContentPane().add(scrollPaneCategories);
		
		setColoumnWidths(categoryTable, 100, 75, 55);
			
		categoryTable.setFont(new Font("Arial", Font.PLAIN, 14));
		categoryTable.setRowHeight(20);
				
		allignTableColoumns(categoryTable, scrollPaneCategories, JLabel.CENTER);
	}
	
	/**
	 * Reset the values of Category table, and repaints the table and frequent category label for updates
	 */
	public void displayCostOfCategories() {
		categoryTable = null;
		scrollPaneCategories = null;
		findMostFrequentCategory();
		createCategoryTableData();
		drawCategoriesTable();
		
	}	
	
	/**
	 * Aligns the data in the provided table, and configures table to it's scrollPane
	 * @param table The table provided that is being formatted
	 * @param scrollPane The scrollPane that the table will be added and configured to
	 * @param centerAlignment A JLabel constant value to allign the center coloumn by
	 */
	public void allignTableColoumns(JTable table, JScrollPane scrollPane, int centerAlignment) {
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment( JLabel.LEFT );
		table.getColumnModel().getColumn(0).setCellRenderer( leftRenderer );
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( centerAlignment );
		table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment( JLabel.RIGHT );
		table.getColumnModel().getColumn(2).setCellRenderer( rightRenderer );
		
		table.setEnabled(false);
		table.setShowGrid(false);
		scrollPane.setViewportView(table);
		scrollPane.setHorizontalScrollBar(null);
	}
	
	/**
	 * Sets the coloumn width for three coloumns in a table
	 * @param table The table who's coloumn width are being set
	 * @param firstColoumn the designated width for the first coloumn in the table 
	 * @param secondColoumn the designated width for the second coloumn in the table
	 * @param thirdColoumn the designated width for the third coloumn in the table
	 */
	public void setColoumnWidths(JTable table, int firstColoumn, int secondColoumn, int thirdColoumn) {
		int[] columnWidth = { firstColoumn, secondColoumn, thirdColoumn};
		int coloumnNumber = 0;
		for(int width : columnWidth) {
			
			table.getColumnModel().getColumn(coloumnNumber).setPreferredWidth(width);
			table.getColumnModel().getColumn(coloumnNumber).setMaxWidth(width);
			table.getColumnModel().getColumn(coloumnNumber).setMinWidth(width);
			
			coloumnNumber++;			
		}
	}
	
	/**
	 * Returns a value of self to be called by another class for a reference
	 * @return returns 'this'
	 */
	public DisplayInfoFrame getSelf() {
		return this;
	}
	
	/**
	 * Adds a purchase to the Purchase table and repaints the table to display it
	 * @param desc The description of the new purchase
	 * @param cost The cost of the new purchase
	 * @param category The category of the new purchase
	 */
	public void updateTable(String desc, String cost, String category) {
		
		((DefaultTableModel) purchaseTabelModel).insertRow(purchaseTabelModel.getRowCount(), new Object[] {desc, "$"+ cost, category});
		purchaseTable.repaint();		
	}
	
	/**
	 * Updates the Current Savings integer and label for current user
	 * @param value The value to change the savings by
	 */
	public void updateSavings(double value) {
		currentSavings += value;
		Label_Savings.setText("Current Savings: $" + costFormat.format(currentSavings ));
	}
	
	public String getFormattedCost(double cost) {
		String result = costFormat.format(cost);
		int decimalCount = result.indexOf('.');
		
		String cents = result.substring(decimalCount + 1);
		
		if(cents.length() == 1) {
			result += "0";
		}
		else if(cents.length() == 0) {
			result += "00";
		}	
		
		return result;
	}
	
	
	/**
	 * Allows another class to set the visibility of the frame 
	 * @param isVisible A boolean type to choose whether or not the current frame is visible
	 */
	public void setVisible(boolean isVisible) {
		frame.setVisible(isVisible);
	}
}
